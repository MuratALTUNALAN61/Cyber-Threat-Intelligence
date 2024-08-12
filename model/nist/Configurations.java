package com.murat.icssupport.model.nist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Configurations {

    @JsonProperty("nodes")
    private List<Nodes> nodesList;

    public List<Nodes> getNodesList() {
        return nodesList;
    }

    public void setNodesList(List<Nodes> nodesList) {
        this.nodesList = nodesList;
    }
}
