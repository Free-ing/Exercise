package service.exerciseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.repository.ExerciseRoutineRepository;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static service.exerciseservice.converter.Converter.toRoutineEntity;

@Service
@RequiredArgsConstructor
public class ExerciseCommonServiceImpl implements ExerciseCommonService {

    private final ExerciseRoutineRepository exerciseRoutineRepository;

    //Todo: 운동 루틴 추가하기
    @Override
    public Long addExerciseRoutine(Long userId, RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto){
        LocalTime startTime = exerciseRoutineDto.getStartTime();
        LocalTime endTime = exerciseRoutineDto.getEndTime();

        // 운동 시간 계산 (분 단위)
        long durationInMinutes = calculateDuration(startTime, endTime);



        return exerciseRoutineRepository.save(toRoutineEntity(exerciseRoutineDto,durationInMinutes,userId)).getId();
    }



    // 운동 시간 계산 (분 단위)
    private long calculateDuration(LocalTime startTime, LocalTime endTime) {
        // 자정을 넘어가는 경우 처리
        if (endTime.isBefore(startTime)) {
            // 다음 날로 넘어가는 경우, 24시간을 더해서 계산
            return ChronoUnit.MINUTES.between(startTime, endTime.plusHours(24));
        }
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }


}
