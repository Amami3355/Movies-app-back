package com.portfolio.mourad.repository;

import com.portfolio.mourad.models.Comment;
import com.portfolio.mourad.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByMovieIdOrderByDateDesc(Integer id);

}