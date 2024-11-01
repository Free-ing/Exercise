package service.exerciseservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.SurveyResultDto;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;



    @Override
    public List<ResponseExerciseDto.AiExerciseResponseDto> generateHobbyRecommendations(SurveyResultDto.surveyResultDto surveyResult) {

        String survey1 = surveyResult.getExercisePurpose();
        String survey2 = surveyResult.getExerciseType();
        String survey3 = surveyResult.getExercisePurpose();
        String survey4 = surveyResult.getPreferredPlace();

        String survey = survey1 + survey2 + survey3 + survey4;

        String userMessageContent = String.format("설문조사 결과를 바탕으로 운동 추천 이유를 간단하게 제시해줘.: %s", survey);

        String jsonResponse = chatClient.prompt()
                .user(userMessageContent)
                .call()
                .content();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode recommendationsNode = rootNode.get("recommendations");
            if (recommendationsNode != null && recommendationsNode.isArray()) {
                return objectMapper.convertValue(recommendationsNode,
                        objectMapper.getTypeFactory().constructCollectionType(List.class,ResponseExerciseDto.AiExerciseResponseDto.class));
            } else {
                throw new RuntimeException("Unexpected response structure: 'recommendations' array not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }
}