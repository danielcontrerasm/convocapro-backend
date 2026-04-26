package com.convocapro.bootstrap;

import com.convocapro.activity.*;
import com.convocapro.course.*;
import com.convocapro.exam.*;
import com.convocapro.user.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final QuestionRepository questions;
    private final CourseUnitRepository units;
    private final PracticeActivityRepository activities;

    public DataSeeder(
            AppUserRepository users,
            PasswordEncoder encoder,
            QuestionRepository questions,
            CourseUnitRepository units,
            PracticeActivityRepository activities
    ) {
        this.users = users;
        this.encoder = encoder;
        this.questions = questions;
        this.units = units;
        this.activities = activities;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedCourses();
        seedQuestions();
    }

    private void seedUsers() {
        if (users.count() > 0) return;

        AppUser admin = new AppUser();
        admin.setFullName("Admin ConvocaPro");
        admin.setUsername("admin");
        admin.setEmail("admin@convocapro.local");
        admin.setPhone("573001112233");
        admin.setPasswordHash(encoder.encode("1234"));
        admin.setRole(UserRole.ADMIN);
        admin.setProfile(ProfileType.FULL);
        admin.setRetryLimit(10);
        users.save(admin);

        AppUser tecnico = new AppUser();
        tecnico.setFullName("Usuario Técnico Demo");
        tecnico.setUsername("tecnico");
        tecnico.setEmail("tecnico@convocapro.local");
        tecnico.setPhone("573001112244");
        tecnico.setPasswordHash(encoder.encode("1234"));
        tecnico.setProfile(ProfileType.TECNICO);
        users.save(tecnico);
    }

    private void seedCourses() {
        if (units.count() > 0) return;

        for (ProfileType profile : List.of(ProfileType.ASISTENCIAL, ProfileType.TECNICO, ProfileType.PROFESIONAL)) {
            for (int i = 1; i <= 5; i++) {
                CourseUnit unit = new CourseUnit();
                unit.setProfile(profile);
                unit.setSortOrder(i);
                unit.setTitle("Unidad " + i + " - " + profile.name());
                unit.setDescription("Unidad de estudio para " + profile.name() + " con lectura, video, PDF, Excel, actividades y preparación para examen final.");
                unit.setVideoUrl("https://example.com/videos/" + profile.name().toLowerCase() + "/unidad-" + i);
                unit.setPdfUrl("/resources/" + profile.name().toLowerCase() + "-unidad-" + i + ".pdf");
                unit.setExcelUrl("/resources/" + profile.name().toLowerCase() + "-unidad-" + i + ".xlsx");
                unit = units.save(unit);

                for (ActivityType type : ActivityType.values()) {
                    PracticeActivity activity = new PracticeActivity();
                    activity.setUnit(unit);
                    activity.setType(type);
                    activity.setPrompt("Actividad " + type.name() + " para " + unit.getTitle());
                    activity.setAnswerKey("Respuesta guía para " + type.name());
                    activities.save(activity);
                }
            }
        }
    }

    private void seedQuestions() {
        if (questions.count() > 0) return;

        seed(ProfileType.DEMO, 10);
        seed(ProfileType.ASISTENCIAL, 200);
        seed(ProfileType.TECNICO, 200);
        seed(ProfileType.PROFESIONAL, 200);
    }

    private void seed(ProfileType profile, int count) {
        List<String> concepts = List.of(
                "legalidad",
                "transparencia",
                "eficiencia",
                "economía",
                "responsabilidad",
                "ética pública",
                "atención al ciudadano",
                "control interno",
                "planeación",
                "gestión documental"
        );

        Random random = new Random(profile.name().hashCode());

        for (int i = 1; i <= count; i++) {
            String concept = concepts.get(random.nextInt(concepts.size()));
            boolean functional = i % 2 == 0;

            Question q = new Question();
            q.setProfile(profile);
            q.setCategory(functional ? QuestionCategory.FUNCIONAL : QuestionCategory.COMPORTAMENTAL);
            q.setDifficulty(i <= 70 ? Difficulty.EASY : i <= 140 ? Difficulty.MEDIUM : Difficulty.HARD);
            q.setQuestionText("[" + profile.name() + "] Pregunta " + i + ": En un contexto de entidad territorial, ¿cuál opción aplica mejor el concepto de " + concept + "?");
            q.setOptionA("Aplicar decisiones por costumbre sin verificar soporte normativo.");
            q.setOptionB("Actuar con criterio objetivo, soporte documental y orientación al servicio.");
            q.setOptionC("Omitir el seguimiento para ahorrar tiempo administrativo.");
            q.setOptionD("Trasladar la responsabilidad sin informar al ciudadano.");
            q.setCorrectAnswer("B");
            q.setExamType("GENERIC");
            questions.save(q);
        }
    }
}
