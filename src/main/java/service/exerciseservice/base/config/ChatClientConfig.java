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

//    private static final String DIET_SYSTEM_PROMPT = """
//        - 당신은 운동 패턴 분석 전문가입니다. 너가 사용자에게 해주고 싶은 피드백을 제시해줘.
//        - 설명이 null이라면 주어진 정보로만 분석해줘.
//        - 반환할 때 "** **" 강조표시나 "#" 로 목차로 나누지 말고 그냥 제시해주세요.
//        - 다음 형식을 지켜서 제시해주세요.
//
//        1. {루틴1}
//         - 분석:
//         - 잘하고 있는 부분:
//         - 개선해야할 부분:
//         - 운동 강도의 적절성:
//
//        2. {루틴2}
//         - 분석:
//         - 잘하고 있는 부분:
//         - 개선해야할 부분:
//         - 운동 강도의 적절성:
//        """;

        private static final String DIET_SYSTEM_PROMPT = """
            As a professional exercise pattern analyst, please evaluate the user's routines.
            If no description is provided, base your analysis solely on the available data.
            Present your feedback in plain text without formatting markers.
            The names of exercises appear in [ ].
            Please structure your response as follows:
  
            {{Routine's name}}   
             

            
            {{Routine's name}}                       
            
        """;

    private static final String REPORT_SYSTEM_PROMPT = """
            "You are 'Huringi,' a friendly AI designed to help users improve their sleep quality. Provide warm, supportive, and insightful feedback based on the following sleep data. Do not use symbols such as '*'.For emphasis, feel free to use emojis or adjust the tone instead.\n\n"
            +"Data Summary:\n"
            +"- Total Exercise Time: \\n"
            +"- Average Exercise Time: \\n"
            +"- Routine times performed by exercise: \\n\\n"
            +"- Daily Exercise Time: \\n\\n"
            +"- Please highlight the positive aspects of your exercise habits, offer specific improvements, and make encouraging closing remarks."
            +"- If there is anything you want to be better about the given information, please give me a strict feedback on this as well"
            +Please structure your response as follows:
  
            {1. {Routine's name}}   
               
            {2. {Routine's name}}     
            ~~
            ~~
            
            {3. 권고사항}
                             
            
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
                .defaultSystem(REPORT_SYSTEM_PROMPT)
                .build();
    }
}
