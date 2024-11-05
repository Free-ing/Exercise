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
        private String exerciseName;
        private String explanation;

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ExerciseRoutineDto{
        private String hobbyName;
        private String imageUrl;
        private Long routineId;
        private Boolean monday;
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
        private Boolean monday;
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
        public ExerciseRoutineGroupDto(Long userId,long duration, List<RoutineWithRecordsDto> routines) {
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
            private String explanation;
            private List<RecordDto> records;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class RecordDto {
            private Long recordId;
            private LocalDate completeDay;
            private long duration;
            private String day;
            private boolean complete;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReportDto {
        private long totalExerciseTime;
        private Long avgExerciseTime;
        private LocalDate startTime;
        private LocalDate endTime;
        private Long monTime;
        private Long tueTime;
        private Long wenTime;
        private Long thuTime;
        private Long friTime;
        private Long satTime;
        private Long sunTime;
        private String feedBack;
        private List<ExecuteRoutineDto> exerciseRoutineDtoList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExecuteRoutineDto {
        private String name;
        private String imageUrl;
        private long routineTime;
    }

    // 운동 정보를 임시 저장하기 위한 헬퍼 클래스
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RoutineInfo {
        private final String name;
        private final String imageUrl;
        private long totalTime;

        public void addTime(long time) {
            this.totalTime += time;
        }
    }

    @Getter
    @NoArgsConstructor
    @Builder
    public static class DayCompleteRoutine {
        private LocalDate completeDate;

        // JPQL new 연산자를 위한 생성자 추가
        public DayCompleteRoutine(LocalDate completeDate) {
            this.completeDate = completeDate;
        }
    }
}
