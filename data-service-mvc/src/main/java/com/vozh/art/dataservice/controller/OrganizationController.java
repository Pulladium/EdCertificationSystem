package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data/organization")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

        private final OrganizationService organizationService;

    @GetMapping("/pagingList")
    public ResponseEntity<Page<OrganizationResponse>> getOrganizationsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrganizationResponse> organizationsPagenated = organizationService.getOrganizationsPagenated(pageRequest);

        return ResponseEntity.ok(organizationsPagenated);
    }
//        @PostMapping("/create")
//
//
//        @GetMapping("/{id}")
//        public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable Long id) {
//
//
//        @DeleteMapping("/{id}")
//
//
//        @PutMapping("/update")
//
//        @PutMapping("/approve/{id}")

}
