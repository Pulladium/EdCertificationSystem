package com.vozh.art.dataservice.controller;

import com.vozh.art.dataservice.dto.request.OrganizationRequest;
import com.vozh.art.dataservice.dto.request.UpdateOrgRequest;
import com.vozh.art.dataservice.dto.response.OrganizationResponse;
import com.vozh.art.dataservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        @PutMapping("/approveOrganization/{id}")
        public ResponseEntity<OrganizationResponse> changeStatus(@PathVariable Long id) {
            log.trace("OrganizationController: Changing status of organization by id: {}", id);
            return ResponseEntity.ok(organizationService.approveOrganization(id));
        }

        @PreAuthorize("hasRole('ROLE_admin')")
        @PutMapping("/rejectOrganization/{id}")
        public ResponseEntity<OrganizationResponse> rejectOrganization(@PathVariable Long id) {
            log.trace("OrganizationController: Rejecting organization by id: {}", id);
            return ResponseEntity.ok(organizationService.rejectOrganization(id));
        }

        //todo validate cert have altleast one org if cert will be removed from org
//todo    also test cascade remove org from cert
//    todo should check if org uuid == user uuid or user has role admin
        @PutMapping("/update")
        public ResponseEntity<OrganizationResponse> updateOrganization(@RequestBody UpdateOrgRequest request) {
            log.trace("OrganizationController: Updating organization: {}", request);
            validateOrgAccessPermission(request.getOrganizationId());
            return ResponseEntity.ok(organizationService.updateOrganization(request));
        }

        private void validateOrgAccessPermission(Long orgId) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Current user authorities: {}",
                    auth.getAuthorities().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "))
            );
            List<GrantedAuthority> userauthorities = auth.getAuthorities().stream().
                    filter(a -> a.getAuthority().startsWith("ROLE_")).collect(Collectors.toList());

            if(userauthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_registered-user"))){
                if(auth.getName().equals(organizationService.
                        getOrganizationResponseById(orgId).getMaintainerKeycloakUUID())){
                    return;
                }
            }
            if(userauthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"))){
                return;
            }
            throw new RuntimeException("You have no permission to update this organization");
        }

        @GetMapping("/getById/{id}")

        public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable Long id) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Keycloak user's uuid:  {}", auth.getName());
//            log.info("Keycloak user;s uuid: {}", auth.getPrincipal());// not it is name

            log.trace("OrganizationController: Getting organization by id: {}", id);
            return ResponseEntity.ok(organizationService.getOrganizationResponseById(id));
        }


        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteOrganizationById(@PathVariable Long id) {
            log.trace("OrganizationController: Deleting organization by id: {}", id);

            validateOrgAccessPermission(id);
            organizationService.deleteOrganizationById(id);
            return ResponseEntity.ok("Organization with id: " + id + " deleted");
        }



//
//        @DeleteMapping("/{id}")
//
//


}
