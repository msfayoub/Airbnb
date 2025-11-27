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
import dao.RoomDAO;
import model.Accommodation;
import model.Room;
import model.User;

class AddRoomsTest {

	@Mock
	private AccommodationDAO accommodationDAO;
	
	@Mock
	private RoomDAO roomDAO;
	
	@Mock
	private AmenityDAO amenityDAO;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private RequestDispatcher dispatcher;
	
	@InjectMocks
	private AddRooms addRoomsServlet;
	
	private User testUser;
	private Accommodation accommodation;
	private List<Accommodation> accommodations;
	private Room testRoom;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 100.0);
		accommodation = new Accommodation();
		accommodation.setId(1);
		accommodations = Arrays.asList(accommodation);
		testRoom = new Room();
		testRoom.setId(1);
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		addRoomsServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=addRooms");
	}
	
	@Test
	void testDoGet_NoAccommodations_ShowsWarning() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(Arrays.asList());
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_WithAccommodations_ShowsForm() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		
		verify(request).setAttribute("accommodations", accommodations);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_AddBedroom_WithBeds() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bedroom");
		when(request.getParameter("singleBedNumber")).thenReturn("2");
		when(request.getParameter("doubleBedNumber")).thenReturn("1");
		when(request.getParameterValues("bedroomAmenities")).thenReturn(new String[]{"wardrobe", "desk"});
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(roomDAO.createRoom(accommodation, "bedroom")).thenReturn(testRoom);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		// Populate accommodations list first
		addRoomsServlet.doGet(request, response);
		
		addRoomsServlet.doPost(request, response);
		
		verify(roomDAO).createRoom(accommodation, "bedroom");
		verify(amenityDAO, atLeast(5)).createAmenity(eq(testRoom), anyString());
		verify(request).setAttribute("alertType", "alert-success");
	}
	
	@Test
	void testDoPost_AddBathroom() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bathroom");
		when(request.getParameterValues("bathroomAmenities")).thenReturn(new String[]{"shower", "bathtub"});
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(roomDAO.createRoom(accommodation, "bathroom")).thenReturn(testRoom);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		verify(roomDAO).createRoom(accommodation, "bathroom");
		verify(amenityDAO, times(2)).createAmenity(eq(testRoom), anyString());
	}
	
	@Test
	void testDoPost_AddKitchen() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("kitchen");
		when(request.getParameterValues("kitchenAmenities")).thenReturn(new String[]{"fridge", "oven", "microwave"});
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(roomDAO.createRoom(accommodation, "kitchen")).thenReturn(testRoom);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		verify(roomDAO).createRoom(accommodation, "kitchen");
		verify(amenityDAO, times(3)).createAmenity(eq(testRoom), anyString());
	}
	
	@Test
	void testDoPost_NoAmenities_ShowsWarning() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bedroom");
		when(request.getParameter("singleBedNumber")).thenReturn("0");
		when(request.getParameter("doubleBedNumber")).thenReturn("0");
		when(request.getParameterValues("bedroomAmenities")).thenReturn(null);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(accommodations);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		verify(roomDAO, never()).createRoom(any(), anyString());
		verify(request, atLeast(1)).setAttribute("alertType", "alert-warning");
	}
	
	@Test
	void testConstructor() {
		AddRooms addRooms = new AddRooms();
		assertNotNull(addRooms);
	}
}
