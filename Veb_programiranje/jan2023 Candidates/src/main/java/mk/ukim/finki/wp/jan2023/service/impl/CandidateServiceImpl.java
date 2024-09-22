package mk.ukim.finki.wp.jan2023.service.impl;

import mk.ukim.finki.wp.jan2023.model.Candidate;
import mk.ukim.finki.wp.jan2023.model.Gender;
import mk.ukim.finki.wp.jan2023.model.Party;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidCandidateIdException;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidPartyIdException;
import mk.ukim.finki.wp.jan2023.repository.CandidateRepository;
import mk.ukim.finki.wp.jan2023.repository.PartyRepository;
import mk.ukim.finki.wp.jan2023.service.CandidateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final PartyRepository partyRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PartyRepository partyRepository) {
        this.candidateRepository = candidateRepository;
        this.partyRepository = partyRepository;
    }

    @Override
    public List<Candidate> listAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate findById(Long id) {
        return candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException::new);
    }

    @Override
    public Candidate create(String name, String bio, LocalDate dateOfBirth, Gender gender, Long party) {
        Party part= partyRepository.findById(party).orElseThrow(InvalidPartyIdException:: new);
        return candidateRepository.save(new Candidate(name,bio,dateOfBirth,gender,part));
    }

    @Override
    public Candidate update(Long id, String name, String bio, LocalDate dateOfBirth, Gender gender, Long party) {
        Party part= partyRepository.findById(party).orElseThrow(InvalidPartyIdException :: new);
        Candidate candidate = candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException :: new);
        candidate.setParty(part);
        candidate.setGender(gender);
        candidate.setDateOfBirth(dateOfBirth);
        candidate.setBio(bio);
        candidate.setName(name);
        candidateRepository.save(candidate);
        return candidate;
    }

    @Override
    public Candidate delete(Long id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException :: new);
        candidateRepository.delete(candidate);
        return candidate;
    }

    @Override
    public Candidate vote(Long id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException :: new);
        candidate.setVotes(candidate.getVotes() + 1);
        candidateRepository.save(candidate);
        return candidate;
    }

    @Override
    public List<Candidate> listCandidatesYearsMoreThanAndGender(Integer yearsMoreThan, Gender gender)
    {
        if(gender != null && yearsMoreThan !=null)
        {
            return  candidateRepository.findByDateOfBirthBeforeAndGender(LocalDate.now().minusYears(yearsMoreThan), gender);
        }
        if(gender != null)
        {
            return candidateRepository.findByGender(gender);
        }
        if(yearsMoreThan != null)
        {
            return candidateRepository.findByDateOfBirthBefore(LocalDate.now().minusYears(yearsMoreThan));
        }
        return candidateRepository.findAll();
    }
}
