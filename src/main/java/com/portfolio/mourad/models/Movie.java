package com.portfolio.mourad.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Table(name = "movies")
@Entity
@Data
@AllArgsConstructor @NoArgsConstructor  @ToString
public class Movie {

    public Movie(Long tmdbID){
        this.tmdbID = tmdbID;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Integer id;

    @Column(name = "tmdb_id")
    private Long tmdbID;


//    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    private List<Comment> comments;

//    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    private List<Rate> rates;


}
