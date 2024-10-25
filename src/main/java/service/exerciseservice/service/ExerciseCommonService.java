package service.exerciseservice.service;

import service.exerciseservice.dto.RequestExerciseDto;

public interface ExerciseCommonService {
    //Todo: 운동 루틴 추가하기
    Long addExerciseRoutine(Long userId, RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto);
}
