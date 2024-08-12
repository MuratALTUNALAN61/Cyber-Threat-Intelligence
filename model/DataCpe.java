package com.murat.icssupport.model;

public class DataCpe {

    private String cpeCriteria;

    private String vendor;
    private String product;
    private String version;
    private String edition;
    private String language;
    private String softwareEdition;
    private String targetSoftware;
    private String targetHardware;
    private String other;

    public String getCpeCriteria() {
        return cpeCriteria;
    }

    public void setCpeCriteria(String cpeCriteria) {
        this.cpeCriteria = cpeCriteria;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSoftwareEdition() {
        return softwareEdition;
    }

    public void setSoftwareEdition(String softwareEdition) {
        this.softwareEdition = softwareEdition;
    }

    public String getTargetSoftware() {
        return targetSoftware;
    }

    public void setTargetSoftware(String targetSoftware) {
        this.targetSoftware = targetSoftware;
    }

    public String getTargetHardware() {
        return targetHardware;
    }

    public void setTargetHardware(String targetHardware) {
        this.targetHardware = targetHardware;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
