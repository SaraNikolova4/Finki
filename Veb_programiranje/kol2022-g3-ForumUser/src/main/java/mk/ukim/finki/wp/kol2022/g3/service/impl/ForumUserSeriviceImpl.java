package mk.ukim.finki.wp.kol2022.g3.service.impl;

import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUserType;
import mk.ukim.finki.wp.kol2022.g3.model.Interest;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidForumUserIdException;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidInterestIdException;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
import mk.ukim.finki.wp.kol2022.g3.repository.InterestRepository;
import mk.ukim.finki.wp.kol2022.g3.service.ForumUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ForumUserSeriviceImpl implements ForumUserService {

    private final ForumUserRepository forumUserRepository;
    private final InterestRepository interestRepository;

    public ForumUserSeriviceImpl(ForumUserRepository forumUserRepository, InterestRepository interestRepository) {
        this.forumUserRepository = forumUserRepository;
        this.interestRepository = interestRepository;
    }

    @Override
    public List<ForumUser> listAll() {
        return forumUserRepository.findAll();
    }

    @Override
    public ForumUser findById(Long id) {
        return forumUserRepository.findById(id).orElseThrow(InvalidForumUserIdException::new);
    }

    @Override
    public ForumUser create(String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
       List<Interest> interests = interestRepository.findAllById(interestId);
        return forumUserRepository.save(new ForumUser(name,email,password,type,interests,birthday));
    }

    @Override
    public ForumUser update(Long id, String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        List<Interest> interests = interestRepository.findAllById(interestId);
        ForumUser user = forumUserRepository.findById(id).orElseThrow(InvalidForumUserIdException :: new);
        user.setName(name);
        user.setBirthday(birthday);
        user.setEmail(email);
        user.setType(type);
        user.setPassword(password);
        user.setInterests(interests);
        forumUserRepository.save(user);
        return user;
    }

    @Override
    public ForumUser delete(Long id) {
        ForumUser user = forumUserRepository.findById(id).orElseThrow(InvalidForumUserIdException::new);
        forumUserRepository.delete(user);
        return user;
    }

    @Override
    public List<ForumUser> filter(Long interestId, Integer age) {

        if (age == null && interestId != null) {
            List<ForumUser> user = forumUserRepository.findAllByInterests(interestRepository.findById(interestId).orElseThrow(InvalidInterestIdException::new));
            return user;
        }
        if(interestId == null && age !=null)
        {
            LocalDate date = LocalDate.now().minusYears(age);
            List<ForumUser> users = forumUserRepository.findAllByBirthdayLessThan(date);
            return  users;
        }
        if(interestId != null && age != null)
        {
            LocalDate date = LocalDate.now().minusYears(age);
            List<ForumUser> user = forumUserRepository.findAllByInterestsAndAndBirthdayLessThan(interestRepository.findById(interestId).orElseThrow(InvalidInterestIdException::new), date);
            return user;

        }
        return forumUserRepository.findAll();
    }
}
