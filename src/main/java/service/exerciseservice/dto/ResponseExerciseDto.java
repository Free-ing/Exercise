package service.exerciseservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ResponseExerciseDto {

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
}
