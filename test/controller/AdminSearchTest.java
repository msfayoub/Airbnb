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
import dao.BookingDAO;
import dao.OfferDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import model.Accommodation;
import model.Booking;
import model.Offer;
import model.User;

class AdminSearchTest {

	@Mock
	private UserDAO userDAO;
	
	@Mock
	private AccommodationDAO accommodationDAO;
	
	@Mock
	private OfferDAO offerDAO;
	
	@Mock
	private BookingDAO bookingDAO;
	
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
	private AdminSearch adminSearchServlet;
	
	private User adminUser;
	private User clientUser;
	private User searchedUser;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		clientUser = new User("client@test.com", "hash", "Client", "User", "2222222222", "Client", 100.0);
		searchedUser = new User("searched@test.com", "hash", "Searched", "User", "3333333333", "Client", 50.0);
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		adminSearchServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=adminSearch");
	}
	
	@Test
	void testDoGet_NonAdminUser_RedirectsToHome() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(request.getContextPath()).thenReturn("/app");
		
		adminSearchServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/home");
	}
	
	@Test
	void testDoGet_AdminUser_ShowsSearchPage() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(request.getRequestDispatcher("/WEB-INF/admin/adminSearch.jsp")).thenReturn(dispatcher);
		
		adminSearchServlet.doGet(request, response);
		
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		adminSearchServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=adminSearch");
	}
	
	@Test
	void testDoPost_NonAdminUser_RedirectsToHome() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(request.getContextPath()).thenReturn("/app");
		
		adminSearchServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/home");
	}
	
	@Test
	void testDoPost_UserNotFound_ShowsWarning() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(request.getParameter("searchInput")).thenReturn("nonexistent@test.com");
		when(userDAO.getUser("nonexistent@test.com")).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/admin/adminSearch.jsp")).thenReturn(dispatcher);
		
		adminSearchServlet.doPost(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_UserFound_DisplaysResults() throws ServletException, IOException {
		List<Accommodation> accommodations = Arrays.asList(new Accommodation());
		List<Offer> offers = Arrays.asList(new Offer());
		List<Booking> bookings = Arrays.asList(new Booking());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(request.getParameter("searchInput")).thenReturn("searched@test.com");
		when(userDAO.getUser("searched@test.com")).thenReturn(searchedUser);
		when(accommodationDAO.getUserAccommodation(searchedUser)).thenReturn(accommodations);
		when(offerDAO.getUserOffer(searchedUser)).thenReturn(offers);
		when(bookingDAO.getUserBooking(searchedUser)).thenReturn(bookings);
		when(request.getRequestDispatcher("/WEB-INF/admin/adminSearch.jsp")).thenReturn(dispatcher);
		
		adminSearchServlet.doPost(request, response);
		
		verify(request).setAttribute("accommodations", accommodations);
		verify(request).setAttribute("offers", offers);
		verify(request).setAttribute("bookings", bookings);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testConstructor() {
		AdminSearch adminSearch = new AdminSearch();
		assertNotNull(adminSearch);
	}
}
