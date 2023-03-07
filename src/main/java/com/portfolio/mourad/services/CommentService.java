package com.portfolio.mourad.services;

import com.portfolio.mourad.models.Comment;
import com.portfolio.mourad.models.Movie;
import com.portfolio.mourad.models.User;
import com.portfolio.mourad.repository.CommentRepository;
import com.portfolio.mourad.repository.MovieRepository;
import com.portfolio.mourad.repository.UserRepository;
import com.portfolio.mourad.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;


    public List<Comment> getCommentsByMovie(Integer id){
        return commentRepository.getCommentsByMovieOrderByDateDesc(id);
    }


    public void createComment(Integer movieId, Integer userId, String text){
//        Movie movie = movieRepository.getMovieById(movieId).get();
        User user = userRepository.getUserById(userId).get();
        Comment comment = new Comment(null, text, OffsetDateTime.now(), movieId, user);
        commentRepository.save(comment);

    }


    public void deleteCommentById(Integer id){
        commentRepository.deleteById(id);
    }



}
