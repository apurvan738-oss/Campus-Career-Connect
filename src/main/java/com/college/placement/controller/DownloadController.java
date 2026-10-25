package com.college.placement.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Controller
public class DownloadController {

    @GetMapping(value = {"/MyProject.zip", "/download/MyProject.zip"}, produces = "application/zip")
    @ResponseBody
    public ResponseEntity<Resource> downloadProjectZip() {
        File file = new File("MyProject.zip");
        if (!file.exists()) {
            file = new File("target/classes/static/MyProject.zip");
        }
        if (!file.exists()) {
            file = new File("src/main/resources/static/MyProject.zip");
        }
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"MyProject.zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .contentLength(file.length())
                .body(resource);
    }
}
