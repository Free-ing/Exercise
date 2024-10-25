package service.exerciseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.exerciseservice.entity.ExerciseRoutine;

public interface ExerciseRoutineRepository extends JpaRepository<ExerciseRoutine, Long> {
}
