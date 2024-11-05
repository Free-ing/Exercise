package service.exerciseservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.ai.chat.client.ChatClient;
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

    //Todo: ai 피드백
    public void createAiFeedBack() throws JsonProcessingException {
        LocalDate testDate = LocalDate.parse("2024-11-11");
        LocalDate endDate = testDate.minusDays(1); // 어제(일요일)
//        LocalDate endDate = LocalDate.now().minusDays(1); // 어제(일요일)
        LocalDate startDate = endDate.minusDays(6); // 지난주 월요일

        System.out.println("시작");
        List<ResponseExerciseDto.ExerciseRoutineGroupDto> recordList = exerciseQueryService.getRoutinesGroupedByUser(startDate,endDate);
        for (ResponseExerciseDto.ExerciseRoutineGroupDto record : recordList) {

            System.out.println("리스트:"+record.toString());
            String recordJson = objectMapper.writeValueAsString(record);

            ExerciseWeekRecord exerciseWeekRecord = exerciseWeekRecordRepository.findByUserId(record.getUserId(),startDate, endDate);

            String userMessageContent = String.format("사용자가 진행한 운동에 대해서 분석하고, 권고사항도 제시해줘.: %s", recordJson);

            String jsonResponse = reportChatClient.prompt()
                    .user(userMessageContent)
                    .call()
                    .content();

            exerciseWeekRecord.setAiFeedback(jsonResponse);
            System.out.println(jsonResponse);
        }
    }
}




