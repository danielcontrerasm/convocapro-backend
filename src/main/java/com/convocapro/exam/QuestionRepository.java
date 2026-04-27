package com.convocapro.exam;

import com.convocapro.user.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByProfile(ProfileType profile);
    List<Question> findByProfileAndExamType(ProfileType profile, String examType);
    List<Question> findByExamType( String examType);
}
