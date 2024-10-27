package service.exerciseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseRoutineRecord;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ExerciseRoutineRecordRepository extends JpaRepository<ExerciseRoutineRecord, Long> {
    Optional<ExerciseRoutineRecord> findByExerciseRoutineAndRoutineDateAndUserId(ExerciseRoutine exerciseRoutine, LocalDate routineDate, Long userId);

}
