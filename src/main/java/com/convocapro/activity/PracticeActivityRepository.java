package com.convocapro.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PracticeActivityRepository extends JpaRepository<PracticeActivity, Long> {
    List<PracticeActivity> findByUnitId(Long unitId);
}
