package com.movie.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.booking.DTO.Admin;
import com.movie.booking.DTO.MovieDetails;
import com.movie.booking.DTO.OrderHistory;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adRepo;
	
	@Autowired
	private MovieReposiroty mvRepo;
	
	@Autowired
	private HistoryRepository hisRepo;
	
	public Admin logingData(String email, String password) {
		Admin logData = adRepo.findByEmailAndPassword(email, password);
		return logData;
	}
	
	public List<MovieDetails> allMovies() {
		List<MovieDetails> mvData = mvRepo.findAll();
		return mvData;
	}
	
	public void saveMovie(MovieDetails mvDto) {
		adRepo.save(mvDto);
	}
	
    public Long getMovieIdByName(String movieName) {
        return mvRepo.findMovieIdByMovieName(movieName);
    }

    public void deleteMovieById(Long movieId) {
        mvRepo.deleteById(movieId);
    }
    
    public List<OrderHistory> getAll() {		
    	return hisRepo.getHistories();
	}
    
    public void updateAdmin(Admin data) {
		adRepo.save(data);
	}
    
    public void deleteAdmin(Admin data) {
		adRepo.deleteById(data.getId());
	}
}
