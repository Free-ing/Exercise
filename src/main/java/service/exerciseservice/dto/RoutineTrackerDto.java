package service.exerciseservice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoutineTrackerDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ExerciseRoutineTrackerDto {
        private String exerciseName;
        private String imageUrl;
        private List<ExerciseRecordDto> records = new ArrayList<>();

        public ExerciseRoutineTrackerDto(String hobbyName) {
        this.exerciseName = hobbyName;
        }
        public void  setImageUrl(String imageUrl){this.imageUrl = imageUrl;}
        public void addRecord(ExerciseRecordDto record) {
            this.records.add(record);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ExerciseRecordDto {
        private Long id;
        private LocalDate routineDate;  // LocalDateTime 대신 LocalDate 사용

        public ExerciseRecordDto(Long id, LocalDate routineDate) {
            this.id = id;
            this.routineDate = routineDate;  // LocalDate로 변환
        }
    }
}
