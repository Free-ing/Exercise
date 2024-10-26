package service.exerciseservice.service;

import service.exerciseservice.dto.RequestExerciseDto;

import java.util.List;

public interface ExerciseQueryService {
    //Todo: 마음 채우기 루틴 리스트 조회
    List<RequestExerciseDto.ExerciseRoutineDto> getExerciseRoutineList(Long userId);
}
