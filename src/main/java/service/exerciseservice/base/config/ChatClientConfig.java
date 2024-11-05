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
                    "exerciseName": "운동1",
                    "explanation": "취미 설명"
                },
                {
                    "exerciseName": "운동2",
                    "explanation": "운동 설명"
                }
            ]
        }
        """;

    private static final String DIET_SYSTEM_PROMPT = """
        - 당신은 운동 패턴 분석 전문가입니다. 너가 사용자에게 해주고 싶은 피드백을 제시해줘.
        - 설명이 null이라면 주어진 정보로만 분석해줘.
        - 반환할 때 "** **" 강조표시나 "#" 로 목차로 나누지 말고 그냥 제시해주세요.
        - 다음 형식을 지켜서 제시해주세요.
        
        1. {루틴1}
         - 분석:
         - 잘하고 있는 부분:
         - 개선해야할 부분:
         - 운동 강도의 적절성:
         
        2. {루틴2}
         - 분석:
         - 잘하고 있는 부분:
         - 개선해야할 부분:
         - 운동 강도의 적절성:
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
