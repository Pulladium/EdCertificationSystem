package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.CertificateAddCategoryRequest;
import com.vozh.art.dataservice.dto.response.CertificateResponse;
import com.vozh.art.dataservice.entity.Category;
import com.vozh.art.dataservice.entity.Certificate;
import com.vozh.art.dataservice.repository.CategoryRepository;
import com.vozh.art.dataservice.repository.CertificateRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final CategoryRepository categoryRepository;

    public Certificate getById(Long certificateID) {
        Optional<Certificate> cert = certificateRepository.findById(certificateID);
        if (cert.isPresent()) {
            return cert.get();
        }
        throw new PersistenceException("Cant find certificate by id");
    }

    public Page<CertificateResponse> getCertificatesPagenated(PageRequest pageRequest) {
        Page<Certificate> certificatePage = findAll(pageRequest);
        return certificatePage.map(CertificateResponse::fromCertificate);
    }

    private Page<Certificate> findAll(Pageable pageRequest) {
        return certificateRepository.findAll(pageRequest);
    }

    private List<Certificate> findAll() {
        return certificateRepository.findAll();
    }


    public Certificate save(Certificate certificate) {
        try {
            return certificateRepository.save(certificate);
        } catch (Exception e) {
            throw new PersistenceException("Failed to save into DB");
        }
    }

    //todo maybe return CertificateResponse
    public Certificate addCategoryToCertificate(CertificateAddCategoryRequest request) {
        Long certificateId = request.getCertificateId();
        Long categoryId = request.getCategoryId();


        Certificate certificate = getById(certificateId);
        Optional<Set<Category>> categories = Optional.ofNullable(certificate.getCategories());
        if (categories.isPresent()) {
            Optional<Category> category2Add = categoryRepository.findById(categoryId);
            if (category2Add.isEmpty()) {
                throw new PersistenceException("Category with id : " + categoryId + " not found");
            }


            //            duplicated code
            categories.get().add(category2Add.get());
            log.trace("Added category with id {} to certificate with id {}", categoryId, certificateId);
            certificate.setCategories(categories.get());
            log.trace("Set categories to certificate with id {}", certificateId);
            //            duplicated code
        } else {
            log.trace("Certificate with id {} has no categories, adding new Set of categories", certificateId);
            Optional<Category> category2Add = categoryRepository.findById(categoryId);
            if (category2Add.isEmpty()) {
                throw new PersistenceException("Category with id : " + categoryId + " not found");
            }

//            duplicated code
            Set<Category> newCategories = Set.of(category2Add.get());
            certificate.setCategories(newCategories);
            log.trace("Set categories to certificate with id {}", certificateId);
//            duplicated code
        }
//        todo do i save cat? i dont have cascade
        return save(certificate);
    }



}
