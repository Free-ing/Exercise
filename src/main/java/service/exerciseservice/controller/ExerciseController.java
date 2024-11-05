package service.exerciseservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.exerciseservice.base.BaseResponse;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.RoutineTrackerDto;
import service.exerciseservice.dto.SurveyResultDto;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.service.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise-service")
public class ExerciseController {


    private final ExerciseCommonService exerciseCommonService;
    private final ExerciseQueryService exerciseQueryService;
    private final OpenAiService openAiService;
    private final TokenProviderService tokenProviderService;
    private final ReportAiService reportAiService;


    @GetMapping("/health_check")
    public String status(){
        return "Exercise Service is working fine!";
    }



    //Todo: 운동루틴 추가
    @PostMapping("/routine")
    public BaseResponse<Long> addExerciseRoutine(
            @RequestBody RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(exerciseCommonService.addExerciseRoutine(userId,exerciseRoutineDto));
    }

    //Todo: ai 운동 추천
    @PostMapping("/routine/ai")
    public BaseResponse<List<ResponseExerciseDto.AiExerciseResponseDto>> recommendationExercise(
            @RequestBody SurveyResultDto.surveyResultDto surveyResultDto
    ){
        System.out.println("운동 추천 시작!!!!!!!!!!!!!!!");
        openAiService.generateHobbyRecommendations(surveyResultDto);
        return BaseResponse.onSuccess(openAiService.generateHobbyRecommendations(surveyResultDto));
    }

    //Todo: 운동 리스트 조회
    @GetMapping("/routine-list")
    private BaseResponse<List<RequestExerciseDto.ExerciseRoutineDto>> getRoutineList(
//            @PathVariable Long userId

            @RequestHeader("Authorization") String authorizationHeader){

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(exerciseQueryService.getExerciseRoutineList(userId));
    }

    //Todo: 운동 루틴 켜기
    @PatchMapping("/{routineId}/on")
    public BaseResponse<String> onMentalRoutine(
            @RequestParam LocalDate date,
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        exerciseCommonService.onMentalRoutine(routineId,date);
        return BaseResponse.onSuccess("성공적으로 루틴 일정을 켰습니다.");
    }
    //Todo: 운동 루틴 끄기
    @PatchMapping("/{routineId}/off")
    public BaseResponse<String> offMentalRoutine(
            @RequestParam LocalDate date,
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){


        exerciseCommonService.offMentalRoutine(routineId,date);
        return BaseResponse.onSuccess("성공적으로 루틴 일정을 껐습니다.");
    }

    //Todo: 운동 수행 완료
    @PatchMapping("/{routineId}/complete")
    public BaseResponse<String> completeExerciseRoutineRecord(
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        exerciseCommonService.completeRoutine(routineId);
        return BaseResponse.onSuccess("성공적으로 일정을 수행하였습니다.");
    }


    //Todo: 운동 수행 취소
    @PatchMapping("/{routineId}/cancel")
    public BaseResponse<String> cancelExerciseRoutineRecord(
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        exerciseCommonService.cancelRoutine(routineId);
        return BaseResponse.onSuccess("일정 수행완료를 취소하였습니다.");
    }

    //Todo: 운동 루틴 삭제
    @DeleteMapping("/{routineId}")
    public BaseResponse<String> deleteRoutine(
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        exerciseCommonService.deleteRoutine(routineId);
        return BaseResponse.onSuccess("성공적으로 루틴을 삭제했습니다.");
    }

    //Todo: 운동 루틴 수정
    @PutMapping("/{routineId}")
    public BaseResponse<Long> updateRoutine(
            @RequestBody RequestExerciseDto.RoutineUpdateDto routineUpdateDto,
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        Long updateRoutineId = exerciseCommonService.updateRoutine(userId, routineId, routineUpdateDto);
        return BaseResponse.onSuccess(updateRoutineId);
    }

    //Todo: 회원의 운동 데이터 모두 삭제
    @DeleteMapping("/users/{userId}")
    public BaseResponse<String> deleteExerciseData(
            @PathVariable Long userId
    ){
//        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);

        exerciseCommonService.deleteExerciseDate(userId);
        return BaseResponse.onSuccess("성공적으로 회원의 모든 데이터가 삭제됐습니다.");
    }


    //Todo: 운동 기본 기능 구현
    @PostMapping("/default-routine/{userId}")
    public void createDefaultService(
            @PathVariable long userId
    ){
        exerciseCommonService.createDefaultService(userId);
    }

    //Todo: 마음 루틴 트래커 조회
    @GetMapping("/tracker")
    public BaseResponse<List<RoutineTrackerDto.ExerciseRoutineTrackerDto>> getExerciseRoutineTracker(
            @RequestParam int year,
            @RequestParam int month,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(exerciseQueryService.getHobbyRoutineTrackers(userId,year,month));

    }


    //Todo: 일별 일정 조회
    @GetMapping("/home")
    public BaseResponse<List<ResponseExerciseDto.DayRoutineDto>> getDayRoutine(
            @RequestParam LocalDate date,
//            @PathVariable Long userId
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(exerciseQueryService.getDayRoutine(date, userId));
    }

    //Todo: 회원별로 모든 루틴 리스트 조회 테스트
    @GetMapping("/grouped-by-user")
    public BaseResponse<List<ResponseExerciseDto.ExerciseRoutineGroupDto>> getRoutinesGroupedByUser() {
        LocalDate testDate = LocalDate.parse("2024-11-11");
        LocalDate endDate = testDate.minusDays(1); // 어제(일요일)
//        LocalDate endDate = LocalDate.now().minusDays(1); // 어제(일요일)
        LocalDate startDate = endDate.minusDays(6); // 지난주 월요일
        return BaseResponse.onSuccess(exerciseQueryService.getRoutinesGroupedByUser(startDate,endDate)
        );
    }

    //Todo: 운동 보고서 작성
    @PostMapping("/report-list")
    public void getReportList(
            @RequestHeader("Authorization") String authorizationHeader

    ){
        exerciseCommonService.createWeeklyRecords();
    }

    //Todo: ai 피드백
    @PostMapping("/aiFeedback")
    public void getFeedback(
            @RequestHeader("Authorization") String authorizationHeader

    ) throws JsonProcessingException {
        reportAiService.createAiFeedBack();
    }

    //Todo: 운동 피드백 리스트 조회
    @GetMapping("/report-list")
    public BaseResponse<List<ResponseExerciseDto.ReportDto>> getReportList(
            @RequestParam int year,
            @RequestParam int month,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(exerciseQueryService.getFeedbackList(year, month,userId));
    }

    //Todo: 하나라도 수행한 일정이 있다면 조회하는 그 날짜 반환하기

    @GetMapping("/home/record-week/{userId}")
    public BaseResponse<?> getDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @PathVariable Long userId
    ) {
        List<ResponseExerciseDto.DayCompleteRoutine> existingDates =
                exerciseQueryService.getCompleteDate(startDate, endDate, userId);

        if (existingDates.isEmpty()) {
            return BaseResponse.onSuccess(Collections.emptyList());
        }

        return BaseResponse.onSuccess(existingDates);
    }
}
