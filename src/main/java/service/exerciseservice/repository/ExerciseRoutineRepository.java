package service.exerciseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.exerciseservice.entity.ExerciseRoutine;

import java.util.List;

public interface ExerciseRoutineRepository extends JpaRepository<ExerciseRoutine, Long> {
    List<ExerciseRoutine> findByUserId(Long userId);
}
