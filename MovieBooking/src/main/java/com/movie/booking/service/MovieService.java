package com.movie.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.booking.DTO.MovieDetails;

@Service
public class MovieService {

	// deals with movie data
	@Autowired
	private MovieReposiroty movieRepo;
	
	public List<MovieDetails> getAllMovies() {
		List<MovieDetails> movieList = movieRepo.findAll();
		return movieList;
	}
		
	public MovieDetails getMovie(String movieName) {
		MovieDetails searchedMovie = movieRepo.findByMovieName(movieName);
		return searchedMovie;
	}
	
}
