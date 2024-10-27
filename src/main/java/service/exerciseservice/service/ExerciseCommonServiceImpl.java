package service.exerciseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.exerciseservice.base.exception.code.RestApiException;
import service.exerciseservice.base.exception.code.RoutineErrorStatus;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseRoutineRecord;
import service.exerciseservice.repository.ExerciseRoutineRecordRepository;
import service.exerciseservice.repository.ExerciseRoutineRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static service.exerciseservice.converter.Converter.toRoutineEntity;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseCommonServiceImpl implements ExerciseCommonService {

    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final ExerciseRoutineRecordRepository exerciseRoutineRecordRepository;

    //Todo: 운동 루틴 추가하기
    @Override
    public Long addExerciseRoutine(Long userId, RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto){
        LocalTime startTime = exerciseRoutineDto.getStartTime();
        LocalTime endTime = exerciseRoutineDto.getEndTime();

        // 운동 시간 계산 (분 단위)
        long durationInMinutes = calculateDuration(startTime, endTime);

        return exerciseRoutineRepository.save(toRoutineEntity(exerciseRoutineDto,durationInMinutes,userId)).getId();
    }

    //Todo : 운동 루틴 on
    @Override
    public void onMentalRoutine(Long routineId, LocalDate today){
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findById(routineId)
                .orElseThrow(()-> new RestApiException(RoutineErrorStatus.ROUTINE_NOT_FOUND));

        // 현재 날짜 정보 가져오기
//        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        handleRoutineOn(exerciseRoutine, today, endOfWeek);

        exerciseRoutineRepository.save(exerciseRoutine);
    }

    //Todo : 운동 루틴 off
    @Override
    public void offMentalRoutine(Long routineId, LocalDate today) {
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findById(routineId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_NOT_FOUND));

        // 현재 날짜 정보 가져오기
//        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        handleRoutineOff(exerciseRoutine, today, endOfWeek);

        exerciseRoutineRepository.save(exerciseRoutine);
    }

    //Todo: 운동 일정 수행 완료
    @Override
    public void completeRoutine(Long routineRecordId){
        ExerciseRoutineRecord exerciseRoutineRecord = exerciseRoutineRecordRepository.findById(routineRecordId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_RECORD_NOT_FOUND));

        //루틴 레코드 일정 완료 표시
        exerciseRoutineRecord.updateCompleteAndCompleteDate(true, LocalDate.now());

    }

    //Todo: 운동 일정 수행 완료 취소
    @Override
    public void cancelRoutine(Long routineRecordId){
        ExerciseRoutineRecord exerciseRoutineRecord = exerciseRoutineRecordRepository.findById(routineRecordId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_RECORD_NOT_FOUND));
        exerciseRoutineRecord.updateCompleteAndCompleteDate(false, null);

    }

    //Todo: 운동 루틴 삭제
    @Override
    public void deleteRoutine(Long routineId){
        ExerciseRoutine exerciseRoutine = exerciseRoutineRepository.findById(routineId)
                .orElseThrow(() -> new RestApiException(RoutineErrorStatus.ROUTINE_NOT_FOUND));

        exerciseRoutineRepository.delete(exerciseRoutine);
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
}
