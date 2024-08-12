package com.murat.icssupport.model;

import java.util.List;

public class DataCve {

    private String cveId;

    private List<DataNodes> nodesList;

    public String getCveId() {
        return cveId;
    }

    public void setCveId(String cveId) {
        this.cveId = cveId;
    }

    public List<DataNodes> getNodesList() {
        return nodesList;
    }

    public void setNodesList(List<DataNodes> nodesList) {
        this.nodesList = nodesList;
    }
}
