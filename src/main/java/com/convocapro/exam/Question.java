package com.convocapro.exam;

import com.convocapro.user.ProfileType;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String questionText;

    @Column(length = 1000, nullable = false)
    private String optionA;

    @Column(length = 1000, nullable = false)
    private String optionB;

    @Column(length = 1000, nullable = false)
    private String optionC;

    @Column(length = 1000, nullable = false)
    private String optionD;

    @Column(length = 1, nullable = false)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private ProfileType profile;

    @Column(name = "exam_type")
    private String examType = "GENERIC";

    public Long getId() { return id; }
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public QuestionCategory getCategory() { return category; }
    public Difficulty getDifficulty() { return difficulty; }
    public ProfileType getProfile() { return profile; }
    public String getExamType() { return examType; }

    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public void setCategory(QuestionCategory category) { this.category = category; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public void setProfile(ProfileType profile) { this.profile = profile; }
    public void setExamType(String examType) { this.examType = examType; }
}
