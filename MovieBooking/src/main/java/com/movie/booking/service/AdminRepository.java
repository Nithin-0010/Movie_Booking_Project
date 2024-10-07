package com.movie.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.booking.DTO.Admin;
import com.movie.booking.DTO.MovieDetails;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	public Admin findByEmailAndPassword(String email, String password);

	public void save(MovieDetails mvDto);
}
