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
}
