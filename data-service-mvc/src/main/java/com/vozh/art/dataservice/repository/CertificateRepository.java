package com.vozh.art.dataservice.repository;

import com.vozh.art.dataservice.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> , PagingAndSortingRepository<Certificate, Long> {
}
