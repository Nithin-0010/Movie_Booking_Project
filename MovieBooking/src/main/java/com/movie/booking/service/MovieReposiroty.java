package com.movie.booking.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.movie.booking.DTO.MovieDetails;

@Repository
public interface MovieReposiroty extends JpaRepository<MovieDetails, Long> {

	MovieDetails findByMovieName(String movieName);
	
	List<MovieDetails> findAll();
	
    @Query("SELECT m.movieId FROM MovieDetails m WHERE m.movieName = ?1")
    Long findMovieIdByMovieName(String movieName);

    @Modifying
    void deleteById(Long movieId);
}
