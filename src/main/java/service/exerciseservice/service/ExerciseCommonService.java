package service.exerciseservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import service.exerciseservice.dto.RequestExerciseDto;

import java.time.LocalDate;

public interface ExerciseCommonService {
    //Todo: 운동 루틴 추가하기
    Long addExerciseRoutine(Long userId, RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto);

    //Todo : 마음 채우기 루틴 on
    void onExerciseRoutine(Long routineId, LocalDate today, Long userId);

    //Todo : 마음 채우기 루틴 off
    void offExerciseRoutine(Long routineId, LocalDate today, Long userId);

    //Todo: 운동 일정 수행 완료
    void completeRoutine(Long routineRecordId , Long userId);

    //Todo: 운동 일정 수행 완료 취소
    void cancelRoutine(Long routineRecordId, Long userId);

    //Todo: 운동 루틴 삭제
    void deleteRoutine(Long routineId);

    //Todo: 운동 루틴 수정
    Long updateRoutine(Long userId, Long routineId, RequestExerciseDto.RoutineUpdateDto routineUpdateDto);

    //Todo: 기본 기능 생성
    void createDefaultService(Long userId);

    //Todo: 회원의 모든 운동 데이터 삭제
    void deleteExerciseDate(Long userId);

    //Todo: 회원 레포트 정보 채우기
    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 0시에 실행
    @Transactional
    void createWeeklyRecords();
}
