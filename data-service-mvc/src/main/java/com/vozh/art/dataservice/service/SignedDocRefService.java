package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.entity.SingedDocRef;
import com.vozh.art.dataservice.repository.SignedDocRefRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignedDocRefService {
    private final SignedDocRefRepo signedDocRefRepo;

    public SingedDocRef save(SingedDocRef signedDocRef) {
        return signedDocRefRepo.save(signedDocRef);
    }

    public List<SingedDocRef> saveAll(List<SingedDocRef> signedDocResults) {
        return signedDocRefRepo.saveAll(signedDocResults);
    }
}
