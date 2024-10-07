package com.movie.booking.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.OneToOne;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity    // we can import lombok.Data and use @Data annotation to avoid creating getter, setter,
@Table(name = "customers")      //  constructor, toString etc. 
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
//	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	private List<Seats> seats;
	
//	public List<Seats> getSeats() {
//		return seats;
//	}
//	public void setSeats(List<Seats> seats) {
//		this.seats = seats;
//	}
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	private List<Seat> seat;
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<OrderHistory> history;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Seat> getSeat() {
		return seat;
	}

	public void setSeat(List<Seat> seat) {
		this.seat = seat;
	}


	public List<OrderHistory> getHistory() {
		return history;
	}

	public void setHistory(List<OrderHistory> history) {
		this.history = history;
	}


	public Customer(String name, String email, String password, List<Seat> seat, List<OrderHistory> history) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.seat = seat;
		this.history = history;
	}

	public Customer(long id, String name, String email, String password, List<Seat> seat, List<OrderHistory> history) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.seat = seat;
		this.history = history;
	}

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public String toString() {
//		return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", seat="
//				+ seat + ", history=" + history + "]";
//	}
	
}
