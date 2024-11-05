package service.exerciseservice.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.exerciseservice.base.BaseEntity;
import service.exerciseservice.dto.RequestExerciseDto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "exercise_routine")
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

    private Long duration;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private Boolean status;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private BasicService basicService;

    @OneToMany(mappedBy = "exerciseRoutine", cascade = CascadeType.ALL)
    private final List<ExerciseRoutineRecord> exerciseRoutineRecordList = new ArrayList<>();

    @Builder
    public ExerciseRoutine(String exerciseName, Long userId, LocalTime startTime,
                           LocalTime endTime, Boolean monday, Boolean tuesday,
                           Boolean wednesday, Boolean thursday, Boolean friday,
                           Boolean saturday, Boolean sunday, Long duration,
                           String explanation, Boolean status, BasicService basicService, String imageUrl) {
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
        this.explanation = explanation;
        this.status = status;
        this.basicService = basicService;
        this.imageUrl = imageUrl;
        this.duration = duration;
    }

    public void update(RequestExerciseDto.RoutineUpdateDto routineUpdateDto, long duration) {
        this.exerciseName = routineUpdateDto.getRoutineName();
        this.explanation = routineUpdateDto.getExplanation();
        this.sunday = routineUpdateDto.getSunday();
        this.saturday = routineUpdateDto.getSaturday();
        this.thursday = routineUpdateDto.getThursday();
        this.friday = routineUpdateDto.getFriday();
        this.wednesday = routineUpdateDto.getWednesday();
        this.tuesday = routineUpdateDto.getTuesday();
        this.monday = routineUpdateDto.getMonday();
        this.endTime = routineUpdateDto.getEndTime();
        this.startTime = routineUpdateDto.getStartTime();
        this.imageUrl = routineUpdateDto.getImageUrl();
        this.duration = duration;
    }

    public void updateStatus(boolean status){
        this.status = status;
    }
}