package service.exerciseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
            "from ExerciseRoutineRecord er where er.routineDate =:date and er.status =:status and er.userId =:userId")
    List<ResponseExerciseDto.DayRoutineDto> getDayRoutine(LocalDate date, Long userId , Boolean status);

}
