package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import dao.OfferDAO;
import model.Accommodation;
import model.Offer;
import model.User;

/**
 * Additional edge case tests for AddOffer controller
 */
class AddOfferEdgeCaseTest {

	@Mock
	private AccommodationDAO accommodationDAO;
	
	@Mock
	private OfferDAO offerDAO;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private RequestDispatcher dispatcher;
	
	@InjectMocks
	private AddOffer addOfferServlet;
	
	private User testUser;
	private Accommodation accommodation;
	private List<Accommodation> accommodations;
	private Offer testOffer;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 100.0);
		accommodation = new Accommodation();
		accommodations = Arrays.asList(accommodation);
		testOffer = new Offer();
		testOffer.setId(1);
		testOffer.setUser(testUser);
		
		when(request.getSession()).thenReturn(session);
		when(request.getContextPath()).thenReturn("/airbnb");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
	}
	
	@Test
	void testDoPost_UpdateExistingOffer() throws ServletException, IOException {
		Calendar start = new GregorianCalendar(2024, 0, 1);
		Calendar end = new GregorianCalendar(2024, 11, 31);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("startAvailability")).thenReturn("2024-01-01");
		when(request.getParameter("endAvailability")).thenReturn("2024-12-31");
		when(request.getParameter("pricePerNight")).thenReturn("150.0");
		when(request.getParameter("cleaningFee")).thenReturn("75.0");
		when(request.getParameter("edit")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/offer/addOffer.jsp")).thenReturn(dispatcher);
		when(request.getContextPath()).thenReturn("/app");
		
		// Setup edit mode first
		addOfferServlet.doGet(request, response);
		
		// Now update
		when(request.getParameter("edit")).thenReturn(null);
		addOfferServlet.doPost(request, response);
		
		verify(offerDAO).updateOffer(testOffer);
		verify(response).sendRedirect("/app/offerCRUD");
	}
	
	@Test
	void testDoPost_NullPriceParameters() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("startAvailability")).thenReturn("2024-01-01");
		when(request.getParameter("endAvailability")).thenReturn("2024-12-31");
		when(request.getParameter("pricePerNight")).thenReturn(null);
		when(request.getParameter("cleaningFee")).thenReturn(null);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/offer/addOffer.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		addOfferServlet.doPost(request, response);
		
		verify(offerDAO, never()).createOffer(any(), any(), any(), any(), anyDouble(), anyDouble());
	}
	
	@Test
	void testDoPost_InvalidDateFormat() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("startAvailability")).thenReturn("invalid-date");
		when(request.getParameter("endAvailability")).thenReturn("also-invalid");
		when(request.getParameter("pricePerNight")).thenReturn("100.0");
		when(request.getParameter("cleaningFee")).thenReturn("50.0");
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/offer/addOffer.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		addOfferServlet.doPost(request, response);
		
		// Should still attempt to create offer even with parse exception
		verify(offerDAO).createOffer(eq(testUser), eq(accommodation), any(Calendar.class), any(Calendar.class), eq(100.0), eq(50.0));
	}
	
	@Test
	void testDoGet_UserOwnsOffer_CanEdit() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(request.getRequestDispatcher("/WEB-INF/addOffer.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		
		verify(request).setAttribute("offer", testOffer);
		verify(dispatcher).forward(request, response);
	}
}
