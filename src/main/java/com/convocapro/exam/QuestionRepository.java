package com.convocapro.exam;

import com.convocapro.user.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByProfile(ProfileType profile);
    List<Question> findByProfileAndExamType(ProfileType profile, String examType);
    List<Question> findByExamType( String examType);

    @Query("""
            select q.examType as examType, count(q) as questions
            from Question q
            where q.examType is not null and trim(q.examType) <> ''
            group by q.examType
            order by q.examType
            """)
    List<ExamTypeSummary> findExamTypeSummaries();

    interface ExamTypeSummary {
        String getExamType();
        long getQuestions();
    }
}
