package com.convocapro.activity;

import com.convocapro.course.CourseUnit;
import jakarta.persistence.*;

@Entity
@Table(name = "practice_activities")
public class PracticeActivity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private CourseUnit unit;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @Column(length = 2000)
    private String prompt;

    @Column(length = 2000)
    private String answerKey;

    public Long getId() { return id; }
    public CourseUnit getUnit() { return unit; }
    public ActivityType getType() { return type; }
    public String getPrompt() { return prompt; }
    public String getAnswerKey() { return answerKey; }

    public void setUnit(CourseUnit unit) { this.unit = unit; }
    public void setType(ActivityType type) { this.type = type; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public void setAnswerKey(String answerKey) { this.answerKey = answerKey; }
}
