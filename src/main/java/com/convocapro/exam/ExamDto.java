package com.convocapro.exam;

public record ExamDto(
        String title,
        String examType,
        String badge,
        String description,
        int questions,
        boolean active
) {}
