package com.edumania.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class NoteFileResourceConfiguration implements WebMvcConfigurer {

    private final NoteFileStorageService noteFileStorageService;

    public NoteFileResourceConfiguration(NoteFileStorageService noteFileStorageService) {
        this.noteFileStorageService = noteFileStorageService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/notes/**")
                .addResourceLocations(noteFileStorageService.getResourceLocation());
    }
}
