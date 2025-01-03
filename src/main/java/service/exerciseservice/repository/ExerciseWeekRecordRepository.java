package service.exerciseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseWeekRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExerciseWeekRecordRepository extends JpaRepository<ExerciseWeekRecord, Long> {

    @Query("select ewr from ExerciseWeekRecord ewr where ewr.userId =:userId")
    List<ExerciseWeekRecord> findExerciseWeekRecordLIstByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM ExerciseWeekRecord e WHERE e.userId = :userId")
    void deleteAllByUserId(Long userId);

    @Query("select ew from ExerciseWeekRecord ew where ew.userId = :userId and ew.startDate = :startDate and ew.endDate = :endDate ")
    ExerciseWeekRecord findByUserId(Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select ewr from ExerciseWeekRecord  ewr where YEAR(ewr.endDate) = :year and MONTH(ewr.endDate) = :month and ewr.userId = :userId")
    List<ExerciseWeekRecord> findByYearAndMonthAndUserId(int year, int month, Long userId);

    @Query("select new service.exerciseservice.dto.ResponseExerciseDto$FeedbackDayListDto (ewr.exerciseWeekRecordId, ewr.startDate, ewr.endDate) from ExerciseWeekRecord ewr where ewr.userId = :userId and YEAR(ewr.endDate) = :year and MONTH(ewr.endDate) = :month ")
    List<ResponseExerciseDto.FeedbackDayListDto> findFeedbackDayList(int year, int month, long userId);

    Optional<ExerciseWeekRecord> findByStartDateAndEndDateAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
}
