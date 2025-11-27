package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.AccommodationDAO;
import dao.AmenityDAO;
import dao.BookingDAO;
import dao.OfferDAO;
import dao.PictureDAO;
import dao.RoomDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import model.Accommodation;
import model.Amenity;
import model.Room;
import model.User;

/**
 * Additional edge case tests for Offer controller
 */
class OfferEdgeCaseTest {

	@Mock
	private UserDAO userDAO;
	
	@Mock
	private AccommodationDAO accommodationDAO;
	
	@Mock
	private OfferDAO offerDAO;
	
	@Mock
	private BookingDAO bookingDAO;
	
	@Mock
	private RoomDAO roomDAO;
	
	@Mock
	private AmenityDAO amenityDAO;
	
	@Mock
	private TransactionDAO transactionDAO;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private RequestDispatcher dispatcher;
	
	@InjectMocks
	private Offer offerServlet;
	
	private model.Offer testOffer;
	private User testUser;
	private User offerOwner;
	private Accommodation accommodation;
	private List<Room> rooms;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		offerOwner = new User("owner@test.com", "hash", "Owner", "User", "1111111111", "Client", 0.0);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 500.0);
		accommodation = new Accommodation();
		testOffer = new model.Offer();
		testOffer.setId(1);
		testOffer.setUser(offerOwner);
		testOffer.setAccommodation(accommodation);
		testOffer.setPricePerNight(50.0);
		testOffer.setCleaningFee(25.0);
		rooms = Arrays.asList(new Room());
	}
	
	@Test
	void testDoPost_OneDayStay_CalculatesCorrectPrice() throws ServletException, IOException {
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(Arrays.asList());
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("arrivalDate")).thenReturn("2024-01-01");
		when(request.getParameter("arrivalTime")).thenReturn("14:00");
		when(request.getParameter("departureDate")).thenReturn("2024-01-02");
		when(request.getParameter("departureTime")).thenReturn("10:00");
		when(request.getParameter("nbPerson")).thenReturn("1");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(transactionDAO.createTransaction(any(), any(), anyDouble())).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		offerServlet.doGet(request, response);
		offerServlet.doPost(request, response);
		
		// 1 night * 50.0 + 25.0 cleaning = 75.0
		verify(userDAO).debit(eq("test@test.com"), eq(75.0));
	}
	
	@Test
	void testDoPost_ExactBalance_SucceedsBooking() throws ServletException, IOException {
		User exactBalanceUser = new User("exact@test.com", "hash", "Exact", "Balance", "9999999999", "Client", 75.0);
		
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(Arrays.asList());
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(exactBalanceUser);
		when(request.getParameter("arrivalDate")).thenReturn("2024-01-01");
		when(request.getParameter("arrivalTime")).thenReturn("14:00");
		when(request.getParameter("departureDate")).thenReturn("2024-01-02");
		when(request.getParameter("departureTime")).thenReturn("10:00");
		when(request.getParameter("nbPerson")).thenReturn("2");
		when(userDAO.getUser("exact@test.com")).thenReturn(exactBalanceUser);
		when(transactionDAO.createTransaction(any(), any(), anyDouble())).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		offerServlet.doGet(request, response);
		offerServlet.doPost(request, response);
		
		verify(userDAO).debit(eq("exact@test.com"), eq(75.0));
		verify(response).sendRedirect("/app/bookingCRUD");
	}
	
	@Test
	void testDoPost_LongStay_CalculatesCorrectly() throws ServletException, IOException {
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(Arrays.asList());
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("arrivalDate")).thenReturn("2024-01-01");
		when(request.getParameter("arrivalTime")).thenReturn("15:00");
		when(request.getParameter("departureDate")).thenReturn("2024-01-08");
		when(request.getParameter("departureTime")).thenReturn("11:00");
		when(request.getParameter("nbPerson")).thenReturn("4");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(transactionDAO.createTransaction(any(), any(), anyDouble())).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		offerServlet.doGet(request, response);
		offerServlet.doPost(request, response);
		
		// 7 nights * 50.0 + 25.0 = 375.0
		verify(userDAO).debit(eq("test@test.com"), eq(375.0));
	}
	
	@Test
	void testDoGet_OfferWithMultipleRooms_LoadsAllAmenities() throws ServletException, IOException {
		Room room1 = new Room();
		Room room2 = new Room();
		Room room3 = new Room();
		List<Room> multipleRooms = Arrays.asList(room1, room2, room3);
		
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(multipleRooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(Arrays.asList(new Amenity()));
		when(request.getRequestDispatcher("/WEB-INF/offer/offer.jsp")).thenReturn(dispatcher);
		
		offerServlet.doGet(request, response);
		
		verify(amenityDAO, times(3)).getRoomAmenity(any(Room.class));
	}
}
