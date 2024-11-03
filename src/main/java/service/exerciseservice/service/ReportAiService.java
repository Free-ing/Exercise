//package service.exerciseservice.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import service.exerciseservice.dto.ResponseExerciseDto;
//import service.exerciseservice.dto.SurveyResultDto;
//import service.exerciseservice.entity.ExerciseRoutineRecord;
//import service.exerciseservice.entity.ExerciseWeekRecord;
//import service.exerciseservice.repository.ExerciseWeekRecordRepository;
//
//import java.util.List;
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class ReportAiService {
//    private final ChatClient reportChatClient;
//    private final ObjectMapper objectMapper;
//    private final ExerciseQueryService exerciseQueryService;
//    private final ExerciseWeekRecordRepository exerciseWeekRecordRepository;
//
//    //Todo: ai 운동 추천
//
//    public List<ResponseExerciseDto.AiExerciseResponseDto> generateHobbyRecommendations() {
//
//        List<ResponseExerciseDto.ExerciseRoutineGroupDto> recordList = exerciseQueryService.getRoutinesGroupedByUser();
//
//        String userMessageContent = String.format("설문조사 결과를 바탕으로 운동 추천 이유를 간단하게 제시해줘.: %s", survey);
//
//        String jsonResponse = reportChatClient.prompt()
//                .user(userMessageContent)
//                .call()
//                .content();
//
//
//        for (ResponseExerciseDto.ExerciseRoutineGroupDto record : recordList) {
//
//            ExerciseWeekRecord exerciseWeekRecord = ExerciseWeekRecord.builder()
//                    .averageTime(record.getRoutines().)
//                    .build();
//        }
//
//    }
//
//
//
//
