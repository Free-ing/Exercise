package service.exerciseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import service.exerciseservice.entity.BasicService;

import java.time.LocalTime;

public class RequestExerciseDto {
    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    public static class ExerciseRoutineDto{
        private String routineName;
        private Long userId;
        private String imageUrl;
        private Boolean monday;
        private Boolean tuesday;
        private Boolean wednesday;
        private Boolean thursday;
        private Boolean friday;
        private Boolean saturday;
        private Boolean sunday;
        private LocalTime startTime;
        private LocalTime endTime;
        private String explanation;
        private Boolean status;
        private BasicService basicService;
    }
}
