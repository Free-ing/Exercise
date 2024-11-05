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
    private long totalTime;
    private long averageTime;
    
    private long monTime;
    private long tueTime;
    private long wenTime;
    private long thuTime;
    private long friTime;
    private long satTime;
    private long sunTime;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    @Builder
    public ExerciseWeekRecord(LocalDate startDate, LocalDate endDate, 
                             Integer totalTime, Integer averageTime, 
                             Integer monTime, Integer tueTime, Integer wenTime, 
                             Integer thuTime, Integer friTime, Integer satTime, 
                             Integer sunTime, Long userId) {
        this.userId = userId;
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

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }
}