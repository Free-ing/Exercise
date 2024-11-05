package service.exerciseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseRoutineRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRoutineRecordRepository extends JpaRepository<ExerciseRoutineRecord, Long> {
    Optional<ExerciseRoutineRecord> findByExerciseRoutineAndRoutineDateAndUserId(ExerciseRoutine exerciseRoutine, LocalDate routineDate, Long userId);



    @Query("select new service.exerciseservice.dto.ResponseExerciseDto$DayRoutineDto(er.exerciseRoutine.exerciseName,er.exerciseRoutine.imageUrl,er.exerciseRoutine.id, er.exerciseRoutine.monday, " +
            "er.exerciseRoutine.tuesday, er.exerciseRoutine.wednesday, er.exerciseRoutine.thursday, er.exerciseRoutine.friday,er.exerciseRoutine.saturday, er.exerciseRoutine.sunday," +
            "er.exerciseRoutine.status, er.exerciseRoutine.startTime, er.exerciseRoutine.endTime, er.exerciseRoutine.explanation,er.exerciseRoutine.basicService,er.id, er.complete)"+
            "from ExerciseRoutineRecord er where er.routineDate =:date and er.complete =:status and er.userId =:userId")
    List<ResponseExerciseDto.DayRoutineDto> getDayRoutine(LocalDate date, Long userId , Boolean status);

    @Query("select err from ExerciseRoutineRecord err where err.userId =:userId")
    List<ExerciseRoutineRecord> findExerciseRoutineRecordLIstByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM ExerciseRoutineRecord e WHERE e.userId = :userId")
    void deleteAllByUserId(Long userId);

    @Query("SELECT DISTINCT e.userId FROM ExerciseRoutineRecord e WHERE e.complete = true")
    List<Long> findDistinctUserIdsByCompleteTrue();

    List<ExerciseRoutineRecord> findByUserIdAndCompleteDayBetweenAndCompleteTrue(
            Long userId, LocalDate startDate, LocalDate endDate);


//        @Query("SELECT DISTINCT e.routineDate FROM ExerciseRoutineRecord e " +
//                "WHERE e.userId = :userId " +
//                "AND e.routineDate BETWEEN :startDate AND :endDate " +
//                "AND e.complete = true " +
//                "ORDER BY e.routineDate")
//        List<ResponseExerciseDto.DayCompleteRoutine> findCompletedDatesByUserIdAndDateRange(
//                @Param("userId") Long userId,
//                @Param("startDate") LocalDate startDate,
//                @Param("endDate") LocalDate endDate
//        );
//
//    @Query("SELECT new service.exerciseservice.dto.ResponseExerciseDto$DayCompleteRoutine(e.routineDate) " +
//            "FROM ExerciseRoutineRecord e " +
//            "WHERE e.userId = :userId " +
//            "AND e.routineDate BETWEEN :startDate AND :endDate " +
//            "AND e.complete = true " +
//            "GROUP BY e.routineDate " +
//            "ORDER BY e.routineDate")
//    List<ResponseExerciseDto.DayCompleteRoutine> findCompletedDatesByUserIdAndDateRange(
//            @Param("userId") Long userId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );

    @Query("SELECT new service.exerciseservice.dto.ResponseExerciseDto$DayCompleteRoutine(e.routineDate) " +
            "FROM ExerciseRoutineRecord e " +
            "WHERE e.userId = :userId " +
            "AND e.routineDate BETWEEN :startDate AND :endDate " +
            "AND e.complete = true " +
            "GROUP BY e.routineDate " +
            "ORDER BY e.routineDate")
    List<ResponseExerciseDto.DayCompleteRoutine> findCompletedDatesByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
