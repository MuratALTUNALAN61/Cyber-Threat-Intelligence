package com.murat.icssupport.controller;

import com.murat.icssupport.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/IcsSupport")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/getData")
    public void getData() throws IOException, InterruptedException {
        dataService.writeDataToFile();
    }
}
