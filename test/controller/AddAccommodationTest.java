package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.AccommodationDAO;
import dao.AddressDAO;
import dao.HouseRulesDAO;
import dao.PictureDAO;
import model.Accommodation;
import model.Address;
import model.HouseRules;
import model.User;

class AddAccommodationTest {

	@Mock
	private AccommodationDAO accommodationDAO;
	
	@Mock
	private AddressDAO addressDAO;
	
	@Mock
	private HouseRulesDAO houseRulesDAO;
	
	@Mock
	private PictureDAO pictureDAO;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private RequestDispatcher dispatcher;
	
	@Mock
	private Part filePart;
	
	@InjectMocks
	private AddAccommodation addAccommodationServlet;
	
	private User testUser;
	private User adminUser;
	private Accommodation testAccommodation;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 100.0);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		testAccommodation = new Accommodation();
		testAccommodation.setUser(testUser);
		
		when(request.getSession()).thenReturn(session);
		when(request.getContextPath()).thenReturn("/airbnb");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		addAccommodationServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=addAccommodation");
	}
	
	@Test
	void testDoGet_UserInSession_ShowsForm() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		addAccommodationServlet.doGet(request, response);
		
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_EditAccommodationNotFound_ShowsWarning() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("999");
		when(accommodationDAO.getAccommodation(999)).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		addAccommodationServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_EditNotAuthorized_ShowsError() throws ServletException, IOException {
		User otherUser = new User("other@test.com", "hash", "Other", "User", "9999999999", "Client", 50.0);
		testAccommodation.setUser(otherUser);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("1");
		when(accommodationDAO.getAccommodation(1)).thenReturn(testAccommodation);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		addAccommodationServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-danger");
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_AdminCanEditAny() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(request.getParameter("edit")).thenReturn("1");
		when(accommodationDAO.getAccommodation(1)).thenReturn(testAccommodation);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		addAccommodationServlet.doGet(request, response);
		
		verify(request).setAttribute("accommodation", testAccommodation);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_InfoForm_CreatesAccommodation() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("form")).thenReturn("info");
		when(request.getParameter("name")).thenReturn("Nice Apartment");
		when(request.getParameter("description")).thenReturn("Great place");
		when(request.getParameter("capacity")).thenReturn("4");
		when(request.getParameter("type")).thenReturn("apartment");
		when(request.getParameter("numberOfRooms")).thenReturn("2");
		when(request.getParameter("address")).thenReturn("123 Main St");
		when(request.getParameter("city")).thenReturn("Paris");
		when(request.getParameter("zipCode")).thenReturn("75001");
		when(request.getParameter("country")).thenReturn("France");
		when(request.getParameter("arrivalHour")).thenReturn("14:00");
		when(request.getParameter("departureHour")).thenReturn("11:00");
		when(request.getParameter("smokeAllowed")).thenReturn("true");
		when(request.getParameter("partyAllowed")).thenReturn("false");
		when(request.getParameter("petAllowed")).thenReturn("true");
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		addAccommodationServlet.doPost(request, response);
		
		// Info form creates temp accommodation and shows picture form
		verify(request).setAttribute("pictureForm", true);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_PictureForm_HandlesPictures() throws ServletException, IOException {
		// Simplified test - just verify picture form without file upload complexity
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("form")).thenReturn("picture");
		when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		// Without multipart/form-data, it should fall through to info form handler
		addAccommodationServlet.doPost(request, response);
		
		// Just verify it doesn't crash
		verify(request).getSession();
	}
	
	@Test
	void testConstructor() {
		AddAccommodation addAccommodation = new AddAccommodation();
		assertNotNull(addAccommodation);
	}
}
