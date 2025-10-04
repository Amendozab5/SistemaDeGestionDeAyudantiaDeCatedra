package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.Carrera;
import com.uteq.sgaac.model.Facultad;
import com.uteq.sgaac.services.CarreraService;
import com.uteq.sgaac.services.FacultadService;
import com.uteq.sgaac.services.AsignaturaService; // Importar AsignaturaService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gestionacademica")
public class GestionAcademicaController {

    @Autowired
    private FacultadService facultadService;

    @Autowired
    private CarreraService carreraService;

    @Autowired // Inyectar AsignaturaService
    private AsignaturaService asignaturaService;

    @GetMapping
    public String showGestionAcademica(Model model,
                                       @RequestParam(name = "fragment", required = false) String fragment,
                                       @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        model.addAttribute("facultades", facultadService.findAll());
        model.addAttribute("carreras", carreraService.findAll());

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);

        if (isFragmentRequest) {
            return "siderbar/GestionAcademica :: content";
        }

        return "siderbar/GestionAcademica";
    }

    @GetMapping("/facultades")
    public String showFacultades(Model model) {
        model.addAttribute("facultades", facultadService.findAll());
        return "siderbar/forms/form-facultad :: content";
    }

    @GetMapping("/facultad/nueva")
    public String showNewFacultadForm(Model model) {
        model.addAttribute("facultad", new Facultad());
        model.addAttribute("isEdit", false);
        return "siderbar/forms/form-facultad-modal :: content";
    }

    @PostMapping("/facultad/guardar")
    public String saveFacultad(Facultad facultad) {
        facultadService.save(facultad);
        return "redirect:/homeAdmin?view=gestion-facultades";
    }

    @GetMapping("/facultad/editar/{id}")
    public String showEditFacultadForm(@PathVariable Long id, Model model) {
        Facultad facultad = facultadService.findById(id);
        if (facultad != null) {
            model.addAttribute("facultad", facultad);
            model.addAttribute("isEdit", true);
            return "siderbar/forms/form-facultad-modal :: content";
        }
        return "redirect:/gestionacademica";
    }

    @DeleteMapping("/facultad/eliminar/{id}")
    public ResponseEntity<?> deleteFacultad(@PathVariable Long id) {
        facultadService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/carreras")
    public String showCarreras(Model model) {
        model.addAttribute("carreras", carreraService.findAll());
        return "siderbar/forms/form-carrera :: content";
    }

    @GetMapping("/carrera/nueva")
    public String showNewCarreraForm(Model model) {
        List<Facultad> facultades = facultadService.findAll();
        model.addAttribute("carrera", new Carrera());
        model.addAttribute("facultades", facultades);
        model.addAttribute("isEdit", false);
        return "siderbar/forms/form-carrera-modal :: content";
    }

    @PostMapping("/carrera/guardar")
    public String saveCarrera(@RequestParam(value = "id", required = false) Long id, @RequestParam("carrera") String nombreCarrera, @RequestParam("facultad") Long facultadId) {
        Carrera carrera;
        if (id != null) {
            carrera = carreraService.findById(id);
        } else {
            carrera = new Carrera();
        }
        carrera.setCarrera(nombreCarrera);
        Facultad facultad = facultadService.findById(facultadId);
        carrera.setFacultad(facultad);
        carreraService.save(carrera);
        return "redirect:/homeAdmin?view=gestion-carreras";
    }

    @GetMapping("/carrera/editar/{id}")
    public String showEditCarreraForm(@PathVariable Long id, Model model) {
        Carrera carrera = carreraService.findById(id);
        if (carrera != null) {
            List<Facultad> facultades = facultadService.findAll();
            model.addAttribute("carrera", carrera);
            model.addAttribute("facultades", facultades);
            model.addAttribute("isEdit", true);
            return "siderbar/forms/form-carrera-modal :: content";
        }
        return "redirect:/gestionacademica";
    }

    @DeleteMapping("/carrera/eliminar/{id}")
    public ResponseEntity<?> deleteCarrera(@PathVariable Long id) {
        carreraService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/asignaturas")
    public String showAsignaturas(Model model) {
        model.addAttribute("asignaturas", asignaturaService.findAll());
        return "siderbar/forms/form-asignatura :: content";
    }
}
