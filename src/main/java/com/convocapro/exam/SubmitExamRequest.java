package com.convocapro.exam;

import java.util.List;

public record SubmitExamRequest(Long userId, List<AnswerRequest> answers) {}
