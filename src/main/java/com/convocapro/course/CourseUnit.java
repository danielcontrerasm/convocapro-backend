package com.convocapro.course;

import com.convocapro.user.ProfileType;
import jakarta.persistence.*;

@Entity
@Table(name = "course_units")
public class CourseUnit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String videoUrl;
    private String pdfUrl;
    private String excelUrl;

    @Enumerated(EnumType.STRING)
    private ProfileType profile;

    private Integer sortOrder;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getVideoUrl() { return videoUrl; }
    public String getPdfUrl() { return pdfUrl; }
    public String getExcelUrl() { return excelUrl; }
    public ProfileType getProfile() { return profile; }
    public Integer getSortOrder() { return sortOrder; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
    public void setExcelUrl(String excelUrl) { this.excelUrl = excelUrl; }
    public void setProfile(ProfileType profile) { this.profile = profile; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
