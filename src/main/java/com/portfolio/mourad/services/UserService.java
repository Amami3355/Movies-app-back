package com.portfolio.mourad.services;

import com.portfolio.mourad.models.Movie;
import com.portfolio.mourad.models.User;
import com.portfolio.mourad.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;



    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){

        return userRepository.findById(id).get();
    }

    public User getUserByUserName(String username){

        return userRepository.findByUsername(username).get();
    }

    public User addMovieToUserWatchList(User user, Movie movie){
        user.getWatchedList().add(movie);
        return userRepository.save(user);
    }

    public User updateUser(Integer id, String firstName, String lastName){
        User user = userRepository.findById(id).get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        System.out.println(user.toString());
        return userRepository.save(user);
    }

    public Integer generateRandomToken(){
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public int updatePasswordByUserName(String username, String password){
        return userRepository.updatePasswordByUsername(username, password);
    }

}
