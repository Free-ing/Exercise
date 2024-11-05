package service.exerciseservice.converter;

import lombok.Builder;
import service.exerciseservice.dto.RequestExerciseDto;
import service.exerciseservice.dto.ResponseExerciseDto;
import service.exerciseservice.entity.ExerciseRoutine;
import service.exerciseservice.entity.ExerciseWeekRecord;

public class Converter {
    @Builder
    public static ExerciseRoutine toRoutineEntity(RequestExerciseDto.ExerciseRoutineDto exerciseRoutineDto, Long duration, Long userId){
                return ExerciseRoutine.builder()
                .userId(userId)
                .exerciseName(exerciseRoutineDto.getRoutineName())
                .startTime(exerciseRoutineDto.getStartTime())
                .endTime(exerciseRoutineDto.getEndTime())
                .monday(exerciseRoutineDto.getMonday())
                .tuesday(exerciseRoutineDto.getTuesday())
                .wednesday(exerciseRoutineDto.getWednesday())
                .thursday(exerciseRoutineDto.getThursday())
                .friday(exerciseRoutineDto.getFriday())
                .saturday(exerciseRoutineDto.getSaturday())
                .sunday(exerciseRoutineDto.getSunday())
                .explanation(exerciseRoutineDto.getExplanation())
                .status(false)
                .basicService(exerciseRoutineDto.getBasicService())
                        .duration(duration)
                        .imageUrl(exerciseRoutineDto.getImageUrl())
                        .userId(userId)
                .build();
    }


    public static ResponseExerciseDto.ReportDto toReportDto(ExerciseWeekRecord exerciseWeekRecord){
        return ResponseExerciseDto.ReportDto.builder()
                .startTime(exerciseWeekRecord.getStartDate())
                .endTime(exerciseWeekRecord.getEndDate())
                .monTime(exerciseWeekRecord.getMonTime())
                .thuTime(exerciseWeekRecord.getThuTime())
                .satTime(exerciseWeekRecord.getSatTime())
                .friTime(exerciseWeekRecord.getFriTime())
                .satTime(exerciseWeekRecord.getSatTime())
                .sunTime(exerciseWeekRecord.getSunTime())
                .build();
    }
}
