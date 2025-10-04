package com.uteq.sgaac.config;

import com.uteq.sgaac.model.Requisito;
import com.uteq.sgaac.repository.RequisitoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Profile;

@Component
@Profile("!test")
public class RequisitoDataInitializer implements CommandLineRunner {

    private final RequisitoRepository requisitoRepository;
    private final com.uteq.sgaac.repository.PostulacionDocumentoRepository postulacionDocumentoRepository;

    public RequisitoDataInitializer(RequisitoRepository requisitoRepository, com.uteq.sgaac.repository.PostulacionDocumentoRepository postulacionDocumentoRepository) {
        this.requisitoRepository = requisitoRepository;
        this.postulacionDocumentoRepository = postulacionDocumentoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Para evitar duplicados, limpiamos la tabla antes de insertar los nuevos requisitos.
        // Esto asegura que solo tengamos la lista definitiva.
        postulacionDocumentoRepository.deleteAllInBatch();
        requisitoRepository.deleteAllInBatch();

        if (requisitoRepository.count() == 0) {
            List<Requisito> requisitos = Arrays.asList(
                createRequisito("Ser estudiante regular de la UTEQ, es decir, estar matriculado en el periodo académico vigente."),
                createRequisito("Haber aprobado la asignatura de la que desea ser ayudante."),
                createRequisito("Estar matriculado al menos en el segundo año de la carrera (no pueden postular los de primer año)."),
                createRequisito("Tener un promedio académico mínimo de 8/10."),
                createRequisito("No haber sido sancionado disciplinariamente por la universidad."),
                createRequisito("Contar con disponibilidad de horario para cumplir las actividades de ayudantía (generalmente 10 h semanales)."),
                createRequisito("Presentar la documentación requerida por la convocatoria, que suele incluir: Solicitud de postulación, Copia de la cédula y certificado de matrícula, Historial académico que demuestre el promedio y aprobación de la asignatura."),
                createRequisito("Aprobar el proceso de selección, que consta de: Evaluación de méritos (rendimiento académico y experiencia previa, si la hubiera) y Prueba de oposición, en la que debe obtener el puntaje mínimo exigido (usualmente ≥ 70/100).")
            );
            requisitoRepository.saveAll(requisitos);
        }
    }

    private Requisito createRequisito(String descripcion) {
        Requisito req = new Requisito();
        req.setDescripcion(descripcion);
        req.setActivo(true);
        req.setTipo("REGLAMENTARIO"); // Asignar el tipo por defecto
        return req;
    }
}
