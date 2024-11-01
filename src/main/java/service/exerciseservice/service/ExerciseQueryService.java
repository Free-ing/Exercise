package service.exerciseservice.service;

import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.RoutineTrackerDto;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseQueryService {
    //Todo: 마음 채우기 루틴 리스트 조회
    List<RequestExerciseDto.ExerciseRoutineDto> getExerciseRoutineList(Long userId);

    //Todo: 일별 루틴 일정 조회
    List<ResponseExerciseDto.DayRoutineDto> getDayRoutine(LocalDate date, Long userId);

    //Todo: 마음 루틴 트래커 조회
    List<RoutineTrackerDto.ExerciseRoutineTrackerDto> getHobbyRoutineTrackers(Long userId, int year, int month);
}
