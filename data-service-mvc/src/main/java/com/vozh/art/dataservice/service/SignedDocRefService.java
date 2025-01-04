package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.entity.SingedDocRef;
import com.vozh.art.dataservice.repository.SignedDocRefRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignedDocRefService {
    private final SignedDocRefRepo signedDocRefRepo;

    public SingedDocRef save(SingedDocRef signedDocRef) {
        return signedDocRefRepo.save(signedDocRef);
    }
}
