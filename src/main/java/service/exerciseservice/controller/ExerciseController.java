package service.exerciseservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.exerciseservice.base.BaseResponse;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.dto.SurveyResultDto;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.service.ExerciseCommonService;
import service.exerciseservice.service.ExerciseQueryService;
import service.exerciseservice.service.OpenAiService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise-service")
public class ExerciseController {


    private final ExerciseCommonService exerciseCommonService;
    private final ExerciseQueryService exerciseQueryService;
    private final OpenAiService openAiService;

    //Todo: 운동루틴 추가
    @PostMapping("/routine/{userId}")
    public BaseResponse<Long> addExerciseRoutine(
            @RequestBody RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto,
            @PathVariable Long userId
    ){
        return BaseResponse.onSuccess(exerciseCommonService.addExerciseRoutine(userId,exerciseRoutineDto));
    }

    //Todo: ai 운동 추천
    @PostMapping("/routine/ai")
    public BaseResponse<List<ResponseExerciseDto.AiExerciseResponseDto>> recommendationExercise(
            @RequestBody SurveyResultDto.surveyResultDto surveyResultDto
    ){

        openAiService.generateHobbyRecommendations(surveyResultDto);
        return BaseResponse.onSuccess(openAiService.generateHobbyRecommendations(surveyResultDto));
    }

    //Todo: 운동 리스트 조회
    @GetMapping("/routine-list/{userId}")
    private BaseResponse<List<RequestExerciseDto.ExerciseRoutineDto>> getRoutineList(
            @PathVariable Long userId){

        return BaseResponse.onSuccess(exerciseQueryService.getExerciseRoutineList(userId));
    }

    //Todo: 운동 루틴 켜기
    @PatchMapping("/{routineId}/on")
    public BaseResponse<String> onMentalRoutine(
            @RequestParam LocalDate date,
            @PathVariable Long routineId
    ){
        exerciseCommonService.onMentalRoutine(routineId,date);
        return BaseResponse.onSuccess("성공적으로 루틴 일정을 켰습니다.");
    }
    //Todo: 운동 루틴 끄기
    @PatchMapping("/{routineId}/off")
    public BaseResponse<String> offMentalRoutine(
            @RequestParam LocalDate date,
            @PathVariable Long routineId
    ){


        exerciseCommonService.offMentalRoutine(routineId,date);
        return BaseResponse.onSuccess("성공적으로 루틴 일정을 껐습니다.");
    }

    //Todo: 운동 수행 완료
    @PatchMapping("/{routineId}/complete")
    public BaseResponse<String> completeExerciseRoutineRecord(
            @PathVariable Long routineId
    ){
        exerciseCommonService.completeRoutine(routineId);
        return BaseResponse.onSuccess("성공적으로 일정을 수행하였습니다.");
    }


    //Todo: 운동 수행 취소
    @PatchMapping("/{routineId}/cancel")
    public BaseResponse<String> cancelExerciseRoutineRecord(
            @PathVariable Long routineId
    ){
        exerciseCommonService.cancelRoutine(routineId);
        return BaseResponse.onSuccess("일정 수행완료를 취소하였습니다.");
    }

    //Todo: 운동 루틴 삭제
    @DeleteMapping("/{routineId}")
    public BaseResponse<String> deleteRoutine(
            @PathVariable Long routineId
    ){
        exerciseCommonService.deleteRoutine(routineId);
        return BaseResponse.onSuccess("성공적으로 루틴을 삭제했습니다.");
    }
}
