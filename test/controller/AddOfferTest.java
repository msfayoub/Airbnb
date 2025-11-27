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

class AddOfferTest {

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
	private User adminUser;
	private Accommodation accommodation;
	private List<Accommodation> accommodations;
	private Offer testOffer;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 100.0);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		accommodation = new Accommodation();
		accommodation.setId(1);
		accommodations = Arrays.asList(accommodation);
		testOffer = new Offer();
		testOffer.setId(1);
		testOffer.setUser(testUser);
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		addOfferServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=addOffer");
	}
	
	@Test
	void testDoGet_NoAccommodations_ShowsWarning() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn(null);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(Arrays.asList());
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_WithAccommodations_ShowsForm() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn(null);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/offer/addOffer.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		
		verify(request).setAttribute("accommodations", accommodations);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_EditOfferNotFound_ShowsWarning() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("999");
		when(offerDAO.getOffer(999)).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/offer/addOffer.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_EditOfferNotAuthorized_ShowsError() throws ServletException, IOException {
		User otherUser = new User("other@test.com", "hash", "Other", "User", "9999999999", "Client", 50.0);
		testOffer.setUser(otherUser);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(request.getRequestDispatcher("/WEB-INF/offer/addOffer.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-danger");
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_AdminCanEditAnyOffer() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(request.getParameter("edit")).thenReturn("1");
		when(offerDAO.getOffer(1)).thenReturn(testOffer);
		when(request.getRequestDispatcher("/WEB-INF/addOffer.jsp")).thenReturn(dispatcher);
		
		addOfferServlet.doGet(request, response);
		
		verify(request).setAttribute("offer", testOffer);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_CreateNewOffer_Success() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("startAvailability")).thenReturn("2024-01-01");
		when(request.getParameter("endAvailability")).thenReturn("2024-12-31");
		when(request.getParameter("pricePerNight")).thenReturn("100.0");
		when(request.getParameter("cleaningFee")).thenReturn("50.0");
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/offer/addOffer.jsp")).thenReturn(dispatcher);
		
		// Populate accommodations list first
		addOfferServlet.doGet(request, response);
		
		addOfferServlet.doPost(request, response);
		
		verify(offerDAO).createOffer(eq(testUser), eq(accommodation), any(Calendar.class), any(Calendar.class), eq(100.0), eq(50.0));
		verify(request).setAttribute("alertType", "alert-success");
	}
	
	@Test
	void testConstructor() {
		AddOffer addOffer = new AddOffer();
		assertNotNull(addOffer);
	}
}
