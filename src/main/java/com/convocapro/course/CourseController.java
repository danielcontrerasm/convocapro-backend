package com.convocapro.course;

import com.convocapro.activity.PracticeActivityRepository;
import com.convocapro.user.ProfileType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseUnitRepository units;
    private final PracticeActivityRepository activities;

    public CourseController(CourseUnitRepository units, PracticeActivityRepository activities) {
        this.units = units;
        this.activities = activities;
    }

    @GetMapping
    public List<CourseDto> byProfile(@RequestParam ProfileType profile) {
        ProfileType profileToLoad = profile == ProfileType.FULL ? ProfileType.TECNICO : profile;

        return units.findByProfileOrderBySortOrderAsc(profileToLoad).stream()
                .map(u -> new CourseDto(
                        u.getId(),
                        u.getTitle(),
                        u.getDescription(),
                        u.getVideoUrl(),
                        u.getPdfUrl(),
                        u.getExcelUrl(),
                        activities.findByUnitId(u.getId()).stream().map(CourseDto.ActivityDto::from).toList()
                ))
                .toList();
    }
}
