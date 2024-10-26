package service.exerciseservice.service;

import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.SurveyResultDto;

import java.util.List;

public interface OpenAiService {

    //Todo:ai 편지 작성
    List<ResponseExerciseDto.AiExerciseResponseDto> generateHobbyRecommendations(SurveyResultDto.surveyResultDto surveyResult);
}
