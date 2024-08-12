package com.murat.icssupport.model;

import java.util.Set;

public class DataFinal {

    private String cveId;
    private Set<String> cpeList;

    public String getCveId() {
        return cveId;
    }

    public void setCveId(String cveId) {
        this.cveId = cveId;
    }

    public Set<String> getCpeList() {
        return cpeList;
    }

    public void setCpeList(Set<String> cpeList) {
        this.cpeList = cpeList;
    }
}
