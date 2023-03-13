package com.portfolio.mourad.controllers;

import com.portfolio.mourad.models.Comment;
import com.portfolio.mourad.payload.response.CommentPayload;
import com.portfolio.mourad.requests.CreateCommentRequest;
import com.portfolio.mourad.security.services.UserDetailsImpl;
import com.portfolio.mourad.services.CommentService;
import com.portfolio.mourad.services.MovieService;
import com.portfolio.mourad.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Transactional
public class CommentController {

    private final MovieService movieService;

    private final UserService userService;

    private final CommentService commentService;


    @GetMapping("/{movieId}")
    public ResponseEntity<List<CommentPayload>> getCommentsByMovie(@PathVariable(name = "movieId") Integer movieId){
        List<Comment> comments = commentService.getCommentsByMovie(movieId);
        List<CommentPayload> response = new ArrayList<>();
        for (Comment comment : comments){
            CommentPayload commentPayload = new CommentPayload();
            commentPayload.setId(comment.getId());
            commentPayload.setText(comment.getText());
            commentPayload.setOwner(comment.getUser().getUsername());
            commentPayload.setMovieId(comment.getMovieId());
            commentPayload.setImageData(userService.getUserImageData(comment.getUser().getId()));
            commentPayload.setDate(comment.getDate());
            response.add(commentPayload);
        }

//        System.out.println(response);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateCommentRequest request, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        System.out.println(userDetails.getEmail());
        commentService.createComment(request.getMovieId(), userDetails.getId(), request.getText());
        return ResponseEntity.ok().body("Comment Created successfully");
    }

    @PostMapping("/delete/{commentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void delete(@RequestBody Comment request){
        commentService.deleteCommentById(request.getId());
    }


}
