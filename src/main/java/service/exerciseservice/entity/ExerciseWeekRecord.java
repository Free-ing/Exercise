package service.exerciseservice.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseWeekRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseWeekRecordId;

    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalTime;
    private Integer averageTime;
    
    private Integer monTime;
    private Integer tueTime;
    private Integer wenTime;
    private Integer thuTime;
    private Integer friTime;
    private Integer satTime;
    private Integer sunTime;

    @Builder
    public ExerciseWeekRecord(LocalDate startDate, LocalDate endDate, 
                             Integer totalTime, Integer averageTime, 
                             Integer monTime, Integer tueTime, Integer wenTime, 
                             Integer thuTime, Integer friTime, Integer satTime, 
                             Integer sunTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalTime = totalTime;
        this.averageTime = averageTime;
        this.monTime = monTime;
        this.tueTime = tueTime;
        this.wenTime = wenTime;
        this.thuTime = thuTime;
        this.friTime = friTime;
        this.satTime = satTime;
        this.sunTime = sunTime;
    }
}