package service.exerciseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseWeekRecord;

import java.util.List;

public interface ExerciseWeekRecordRepository extends JpaRepository<ExerciseWeekRecord, Long> {

    @Query("select ewr from ExerciseWeekRecord ewr where ewr.userId =:userId")
    List<ExerciseWeekRecord> findExerciseWeekRecordLIstByUserId(Long userId);


}
