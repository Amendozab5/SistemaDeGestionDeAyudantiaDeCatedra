package com.uteq.sgaac.controller;

import com.uteq.sgaac.services.RequisitoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
public class AdminViewController {

    private final RequisitoService requisitoService;

    public AdminViewController(RequisitoService requisitoService) {
        this.requisitoService = requisitoService;
    }

    @GetMapping("/configuracion")
    public String getConfiguracionPage() {
        return "siderbar/configuracion-admin :: content";
    }

    @GetMapping("/configuracion/requisitos")
    public String getConfiguracionRequisitosPage(Model model) {
        model.addAttribute("requisitos", requisitoService.findAll());
        return "siderbar/configurar-requisitos :: content";
    }

    @GetMapping("/configuracion/meritos")
    public String getConfiguracionMeritosPage() {
        return "siderbar/configurar-meritos :: content";
    }

    @GetMapping("/auditoria")
    public String getAuditoriaPage() {
        return "siderbar/auditoria :: content";
    }
}
