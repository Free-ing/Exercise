package service.exerciseservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.SurveyResultDto;
import service.exerciseservice.entity.ExerciseRoutineRecord;
import service.exerciseservice.entity.ExerciseWeekRecord;
import service.exerciseservice.repository.ExerciseWeekRecordRepository;

import java.time.LocalDate;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class ReportAiService {
    private final ChatClient reportChatClient;
    private final ObjectMapper objectMapper;
    private final ExerciseQueryService exerciseQueryService;
    private final ExerciseWeekRecordRepository exerciseWeekRecordRepository;
    private final TranslationService translationService;

    //Todo: ai 피드백
    @Scheduled(cron = "0 30 0 * * MON") // 매주 월요일 0시에 실행
    @Transactional
    public void createAiFeedBack() throws JsonProcessingException {
        // 지난 주의 시작일(월요일)과 종료일(일요일) 계산
//        LocalDate endDate = LocalDate.now().minusDays(1); // 어제(일요일)
//        LocalDate startDate = endDate.minusDays(6); // 지난주 월요일

        LocalDate todayDate = LocalDate.parse("2024-12-09");
        LocalDate endDate = todayDate.minusDays(1); // 어제(일요일)
        LocalDate startDate = endDate.minusDays(6); // 지난주 월요일

        System.out.println("시작");
        List<ResponseExerciseDto.ExerciseRoutineGroupDto> recordList = exerciseQueryService.getRoutinesGroupedByUser(startDate,endDate);
        for (ResponseExerciseDto.ExerciseRoutineGroupDto record : recordList) {

            System.out.println("리스트:"+record.toString());
            String recordJson = objectMapper.writeValueAsString(record);

            ExerciseWeekRecord exerciseWeekRecord = exerciseWeekRecordRepository.findByUserId(record.getUserId(),startDate, endDate);

            String userMessageContent = String.format("Analyze the user's workout history and provide personalized recommendations and guidelines.: %s", recordJson);


            String jsonResponse = reportChatClient.prompt()
                    .user(userMessageContent)
                    .call()
                    .content();

            String translatedContent = translationService.translateToKorean(jsonResponse);

            exerciseWeekRecord.setAiFeedback(translatedContent);
            System.out.println(translatedContent);
        }
    }
}




