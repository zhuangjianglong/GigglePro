package com.whisper.pro.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfiguration {

    @Value("${tss.basePath}")
    private String basePath;

    @Value("${tss.whisper.url}")
    private String whisperSttUrl;

    @Value("${translate.google.apiKey}")
    private String googleApiKey;

    @Value("${translate.dlj.apiKey}")
    private String dljApiKey;



}