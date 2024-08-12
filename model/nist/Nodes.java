package com.murat.icssupport.model.nist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Nodes {

    @JsonProperty("cpeMatch")
    private List<CPE> cpeList;

    public List<CPE> getCpeList() {
        return cpeList;
    }

    public void setCpeList(List<CPE> cpeList) {
        this.cpeList = cpeList;
    }
}
