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
import model.Accommodation;
import model.User;

class AccommodationCRUDTest {

	@Mock
	private AccommodationDAO accommodationDAO;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private RequestDispatcher dispatcher;
	
	@InjectMocks
	private AccommodationCRUD accommodationCRUDServlet;
	
	private User clientUser;
	private User adminUser;
	private List<Accommodation> accommodations;
	private Accommodation testAccommodation;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		clientUser = new User("client@test.com", "hash", "Client", "User", "2222222222", "Client", 100.0);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		testAccommodation = new Accommodation();
		accommodations = Arrays.asList(testAccommodation, new Accommodation());
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		accommodationCRUDServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=accommodationCRUD");
	}
	
	@Test
	void testDoGet_ClientUser_ShowsOwnAccommodations() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(accommodationDAO.getUserAccommodation(clientUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/crud/accommodationCRUD.jsp")).thenReturn(dispatcher);
		
		accommodationCRUDServlet.doGet(request, response);
		
		verify(accommodationDAO).getUserAccommodation(clientUser);
		verify(request).setAttribute("accommodations", accommodations);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_AdminUser_ShowsAllAccommodations() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(accommodationDAO.getAllAccommodation()).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/crud/accommodationCRUD.jsp")).thenReturn(dispatcher);
		
		accommodationCRUDServlet.doGet(request, response);
		
		verify(accommodationDAO).getAllAccommodation();
		verify(request).setAttribute("accommodations", accommodations);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_DeleteAction_DeletesAccommodation() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(request.getParameter("action")).thenReturn("delete");
		when(request.getParameter("index")).thenReturn("0");
		when(accommodationDAO.getUserAccommodation(clientUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/crud/accommodationCRUD.jsp")).thenReturn(dispatcher);
		
		// Populate accommodations list first
		accommodationCRUDServlet.doGet(request, response);
		
		accommodationCRUDServlet.doPost(request, response);
		
		verify(accommodationDAO).deleteAccommodation(testAccommodation);
	}
	
	@Test
	void testDoPost_EditAction_RedirectsToAddAccommodation() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(request.getParameter("action")).thenReturn("edit");
		when(request.getParameter("index")).thenReturn("0");
		when(request.getContextPath()).thenReturn("/app");
		when(accommodationDAO.getUserAccommodation(clientUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/crud/accommodationCRUD.jsp")).thenReturn(dispatcher);
		
		accommodationCRUDServlet.doGet(request, response);
		accommodationCRUDServlet.doPost(request, response);
		
		verify(response).sendRedirect(anyString());
	}
	
	@Test
	void testDoPost_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		accommodationCRUDServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=accommodationCRUD");
	}
	
	@Test
	void testConstructor() {
		AccommodationCRUD accommodationCRUD = new AccommodationCRUD();
		assertNotNull(accommodationCRUD);
	}
}
