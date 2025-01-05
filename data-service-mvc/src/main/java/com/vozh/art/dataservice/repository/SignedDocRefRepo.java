package com.vozh.art.dataservice.repository;

import com.vozh.art.dataservice.entity.SingedDocRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignedDocRefRepo extends JpaRepository<SingedDocRef, Long> {

}
