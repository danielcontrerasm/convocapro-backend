package com.convocapro.exam;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Component
public class QuestionClassifier {

    private static final List<Rule> RULES = List.of(
            new Rule("Constitución y Estado", "Constitución Política", "Derechos y estructura constitucional",
                    "constitucion", "derecho fundamental", "tutela", "accion popular", "accion de cumplimiento",
                    "estado social", "rama legislativa", "rama ejecutiva", "rama judicial", "articulo 2",
                    "articulo 11", "articulo 41"),
            new Rule("Constitución y Estado", "Estructura del Estado", "Organización estatal y territorial",
                    "estructura del estado", "alcaldia", "gobernacion", "concejo", "asamblea", "entidad territorial",
                    "municipio", "departamento", "descentralizacion", "sector central", "sector descentralizado"),
            new Rule("Gestión Pública", "MIPG", "Modelo Integrado de Planeación y Gestión",
                    "mipg", "modelo integrado de planeacion", "dimension", "talento humano", "direccionamiento estrategico",
                    "gestion con valores", "evaluacion de resultados", "informacion y comunicacion"),
            new Rule("Gestión Pública", "Control Interno", "MECI y evaluación independiente",
                    "control interno", "meci", "auditoria interna", "autocontrol", "autogestion", "evaluacion independiente",
                    "lineas de defensa"),
            new Rule("Servicio al Ciudadano y Transparencia", "Atención al Ciudadano", "Servicio, CPACA y PQRSD",
                    "atencion al ciudadano", "servicio al ciudadano", "pqrs", "pqrsd", "peticion", "queja", "reclamo",
                    "sugerencia", "denuncia", "cpaca", "ley 1437", "derecho de peticion", "respuesta al ciudadano"),
            new Rule("Gestión Pública", "Gestión Pública", "Administración pública y gestión por resultados",
                    "gestion publica", "funcion administrativa", "resultado", "eficiencia", "eficacia", "economia",
                    "legalidad", "transparencia", "responsabilidad", "archivo", "gestion documental", "ley 594",
                    "tabla de retencion documental", "trd"),
            new Rule("Gestión Pública", "Contratación Estatal", "Proceso contractual público",
                    "contratacion", "contrato estatal", "ley 80", "ley 1150", "secop", "licitacion", "seleccion abreviada",
                    "minima cuantia", "supervision", "interventoria", "pliego"),
            new Rule("Gestión Pública", "Presupuesto Público", "Ciclo y principios presupuestales",
                    "presupuesto", "apropiacion", "cdp", "registro presupuestal", "pac", "vigencia fiscal",
                    "principio presupuestal", "ejecucion presupuestal"),
            new Rule("Función Pública", "Función Pública", "Servidor público y carrera administrativa",
                    "funcion publica", "servidor publico", "empleo publico", "carrera administrativa", "ley 909",
                    "cnsc", "inhabilidad", "incompatibilidad", "ley 1952", "disciplinario", "deberes"),
            new Rule("Competencias Básicas", "Razonamiento Lógico", "Lógica y patrones",
                    "razonamiento logico", "secuencia", "patron", "analogias", "silogismo", "probabilidad",
                    "proporcion", "porcentaje", "grafica"),
            new Rule("Competencias Básicas", "Competencias Comunicativas", "Lectura crítica",
                    "comprension lectora", "lectura critica", "texto", "inferir", "inferencia", "idea principal",
                    "argumento", "sinonimo", "antonimo"),
            new Rule("Competencias Comportamentales", "Competencias Ciudadanas", "Ética y convivencia",
                    "competencias ciudadanas", "etica", "convivencia", "conflicto", "valores", "integridad",
                    "moralidad", "participacion ciudadana"),
            new Rule("Competencias Digitales", "Ofimática", "Herramientas de oficina",
                    "ofimatica", "excel", "word", "powerpoint", "hoja de calculo", "procesador de texto",
                    "tabla dinamica", "formula"),
            new Rule("Competencias Digitales", "Tecnologías de la Información", "TIC y gobierno digital",
                    "tecnologias de la informacion", "tic", "gobierno digital", "seguridad digital", "datos abiertos",
                    "sistema de informacion", "correo electronico"),
            new Rule("Gestión Pública", "Planeación", "Planeación estratégica y operativa",
                    "planeacion", "plan de accion", "plan estrategico", "indicador", "meta", "seguimiento",
                    "plan de desarrollo")
    );

    public ClassificationResult classify(String questionText) {
        String normalizedText = normalize(questionText);

        return RULES.stream()
                .filter(rule -> rule.matches(normalizedText))
                .findFirst()
                .map(rule -> new ClassificationResult(rule.axis(), rule.category(), rule.subcategory()))
                .orElse(new ClassificationResult("General", "Gestión Pública", "Clasificación general"));
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT);
    }

    private record Rule(String axis, String category, String subcategory, List<String> terms) {
        Rule(String axis, String category, String subcategory, String... terms) {
            this(axis, category, subcategory, List.of(terms));
        }

        boolean matches(String normalizedText) {
            return terms.stream().map(QuestionClassifier::normalize).anyMatch(normalizedText::contains);
        }
    }
}
