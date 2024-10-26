package service.exerciseservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.SurveyResultDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenAiServiceImpl implements OpenAiService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<ResponseExerciseDto.AiExerciseResponseDto> generateHobbyRecommendations(SurveyResultDto.surveyResultDto surveyResult) {

        String systemPromptContent =  "너는 운동 추천 전문가야. 사용자의 설문조사 결과를 바탕으로 추천해야 돼 " +
                "1. 사용자의 선호하는 운동 유형을 고려해 " +
                "2. 사용자의 운동 선호 시간대를 고려해. " +
                "3. 사용자의 운동 장소를 고려해. " +
                "4. 사용자의 운동 목적을 고려해" +
                "5. 응답은 반드시 4개의 추천 항목을 JSON 형식으로 반환하고 hobbyName, explanation만 작성해줘 " +
                "6. 설명은 간결하게 작성하되, 해당 운동이 사용자의 설문조사와 어떻게 부합하는지 간단하게 설명."+
                "응답은 프론트엔드에게 전달할 JSON 형식으로 제공해야 하며, 반드시 recommendations: json 형식으로 반환돼야 하고 recommendations가 루트 노드에 와야합니다. " ;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPromptContent);
        Message systemMessage = systemPromptTemplate.createMessage();
        String survey1 = surveyResult.getExercisePurpose();
        String survey2 = surveyResult.getExerciseType();
        String survey3 = surveyResult.getPreferredPlace();
        String survey4 =surveyResult.getPreferredTime();
        String survey = survey1 + survey2 + survey3 + survey4;
        String userMessageContent = String.format("설문조사 결과를 바탕으로 취미 활동 추천 이유를 간단하게 제시해줘: %s",survey+ "응답은 프론트엔드에게 전달할 JSON 형식으로 제공해야 하며, 반드시 recommendations: json 형식으로 반환하고 recommendations가 루트 노드에 와야합니다. "+"또한 리스트의 마지막 데이터도 중괄호로 닫아주세요.");

        UserMessage userMessage = new UserMessage(userMessageContent);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        ChatResponse response = chatClient.call(prompt);
        String jsonResponse = response.getResult().getOutput().getContent();
        System.out.println(jsonResponse);

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
