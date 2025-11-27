package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
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
import model.Picture;
import model.Room;
import model.Transaction;
import model.User;

class OfferTest {

	@Mock
	private UserDAO userDAO;
	
	@Mock
	private AccommodationDAO accommodationDAO;
	
	@Mock
	private PictureDAO pictureDAO;
	
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
	private List<Amenity> amenities;
	
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
		testOffer.setPricePerNight(100.0);
		testOffer.setCleaningFee(50.0);
		rooms = Arrays.asList(new Room());
		amenities = Arrays.asList(new Amenity());
		
		when(request.getSession()).thenReturn(session);
		when(request.getContextPath()).thenReturn("/airbnb");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
	}
	
	@Test
	void testDoGet_DisplaysOffer() throws ServletException, IOException {
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(amenities);
		when(request.getRequestDispatcher("/WEB-INF/offer/offer.jsp")).thenReturn(dispatcher);
		
		offerServlet.doGet(request, response);
		
		verify(offerDAO).getOffer(1);
		verify(roomDAO).getAccommodationRoom(accommodation);
		verify(request).setAttribute("offer", testOffer);
		verify(request).setAttribute("rooms", rooms);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(amenities);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		// Load offer first
		offerServlet.doGet(request, response);
		
		offerServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=offer?id=1");
	}
	
	@Test
	void testDoPost_UserTriesToBookOwnOffer_ShowsWarning() throws ServletException, IOException {
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(amenities);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(offerOwner);
		when(request.getRequestDispatcher("/WEB-INF/offer/offer.jsp")).thenReturn(dispatcher);
		
		offerServlet.doGet(request, response);
		offerServlet.doPost(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(request).setAttribute(eq("alertMessage"), anyString());
	}
	
	@Test
	void testDoPost_InsufficientBalance_ShowsError() throws ServletException, IOException {
		User poorUser = new User("poor@test.com", "hash", "Poor", "User", "9999999999", "Client", 10.0);
		
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(amenities);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(poorUser);
		when(request.getParameter("arrivalDate")).thenReturn("2024-01-01");
		when(request.getParameter("arrivalTime")).thenReturn("14:00");
		when(request.getParameter("departureDate")).thenReturn("2024-01-03");
		when(request.getParameter("departureTime")).thenReturn("10:00");
		when(request.getParameter("nbPerson")).thenReturn("2");
		when(userDAO.getUser("poor@test.com")).thenReturn(poorUser);
		when(request.getRequestDispatcher("/WEB-INF/offer/offer.jsp")).thenReturn(dispatcher);
		
		offerServlet.doGet(request, response);
		offerServlet.doPost(request, response);
		
		verify(request).setAttribute("alertType", "alert-danger");
		verify(request).setAttribute(eq("alertMessage"), anyString());
	}
	
	@Test
	void testDoPost_SuccessfulBooking_CreatesBooking() throws ServletException, IOException {
		when(request.getParameter("id")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(roomDAO.getAccommodationRoom(accommodation)).thenReturn(rooms);
		when(amenityDAO.getRoomAmenity(any(Room.class))).thenReturn(amenities);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("arrivalDate")).thenReturn("2024-01-01");
		when(request.getParameter("arrivalTime")).thenReturn("14:00");
		when(request.getParameter("departureDate")).thenReturn("2024-01-03");
		when(request.getParameter("departureTime")).thenReturn("10:00");
		when(request.getParameter("nbPerson")).thenReturn("2");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(transactionDAO.createTransaction(eq(testUser), eq(offerOwner), anyDouble())).thenReturn(new Transaction());
		when(request.getContextPath()).thenReturn("/app");
		
		offerServlet.doGet(request, response);
		offerServlet.doPost(request, response);
		
		verify(userDAO).debit(eq("test@test.com"), anyDouble());
		verify(userDAO).credit(eq("owner@test.com"), anyDouble());
		verify(transactionDAO).createTransaction(eq(testUser), eq(offerOwner), anyDouble());
		verify(bookingDAO).createBooking(eq(testUser), eq(testOffer), any(Transaction.class), any(), any(), eq(2), anyDouble());
		verify(response).sendRedirect("/app/bookingCRUD");
	}
	
	@Test
	void testConstructor() {
		Offer offer = new Offer();
		assertNotNull(offer);
	}
}
