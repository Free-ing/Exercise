package service.exerciseservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.exerciseservice.base.BaseResponse;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.service.ExerciseCommonService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise-service")
public class ExerciseController {


    private final ExerciseCommonService exerciseCommonService;
    //Todo: 운동루틴 추가
    @PostMapping("/routine/{userId}")
    public BaseResponse<Long> addExerciseRoutine(
            @RequestBody RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto,
            @PathVariable Long userId
    ){
        return BaseResponse.onSuccess(exerciseCommonService.addExerciseRoutine(userId,exerciseRoutineDto));
    }
}
