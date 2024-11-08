package service.exerciseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.exerciseservice.base.exception.code.RestApiException;
import service.exerciseservice.base.exception.code.RoutineErrorStatus;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.RoutineTrackerDto;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseRoutineRecord;
import service.exerciseservice.entity.ExerciseWeekRecord;
import service.exerciseservice.repository.ExerciseRoutineRecordRepository;
import service.exerciseservice.repository.ExerciseRoutineRepository;
import service.exerciseservice.repository.ExerciseWeekRecordRepository;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseQueryServiceImpl implements ExerciseQueryService {

    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final ExerciseRoutineRecordRepository exerciseRoutineRecordRepository;
    private final ExerciseWeekRecordRepository exerciseWeekRecordRepository;

    //Todo: 마음 채우기 루틴 리스트 조회
    @Override
    public List<RequestExerciseDto.ExerciseRoutineDto> getExerciseRoutineList(Long userId) {
        return exerciseRoutineRepository.findByUserId(userId)
                .stream()
                .map(exerciseRoutine -> RequestExerciseDto.ExerciseRoutineDto.builder()
                        .routineName(exerciseRoutine.getExerciseName())
                        .routineId(exerciseRoutine.getId())
                        .imageUrl(exerciseRoutine.getImageUrl())
                        .status(exerciseRoutine.getStatus())
                        .monday(exerciseRoutine.getMonday())
                        .tuesday(exerciseRoutine.getTuesday())
                        .wednesday(exerciseRoutine.getWednesday())
                        .thursday(exerciseRoutine.getThursday())
                        .friday(exerciseRoutine.getFriday())
                        .saturday(exerciseRoutine.getSaturday())
                        .sunday(exerciseRoutine.getSunday())
                        .startTime(exerciseRoutine.getStartTime())
                        .endTime(exerciseRoutine.getEndTime())
                        .explanation(exerciseRoutine.getExplanation())
                        .build())
                .collect(Collectors.toList());

    }

    //Todo: 일별 루틴 일정 조회
    @Override
    public List<ResponseExerciseDto.DayRoutineDto> getDayRoutine(LocalDate date, Long userId) {

        System.out.println(date);
        List<ResponseExerciseDto.DayRoutineDto> dayRoutineDtoList = exerciseRoutineRecordRepository.getDayRoutine(date, userId);
        System.out.println(dayRoutineDtoList);
        return dayRoutineDtoList;

    }

    //Todo: 마음 루틴 트래커 조회
    @Override
    public List<RoutineTrackerDto.ExerciseRoutineTrackerDto> getHobbyRoutineTrackers(Long userId, int year, int month) {
        Map<String, RoutineTrackerDto.ExerciseRoutineTrackerDto> routineMap = new LinkedHashMap<>();
        List<ExerciseRoutine> routines = exerciseRoutineRepository.findAllWithRecordsByUserId(userId, year, month);
        System.out.println(routines);

        for (ExerciseRoutine routine : routines) {
            if (!routine.getExerciseRoutineRecordList().isEmpty()) {  // 레코드가 있는 경우만 처리
                System.out.println("test");
                RoutineTrackerDto.ExerciseRoutineTrackerDto trackerDto =
                        routineMap.computeIfAbsent(routine.getExerciseName(),
                                k -> new RoutineTrackerDto.ExerciseRoutineTrackerDto(routine.getExerciseName()));

                for (ExerciseRoutineRecord record : routine.getExerciseRoutineRecordList()) {
                    trackerDto.addRecord(new RoutineTrackerDto.ExerciseRecordDto(
                            record.getId(),
                            record.getRoutineDate()
                    ));
                }
            }
        }

        return new ArrayList<>(routineMap.values());
    }



    //Todo: user별 루틴 기록 리스트 만들기
    @Override
    public List<ResponseExerciseDto.ExerciseRoutineGroupDto> getRoutinesGroupedByUser(LocalDate startDate, LocalDate endDate) {
        List<ExerciseRoutine> allRoutines = exerciseRoutineRepository.findAllWithRecords(startDate,endDate);

        Map<Long, List<ExerciseRoutine>> routinesByUser = allRoutines.stream()
                .collect(Collectors.groupingBy(ExerciseRoutine::getUserId));

        return routinesByUser.entrySet().stream()
                .map(this::convertToExerciseRoutineGroupDto)
                .collect(Collectors.toList());
    }


    //Todo: 운동 피드백 리스트(날짜 조회)
    @Override
    public List<ResponseExerciseDto.FeedbackDayListDto> getFeedbackDayList(int year, int month, long userId){
        return  exerciseWeekRecordRepository.findFeedbackDayList(year,month,userId);
    }

    //Todo: 운동 피드백 상세 조회
    @Override
    public ResponseExerciseDto.ReportDto getFeedback(LocalDate startDate, LocalDate endDate, long userId) {
//        List<ResponseExerciseDto.ReportDto> reportDtoList = new ArrayList<>();
//        List<ExerciseWeekRecord> exerciseWeekRecordList = exerciseWeekRecordRepository.findByYearAndMonthAndUserId(year,month,userId);

        ExerciseWeekRecord exerciseWeekRecord = exerciseWeekRecordRepository.findByStartDateAndEndDateAndUserId(startDate,endDate,userId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.EXERCISE_WEEK_RECORD_NOT_FOUND));

        LocalDate startTime = exerciseWeekRecord.getStartDate();
        LocalDate endTime = exerciseWeekRecord.getEndDate();

        // 해당 기간의 운동 기록들을 조회
        List<ExerciseRoutineRecord> routineRecords = exerciseRoutineRecordRepository
                .findByUserIdAndCompleteDayBetweenAndCompleteTrue(userId, startTime, endTime);

        // 운동별로 그룹화하여 총 시간 계산
        Map<String, ResponseExerciseDto.RoutineInfo> routineInfoMap = new HashMap<>();

        for (ExerciseRoutineRecord record : routineRecords) {
            ExerciseRoutine routine = record.getExerciseRoutine();
            String exerciseName = record.getExerciseName();

            routineInfoMap.computeIfAbsent(exerciseName, k -> new ResponseExerciseDto.RoutineInfo(
                    exerciseName,
                    routine.getImageUrl(),
                    0L
            )).addTime(record.getExerciseDurationTime());
        }

        // 총 운동 시간 계산
        long totalExerciseTime = routineRecords.stream()
                .mapToLong(ExerciseRoutineRecord::getExerciseDurationTime)
                .sum();

        // 일평균 운동 시간 계산 (7일 기준)
        long avgExerciseTime = totalExerciseTime / 7;

        // executeRoutineDto 리스트 생성
        List<ResponseExerciseDto.ExecuteRoutineDto> executionRoutineDtoList = routineInfoMap.values().stream()
                .map(info -> ResponseExerciseDto.ExecuteRoutineDto.builder()
                        .name(info.getName())
                        .imageUrl(info.getImageUrl())
                        .routineTime(info.getTotalTime())
                        .build())
                .collect(Collectors.toList());

        // ReportDto 생성
        ResponseExerciseDto.ReportDto reportDto = ResponseExerciseDto.ReportDto.builder()
                .totalExerciseTime(totalExerciseTime)
                .avgExerciseTime(avgExerciseTime)
                .startTime(startTime)
                .endTime(endTime)
                .monTime(exerciseWeekRecord.getMonTime())
                .tueTime(exerciseWeekRecord.getTueTime())
                .wenTime(exerciseWeekRecord.getWenTime())
                .thuTime(exerciseWeekRecord.getThuTime())
                .friTime(exerciseWeekRecord.getFriTime())
                .satTime(exerciseWeekRecord.getSatTime())
                .sunTime(exerciseWeekRecord.getSunTime())
                .feedBack(exerciseWeekRecord.getAiFeedback())
                .exerciseRoutineDtoList(executionRoutineDtoList)  // executeRoutineDto 리스트 사용
                .build();
        return reportDto;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ResponseExerciseDto.DayCompleteRoutine> getCompleteDate(LocalDate startDate, LocalDate endDate, Long userId) {
        return exerciseRoutineRecordRepository.findCompletedDatesByUserIdAndDateRange(userId, startDate, endDate);
    }




    private ResponseExerciseDto.ExerciseRoutineGroupDto convertToExerciseRoutineGroupDto(
            Map.Entry<Long, List<ExerciseRoutine>> entry) {
        List<ResponseExerciseDto.ExerciseRoutineGroupDto.RoutineWithRecordsDto> routineDtos =
                entry.getValue().stream()
                        .map(this::convertToRoutineWithRecordsDto)
                        .collect(Collectors.toList());

        return ResponseExerciseDto.ExerciseRoutineGroupDto.builder()
                .userId(entry.getKey())
                .routines(routineDtos)
                .build();
    }

    private ResponseExerciseDto.ExerciseRoutineGroupDto.RoutineWithRecordsDto convertToRoutineWithRecordsDto(
            ExerciseRoutine routine) {
        List<ExerciseRoutineRecord> records = routine.getExerciseRoutineRecordList();

//        // 총 운동 시간 계산
//        long totalTime = records.stream()
//                .mapToLong(ExerciseRoutineRecord::getExerciseDurationTime)
//                .sum();
//
//        // 평균 운동 시간 계산
//        long averageTime = records.isEmpty() ? 0 : totalTime / records.size();

        String explanation;
        if(routine.getBasicService() == null){
            explanation = routine.getExplanation();
        }else{
            explanation = null;
        }
        return ResponseExerciseDto.ExerciseRoutineGroupDto.RoutineWithRecordsDto.builder()
                .routineId(routine.getId())
                .exerciseName(routine.getExerciseName())
                .explanation(explanation)
                .records(records.stream()
                        .map(this::convertToRecordDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private ResponseExerciseDto.ExerciseRoutineGroupDto.RecordDto convertToRecordDto(
            ExerciseRoutineRecord record) {
        return ResponseExerciseDto.ExerciseRoutineGroupDto.RecordDto.builder()
                .recordId(record.getId())
                .completeDay(record.getCompleteDay())
                .duration(record.getExerciseDurationTime())
                .day(getDayOfWeek(record.getRoutineDate()))
                .complete(record.isComplete())
                .build();
    }

    private String getDayOfWeek(LocalDate date) {
        if (date == null) return null;
        return date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.KOREA)
                .toUpperCase();
    }
}
