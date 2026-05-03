package com.convocapro.exam;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamService service;

    public ExamController(ExamService service) {
        this.service = service;
    }

    @GetMapping("/demo")
    public List<QuestionResponse> demo(@RequestParam(defaultValue = "10") int count) {
        return service.demo(count);
    }

    @GetMapping("/full")
    public List<QuestionResponse> full(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "GENERIC") String examType
    ) {
        return service.full(userId, examType);
    }
    
    @GetMapping
    public List<ExamDto> getAvailableExams() {
        return service.findAvailableExams();
    }

    @PostMapping("/submit")
    public ExamResultResponse submit(@RequestBody SubmitExamRequest req) {
        return service.submit(req);
    }
}
