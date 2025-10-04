package com.uteq.sgaac.dto;

import java.util.List;

public class AsignarTribunalDTO {

    private List<Long> postulacionIds;
    private List<Long> tribunalIds;

    // Getters and Setters
    public List<Long> getPostulacionIds() {
        return postulacionIds;
    }

    public void setPostulacionIds(List<Long> postulacionIds) {
        this.postulacionIds = postulacionIds;
    }

    public List<Long> getTribunalIds() {
        return tribunalIds;
    }

    public void setTribunalIds(List<Long> tribunalIds) {
        this.tribunalIds = tribunalIds;
    }
}
