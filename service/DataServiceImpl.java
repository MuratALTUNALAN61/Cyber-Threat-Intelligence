package com.murat.icssupport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murat.icssupport.model.*;
import com.murat.icssupport.model.nist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger log = LogManager.getLogger(DataServiceImpl.class);

    private int totalIndex = 1;

    private static final Path WORKING_DIRECTORY = Path.of("C:", "Users", "Murat Altunalan", "Desktop", "IcsSupportNvd");

    @Override
    public void writeDataToFile() throws IOException, InterruptedException {
        int startIndex = 0;
        while (startIndex < totalIndex) {
            List<DataFinal> dataFinalList = retrieveCveList(startIndex, 20000);
            startIndex += dataFinalList.size();
            writeCveListToFile(dataFinalList);
        }
        Instant now = Instant.now();
        String now1 = now.toString().replace(":", "-");

        String zipName = WORKING_DIRECTORY.resolve("NDV-" + now1 + ".zip").toString();

        zipFiles(WORKING_DIRECTORY.toFile(), zipName);
        deleteFormerFiles();
    }

    private List<DataFinal> retrieveCveList(int startIndex, int sizeLimit) throws InterruptedException {
        List<DataFinal> dataFinalList = new ArrayList<>();
        while (dataFinalList.size() < sizeLimit && startIndex < totalIndex) {
            List<DataFinal> subDataFinalList = retrieveCveList(startIndex);
            startIndex += subDataFinalList.size();
            dataFinalList.addAll(subDataFinalList);
        }
        return dataFinalList;
    }

    private List<DataFinal> retrieveCveList(int startIndex) throws InterruptedException {
        NistResponse response = makeRequestToNistUntilSuccess(startIndex, 5);
        List<DataCve> dataCveList = parse(response);

        return dataCveList.stream().map(this::convert).toList();
    }

    private NistResponse makeRequestToNistUntilSuccess(int startIndex, int tryLimit) throws InterruptedException {

        log.info("Finding data");
        String url = "https://services.nvd.nist.gov/rest/json/cves/2.0?startIndex=" + startIndex;
        int tryCount = 0;
        while (tryCount < tryLimit) {
            NistResponse nistResponse = makeRequest(url, startIndex);
            tryCount++;
            if (nistResponse != null) {
                totalIndex = nistResponse.getTotalResults();
                return nistResponse;
            } else {
                long sleepTime = 60000;
                log.error("Waiting before make another request to nist! Milliseconds: {}", sleepTime);
                Thread.sleep(sleepTime);
            }
        }
        return null;
    }

    private NistResponse makeRequest(String url, int startIndex) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("Requesting for fetching data for startIndex: {}", startIndex);
            NistResponse nistResponse = restTemplate.getForObject(url, NistResponse.class);
            log.info("Nist fetching data successful for startIndex: {}, Return size: {}", startIndex, nistResponse.getResultsPerPage());
            return nistResponse;
        } catch (ResourceAccessException | RestClientResponseException | UnknownContentTypeException e) {
            log.error("Error occurred while fetching data from NIST. StartIndex: {}, Exception: {}", startIndex, e.getMessage());
            return null;
        }
    }

    private List<DataCve> parse(NistResponse nistResponse) {
        List<DataCve> dataCveList = new ArrayList<>();

        List<Vulnerabilities> vulnerabilitiesList = nistResponse.getVulnerabilitiesList();

        for (Vulnerabilities vulnerabilities : vulnerabilitiesList) {
            DataCve dataCve = parseNistCve(vulnerabilities.getCve());
            dataCveList.add(dataCve);
        }

        return dataCveList;
    }

    private DataCve parseNistCve(CVE nistResponseCve) {
        DataCve dataCve = new DataCve();
        dataCve.setCveId(nistResponseCve.getId());

        List<DataNodes> dataNodesList = retrieveNodes(nistResponseCve.getConfigurationsList());
        dataCve.setNodesList(dataNodesList);

        return dataCve;
    }

    private List<DataNodes> retrieveNodes(List<Configurations> configurationsList) {
        List<DataNodes> dataNodesList = new ArrayList<>();

        if (configurationsList != null) {
            for (Configurations configurations : configurationsList) {
                DataNodes dataNodes = convertConfiguration(configurations);
                dataNodesList.add(dataNodes);
            }
        }
        return dataNodesList;
    }

    private DataNodes convertConfiguration(Configurations configurations) {
        DataNodes dataNodes = new DataNodes();

        List<DataCpe> dataCpeList = new ArrayList<>();
        for (Nodes nodes : configurations.getNodesList()) {
            for (CPE cpe : nodes.getCpeList()) {
                dataCpeList.add(parseCpe(cpe));
            }
        }

        dataNodes.setCpeList(dataCpeList);
        return dataNodes;
    }

    private DataCpe parseCpe(CPE cpe) {
        DataCpe dataCpe = new DataCpe();

        dataCpe.setCpeCriteria(cpe.getCriteria());

        String[] disintegrateCpeList = dataCpe.getCpeCriteria().split(":");
        if (disintegrateCpeList.length >= 12) {
            dataCpe.setVendor(disintegrateCpeList[3]);
            dataCpe.setProduct(disintegrateCpeList[4]);
            dataCpe.setVersion(disintegrateCpeList[5]);
            dataCpe.setEdition(disintegrateCpeList[6]);
            dataCpe.setLanguage(disintegrateCpeList[7]);
            dataCpe.setSoftwareEdition(disintegrateCpeList[8]);
            dataCpe.setTargetSoftware(disintegrateCpeList[9]);
            dataCpe.setTargetHardware(disintegrateCpeList[10]);
            dataCpe.setOther(disintegrateCpeList[11]);
        }
        return dataCpe;
    }

    private DataFinal convert(DataCve dataCve) {
        DataFinal dataFinal = new DataFinal();
        dataFinal.setCveId(dataCve.getCveId());

        //stream flatmap ile sadeleşebilir mi?
        Set<String> cpeCriteriaList = new HashSet<>();
        for (DataNodes dataNodes : dataCve.getNodesList()) {
            for (DataCpe dataCpe : dataNodes.getCpeList()) {
                cpeCriteriaList.add(dataCpe.getCpeCriteria());
            }
        }
        dataFinal.setCpeList(cpeCriteriaList);
        return dataFinal;
    }

    private void writeCveListToFile(List<DataFinal> dataFinalList) throws IOException {
        Instant now = Instant.now();
        String fileDateText = now.toString().replace(":", "-");
        Path outputPath = WORKING_DIRECTORY.resolve("NDV-" + fileDateText + ".json");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputPath.toFile(), dataFinalList);
        log.info("CVE json file created. Count: {}, Filename: {}", dataFinalList.size(), outputPath.toFile().getName());
    }

    private void zipFiles(File srcDirectory, String zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            byte[] buffer = new byte[1024];

            for (File fileToZip : srcDirectory.listFiles()) {
                if(fileToZip.getName().endsWith(".json")) {
                    try (FileInputStream fis = new FileInputStream(fileToZip)) {
                        zos.putNextEntry(new ZipEntry(fileToZip.getName()));
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    }
                }
            }
        }
    }

    private  void deleteFormerFiles() {
        try {
            Path latestZipFile = Files.list(WORKING_DIRECTORY)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".zip"))
                    .max(Comparator.comparingLong(this::getLastModifiedTime))
                    .orElse(null);

            if (latestZipFile != null) {
                Files.list(WORKING_DIRECTORY)
                        .filter(Files::isRegularFile)
                        .filter(path -> !path.equals(latestZipFile))
                        .forEach(this::deleteFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error accessing or deleting files", e);
        }
    }

    private long getLastModifiedTime(Path path) {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            throw new RuntimeException("Error getting last modified time of file", e);
        }
    }

    private void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + path, e);
        }
    }
}