package com.convocapro.exam;

import com.convocapro.user.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExamService {
    private final QuestionRepository questions;
    private final AppUserRepository users;
    private final QuestionClassifier classifier;
    private final FeedbackService feedbackService;

    public ExamService(
            QuestionRepository questions,
            AppUserRepository users,
            QuestionClassifier classifier,
            FeedbackService feedbackService
    ) {
        this.questions = questions;
        this.users = users;
        this.classifier = classifier;
        this.feedbackService = feedbackService;
    }

    public List<QuestionResponse> demo(int count) {
        List<Question> list = questions.findByProfile(ProfileType.DEMO);
        Collections.shuffle(list);
        return list.stream().limit(Math.max(1, count)).map(QuestionResponse::from).toList();
    }

    public List<ExamDto> findAvailableExams() {
        return questions.findExamTypeSummaries().stream()
                .map(summary -> {
                    String examType = summary.getExamType();
                    String title = titleFromExamType(examType);
                    return new ExamDto(
                            title,
                            examType,
                            badgeFromExamType(examType),
                            descriptionFromExamType(examType),
                            Math.toIntExact(summary.getQuestions()),
                            true
                    );
                })
                .toList();
    }

    public List<QuestionResponse> full(Long userId, String examType) {
        AppUser user = users.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ProfileType profile = user.getProfile();

        if (profile == ProfileType.FULL) profile = ProfileType.TECNICO;
        if (profile == ProfileType.DEMO) profile = ProfileType.TECNICO;

        String type = (examType == null || examType.isBlank()) ? "GENERIC" : examType.trim().toUpperCase();

        List<Question> list = questions.findByExamType( type);

        // Backward compatibility: old questions may have NULL examType. If GENERIC returns empty, use old profile query.
        if (list.isEmpty() && "GENERIC".equals(type)) {
            list = questions.findByProfile(profile);
        }

        Collections.shuffle(list);
        return list.stream().limit(200).map(QuestionResponse::from).toList();
    }

    public ExamResultResponse submit(SubmitExamRequest req) {
        AppUser user = null;
        if (req.userId() != null) {
            user = users.findById(req.userId()).orElse(null);
        }

        if (user != null) {
            int used = user.getRetriesUsed() == null ? 0 : user.getRetriesUsed();
            int limit = user.getRetryLimit() == null ? 3 : user.getRetryLimit();
            if (used >= limit) {
                throw new IllegalArgumentException("Retry limit reached");
            }
            user.setRetriesUsed(used + 1);
            users.save(user);
        }

        int score = 0;
        Map<String, Integer> totalByCat = new LinkedHashMap<>();
        Map<String, Integer> correctByCat = new LinkedHashMap<>();
        List<ExamResultResponse.QuestionReview> review = new ArrayList<>();

        for (AnswerRequest answer : req.answers()) {
            Question q = questions.findById(answer.questionId()).orElseThrow();
            ClassificationResult classification = classifier.classify(q.getQuestionText());
            String category = classification.category();

            totalByCat.put(category, totalByCat.getOrDefault(category, 0) + 1);
            boolean ok = q.getCorrectAnswer().equalsIgnoreCase(answer.selectedOption());

            if (ok) {
                score++;
                correctByCat.put(category, correctByCat.getOrDefault(category, 0) + 1);
            }

            review.add(new ExamResultResponse.QuestionReview(
                    q.getId(),
                    answer.selectedOption(),
                    q.getCorrectAnswer(),
                    ok,
                    classification.axis(),
                    classification.category(),
                    classification.subcategory()
            ));
        }

        Map<String, ExamResultResponse.CategoryScore> categories = new LinkedHashMap<>();
        Map<String, AxisResult> feedbackResults = new LinkedHashMap<>();
        for (String category : feedbackService.getAllCategories()) {
            int correct = correctByCat.getOrDefault(category, 0);
            int total = totalByCat.getOrDefault(category, 0);
            AxisResult result = new AxisResult(category, correct, total);
            feedbackResults.put(category, result);
            categories.put(category, new ExamResultResponse.CategoryScore(
                    correct,
                    total,
                    result.getPercentage(),
                    total == 0
                            ? "No hubo preguntas clasificadas en esta categoría durante este simulacro."
                            : feedbackService.getSpecificFeedback(category, result.getPercentage())
            ));
        }

        double percentage = req.answers().isEmpty() ? 0.0 : Math.round(score * 10000.0 / req.answers().size()) / 100.0;
        String feedback = feedbackService.generateGeneralFeedback(feedbackResults);
        return new ExamResultResponse(score, req.answers().size(), percentage, categories, feedback, review);
    }

    private String titleFromExamType(String examType) {
        List<String> parts = Arrays.stream(examType.split("_"))
                .filter(part -> !part.isBlank())
                .map(this::capitalize)
                .toList();

        if (parts.isEmpty()) {
            return "Examen";
        }

        String badge = badgeFromExamType(examType);
        List<String> titleParts = new ArrayList<>(parts);

        if (!badge.isBlank()) {
            titleParts.removeIf(part -> part.equalsIgnoreCase(badge));
            titleParts.add(0, badge);
        }

        if (titleParts.size() > 1 && isNumeric(titleParts.get(titleParts.size() - 1))) {
            String last = titleParts.remove(titleParts.size() - 1);
            return String.join(" ", titleParts) + " - " + last;
        }

        return String.join(" ", titleParts);
    }

    private String badgeFromExamType(String examType) {
        List<String> badges = List.of("PROFESIONAL", "TECNICO", "ASISTENCIAL", "TERRITORIAL", "GENERIC");
        Set<String> parts = new HashSet<>(Arrays.asList(examType.split("_")));
        return badges.stream()
                .filter(parts::contains)
                .findFirst()
                .map(this::capitalize)
                .orElse("");
    }

    private String descriptionFromExamType(String examType) {
        if (examType.contains("TERRITORIAL")) {
            return "Banco técnico ampliado.";
        }
        return "Banco de preguntas disponible.";
    }

    private String capitalize(String value) {
        String lower = value.toLowerCase(Locale.ROOT);
        return lower.substring(0, 1).toUpperCase(Locale.ROOT) + lower.substring(1);
    }

    private boolean isNumeric(String value) {
        return value.chars().allMatch(Character::isDigit);
    }
}
