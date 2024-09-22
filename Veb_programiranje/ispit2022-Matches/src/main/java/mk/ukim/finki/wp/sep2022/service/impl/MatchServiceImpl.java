package mk.ukim.finki.wp.sep2022.service.impl;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.sep2022.repository.MatchLocationRepository;
import mk.ukim.finki.wp.sep2022.repository.MatchRepository;
import mk.ukim.finki.wp.sep2022.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchLocationRepository matchLocationRepository;
    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchLocationRepository matchLocationRepository, MatchRepository matchRepository) {
        this.matchLocationRepository = matchLocationRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Match> listAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match findById(Long id) {
        return matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
    }

    @Override
    public Match create(String name, String description, Double price, MatchType type, Long location) {
        MatchLocation matchLocation = matchLocationRepository.findById(location).orElseThrow(InvalidMatchLocationIdException::new);
        return matchRepository.save(new Match(name,description,price,type, matchLocation));
    }

    @Override
    public Match update(Long id, String name, String description, Double price, MatchType type, Long location) {
        MatchLocation matchLocation = matchLocationRepository.findById(location).orElseThrow(InvalidMatchLocationIdException::new);
        Match match = matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        match.setName(name);
        match.setDescription(description);
        match.setPrice(price);
        match.setType(type);
        match.setLocation(matchLocation);
        return matchRepository.save(match);
    }

    @Override
    public Match delete(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        matchRepository.delete(match);
        return match;
    }

    @Override
    public Match follow(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        match.setFollows(match.getFollows() + 1);
        matchRepository.save(match);
        return match;
    }

    //cena pomala od odredena i vid na natprevarot
    @Override
    public List<Match> listMatchesWithPriceLessThanAndType(Double price, MatchType type) {
        if(type != null && price != null)
        {
            return matchRepository.findByPriceLessThanAndType(price,type);
        }
        if(type != null)
        {
            return matchRepository.findByType(type);
        }
        if(price != null)
        {
            return matchRepository.findByPriceLessThan(price);
        }
        return  matchRepository.findAll();
    }
}
