package com.portfolio.mourad.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rates")
@Data   @NoArgsConstructor @AllArgsConstructor
public class Rate {

    @Id
    @Column(name = "rate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer value;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
