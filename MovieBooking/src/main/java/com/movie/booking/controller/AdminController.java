package com.movie.booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.movie.booking.DTO.Admin;
import com.movie.booking.DTO.MovieDetails;
import com.movie.booking.DTO.OrderHistory;
import com.movie.booking.service.AdminService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class AdminController {
	
	@Autowired
	private AdminService adsevice;

	@GetMapping("/addMovies")
	public String addMovies(HttpSession session, Model mdl) {
		
		Admin admin = (Admin) session.getAttribute("adminData");
		mdl.addAttribute("admin", admin);
		return "addMovieDetail";
	}
	
	@PostMapping("/admin")
	public String movieAddSuccess(@RequestParam("movieName") String movieName, @RequestParam("movieGenre") String movieGenre,
			@RequestParam("movieImg") MultipartFile movieImg, Model mdl, HttpSession session) throws IOException {
	
		String uploadDirectory = "uploads/";
		
        Path directoryPath = Paths.get(uploadDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
		
        String fileName = System.currentTimeMillis() + "_" + movieImg.getOriginalFilename();
        Path filePath = directoryPath.resolve(fileName);
        Files.write(filePath, movieImg.getBytes());
        
        MovieDetails mvDetails = new MovieDetails();
        
		mvDetails.setMovieName(movieName);
		mvDetails.setMovieGenre(movieGenre);
		mvDetails.setMovieImg("/uploads/" + fileName);
		
		// saving movie
		adsevice.saveMovie(mvDetails);
		
		List<MovieDetails> movies = adsevice.allMovies();
		
		if(movies == null) {
			mdl.addAttribute("failed", "Failed To Upload..!!");
			return "admin";
		}else {
			Admin admin = (Admin) session.getAttribute("adminData");
			mdl.addAttribute("admin", admin);
			mdl.addAttribute("successMessage", "Movie added successfully!");
			mdl.addAttribute("allMovies", movies);
			return "admin";
		}
	}
	
	@PostMapping("/removeMovie")
	public String removeMovie(@RequestParam("movieName") String movieName, Model mdl, HttpSession session) {
		
		try {
		    Long movieId = adsevice.getMovieIdByName(movieName);
		    if (movieId != null) {
                adsevice.deleteMovieById(movieId);
                mdl.addAttribute("deleted", "Movie deleted successfully!");
            } else {
                mdl.addAttribute("errorMessage", "Movie not found.");
            }
        } catch (Exception e) {
            mdl.addAttribute("errorMessage", "Error deleting movie: " + e.getMessage());
        }
		
		List<MovieDetails> movies = adsevice.allMovies();
		mdl.addAttribute("allMovies", movies);
		
		Admin admin = (Admin) session.getAttribute("adminData");
		mdl.addAttribute("admin", admin);
		return "admin";
	}
	
	@GetMapping("/allOrders")
	public String getAllHistories(Model mdl, HttpSession session) {
		
		List<OrderHistory> allHistory = adsevice.getAll();
		mdl.addAttribute("allOrders", allHistory);
		
		System.out.println(allHistory);
		
		Admin admin = (Admin) session.getAttribute("adminData");
		mdl.addAttribute("admin", admin);
		
		return "allOrders";
	}
	
	@GetMapping("/adminHome")
	public String backToHome(HttpSession session, Model mdl) {
		
		Admin admin = (Admin) session.getAttribute("adminData");
		
		if(admin != null) {
			
			List<MovieDetails> movies = adsevice.allMovies();
			mdl.addAttribute("allMovies", movies);
        	
			mdl.addAttribute("admin", admin);
			return "admin";
		}else {
			return "login";
		}
	}
	
	@GetMapping("/adminSettings")
	public String admnSettings(HttpSession session, Model mdl) {
		
		Admin log = (Admin) session.getAttribute("adminData");
		Admin admin = adsevice.logingData(log.getEmail(), log.getPassword());
		
		mdl.addAttribute("admin", admin);
		return "admnSettings";
	}
	
	@GetMapping("/editAdmin")
	public String editAdmin(HttpSession session, Model mdl) {
		
		Admin log = (Admin) session.getAttribute("adminData");
		Admin admin = adsevice.logingData(log.getEmail(), log.getPassword());
		
		mdl.addAttribute("admin", admin);
		return "editAdmin";
	}
	
	@GetMapping("/updateAdmin")
	public String updateAdmin(@RequestParam("name") String name, @RequestParam("email") String email,
 			@RequestParam("password") String password, Model mdl, HttpSession session) {
		
		Admin edited = new Admin();
		edited.setName(name);
		edited.setEmail(email);
		edited.setPassword(password);
		
		adsevice.updateAdmin(edited);
		
		Admin oldData = (Admin) session.getAttribute("adminData");
		adsevice.deleteAdmin(oldData);
		
 		mdl.addAttribute("relogin", "Login Using New Credentials");
 		return "/login";
	}
	
}
