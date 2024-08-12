package com.murat.icssupport.model.nist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NistResponse {
    private int resultsPerPage;

    private int startIndex;

    private int totalResults;

    private String format;

    private String version;

    private String timestamp;

    @JsonProperty("vulnerabilities")
    private List<Vulnerabilities> vulnerabilitiesList;

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }


    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<Vulnerabilities> getVulnerabilitiesList() {
        return vulnerabilitiesList;
    }

    public void setVulnerabilitiesList(List<Vulnerabilities> vulnerabilitiesList) {
        this.vulnerabilitiesList = vulnerabilitiesList;
    }
}
