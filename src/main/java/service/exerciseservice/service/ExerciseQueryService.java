package service.exerciseservice.service;

import org.springframework.transaction.annotation.Transactional;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.RoutineTrackerDto;
import service.exerciseservice.entity.ExerciseRoutine;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseQueryService {
    //Todo: 마음 채우기 루틴 리스트 조회
    List<RequestExerciseDto.ExerciseRoutineDto> getExerciseRoutineList(Long userId);

    //Todo: 일별 루틴 일정 조회
    List<ResponseExerciseDto.DayRoutineDto> getDayRoutine(LocalDate date, Long userId);

    //Todo: 마음 루틴 트래커 조회
    List<RoutineTrackerDto.ExerciseRoutineTrackerDto> getHobbyRoutineTrackers(Long userId, int year, int month);

    //Todo: 운동 루틴의 기록 조회하기

    //Todo: user별 루틴 기록 리스트 만들기
    List<ResponseExerciseDto.ExerciseRoutineGroupDto> getRoutinesGroupedByUser(LocalDate startDate, LocalDate endDate);

    //Todo: user별 루틴 기록 리스트 만들기
//    List<ResponseExerciseDto.ExerciseRoutineGroupDto> getRoutinesGroupedByUser();

    //Todo: 운동 피드백 리스트(날짜 조회)
    List<ResponseExerciseDto.FeedbackDayListDto> getFeedbackDayList(int year, int month, long userId);

    //Todo: 운동 피드백 리스트 조회
    ResponseExerciseDto.ReportDto getFeedback(long feedbackId, long userId);

    @Transactional(readOnly = true)
    List<ResponseExerciseDto.DayCompleteRoutine> getCompleteDate(LocalDate startDate, LocalDate endDate, Long userId);

    //Todo: 일주일간 일정을 하나라도 완료한 것이 있다면 반환
}
