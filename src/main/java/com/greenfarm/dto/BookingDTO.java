package com.greenfarm.dto;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.StatusBooking;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

	private Integer bookingid;
    private LocalDateTime bookingdate;
    private Integer numparticipants;
    private Float totalprice;
    private User user;
    private Tour tour;
    private StatusBooking statusbooking;
    
}