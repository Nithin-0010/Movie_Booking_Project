package com.movie.booking.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.movie.booking.DTO.OrderHistory;

@Repository
public interface HistoryRepository extends JpaRepository<OrderHistory, Long>{

	@Query(value = "select * from order_history where customer_id=? ORDER BY h_id DESC", nativeQuery = true)
	public List<OrderHistory> getAllHistory(long id);
	
	@Query("SELECT h FROM OrderHistory h")
	public List<OrderHistory> getHistories();
}
