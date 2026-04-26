package com.convocapro.exam;

import java.util.List;

public record QuestionResponse(
        Long id,
        String question,
        List<String> options,
        String category,
        String difficulty,
        String profile
) {
    public static QuestionResponse from(Question q) {
        return new QuestionResponse(
                q.getId(),
                q.getQuestionText(),
                List.of(
                        "A. " + q.getOptionA(),
                        "B. " + q.getOptionB(),
                        "C. " + q.getOptionC(),
                        "D. " + q.getOptionD()
                ),
                q.getCategory().name(),
                q.getDifficulty().name(),
                q.getProfile().name()
        );
    }
}
