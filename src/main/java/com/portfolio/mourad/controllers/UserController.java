package com.portfolio.mourad.controllers;

import com.portfolio.mourad.Utils.FileUploadUtil;
import com.portfolio.mourad.mailing.service.EmailService;
import com.portfolio.mourad.models.Movie;
import com.portfolio.mourad.models.User;
import com.portfolio.mourad.repository.UserRepository;
import com.portfolio.mourad.security.jwt.JwtUtils;
import com.portfolio.mourad.security.services.UserDetailsImpl;
import com.portfolio.mourad.services.MovieService;
import com.portfolio.mourad.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:3000",exposedHeaders = "Set-Cookie", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Transactional
public class UserController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final MovieService movieService;
    private final UserRepository userRepository;

    private final EmailService emailService;

    @GetMapping("all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("auth")
    public User getUserByUserName(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt){
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userService.getUserByUserName(username);
        return user;
    }

    @PostMapping("/add-to-watchlist")
    public void addMovieToUserWatchList(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,@RequestParam(name = "movieId")  Long movieId){
        System.out.println(movieId);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userService.getUserByUserName(username);
        Movie movie;
        if (movieService.getMovieByTmdbID(movieId).isPresent()){
            movie = movieService.getMovieByTmdbID(movieId).get();
        }else{
            movie = movieService.addMovie(movieId);
        }


        userService.addMovieToUserWatchList(user, movie);
    }


    @PostMapping("/update")
    public User updateUser(@RequestParam(name = "id") Integer id,
                           @RequestParam(name = "first_name") String firstName,
                           @RequestParam(name = "last_name") String lastName,
                           Authentication authentication
    ){
        return userService.updateUser(id, firstName, lastName);
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadAvatar(@PathVariable(name = "id") Integer id,
                                 @RequestParam(name = "image") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        User user = userService.getUserById(id); // Getting the user from the db given the ID

        user.setImgUrl(fileName);

        User savedUser = userRepository.save(user);

        String uploadDir = "user-photos/" + savedUser.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return ResponseEntity.ok().body("File has been uploaded successfully");
    }


    @GetMapping("/get/image/{id}")
    public ResponseEntity<?> getImage(@PathVariable(name="id") Integer id){
        String FILE_PATH_ROOT = "user-photos/" + id + "/";
        String filename = userService.getUserById(id).getImgUrl();
        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File(FILE_PATH_ROOT+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String base64Image = Base64.getEncoder().encodeToString(image);
        String dataUrl = "data:image/jpeg;base64," + base64Image;
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(dataUrl);
    }


    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> UpdateUserPassword(@PathVariable("id") Integer id, @RequestParam("newPassword") String newPassword,
                                   @RequestParam("confirmedPassword") String confirmedPassword,
                                   Authentication authentication){
        if (!newPassword.equals(confirmedPassword)){
            System.out.println(newPassword);
            System.out.println(confirmedPassword);
            System.out.println("bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        System.out.println(userDetails.getEmail());
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        Integer randomNumber = random.nextInt(max - min + 1) + min;

        emailService.sendSimpleMessage(userDetails.getEmail(), "Password Confirmation", "Vous avez initié un changement de mot de passe. Veillez utiliser le code ci dessous pour confirmer \n" +
                randomNumber.toString());
        return ResponseEntity.status(200).body(null);
    }

    @PostMapping("/test")
    public String test(){
        System.out.println("test");
        return "test";
    }


}
