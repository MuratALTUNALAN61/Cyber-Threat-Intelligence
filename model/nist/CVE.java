package com.murat.icssupport.model.nist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CVE {

    private String id;

    @JsonProperty("configurations")
    private List<Configurations> configurationsList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Configurations> getConfigurationsList() {
        return configurationsList;
    }

    public void setConfigurationsList(List<Configurations> configurationsList) {
        this.configurationsList = configurationsList;
    }
}
