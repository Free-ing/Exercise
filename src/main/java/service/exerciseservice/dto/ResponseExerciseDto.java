package service.exerciseservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.exerciseservice.entity.BasicService;
import service.exerciseservice.entity.ExerciseRoutineRecord;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseExerciseDto {

    public static Object ExerciseRoutineDto;
    @JsonProperty("recommendations")
    @Getter
    private List<AiExerciseResponseDto> AiRecommendations;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AiExerciseResponseDto{
        private String hobbyName;
        private String explanation;

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ExerciseRoutineDto{
        private String hobbyName;
        private String imageUrl;
        private Long routineId;
        private Boolean monthday;
        private Boolean tuesday;
        private Boolean wednesday;
        private Boolean thursday;
        private Boolean friday;
        private Boolean saturday;
        private Boolean sunday;
        private Boolean status;
        private LocalTime startTime;
        private LocalTime endTime;
        private String explanation;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DayRoutineDto{
        private String Name;
        private String imageUrl;
        private Long routineId;
        private Boolean monthday;
        private Boolean tuesday;
        private Boolean wednesday;
        private Boolean thursday;
        private Boolean friday;
        private Boolean saturday;
        private Boolean sunday;
        private Boolean status;
        private LocalTime startTime;
        private LocalTime endTime;
        private String explanation;
        private BasicService basicService;

        private Long recordId;
        private Boolean complete;

    }

    @Getter
    @NoArgsConstructor
    public static class ExerciseRoutineGroupDto {
        private Long userId;
        private List<RoutineWithRecordsDto> routines;

        @Builder
        public ExerciseRoutineGroupDto(Long userId, List<RoutineWithRecordsDto> routines) {
            this.userId = userId;
            this.routines = routines;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class RoutineWithRecordsDto {
            private Long routineId;
            private String exerciseName;
            private List<RecordDto> records;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class RecordDto {
            private Long recordId;
            private LocalDate completeDay;
            private long exerciseDurationTime;
            private boolean complete;
        }
    }
}
