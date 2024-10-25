package service.exerciseservice.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.exerciseservice.base.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseRoutineRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseRecordId;

    private Long userId;
    private LocalDate completeDay;
    private String exerciseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_routine_id")
    private ExerciseRoutine exerciseRoutine;

    @Builder
    public ExerciseRoutineRecord(Long userId, LocalDate completeDay,
                                 String exerciseName, ExerciseRoutine exerciseRoutine) {
        this.userId = userId;
        this.completeDay = completeDay;
        this.exerciseName = exerciseName;
        this.exerciseRoutine = exerciseRoutine;
    }
}