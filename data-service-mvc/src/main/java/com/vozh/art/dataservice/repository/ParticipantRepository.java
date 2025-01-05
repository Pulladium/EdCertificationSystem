package com.vozh.art.dataservice.repository;

import com.vozh.art.dataservice.entity.Participant;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query(name = "Participant.findByParticipantKey")
    Participant findByParticipantKey(
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("email") String email
    );
}
