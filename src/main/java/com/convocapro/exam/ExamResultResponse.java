package com.convocapro.exam;

import java.util.List;
import java.util.Map;

public record ExamResultResponse(
        int score,
        int total,
        double percentage,
        Map<String, CategoryScore> categories,
        String feedback,
        List<QuestionReview> review
) {
    public record CategoryScore(int correct, int total, double percentage, String feedback) {}
    public record QuestionReview(
            Long questionId,
            String selectedOption,
            String correctAnswer,
            boolean correct,
            String axis,
            String category,
            String subcategory
    ) {}
}
