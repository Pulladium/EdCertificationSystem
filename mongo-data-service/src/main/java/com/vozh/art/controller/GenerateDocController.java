package com.vozh.art.controller;

import com.vozh.art.dto.Participant;
import com.vozh.art.dto.request.ParticipantTemplateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data/documents-generate")
@RequiredArgsConstructor
@Slf4j
public class GenerateDocController {

    @PostMapping
    public void generateDocument(
            @RequestBody ParticipantTemplateRequest participantData
    ) {
        log.info("Document generation started");
    }
}
