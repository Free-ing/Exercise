package service.exerciseservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.exerciseservice.base.BaseEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseRoutine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exerciseName;
    private Long userId;

    private LocalTime startTime;
    private LocalTime endTime;

    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    private Boolean sunday;

    private Integer duration;
    private String explanation;

    private Boolean status;

    @Enumerated(EnumType.STRING)
    private BasicService basicService;

    @OneToMany(mappedBy = "exerciseRoutine", cascade = CascadeType.ALL)
    private final List<ExerciseRoutineRecord> exerciseRoutineRecordList = new ArrayList<>();

    @Builder
    public ExerciseRoutine(String exerciseName, Long userId, LocalTime startTime,
                           LocalTime endTime, Boolean monday, Boolean tuesday,
                           Boolean wednesday, Boolean thursday, Boolean friday,
                           Boolean saturday, Boolean sunday, Integer duration,
                           String explanation, Boolean status, BasicService basicService) {
        this.exerciseName = exerciseName;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.duration = duration;
        this.explanation = explanation;
        this.status = status;
        this.basicService = basicService;
    }
}
