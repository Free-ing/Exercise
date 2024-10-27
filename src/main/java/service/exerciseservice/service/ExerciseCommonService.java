package service.exerciseservice.service;

import service.exerciseservice.dto.RequestExerciseDto;

import java.time.LocalDate;

public interface ExerciseCommonService {
    //Todo: 운동 루틴 추가하기
    Long addExerciseRoutine(Long userId, RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto);

    //Todo : 마음 채우기 루틴 on
    void onMentalRoutine(Long routineId, LocalDate today);

    //Todo : 마음 채우기 루틴 off
    void offMentalRoutine(Long routineId, LocalDate today);

    //Todo: 운동 일정 수행 완료
    void completeRoutine(Long routineRecordId);

    //Todo: 운동 일정 수행 완료 취소
    void cancelRoutine(Long routineRecordId);

    //Todo: 운동 루틴 삭제
    void deleteRoutine(Long routineId);
}
