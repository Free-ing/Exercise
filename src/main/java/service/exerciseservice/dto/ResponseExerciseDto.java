package service.exerciseservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

public class ResponseExerciseDto {

    public static Object ExerciseRoutineDto;
    @JsonProperty("recommendations")
    @Getter
    private List<AiExerciseResponseDto> AiRecommendations;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AiExerciseResponseDto{
        private String hobbyName;
        private String explanation;

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ExerciseRoutineDto{
        private String hobbyName;
        private String imageUrl;
        private Long routineId;
        private Boolean monthday;
        private Boolean tuesday;
        private Boolean wednesday;
        private Boolean thursday;
        private Boolean friday;
        private Boolean saturday;
        private Boolean sunday;
        private Boolean status;
        private LocalTime startTime;
        private LocalTime endTime;
        private String explanation;
    }
}
