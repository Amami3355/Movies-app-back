package com.portfolio.mourad.repository;

import com.portfolio.mourad.models.Comment;
import com.portfolio.mourad.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findByTmdbID(Long tmdbID);
}
