package com.vnakhoa.crudtest.service;

import antlr.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class StrongeService {

    @GetMapping("/uploads/{file:.+}")
    public ResponseEntity<byte[]> loadFile(@PathVariable String file) {
        try {
            Path path = Paths.get("uploads/"+file);
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(StreamUtils.copyToByteArray(resource.getInputStream()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
