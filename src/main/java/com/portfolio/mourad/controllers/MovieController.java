package com.portfolio.mourad.controllers;

import com.portfolio.mourad.models.Comment;
import com.portfolio.mourad.models.Movie;
import com.portfolio.mourad.repository.CommentRepository;
import com.portfolio.mourad.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",exposedHeaders = "Set-Cookie", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Transactional
public class MovieController {
    private final MovieRepository movieRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies(){
        return ResponseEntity.ok(movieRepository.findAll());
    }


//    @GetMapping("/comments")
//    public ResponseEntity<List<String>> getMovieComments(){
//        List<String> comments = commentRepository.findAll().stream().map(comment -> comment.getText());
//
//
//        return ResponseEntity.ok(comments.);
//    }

    @GetMapping("/all2")
    public String allAccess() {
        return "Public Content.";
    }

}
