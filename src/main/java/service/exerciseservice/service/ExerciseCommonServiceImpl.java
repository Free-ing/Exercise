package service.exerciseservice.service;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.exerciseservice.base.exception.code.RestApiException;
import service.exerciseservice.base.exception.code.RoutineErrorStatus;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.entity.BasicService;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseRoutineRecord;
import service.exerciseservice.entity.ExerciseWeekRecord;
import service.exerciseservice.repository.ExerciseRoutineRecordRepository;
import service.exerciseservice.repository.ExerciseRoutineRepository;
import service.exerciseservice.repository.ExerciseWeekRecordRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static service.exerciseservice.converter.Converter.toRoutineEntity;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ExerciseCommonServiceImpl implements ExerciseCommonService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseCommonServiceImpl.class);
    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final ExerciseRoutineRecordRepository exerciseRoutineRecordRepository;
    private final ExerciseWeekRecordRepository exerciseWeekRecordRepository;

    private final ExerciseQueryService exerciseQueryService;

    //Todo: 운동 루틴 추가하기
    @Override
    public Long addExerciseRoutine(Long userId, RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto){
            LocalTime startTime;
            LocalTime endTime;
            long durationInMinutes;

            ExerciseRoutine existRoutine = exerciseRoutineRepository.findByUserIdAndExerciseName(userId, exerciseRoutineDto.getRoutineName());

            if(existRoutine != null){
                throw new RestApiException(RoutineErrorStatus.EXERCISE_ROUTINE_ALREADY_EXIST);

            }else {
                if (exerciseRoutineDto.getStartTime() != null && exerciseRoutineDto.getEndTime() != null) {
                    startTime = exerciseRoutineDto.getStartTime();
                    endTime = exerciseRoutineDto.getEndTime();
                    durationInMinutes = calculateDuration(startTime, endTime);

                } else {
                    durationInMinutes = 0;
                }
            }

        // 운동 시간 계산 (분 단위)
        return exerciseRoutineRepository.save(toRoutineEntity(exerciseRoutineDto,durationInMinutes,userId)).getId();
    }

    //Todo : 운동 루틴 on
    @Override
    public void onExerciseRoutine(Long routineId, LocalDate today, Long userId){
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findByIdAndUserId(routineId,userId)
                .orElseThrow(()-> new RestApiException(RoutineErrorStatus.ROUTINE_NOT_FOUND));
        exerciseRoutine.updateStatus(true);  // 이 부분 추가

        // 현재 날짜 정보 가져오기
//        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        handleRoutineOn(exerciseRoutine, today, endOfWeek);

        exerciseRoutineRepository.save(exerciseRoutine);
    }

    //Todo : 운동 루틴 off
    @Override
    public void offExerciseRoutine(Long routineId, LocalDate today, Long userId) {
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findByIdAndUserId(routineId,userId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_NOT_FOUND));

        exerciseRoutine.updateStatus(false);

        // 현재 날짜 정보 가져오기
