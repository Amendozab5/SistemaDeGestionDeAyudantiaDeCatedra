package com.uteq.sgaac.model;

import java.util.ArrayList;
import java.util.List;

public class PruebaOposicionEvalWrapper {

    private List<PruebaOposicionEval> evaluaciones;

    public PruebaOposicionEvalWrapper() {
        this.evaluaciones = new ArrayList<>();
    }

    public List<PruebaOposicionEval> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<PruebaOposicionEval> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }
}