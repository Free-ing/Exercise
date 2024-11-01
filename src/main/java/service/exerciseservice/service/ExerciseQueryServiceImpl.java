package service.exerciseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.repository.ExerciseRoutineRecordRepository;
import service.exerciseservice.repository.ExerciseRoutineRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseQueryServiceImpl implements ExerciseQueryService{

    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final ExerciseRoutineRecordRepository exerciseRoutineRecordRepository;

    //Todo: 마음 채우기 루틴 리스트 조회
    @Override
    public List<RequestExerciseDto.ExerciseRoutineDto> getExerciseRoutineList(Long userId){
        return exerciseRoutineRepository.findByUserId(userId)
                .stream()
                .map(exerciseRoutine -> RequestExerciseDto.ExerciseRoutineDto.builder()
                        .routineName(exerciseRoutine.getExerciseName())
                        .routineId(exerciseRoutine.getId())
                        .imageUrl(exerciseRoutine.getImageUrl())
                        .status(exerciseRoutine.getStatus())
                        .monday(exerciseRoutine.getMonday())
                        .tuesday(exerciseRoutine.getTuesday())
                        .wednesday(exerciseRoutine.getWednesday())
                        .thursday(exerciseRoutine.getThursday())
                        .friday(exerciseRoutine.getFriday())
                        .saturday(exerciseRoutine.getSaturday())
                        .sunday(exerciseRoutine.getSunday())
                        .startTime(exerciseRoutine.getStartTime())
                        .endTime(exerciseRoutine.getEndTime())
                        .explanation(exerciseRoutine.getExplanation())
                        .build())
                .collect(Collectors.toList());

    }

    //Todo: 일별 루틴 일정 조회
    @Override
    public  List<ResponseExerciseDto.DayRoutineDto> getDayRoutine(LocalDate date, Long userId){

        List<ResponseExerciseDto.DayRoutineDto> dayRoutineDtoList = exerciseRoutineRecordRepository.getDayRoutine(date,userId, true);
        System.out.println(dayRoutineDtoList);
        return dayRoutineDtoList;

    }
}
