package com.convocapro.course;

import com.convocapro.activity.PracticeActivity;
import java.util.List;

public record CourseDto(
        Long id,
        String title,
        String description,
        String videoUrl,
        String pdfUrl,
        String excelUrl,
        List<ActivityDto> activities
) {
    public record ActivityDto(Long id, String type, String prompt, String answerKey) {
        public static ActivityDto from(PracticeActivity a) {
            return new ActivityDto(a.getId(), a.getType().name(), a.getPrompt(), a.getAnswerKey());
        }
    }
}
