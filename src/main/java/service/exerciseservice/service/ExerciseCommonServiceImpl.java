package service.exerciseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.repository.ExerciseRoutineRepository;

import static service.exerciseservice.converter.Converter.toRoutineEntity;

@Service
@RequiredArgsConstructor
public class ExerciseCommonServiceImpl implements ExerciseCommonService {

    private final ExerciseRoutineRepository exerciseRoutineRepository;

    //Todo: 운동 루틴 추가하기
    @Override
    public Long addExerciseRoutine(Long userId, RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto){
        System.out.println(exerciseRoutineDto.getRoutineName());
        return exerciseRoutineRepository.save(toRoutineEntity(exerciseRoutineDto)).getId();
    }

}
