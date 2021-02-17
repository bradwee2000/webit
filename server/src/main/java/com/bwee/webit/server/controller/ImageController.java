package com.bwee.webit.server.controller;

import com.bwee.webit.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@Slf4j
@Controller
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "/{bucket}/{id}", produces = IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] get(@PathVariable final String bucket, @PathVariable final String id) {
        return imageService.get(bucket, id);
    }
}
