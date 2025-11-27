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

import dao.BookingDAO;
import dao.TransactionDAO;
import model.Booking;
import model.User;

class BookingCRUDTest {

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
	private BookingCRUD bookingCRUDServlet;
	
	private User clientUser;
	private User adminUser;
	private List<Booking> bookings;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		clientUser = new User("client@test.com", "hash", "Client", "User", "2222222222", "Client", 100.0);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		bookings = Arrays.asList(new Booking(), new Booking());
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		bookingCRUDServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=bookingCRUD");
	}
	
	@Test
	void testDoGet_ClientUser_ShowsOwnBookings() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(bookingDAO.getUserBooking(clientUser)).thenReturn(bookings);
		when(request.getRequestDispatcher("/WEB-INF/crud/bookingCRUD.jsp")).thenReturn(dispatcher);
		
		bookingCRUDServlet.doGet(request, response);
		
		verify(bookingDAO).getUserBooking(clientUser);
		verify(request).setAttribute("bookings", bookings);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_AdminUser_ShowsAllBookings() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(bookingDAO.getAllBooking()).thenReturn(bookings);
		when(request.getRequestDispatcher("/WEB-INF/crud/bookingCRUD.jsp")).thenReturn(dispatcher);
		
		bookingCRUDServlet.doGet(request, response);
		
		verify(bookingDAO).getAllBooking();
		verify(request).setAttribute("bookings", bookings);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_RedirectsToDoGet() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(bookingDAO.getUserBooking(clientUser)).thenReturn(bookings);
		when(request.getRequestDispatcher("/WEB-INF/crud/bookingCRUD.jsp")).thenReturn(dispatcher);
		
		bookingCRUDServlet.doPost(request, response);
		
		verify(bookingDAO).getUserBooking(clientUser);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testConstructor() {
		BookingCRUD bookingCRUD = new BookingCRUD();
		assertNotNull(bookingCRUD);
	}
}
