package com.movie.booking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.booking.DTO.OrderHistory;
import com.movie.booking.DTO.CurrentDateOperation;
import com.movie.booking.DTO.Customer;
import com.movie.booking.DTO.Seat;

@Service
public class CustomerService {

	// deals with customer data
	@Autowired
	private CustomerRepository cusRepo;
	
	// deals with seat
	@Autowired
	private SeatRepository seatRepo;
	
	// deals with order history
	@Autowired
	private HistoryRepository hrepo;
	
	// movie methods are in movieService
	
	public void saveRegister(Customer dto) {
		cusRepo.save(dto);
	}
	
	public Customer loginInfo(String email, String password) {
		Customer loginData = cusRepo.findByEmailAndPassword(email, password);
		return loginData;
	}
	
	public void updateUser(Customer newDto) {
		cusRepo.save(newDto);
	}
	
	public void deleteUser(Customer dto) {
		cusRepo.deleteById(dto.getId());
	}
	
	public List<Customer> getAll(){
		List<Customer> findAll = cusRepo.findAll();
		return findAll;
	}
	
	public Customer findCustomer(String email) {
		Customer user = cusRepo.findByEmail(email);
		return user;
	}
		
	// seats Mehtods
	public List<Seat> getAllSeat(LocalDate date, String time){
		List<Seat> list = seatRepo.getAllByDate(date, time);
		return list;
	}
		
	public int saveSeat(Seat seat, Customer customer, Date date, String time){
		List<Seat> list = new ArrayList<Seat>();
		list.add(seat);
		customer.setSeat(list);
		CurrentDateOperation cdo= new CurrentDateOperation();
		cdo.setShowDate(date);
		cdo.setShowTime(time);
		cdo.setSeat(list);
		
		seat.setOperation(cdo);
		seat.setOperation(cdo);
		seat.setCustomer(customer);
		Seat save = seatRepo.save(seat);
		return 1;
	}
	
	public List<Seat> getSeats(long id){
		List<Seat> list = seatRepo.getAllSeat(id);
		return list;
	}
	
	// seats saving
//	public void saveSeats(Seats seats) {
//		seatRepo.save(seats);
//	}
	
	
	// history repo
	
	public OrderHistory saveHistory(OrderHistory history, Customer customer) {
		List<OrderHistory> his = new ArrayList<>();
		his.add(history);
		customer.setHistory(his);
		OrderHistory save = hrepo.save(history);
		return save;
	}
	
	public List<OrderHistory> getAllHistory(long id){
		List<OrderHistory> list = hrepo.getAllHistory(id);		
		return list;
	}
	
}
