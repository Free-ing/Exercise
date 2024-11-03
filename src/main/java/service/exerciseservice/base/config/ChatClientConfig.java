package service.exerciseservice.base.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    private static final String EXERCISE_SYSTEM_PROMPT = """
        당신은 운동 추천 전문가입니다. 사용자의 설문조사 결과를 바탕으로 4개의 운동을 추천해야 합니다. 
        반드시 다음 형식으로 반환해주세요.
        {
            "recommendations": [
                {
                    "hobbyName": "취미1",
                    "explanation": "취미 설명"
                },
                {
                    "hobbyName": "취미2",
                    "explanation": "취미 설명"
                }
            ]
        }
        """;

    private static final String DIET_SYSTEM_PROMPT = """
        당신은 운동 패턴 분석 전문가입니다. 사용자가 진행한 운동 패턴에 대해서 분석하고 권고사항을 제시해주세요. -
        반드시 다음 형식으로 반환해주세요.
        {
            "dietRecommendations": [
                {
                    "mealType": "아침",
                    "menu": "식단 메뉴",
                    "explanation": "추천 이유"
                },
                {
                    "mealType": "점심",
                    "menu": "식단 메뉴",
                    "explanation": "추천 이유"
                }
            ]
        }
        """;

    @Bean
    public ChatClient recommendationChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(EXERCISE_SYSTEM_PROMPT)
                .build();
    }

    @Bean
    public ChatClient reportChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(DIET_SYSTEM_PROMPT)
                .build();
    }
}
