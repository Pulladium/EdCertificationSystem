package com.vozh.art.dataservice.service;

import com.vozh.art.dataservice.dto.request.ParticipantRequest;
import com.vozh.art.dataservice.dto.response.ParticipantResponse;
import com.vozh.art.dataservice.dto.utils.ParticipantMapper;
import com.vozh.art.dataservice.entity.Participant;
import com.vozh.art.dataservice.entity.embedKey.ParticipantKey;
import com.vozh.art.dataservice.repository.ParticipantRepository;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public Participant getById(Long participantID){
        Optional<Participant> participant = participantRepository.findById(participantID);
        if(participant.isPresent()){
            return participant.get();
        }
        throw new PersistenceException("Cant find participant by id");
    }
    public Participant savePaticipant(Participant participant){
        try {
            return participantRepository.save(participant);
        }
        catch (Exception e){
            throw new PersistenceException("Failed to save into DB");
        }
    }
    public ParticipantResponse postNewParticipant(ParticipantRequest request){
        Participant participant = ParticipantMapper.mapToEntity(request);
        Participant savedParticipant = savePaticipant(participant);
        return ParticipantMapper.mapToResponse(savedParticipant);
    }
    public ParticipantResponse getParticipantResponseById(Long id) {
        Participant participant = getById(id);
        return ParticipantMapper.mapToResponse(participant);
    }
    public List<Participant> getAllParticipants(){
        return participantRepository.findAll();
    }


    public Participant getParticipantByKey(ParticipantKey key){
        return participantRepository.findByParticipantKey(key);
    }

//    returns all participants in db
    public List<Participant> addNewParticipants(List<ParticipantRequest> participants){

        for(ParticipantRequest participant: participants){
            Participant foundParticipant = participantRepository.findByParticipantKey(
//                    expecting problems here with query
                    new ParticipantKey(participant.getName(), participant.getSurname(), participant.getEmail()));
            if(foundParticipant == null){
                participantRepository.save(ParticipantMapper.mapToEntity(participant));
            }
        }
        return getAllParticipants();
    }





}
