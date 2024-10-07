package com.movie.booking.DTO;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ElementCollection;

@Entity
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "s_id")
	private long id;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> seatNo;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Double> price;
	
	@Column(name="total")
	private double total;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Customer customer;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private CurrentDateOperation operation;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(List<String> seatNo) {
		this.seatNo = seatNo;
	}

	public List<Double> getPrice() {
		return price;
	}

	public void setPrice(List<Double> price) {
		this.price = price;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CurrentDateOperation getOperation() {
		return operation;
	}

	public void setOperation(CurrentDateOperation operation) {
		this.operation = operation;
	}

	public Seat(long id, List<String> seatNo, List<Double> price, double total, Customer customer,
			CurrentDateOperation operation) {
		super();
		this.id = id;
		this.seatNo = seatNo;
		this.price = price;
		this.total = total;
		this.customer = customer;
		this.operation = operation;
	}

	public Seat(List<String> seatNo, List<Double> price, double total, Customer customer,
			CurrentDateOperation operation) {
		super();
		this.seatNo = seatNo;
		this.price = price;
		this.total = total;
		this.customer = customer;
		this.operation = operation;
	}

	public Seat() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Seats [id=" + id + ", seatNo=" + seatNo + ", price=" + price + ", total=" + total + ", customer="
				+ customer + ", operation=" + operation + "]";
	}

	
//	@ManyToOne
//	@JoinColumn(name = "customer_id")
//	private Customer customer;
	
}
