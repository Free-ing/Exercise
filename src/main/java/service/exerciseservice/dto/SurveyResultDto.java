package service.exerciseservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;


public class SurveyResultDto {


    @AllArgsConstructor
    @Getter
    public static class surveyResultDto{

        private String exerciseType;

        private String preferredTime;

        private String preferredPlace;

        private String exercisePurpose;

    }

}
