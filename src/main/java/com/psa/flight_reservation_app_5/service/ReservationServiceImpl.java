package com.psa.flight_reservation_app_5.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psa.flight_reservation_app_5.dto.ReservationRequest;
import com.psa.flight_reservation_app_5.entity.Flight;
import com.psa.flight_reservation_app_5.entity.Passenger;
import com.psa.flight_reservation_app_5.entity.Reservation;
import com.psa.flight_reservation_app_5.repository.FlightRepository;
import com.psa.flight_reservation_app_5.repository.PassengerRepository;
import com.psa.flight_reservation_app_5.repository.ReservationRepository;
import com.psa.flight_reservation_app_5.utilities.EmailUtil;
import com.psa.flight_reservation_app_5.utilities.PdfGenerator;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private PassengerRepository passengerRepo;

	@Autowired
	private FlightRepository flightRepo;

	@Autowired
	private ReservationRepository reservationRepo;

	private static String filePath = "D:\\stscode\\flight_reservation_app_5\\tickets\\booking";

	@Autowired
	private PdfGenerator pdf;

	@Autowired
	private EmailUtil emailUtil;

	@Override
	public Reservation bookFlight(ReservationRequest request) {

		Passenger passenger = new Passenger();
		// go to passenger table
		passenger.setFirstName(request.getFirstName());
		passenger.setLastName(request.getLastName());
		passenger.setMiddleName(request.getMiddleName());
		passenger.setEmail(request.getEmail());
		passenger.setPhone(request.getPhone());

		passengerRepo.save(passenger);
		//
		Long flightId = request.getFlightId();
		Optional<Flight> findById = flightRepo.findById(flightId);
		Flight flight = findById.get();

		Reservation reservation = new Reservation();
		reservation.setFlight(flight);
		reservation.setPassenger(passenger);
		reservation.setCheckedIn(false);
		reservation.setNumberOfBags(0);

		pdf.generatePDF(filePath + reservation.getId() + ".pdf", reservation.getPassenger().getFirstName(),
				reservation.getPassenger().getEmail(), reservation.getPassenger().getPhone(),
				reservation.getFlight().getOperatingAirlines(), reservation.getFlight().getDateOfDeparture(),
				reservation.getFlight().getDepartureCity(), reservation.getFlight().getArrivalCity());

		String attachement = filePath + reservation.getId() + ".pdf";
		emailUtil.sendItinerary(request.getEmail(), attachement);

		reservationRepo.save(reservation);

		return reservation;
	}

}
