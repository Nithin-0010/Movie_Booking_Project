package com.movie.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.booking.DTO.CurrentDateOperation;


@Repository
public interface OpeningDateRepository extends JpaRepository<CurrentDateOperation, Long> {

}

