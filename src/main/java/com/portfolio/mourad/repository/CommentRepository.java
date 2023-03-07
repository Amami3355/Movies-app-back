package com.portfolio.mourad.repository;

import com.portfolio.mourad.models.Comment;
import com.portfolio.mourad.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByMovieIdOrderByDateDesc(Integer id);

    @Modifying
    @Query("SELECT c FROM Comment c WHERE c.movieId = :movieId ORDER BY c.date DESC")
//    @Query(value = "SELECT * FROM comment WHERE movie_id = :movieId ORDER BY date DESC", nativeQuery = true)
    List<Comment> getCommentsByMovieOrderByDateDesc(@Param("movieId") Integer movieId);

}