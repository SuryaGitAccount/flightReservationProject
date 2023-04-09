package com.psa.flight_reservation_app_5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.psa.flight_reservation_app_5.dto.ReservationRequest;
import com.psa.flight_reservation_app_5.entity.Reservation;
import com.psa.flight_reservation_app_5.service.ReservationService;
import com.psa.flight_reservation_app_5.utilities.EmailUtil;

@Controller
public class ReservationController {
	@Autowired
	EmailUtil emailUtil;

	@Autowired
	private ReservationService reservationService;

	@RequestMapping("/completeReservation")
	public String completeReservation(ReservationRequest request, ModelMap model) {
		Reservation reservation = reservationService.bookFlight(request);
		model.addAttribute("reservationId", reservation.getId());

		return "confirmReservation";
	}

}
