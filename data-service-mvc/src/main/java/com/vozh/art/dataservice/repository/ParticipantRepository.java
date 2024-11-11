package com.vozh.art.dataservice.repository;

import com.vozh.art.dataservice.entity.Participant;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query(name = "Participant.findByParticipantKey")
    Participant findByParticipantKey(ParticipantKey participantKey);
}
