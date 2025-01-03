package service.exerciseservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
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
    public BaseResponse<String> onExerciseRoutine(
            @RequestParam LocalDate date,
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        exerciseCommonService.onExerciseRoutine(routineId,date, userId);
        return BaseResponse.onSuccess("성공적으로 루틴 일정을 켰습니다.");
    }
    //Todo: 운동 루틴 끄기
    @PatchMapping("/{routineId}/off")
    public BaseResponse<String> offExerciseRoutine(
            @RequestParam LocalDate date,
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        exerciseCommonService.offExerciseRoutine(routineId,date,userId);
        return BaseResponse.onSuccess("성공적으로 루틴 일정을 껐습니다.");
    }

    //Todo: 운동 수행 완료
    @PatchMapping("/{routineId}/complete")
    public BaseResponse<String> completeExerciseRoutineRecord(
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        exerciseCommonService.completeRoutine(routineId, userId);
        return BaseResponse.onSuccess("성공적으로 일정을 수행하였습니다.");
    }


    //Todo: 운동 수행 취소
    @PatchMapping("/{routineId}/cancel")
    public BaseResponse<String> cancelExerciseRoutineRecord(
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        exerciseCommonService.cancelRoutine(routineId, userId);
        return BaseResponse.onSuccess("일정 수행완료를 취소하였습니다.");
    }

    //Todo: 운동 루틴 삭제
    @DeleteMapping("/{routineId}")
    public BaseResponse<String> deleteRoutine(
            @PathVariable Long routineId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        exerciseCommonService.deleteRoutine(routineId, userId);
        return BaseResponse.onSuccess("성공적으로 루틴을 삭제했습니다.");
    }

    //Todo: 운동 루틴 수정
    @PutMapping("/{routineId}")
    public BaseResponse<Long> updateRoutine(
            @RequestBody RequestExerciseDto.RoutineUpdateDto routineUpdateDto,
            @PathVariable Long routineId,
            @RequestParam LocalDate today,
            @RequestHeader("Authorization") String authorizationHeader

    ){

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        Long updateRoutineId = exerciseCommonService.updateRoutine(userId, routineId, routineUpdateDto, today);
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
        // 지난 주의 시작일(월요일)과 종료일(일요일) 계산
//        LocalDate endDate = LocalDate.now().minusDays(1); // 어제(일요일)
//        LocalDate startDate = endDate.minusDays(6); // 지난주 월요일

        LocalDate todayDate = LocalDate.parse("2024-11-04");
        LocalDate endDate = todayDate.minusDays(1); // 어제(일요일)
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

    ) throws JsonProcessingException {
        reportAiService.createAiFeedBack();
    }

    //Todo: 운동 피드백 리스트 조회(날짜)
    @GetMapping("/report-list")
    public BaseResponse<List<ResponseExerciseDto.FeedbackDayListDto>> getFeedbackDayList(
            @RequestParam int year,
            @RequestParam int month,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(exerciseQueryService.getFeedbackDayList(year, month,userId));
    }
//
//    //Todo: 운동 피드백 상세 조회
//    @GetMapping("/report/{reportId}")
//    public BaseResponse<ResponseExerciseDto.ReportDto> getReportList(
//            @PathVariable Long reportId,
//            @RequestHeader("Authorization") String authorizationHeader
//
//    ){
//        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
//        return BaseResponse.onSuccess(exerciseQueryService.getFeedback(reportId, userId));
//    }

    //Todo: 운동 피드백 상세 조회
    @GetMapping("/report")
    public BaseResponse<ResponseExerciseDto.ReportDto> getReportList(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestHeader("Authorization") String authorizationHeader

    ){

        System.out.println("요청 받음!!!!!!!!!!!1");
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        ResponseExerciseDto.ReportDto reportDto = exerciseQueryService.getFeedback(startDate,endDate, userId);
        System.out.println(reportDto.toString());
        System.out.println("요청 완료");
        return BaseResponse.onSuccess(reportDto);
    }

    //Todo: 하나라도 수행한 일정이 있다면 조회하는 그 날짜 반환하기
    @GetMapping("/home/record-week")
    public BaseResponse<?> getDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);

        List<ResponseExerciseDto.DayCompleteRoutine> existingDates =
                exerciseQueryService.getCompleteDate(startDate, endDate, userId);

        if (existingDates.isEmpty()) {
            return BaseResponse.onSuccess(Collections.emptyList());
        }

        return BaseResponse.onSuccess(existingDates);
    }

    //Todo : 쉬어가기
    @PatchMapping("/record-off/{recordId}")
    public BaseResponse<String> offTodayRecord(
            @PathVariable Long recordId,
            @RequestHeader("Authorization") String authorizationHeader

    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);

        exerciseCommonService.offDayRecord(recordId, userId);

        return BaseResponse.onSuccess("성공적으로 루틴 쉬어가기를 완료했습니다.");

    }
}
