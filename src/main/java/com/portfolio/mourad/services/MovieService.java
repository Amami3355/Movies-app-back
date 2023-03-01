package com.portfolio.mourad.services;

import com.portfolio.mourad.models.Movie;
import com.portfolio.mourad.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;


    public Movie getMovieById(Integer id){
        return movieRepository.findById(id).get();
    }

    public Movie addMovie(Long movieId){
        Movie movie = new Movie(null, movieId);
        return movieRepository.save(movie);
    }

    public Optional<Movie> getMovieByTmdbID(Long tmdbID){
        return movieRepository.findByTmdbID(tmdbID);
    }

}
