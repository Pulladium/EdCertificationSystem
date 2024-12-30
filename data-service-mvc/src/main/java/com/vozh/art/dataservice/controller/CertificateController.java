package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.dto.request.CertificateAddCategoryRequest;
import com.vozh.art.dataservice.dto.request.CreateCertificateRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.dto.utils.CertificateMapper;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.service.CategoryService;
import com.vozh.art.dataservice.service.CertificateParticipantService;
import com.vozh.art.dataservice.service.CertificateService;
import com.vozh.art.dataservice.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data/certificates")
@RequiredArgsConstructor
@Slf4j
public class CertificateController {



    private final CertificateService certificateService;
    private final CategoryService categoryService;
    private final CertificateParticipantService certificateParticipantService;
    private final ParticipantService participantService;

// Logic for frontend
    @GetMapping("/pagingList")
    public ResponseEntity<Page<CertificateResponse>> getCertificates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CertificateResponse> certificatePage = certificateService.getCertificatesPagenated(pageRequest);

        return ResponseEntity.ok(certificatePage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getCertificates(@PathVariable Long id) {
        log.trace("Getting certificates for user with id: {}", id);

        return ResponseEntity.ok(CertificateMapper.
                mapToResponse(certificateService.getById(id)));
    }

    /**
     * Create certificate
     *  String name;
     *  String description;
     *  can be setted later
     *  Set<CategoryRequest> categories = null;
     *  can be setted later
     *  List<ParticipantRequest> certificateParticipants = null;
     * @param request
     * @return CertificateResponse
     */
    @PostMapping("/create")
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody CreateCertificateRequest request) {
        log.trace("Creating certificate: {}", request);

        Certificate cert2Save = CertificateMapper.mapToCertificateEntity(request, categoryService,participantService,certificateParticipantService);

        Certificate savedCertificate = certificateService.save(cert2Save);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(savedCertificate));
    }


/**
     * Add category to certificate
     * @param request
     * @return CertificateResponse
     */
    @PostMapping("/addCategory")
    public ResponseEntity<CertificateResponse> addCategoryToCertificate(@RequestBody CertificateAddCategoryRequest request) {
        log.info("Adding category to certificate: {}", request);
        Certificate cert = certificateService.addCategoryToCertificate(request);
        return ResponseEntity.ok(CertificateMapper.mapToResponse(cert));
    }
}
