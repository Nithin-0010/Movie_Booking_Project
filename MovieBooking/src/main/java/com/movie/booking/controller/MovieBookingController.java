package com.movie.booking.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movie.booking.DTO.OrderHistory;
import com.movie.booking.DTO.Admin;
import com.movie.booking.DTO.Customer;
import com.movie.booking.DTO.MovieDetails;
import com.movie.booking.DTO.Seat;
import com.movie.booking.service.AdminService;
import com.movie.booking.service.CustomerService;
import com.movie.booking.service.MovieService;
import com.movie.booking.service.SeatRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MovieBookingController {

    @Autowired
    private CustomerService dao;
    
    @Autowired
    private MovieService mdao;
    
//    @Autowired
//    private SeatRepository seatRepo;
    
    @Autowired
    private AdminService adminDao;

    // Home page displaying all movies
    @GetMapping("/")
    public String home(Model mdl) {
        List<MovieDetails> allMovies = mdao.getAllMovies();
        mdl.addAttribute("allMovies", allMovies);
        return "index";
    }
    
    // Register page
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Save registration details
    @PostMapping("/save")
    public String registerUser(@ModelAttribute("dto") Customer dto, Model mdl, RedirectAttributes redirect) {
        Customer existingUsr = dao.findCustomer(dto.getEmail());
        if (existingUsr != null) {
            redirect.addFlashAttribute("failed", "User Already Exists.!!");
            return "redirect:/register";
        }
        dao.saveRegister(dto);
        redirect.addFlashAttribute("confirm", "Please Login To Continue");
        return "redirect:/login";
    }
    
    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Login check and redirection to home
    @PostMapping("/loginCheck")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession usrSession, Model mdl) {      
            	
        Admin adminLogData = adminDao.logingData(email, password);
        if(adminLogData != null) {
        	mdl.addAttribute("admin", adminLogData);
        	usrSession.setAttribute("adminData", adminLogData);
        	
        	List<MovieDetails> mvDetails = mdao.getAllMovies();
        	mdl.addAttribute("allMovies", mvDetails);
        	return "admin";
        }
        
        // if user
        Customer logData = dao.loginInfo(email, password);
        if (logData == null) {
            mdl.addAttribute("failed", "Something Went Wrong..!! Try Again");
            return "login";
        }
        
        // customer success
			usrSession.setAttribute("usrData", logData);
            return "redirect:/home";
               
    }
    
    // Main Dashboard
    @GetMapping("/home")
    public String mainDashboard(HttpSession usrSession, Model mdl) {
        Customer usrInfo = (Customer) usrSession.getAttribute("usrData");
        if (usrInfo == null) {
            return "redirect:/login";
        }
        List<MovieDetails> allMovies = mdao.getAllMovies();
        mdl.addAttribute("allMovies", allMovies);
        mdl.addAttribute("user", usrInfo);
        return "home";
    }
    
    // About Us Page
    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }

    // Contact Us Page
    @GetMapping("/contact")
    public String contactUs() {
        return "contact";
    }
    
	// back button in contact and about
	@GetMapping("/back")
	public String backTo(HttpSession usrSession) {
		
		Customer info = (Customer) usrSession.getAttribute("usrData");
		
		if(info == null) {
			return "redirect:/";
		}else {
			return "redirect:/home";
		}
	}
	
	// search
		@GetMapping("/search")
		public String searchMovieName(@RequestParam ("movieName") String movieName,Model mdl, HttpSession usrSession) {
			Customer info = (Customer) usrSession.getAttribute("usrData");
			
			if(info == null) {
				
				mdl.addAttribute("refer", "Register/Login To Continue..!");
				return "register";
		
			}else {
				return "redirect:/searchResult?movieName=" + movieName;
			}

		}
		
		
		@GetMapping("/searchResult")
		public String serachList(@RequestParam ("movieName") String movieName,HttpSession usrSession, Model mdl) {
			Customer usrInfo = (Customer) usrSession.getAttribute("usrData");
			MovieDetails searchMovie = mdao.getMovie(movieName);
			mdl.addAttribute("user", usrInfo);
			mdl.addAttribute("searchMovie", searchMovie);
			return "searchResult";
		}
	

    // Book Now for Movies - Requires Authentication
    @GetMapping("/bookNow")
    public String bookNow(HttpSession usrSession, Model mdl, @RequestParam("movieName") String movieName) {
        Customer usrInfo = (Customer) usrSession.getAttribute("usrData");
        if (usrInfo == null) {
            mdl.addAttribute("refer", "Register/Login To Continue..!");
            return "register";
        }
        usrSession.setAttribute("bookdMovie", mdao.getMovie(movieName));
        return "redirect:/bookSeats";
    }
    
    @GetMapping("/bookSeats")
    public String seatBooking(@RequestParam("movieName") String movieName, HttpSession usrSession, Model mdl) {
        if (movieName == null || movieName.isEmpty()) {
            throw new IllegalArgumentException("Movie name cannot be null or empty");
        }

        Customer usrInfo = (Customer) usrSession.getAttribute("usrData");
        if (usrInfo == null) {
            return "redirect:/login";
        }

        MovieDetails bookdMovie = mdao.getMovie(movieName);
        if (bookdMovie == null) {
            throw new IllegalArgumentException("Movie not found for name: " + movieName);
        }

        usrSession.setAttribute("bookdMovie", bookdMovie);

        LocalDate now = LocalDate.now();
        LocalDate monthLimit = now.plusMonths(1);
        String time = "09:00 am";
        List<String> seatNo1 = new ArrayList<>();

        List<Seat> all = dao.getAllSeat(now, time);
        for (Seat s : all) {
            seatNo1.addAll(s.getSeatNo());
        }

        mdl.addAttribute("user", usrInfo);
        mdl.addAttribute("bookdMovie", bookdMovie);
        mdl.addAttribute("date", now);
        mdl.addAttribute("time", time);
        mdl.addAttribute("max", monthLimit);
        mdl.addAttribute("min", now);
        mdl.addAttribute("seats", seatNo1);
        mdl.addAttribute("seat", usrInfo.getSeat());

        return "bookSeats";
    }
    
    @PostMapping("/check-time")
	public String checkDate(@RequestParam("localdate") String date, @RequestParam("localtime") String time, Model m,
			HttpSession session) {
		Customer object = (Customer) session.getAttribute("usrData");
		MovieDetails bookdMovie = (MovieDetails) session.getAttribute("bookdMovie");
		LocalDate monthLimit = LocalDate.now();     
		LocalDate now = LocalDate.parse(date);
		List<String> seatNo1 = new ArrayList<String>();
		List<Seat> all = dao.getAllSeat(now, time);

			for (Seat s : all) {
				for (String s1 : s.getSeatNo()) {
					seatNo1.add(s1);
				}

			}
			
			session.setAttribute("bookingdate", now);
			session.setAttribute("bookingtime", time);
			m.addAttribute("bookdMovie", bookdMovie);
			m.addAttribute("date", now);
			m.addAttribute("max", monthLimit.plusMonths(1));
			m.addAttribute("min", monthLimit);
			m.addAttribute("time", time);
			m.addAttribute("seats", seatNo1);
			m.addAttribute("user", object);
			
			return "dashboard";
		}
    
    @PostMapping("/seatSelection")
	public String seatSelection(Model mdl, @ModelAttribute("Seat") Seat seat, HttpSession usrSession){
	    
	    // Retrieve session attributes
	    Customer usrInfo = (Customer) usrSession.getAttribute("usrData");
	    MovieDetails movie = (MovieDetails) usrSession.getAttribute("bookdMovie");
	   
	    
//	    mdl.addAttribute("usrSeats",usrSeats);
//	    mdl.addAttribute("user", usrInfo);
	    
	    LocalDate currentDate = LocalDate.now();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date todayDate = Date.from(currentDate.atStartOfDay(defaultZoneId).toInstant());
		LocalDate date = (LocalDate) usrSession.getAttribute("bookingdate");
		String time = (String) usrSession.getAttribute("bookingtime");
		
		if ((seat.getSeatNo().isEmpty()) && (movie.getMovieName().equals(null))) {
//			System.out.println("Seat is null");
			return "redirect:/dashboard";
		}else if (date == null) {
			date = currentDate;
			time = "09:00 am";
			if (((date.isAfter(currentDate)) || (date.equals(currentDate)))
					&& (date.isBefore(currentDate.plusMonths(1)) || date.equals(currentDate.plusMonths(1)))) {
			
				Date date2 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
				List<Double> price = new ArrayList<Double>();
				double sum = 0;
				double p = 270;
				for (String s : seat.getSeatNo()) {
					sum = sum + p;
					price.add(p);
				}
				
				seat.setTotal(sum);
				seat.setPrice(price);
//				seat.setCustomer(usrInfo);

				OrderHistory history = new OrderHistory(seat.getSeatNo(), price, sum, movie.getMovieName(), todayDate, date2, time,
						usrInfo);
				dao.saveSeat(seat, usrInfo, date2, time);
				dao.saveHistory(history, usrInfo);
				List<String> seatNo1 = new ArrayList<String>();
				List<Customer> all = dao.getAll();
				for (Customer c : all) {
					for (Seat s : c.getSeat()) {
						for (String s1 : s.getSeatNo()) {
							seatNo1.add(s1);
						}

					}
				}

				mdl.addAttribute("seats", seatNo1);
				mdl.addAttribute("booked", "your seat book successsfully");
				return "redirect:/final";

			}

		} else {
			if (((date.isAfter(currentDate)) || (date.equals(currentDate)))
					&& (date.isBefore(currentDate.plusMonths(1)) || date.equals(currentDate.plusMonths(1)))) {
				Date date2 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
				List<Double> price = new ArrayList<Double>();
				double sum = 0;
				double p = 270d;
				for (String s : seat.getSeatNo()) {
					sum = sum + p;
					price.add(p);
				}
				seat.setTotal(sum);
				seat.setPrice(price);

				OrderHistory history = new OrderHistory(seat.getSeatNo(), price, sum, movie.getMovieName(), todayDate, date2, time,
						usrInfo);
				dao.saveSeat(seat, usrInfo, date2, time);
				dao.saveHistory(history, usrInfo);
				List<String> seatNo1 = new ArrayList<String>();
				List<Customer> all = dao.getAll();
				for (Customer c : all) {
					for (Seat s : c.getSeat()) {
						for (String s1 : s.getSeatNo()) {
							seatNo1.add(s1);
						}

					}
				}

				mdl.addAttribute("seats", seatNo1);
				mdl.addAttribute("booked", "your seat book successsfully");
				return "redirect:/final";

			}
					
		}
		
		return "redirect:/final";
	}

	@GetMapping("/final")
	public String success(Model mdl, HttpSession session) {
		
		Customer usrInfo = (Customer) session.getAttribute("usrData");
		mdl.addAttribute("user", usrInfo);
		
		session.removeAttribute("bookingdate");
		session.removeAttribute("bookingtime");
		session.removeAttribute("bookdMovie");
		
		String message = (String) session.getAttribute("booked");
		mdl.addAttribute("message", message);
		session.removeAttribute("booked");
		List<MovieDetails> allMovies = mdao.getAllMovies();
		mdl.addAttribute("allMovies", allMovies);
		
		mdl.addAttribute("booked", "your seat book successsfully");
		
		return "main-dashboard";
	}
	
	// orders
	@GetMapping("/orders")
	public String usersOrders(HttpSession usrSession, Model mdl) {
		
		Customer object = (Customer) usrSession.getAttribute("usrData");
		List<OrderHistory> order = dao.getAllHistory(object.getId());
			
		mdl.addAttribute("user", object);
		mdl.addAttribute("orders", order);
		
		return "userOrders";
	}

    
    // Logout User
    @GetMapping("/logout")
    public String logout(HttpSession usrSession) {
        usrSession.invalidate(); // Invalidate entire session
        return "redirect:/";
    }

    // Forgot Password
    @GetMapping("/forgot-password")
    public String forgotPwd() {
        return "forgotPassword";
    }

    @PostMapping("/changePwd")
    public String changePwd(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password, Model mdl) {
        Customer existing = dao.findCustomer(email);
        if (existing != null) {
            dao.deleteUser(existing);
        }
        Customer edited = new Customer();
        edited.setName(name);
        edited.setEmail(email);
        edited.setPassword(password);
        dao.updateUser(edited);

        mdl.addAttribute("relogin", "Login Using New Credentials");
        return "login";
    }
    
 // settings
 	@GetMapping("/settings")
 	public String settings(HttpSession usrSession, Model mdl) {
 		
 		Customer usrInfo = (Customer) usrSession.getAttribute("usrData");
 		mdl.addAttribute("user", usrInfo);
 		return "settings";
 	}
 	
 	// edit
 	@GetMapping("/edit")
 	public String edit() {
 		return "redirect:/editUser";
 	}
 	
 	@GetMapping("/editUser")
 	public String editUser(HttpSession usrSession, Model mdl) {
 		
 		Customer usrInfo = (Customer) usrSession.getAttribute("usrData");
 		mdl.addAttribute("user", usrInfo);
 		return "editUser";
 	}
 	
 	@GetMapping("/updateUser")
 	public String update(@RequestParam("name") String name, @RequestParam("email") String email,
 			@RequestParam("password") String password, Model mdl, HttpSession usrSession) {
 		
 		Customer edited = new Customer();
 		edited.setName(name);
 		edited.setEmail(email);
 		edited.setPassword(password);
 		
 		dao.updateUser(edited);	
 		
 		Customer oldData = (Customer) usrSession.getAttribute("usrData");
 		dao.deleteUser(oldData);
 		
 		mdl.addAttribute("relogin", "Login Using New Credentials");
 		return "/login";
 	}

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model m) {
        m.addAttribute("exception", "Something Went Wrong");
        System.out.println(ex.getMessage());
        System.out.println(ex.getClass().getSimpleName());
        System.out.println(ex.getStackTrace());
        return "error";
    }
}
