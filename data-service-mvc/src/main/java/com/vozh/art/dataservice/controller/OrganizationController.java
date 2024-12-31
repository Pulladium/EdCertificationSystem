package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        @PostMapping("/create")
        public ResponseEntity<OrganizationResponse> createOrganization(@RequestBody OrganizationRequest request) {
            log.trace("OrganizationController: Creating organization: {}", request);
            return ResponseEntity.ok(organizationService.createOrganization(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable Long id) {
            log.trace("OrganizationController: Getting organization by id: {}", id);
            return ResponseEntity.ok(organizationService.getOrganizationResponseById(id));
        }


        @PreAuthorize("hasRole('ROLE_admin')")
        @PostMapping("/approveOrganization/{id}")
        public ResponseEntity<OrganizationResponse> changeStatus(@PathVariable Long id) {
            log.trace("OrganizationController: Changing status of organization by id: {}", id);
            return ResponseEntity.ok(organizationService.approveOrganization(id));
        }

        @PreAuthorize("hasRole('ROLE_admin')")
        @PostMapping("/rejectOrganization/{id}")
        public ResponseEntity<OrganizationResponse> rejectOrganization(@PathVariable Long id) {
            log.trace("OrganizationController: Rejecting organization by id: {}", id);
            return ResponseEntity.ok(organizationService.rejectOrganization(id));
        }

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