//        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        handleRoutineOff(exerciseRoutine, today, endOfWeek);

        exerciseRoutineRepository.save(exerciseRoutine);
    }

    //Todo: 운동 일정 수행 완료
    @Override
    public void completeRoutine(Long routineRecordId, Long userId){
        ExerciseRoutineRecord exerciseRoutineRecord = exerciseRoutineRecordRepository.findByIdAndUserId(routineRecordId, userId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_RECORD_NOT_FOUND));

        Long duration = exerciseRoutineRecord.getExerciseRoutine().getDuration();

        //루틴 레코드 일정 완료 표시
        exerciseRoutineRecord.updateCompleteAndCompleteDate(true, LocalDate.now(),duration);

    }

    //Todo: 운동 일정 수행 완료 취소
    @Override
    public void cancelRoutine(Long routineRecordId , Long userId){
        ExerciseRoutineRecord exerciseRoutineRecord = exerciseRoutineRecordRepository.findByIdAndUserId(routineRecordId, userId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_RECORD_NOT_FOUND));
        exerciseRoutineRecord.updateCompleteAndCompleteDate(false, null,0);

    }

    //Todo: 운동 루틴 삭제
    @Override
    public void deleteRoutine(Long routineId, Long userId){
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_NOT_FOUND));

        exerciseRoutineRepository.delete(exerciseRoutine);
    }

    //Todo: 운동 루틴 수정
    @Override
    public Long updateRoutine(Long userId, Long routineId, RequestExerciseDto.RoutineUpdateDto routineUpdateDto, LocalDate today){
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.USER_CANT_UPDATE));

        LocalTime startTime;
        LocalTime endTime;
        long durationInMinutes;
        if(routineUpdateDto.getStartTime() != null && routineUpdateDto.getEndTime() !=null){

            startTime = routineUpdateDto.getStartTime();
            endTime = routineUpdateDto.getEndTime();
            durationInMinutes = calculateDuration(startTime, endTime);

        }else {
            durationInMinutes = 0;
        }

        // 운동 시간 계산 (분 단위)
        exerciseRoutine.update(routineUpdateDto,durationInMinutes );

        offExerciseRoutine(routineId,today,userId);
        onExerciseRoutine(routineId,today,userId);


        return exerciseRoutine.getId();
    }


    //Todo: 기본 기능 생성
    @Override
    public void createDefaultService(Long userId) {
        ExerciseRoutine staticRoutine = createMentalRoutine(userId, "정적 스트레칭", """
                        정적 스트레칭은 근육을 천천히 늘려 15-30초간 자세를 유지하는 방법입니다.\n\n 운동 후나 일상생활에서 뭉친 근육을 풀어주고 유연성을 향상시키는데 효과적이에요.\n\n 특히 목, 어깨, 허리 같은 피로가 쌓이기 쉬운 부위에 좋습니다.\n\n 단, 운동 전에는 동적 스트레칭이 더 적합하니 주의하세요!
                        """,
                LocalTime.of(20, 0, 0), LocalTime.of(22, 0, 0), "https://freeingimage.s3.ap-northeast-2.amazonaws.com/static_stretching.png", BasicService.STATIC_STRETCHING);

        ExerciseRoutine dynamicRoutine = createMentalRoutine(userId, "동적 스트레칭", """
                        동적 스트레칭은 관절을 부드럽게 움직이며 근육을 능동적으로 늘리는 방법입니다.\n\n 운동 전 워밍업으로 적합하며, 근육의 체온을 높이고 혈액순환을 촉진시켜 운동 수행능력을 향상시킵니다.\n\n 특히 관절의 가동범위를 늘리고 부상 예방에도 효과적이에요!
                        """,
                LocalTime.of(20, 0, 0), LocalTime.of(22, 0, 0), "https://freeingimage.s3.ap-northeast-2.amazonaws.com/dynamic_stretching.png", BasicService.DYNAMIC_STRETCHING);

        ExerciseRoutine workRoutine = createMentalRoutine(userId, "걷기", """
                        걷기는 모든 연령대가 쉽게 할 수 있는 기초적인 유산소 운동입니다.\n\n 심장 건강 증진, 체중 관리, 스트레스 해소에 효과적이며 관절에 무리가 적어 안전합니다.\n\n 하루 30분 이상, 약간 숨이 찰 정도의 속도로 걷는 것이 좋아요!
                        """,
                LocalTime.of(20, 0, 0), LocalTime.of(22, 0, 0), "https://freeingimage.s3.ap-northeast-2.amazonaws.com/walking.png", BasicService.WORKING);

//        1. 기본 걷기
//        바른 자세로 시선은 전방 15도를 바라보고, 팔은 자연스럽게 흔들며 발뒤꿈치부터 착지합니다. 처음에는 15-20분부터 시작해서 점차 시간을 늘려가세요."
//
//        2. 파워 워킹
//        일반 걷기보다 빠른 속도로 팔을 크게 흔들며 걷습니다. 복근에 힘을 주고 보폭을 넓게 하여 운동 강도를 높일 수 있어요.s

        ExerciseRoutine runRoutine = createMentalRoutine(userId, "달리기", """
                        달리기는 체력 향상과 체중 감량에 매우 효과적인 유산소 운동입니다.\n\n 심폐지구력을 향상시키고 칼로리 소모가 크며, 달리기 후 분비되는 엔도르핀으로 스트레스 해소에도 도움이 됩니다.\n\n 단, 무릎 등 관절에 충격이 있으니 적절한 준비운동이 필수에요!
                        """,
                LocalTime.of(20, 0, 0), LocalTime.of(22, 0, 0), "https://freeingimage.s3.ap-northeast-2.amazonaws.com/running.png", BasicService.RUNNING);
//        1. 조깅
//        편안한 속도로 일정한 페이스를 유지하며 달립니다. 발바닥 전체로 착지하고, 팔은 90도로 굽혀 자연스럽게 흔드세요. 초보자는 10-15분부터 시작하는 것이 좋습니다.
//
//        2. 인터벌 러닝
//        빠른 달리기와 걷기 또는 천천히 달리기를 번갈아 하는 방법입니다. 예를 들어 2분 달리기 후 1분 걷기를 반복하는 식으로 진행하면 짧은 시간에 더 많은 칼로리를 소모하고, 기초대사량 증가와 심폐지구력 향상시킬 수 있어요.s

        exerciseRoutineRepository.save(staticRoutine);
        exerciseRoutineRepository.save(dynamicRoutine);
        exerciseRoutineRepository.save(workRoutine);
        exerciseRoutineRepository.save(runRoutine);

    }


    //Todo: 회원의 모든 운동 데이터 삭제
    @Override
    @Transactional
    public void deleteExerciseDate(Long userId){
        List<ExerciseRoutine> exerciseRoutineList = exerciseRoutineRepository.findExerciseRoutineLIstByUserId(userId);
        List<ExerciseRoutineRecord> exerciseRoutineRecordList = exerciseRoutineRecordRepository.findExerciseRoutineRecordLIstByUserId(userId);
        List<ExerciseWeekRecord> exerciseWeekRecordList = exerciseWeekRecordRepository.findExerciseWeekRecordLIstByUserId(userId);

        // 1. 먼저 외래 키를 가진 자식 엔티티들부터 삭제
        exerciseRoutineRecordRepository.deleteAllByUserId(userId);
        exerciseWeekRecordRepository.deleteAllByUserId(userId);

        // 2. 그 다음 부모 엔티티 삭제
        exerciseRoutineRepository.deleteAllByUserId(userId);

    }

    //Todo: 회원 레포트 정보 채우기
    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 0시에 실행
    @Transactional
    @Override
    public void createWeeklyRecords() {
        // 지난 주의 시작일(월요일)과 종료일(일요일) 계산
//        LocalDate endDate = LocalDate.now().minusDays(1); // 어제(일요일)
//        LocalDate startDate = endDate.minusDays(6); // 지난주 월요일

        LocalDate todayDate = LocalDate.parse("2024-11-04");
        LocalDate endDate = todayDate.minusDays(1); // 어제(일요일)
        LocalDate startDate = endDate.minusDays(6); // 지난주 월요일

        // 완료된 운동이 있는 사용자 목록 조회
        List<Long> userIds = exerciseRoutineRecordRepository.findDistinctUserIdsByCompleteTrue();

        for (Long userId : userIds) {
            try {
                createWeeklyRecordForUser(userId, startDate, endDate);
            } catch (Exception e) {
                log.error("Error creating weekly record for user {}: {}", userId, e.getMessage(), e);
            }
        }
    }

    //Todo: 쉬어가기
    @Override
    public void offDayRecord(Long recordId, Long userId){
        ExerciseRoutineRecord exerciseRoutineRecord = exerciseRoutineRecordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.EXERCISE_WEEK_RECORD_NOT_FOUND));

        exerciseRoutineRecord.offRoutineRecord();

    }
















    private void createWeeklyRecordForUser(Long userId, LocalDate startDate, LocalDate endDate) {
        // 해당 주의 모든 운동 기록 조회
        List<ExerciseRoutineRecord> weeklyRecords = exerciseRoutineRecordRepository
                .findByUserIdAndCompleteDayBetweenAndCompleteTrue(userId, startDate, endDate);

        // 요일별 운동 시간 계산
        Map<DayOfWeek, Integer> dailyExerciseTimes = calculateDailyExerciseTimes(weeklyRecords);

        // 총 운동 시간 계산
        int totalTime = dailyExerciseTimes.values().stream().mapToInt(Integer::intValue).sum();

        // 운동한 날짜 수 계산 (0으로 나누는 것 방지)
        long exerciseDays = dailyExerciseTimes.values().stream().filter(time -> time > 0).count();
//        int averageTime = exerciseDays > 0 ? (int) (totalTime / exerciseDays) : 0;

        int calculateAvgTime = totalTime / 7;
        int averageTime = Math.max(calculateAvgTime, 0);

        ExerciseWeekRecord weekRecord = ExerciseWeekRecord.builder()
                .userId(userId)
                .startDate(startDate)
                .endDate(endDate)
                .totalTime(totalTime)
                .averageTime(averageTime)
                .monTime(dailyExerciseTimes.getOrDefault(DayOfWeek.MONDAY, 0))
                .tueTime(dailyExerciseTimes.getOrDefault(DayOfWeek.TUESDAY, 0))
                .wenTime(dailyExerciseTimes.getOrDefault(DayOfWeek.WEDNESDAY, 0))
                .thuTime(dailyExerciseTimes.getOrDefault(DayOfWeek.THURSDAY, 0))
                .friTime(dailyExerciseTimes.getOrDefault(DayOfWeek.FRIDAY, 0))
                .satTime(dailyExerciseTimes.getOrDefault(DayOfWeek.SATURDAY, 0))
                .sunTime(dailyExerciseTimes.getOrDefault(DayOfWeek.SUNDAY, 0))
                .build();

        exerciseWeekRecordRepository.save(weekRecord);
        log.info("Created weekly record for user {}: total time {}, average time {}",
                userId, totalTime, averageTime);
    }


    private Map<DayOfWeek, Integer> calculateDailyExerciseTimes(List<ExerciseRoutineRecord> records) {
        return records.stream()
                .filter(record -> record.getCompleteDay() != null)
                .collect(Collectors.groupingBy(
                        record -> record.getRoutineDate().getDayOfWeek(),
                        Collectors.summingInt(record -> (int) record.getExerciseDurationTime())
                ));
    }


    // 운동 시간 계산 (분 단위)
    private long calculateDuration(LocalTime startTime, LocalTime endTime) {
        // 자정을 넘어가는 경우 처리
        if (endTime.isBefore(startTime)) {
            // 다음 날로 넘어가는 경우, 24시간을 더해서 계산
            return ChronoUnit.MINUTES.between(startTime, endTime.plusHours(24));
        }
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }



    private void handleRoutineOff(ExerciseRoutine routine, LocalDate today, LocalDate endOfWeek) {
        LocalDate currentDate = today;
        while (!currentDate.isAfter(endOfWeek)) {
            // routineDate로 레코드를 찾아야 함
            Optional<ExerciseRoutineRecord> existingRecord = exerciseRoutineRecordRepository
                    .findByExerciseRoutineAndRoutineDateAndUserId(
                            routine,
                            currentDate,
                            routine.getUserId());

            existingRecord.ifPresent(record -> {
                record.updateStatus(false);
                exerciseRoutineRecordRepository.save(record);  // 변경사항 저장
            });

            currentDate = currentDate.plusDays(1);
        }
    }
    private void handleRoutineOn(ExerciseRoutine routine, LocalDate today, LocalDate endOfWeek) {
        // 현재 날짜부터 이번 주 일요일까지의 날짜들에 대해 처리
        LocalDate currentDate = today;
        while (!currentDate.isAfter(endOfWeek )) {
            // 해당 요일에 대한 루틴 설정이 되어있는지 확인
            if (isDayEnabled(routine, currentDate)) {
                // 이미 record가 있는지 확인
                Optional<ExerciseRoutineRecord> existingRecord = exerciseRoutineRecordRepository
                        .findByExerciseRoutineAndRoutineDateAndUserId(
                                routine, currentDate, routine.getUserId());

                if (existingRecord.isPresent()) {
                    // 이미 존재하는 record의 status를 TRUE로 업데이트
                    existingRecord.get().updateStatus(true);
                } else {
                    // 새로운 record 생성
                    ExerciseRoutineRecord newRecord = ExerciseRoutineRecord.builder()
                            .userId(routine.getUserId())
                            .routineDate(currentDate)
                            .completeDay(null)
                            .complete(false)
                            .exerciseRoutine(routine)
                            .exerciseName(routine.getExerciseName())
                            .status(true)
                            .build();
                    exerciseRoutineRecordRepository.save(newRecord);
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }


    private boolean isDayEnabled(ExerciseRoutine routine, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> routine.getMonday() != null && routine.getMonday();
            case TUESDAY -> routine.getTuesday() != null && routine.getTuesday();
            case WEDNESDAY -> routine.getWednesday() != null && routine.getWednesday();
            case THURSDAY -> routine.getThursday() != null && routine.getThursday();
            case FRIDAY -> routine.getFriday() != null && routine.getFriday();
            case SATURDAY -> routine.getSaturday() != null && routine.getSaturday();
            case SUNDAY -> routine.getSunday() != null && routine.getSunday();
        };
    }






    private ExerciseRoutine createMentalRoutine(Long userId, String routineName, String explanation, LocalTime startTime , LocalTime endTime, String imageUrl, BasicService basicService) {
        return ExerciseRoutine.builder()
                .exerciseName(routineName)
                .userId(userId)
                .monday(true)
                .tuesday(true)
                .wednesday(true)
                .thursday(true)
                .friday(true)
                .saturday(true)
                .sunday(true)
                .explanation(explanation)
                .startTime(startTime)
                .endTime(endTime)
                .status(true)
                .imageUrl(imageUrl)
                .duration(calculateDuration(startTime,endTime))
                .basicService(basicService)
                .status(false)
                .build();
    }
}
