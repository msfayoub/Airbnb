package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

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

/**
 * Additional edge case tests for AddRooms controller
 */
class AddRoomsEdgeCaseTest {

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
	private Room testRoom;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 100.0);
		accommodation = new Accommodation();
		accommodation.setId(1);
		testRoom = new Room();
		testRoom.setId(1);
	}
	
	@Test
	void testDoPost_BedroomWithOnlySingleBeds() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bedroom");
		when(request.getParameter("singleBedNumber")).thenReturn("3");
		when(request.getParameter("doubleBedNumber")).thenReturn("0");
		when(request.getParameterValues("bedroomAmenities")).thenReturn(null);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(Arrays.asList(accommodation));
		when(roomDAO.createRoom(accommodation, "bedroom")).thenReturn(testRoom);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		verify(amenityDAO, times(3)).createAmenity(eq(testRoom), eq("singleBed"));
	}
	
	@Test
	void testDoPost_BedroomWithOnlyDoubleBeds() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bedroom");
		when(request.getParameter("singleBedNumber")).thenReturn("0");
		when(request.getParameter("doubleBedNumber")).thenReturn("2");
		when(request.getParameterValues("bedroomAmenities")).thenReturn(null);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(Arrays.asList(accommodation));
		when(roomDAO.createRoom(accommodation, "bedroom")).thenReturn(testRoom);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		verify(amenityDAO, times(2)).createAmenity(eq(testRoom), eq("doubleBed"));
	}
	
	@Test
	void testDoPost_InvalidBedNumbers_HandlesException() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bedroom");
		when(request.getParameter("singleBedNumber")).thenReturn("not-a-number");
		when(request.getParameter("doubleBedNumber")).thenReturn("also-invalid");
		when(request.getParameterValues("bedroomAmenities")).thenReturn(null);
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(Arrays.asList(accommodation));
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		verify(roomDAO, never()).createRoom(any(), anyString());
		verify(request, atLeast(1)).setAttribute("alertType", "alert-warning");
	}
	
	@Test
	void testDoPost_BathroomWithMultipleAmenities() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bathroom");
		when(request.getParameterValues("bathroomAmenities")).thenReturn(new String[]{"shower", "bathtub", "toilet", "sink"});
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(Arrays.asList(accommodation));
		when(roomDAO.createRoom(accommodation, "bathroom")).thenReturn(testRoom);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		verify(amenityDAO, times(4)).createAmenity(eq(testRoom), anyString());
	}
	
	@Test
	void testDoPost_BedroomWithBedsAndAmenities() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("accommodationIndex")).thenReturn("0");
		when(request.getParameter("type")).thenReturn("bedroom");
		when(request.getParameter("singleBedNumber")).thenReturn("1");
		when(request.getParameter("doubleBedNumber")).thenReturn("1");
		when(request.getParameterValues("bedroomAmenities")).thenReturn(new String[]{"wardrobe", "desk", "tv"});
		when(accommodationDAO.getUserAccommodation(testUser)).thenReturn(Arrays.asList(accommodation));
		when(roomDAO.createRoom(accommodation, "bedroom")).thenReturn(testRoom);
		when(request.getRequestDispatcher("/WEB-INF/accommodation/addRooms.jsp")).thenReturn(dispatcher);
		
		addRoomsServlet.doGet(request, response);
		addRoomsServlet.doPost(request, response);
		
		// 1 single bed + 1 double bed + 3 amenities = 5 total
		verify(amenityDAO, times(5)).createAmenity(eq(testRoom), anyString());
	}
}
