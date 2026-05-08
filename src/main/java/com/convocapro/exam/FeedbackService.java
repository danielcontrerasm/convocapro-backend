package com.convocapro.exam;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class FeedbackService {

    private static final Map<String, CategoryFeedback> CATEGORY_FEEDBACK = new LinkedHashMap<>();
    private static final Map<String, CategoryAlert> CATEGORY_ALERTS = new LinkedHashMap<>();

    static {
        CATEGORY_FEEDBACK.put("Constitución Política", new CategoryFeedback(
                "Excelente dominio de la Constitución. Comprendes derechos fundamentales, estructura del Estado y principios constitucionales.",
                "Buen nivel, pero debes reforzar derechos fundamentales, acciones constitucionales y funciones de las ramas del poder.",
                "Debes estudiar artículos clave (Art. 2, 11-41) y la estructura del Estado. Es un eje fundamental del examen."
        ));
        CATEGORY_FEEDBACK.put("Estructura del Estado", new CategoryFeedback(
                "Dominas la organización del Estado y funciones de entidades territoriales.",
                "Refuerza funciones de alcaldías, gobernaciones y corporaciones públicas.",
                "Debes entender cómo se organiza el Estado colombiano y sus niveles territorial y nacional."
        ));
        CATEGORY_FEEDBACK.put("MIPG", new CategoryFeedback(
                "Excelente comprensión del MIPG y sus dimensiones.",
                "Refuerza las 7 dimensiones, especialmente Control Interno y Talento Humano.",
                "Debes estudiar el MIPG completo, sus dimensiones y su aplicación en la gestión pública."
        ));
        CATEGORY_FEEDBACK.put("Control Interno", new CategoryFeedback(
                "Buen dominio de los principios de control interno y evaluación.",
                "Refuerza roles, responsabilidades y modelos de control (MECI).",
                "Debes estudiar fundamentos de control interno y su aplicación en entidades públicas."
        ));
        CATEGORY_FEEDBACK.put("Gestión Pública", new CategoryFeedback(
                "Comprendes bien la gestión pública y su orientación a resultados.",
                "Refuerza planeación, evaluación y gestión por resultados.",
                "Debes estudiar principios básicos de gestión pública y funcionamiento del Estado."
        ));
        CATEGORY_FEEDBACK.put("Contratación Estatal", new CategoryFeedback(
                "Excelente dominio de la contratación pública y sus principios.",
                "Refuerza modalidades de contratación y supervisión.",
                "Debes estudiar Ley 80, Ley 1150 y el proceso contractual completo."
        ));
        CATEGORY_FEEDBACK.put("Presupuesto Público", new CategoryFeedback(
                "Buen manejo del sistema presupuestal.",
                "Refuerza ejecución y clasificación del presupuesto.",
                "Debes estudiar principios presupuestales y ciclo del presupuesto público."
        ));
        CATEGORY_FEEDBACK.put("Función Pública", new CategoryFeedback(
                "Excelente comprensión del régimen del servidor público y carrera administrativa.",
                "Refuerza deberes, derechos e inhabilidades.",
                "Debes estudiar régimen disciplinario (Ley 1952) y normas de la función pública (Ley 909)."
        ));
        CATEGORY_FEEDBACK.put("Razonamiento Lógico", new CategoryFeedback(
                "Excelente capacidad analítica y resolución de problemas.",
                "Buen nivel, pero necesitas mejorar velocidad y precisión.",
                "Debes practicar ejercicios de lógica y patrones con mayor frecuencia."
        ));
        CATEGORY_FEEDBACK.put("Competencias Comunicativas", new CategoryFeedback(
                "Muy buena comprensión lectora y análisis de textos.",
                "Refuerza inferencias, interpretación y análisis crítico.",
                "Debes practicar lectura crítica y comprensión de textos complejos."
        ));
        CATEGORY_FEEDBACK.put("Competencias Ciudadanas", new CategoryFeedback(
                "Buen entendimiento de valores ciudadanos, convivencia y ética pública.",
                "Refuerza aplicación de normas y resolución de conflictos.",
                "Debes estudiar principios de convivencia, ética y ciudadanía."
        ));
        CATEGORY_FEEDBACK.put("Ofimática", new CategoryFeedback(
                "Buen dominio de herramientas digitales de oficina.",
                "Refuerza funciones específicas (Excel, Word).",
                "Debes practicar herramientas básicas de ofimática."
        ));
        CATEGORY_FEEDBACK.put("Tecnologías de la Información", new CategoryFeedback(
                "Buen conocimiento en sistemas y herramientas digitales.",
                "Refuerza conceptos clave de TIC y Gobierno Digital.",
                "Debes estudiar fundamentos básicos de tecnologías de información."
        ));
        CATEGORY_FEEDBACK.put("Atención al Ciudadano", new CategoryFeedback(
                "Excelente enfoque en servicio al ciudadano y protocolos de atención.",
                "Refuerza protocolos, calidad del servicio y manejo de PQRSD.",
                "Debes estudiar principios de atención y servicio al usuario (Ley 1437)."
        ));
        CATEGORY_FEEDBACK.put("Planeación", new CategoryFeedback(
                "Excelente comprensión de procesos de planeación estratégica.",
                "Refuerza formulación, indicadores y seguimiento de metas.",
                "Debes estudiar planeación estratégica y operativa (Plan de Acción)."
        ));

        CATEGORY_ALERTS.put("Constitución Política", new CategoryAlert(
                60,
                "⚠️ **ALERTA CONSTITUCIONAL:** Tu desempeño en Constitución Política es bajo (%s%%). Refuerza derechos fundamentales, acciones constitucionales y estructura del Estado."
        ));
        CATEGORY_ALERTS.put("Estructura del Estado", new CategoryAlert(
                60,
                "⚠️ **ALERTA DE ESTRUCTURA ESTATAL:** Tu desempeño en Estructura del Estado es bajo (%s%%). Revisa organización territorial, ramas del poder público y funciones de entidades."
        ));
        CATEGORY_ALERTS.put("MIPG", new CategoryAlert(
                60,
                "⚠️ **ALERTA MIPG:** Tu desempeño en MIPG es bajo (%s%%). Refuerza las 7 dimensiones, líneas de defensa, Control Interno y Talento Humano."
        ));
        CATEGORY_ALERTS.put("Control Interno", new CategoryAlert(
                60,
                "⚠️ **ALERTA DE CONTROL INTERNO:** Tu desempeño en Control Interno es bajo (%s%%). Revisa MECI, roles, responsabilidades y evaluación independiente."
        ));
        CATEGORY_ALERTS.put("Gestión Pública", new CategoryAlert(
                60,
                "⚠️ **ALERTA DE GESTIÓN PÚBLICA:** Tu desempeño en Gestión Pública es bajo (%s%%). Refuerza principios administrativos, gestión por resultados y funcionamiento estatal."
        ));
        CATEGORY_ALERTS.put("Contratación Estatal", new CategoryAlert(
                60,
                "⚠️ **ALERTA CONTRACTUAL:** Tu desempeño en Contratación Estatal es bajo (%s%%). Estudia Ley 80, Ley 1150, modalidades de selección y supervisión contractual."
        ));
        CATEGORY_ALERTS.put("Presupuesto Público", new CategoryAlert(
                60,
                "⚠️ **ALERTA PRESUPUESTAL:** Tu desempeño en Presupuesto Público es bajo (%s%%). Revisa principios presupuestales, ejecución, CDP, registro presupuestal y ciclo presupuestal."
        ));
        CATEGORY_ALERTS.put("Función Pública", new CategoryAlert(
                60,
                "⚠️ **ALERTA DE FUNCIÓN PÚBLICA:** Tu desempeño en Función Pública es bajo (%s%%). Refuerza Ley 909, carrera administrativa, deberes, inhabilidades y régimen disciplinario."
        ));
        CATEGORY_ALERTS.put("Razonamiento Lógico", new CategoryAlert(
                60,
                "⚠️ **ALERTA DE RAZONAMIENTO:** Tu desempeño en Razonamiento Lógico es bajo (%s%%). Practica patrones, secuencias, proporciones, inferencias y resolución rápida de problemas."
        ));
        CATEGORY_ALERTS.put("Competencias Comunicativas", new CategoryAlert(
                60,
                "⚠️ **ALERTA COMUNICATIVA:** Tu desempeño en Competencias Comunicativas es bajo (%s%%). Refuerza comprensión lectora, inferencias, idea principal y análisis crítico."
        ));
        CATEGORY_ALERTS.put("Competencias Ciudadanas", new CategoryAlert(
                60,
                "⚠️ **ALERTA CIUDADANA:** Tu desempeño en Competencias Ciudadanas es bajo (%s%%). Revisa ética pública, convivencia, resolución de conflictos e integridad."
        ));
        CATEGORY_ALERTS.put("Ofimática", new CategoryAlert(
                60,
                "⚠️ **ALERTA DE OFIMÁTICA:** Tu desempeño en Ofimática es bajo (%s%%). Practica funciones básicas de Excel, Word, PowerPoint y manejo de documentos."
        ));
        CATEGORY_ALERTS.put("Tecnologías de la Información", new CategoryAlert(
                60,
                "⚠️ **ALERTA TIC:** Tu desempeño en Tecnologías de la Información es bajo (%s%%). Refuerza Gobierno Digital, seguridad digital, datos abiertos y sistemas de información."
        ));
        CATEGORY_ALERTS.put("Atención al Ciudadano", new CategoryAlert(
                60,
                "⚠️ **ALERTA LEGAL:** Tus conocimientos en CPACA (Ley 1437) son bajos (%s%%). Este eje es eliminatorio en la práctica por su alto volumen de preguntas en Territorial 12."
        ));
        CATEGORY_ALERTS.put("Planeación", new CategoryAlert(
                60,
                "⚠️ **ALERTA DE PLANEACIÓN:** Tu desempeño en Planeación es bajo (%s%%). Refuerza plan de acción, indicadores, metas, seguimiento y planeación estratégica."
        ));
    }

    public String generateGeneralFeedback(Map<String, AxisResult> results) {
        return generateDetailedReport(results);
    }

    public String generateDetailedReport(Map<String, AxisResult> axisResults) {
        if (axisResults.isEmpty()) {
            return "";
        }

        AxisResult weakest = axisResults.values().stream()
                .filter(result -> result.getTotal() > 0)
                .min(Comparator.comparing(AxisResult::getPercentage))
                .orElse(null);

        StringBuilder report = new StringBuilder();
        report.append("## Resultado Final del Simulacro\n\n");
        if (weakest == null) {
            report.append("> No hay preguntas evaluadas para calcular una debilidad principal.\n\n");
        } else {
            report.append("> **Tu mayor debilidad es: ").append(weakest.getAxisName()).append("**\n");
            report.append("> ").append(getSpecificFeedback(weakest.getAxisName(), weakest.getPercentage())).append("\n\n");
        }

        report.append("### Retroalimentación por Categoría\n\n");
        CATEGORY_FEEDBACK.keySet().forEach(category -> {
            AxisResult result = axisResults.get(category);
            if (result == null || result.getTotal() == 0) {
                report.append("- **")
                        .append(category)
                        .append(" (sin preguntas evaluadas)**: ")
                        .append("No hubo preguntas clasificadas en esta categoría durante este simulacro.\n");
                return;
            }

            report.append("- **")
                    .append(category)
                    .append(" (")
                    .append(result.getPercentage())
                    .append("%)**: ")
                    .append(getSpecificFeedback(category, result.getPercentage()))
                    .append("\n");
        });
        report.append("\n");

        appendCategoryAlerts(report, axisResults);

        return report.toString();
    }

    private void appendCategoryAlerts(StringBuilder report, Map<String, AxisResult> axisResults) {
        CATEGORY_ALERTS.forEach((category, alert) -> {
            AxisResult result = axisResults.get(category);
            if (result != null && result.getTotal() > 0 && result.getPercentage() < alert.threshold()) {
                report.append(String.format(alert.message(), result.getPercentage())).append("\n\n");
            }
        });
    }

    public String getSpecificFeedback(String category, double percentage) {
        CategoryFeedback feedback = CATEGORY_FEEDBACK.get(category);
        if (feedback == null) {
            if (percentage >= 80) return "Buen dominio del tema. Mantén práctica constante.";
            if (percentage >= 50) return "Nivel intermedio. Revisa errores y refuerza conceptos.";
            return "Tema débil. Se recomienda estudiar teoría antes de repetir simulacros.";
        }

        if (percentage >= 80) return feedback.high;
        if (percentage >= 50) return feedback.medium;
        return feedback.low;
    }

    public Set<String> getAllCategories() {
        return CATEGORY_FEEDBACK.keySet();
    }

    private record CategoryFeedback(String high, String medium, String low) {}
    private record CategoryAlert(double threshold, String message) {}
}
