package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.OfferDAO;
import model.Offer;

class DisplaySearchTest {

	@Mock
	private OfferDAO offerDAO;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private RequestDispatcher dispatcher;
	
	@Mock
	private ServletContext servletContext;
	
	@InjectMocks
	private DisplaySearch displaySearchServlet;
	
	private List<Offer> offers;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		offers = Arrays.asList(new Offer(), new Offer());
	}
	
	@Test
	void testDoGet_ValidSearch_DisplaysOffers() throws ServletException, IOException {
		when(request.getParameter("ville")).thenReturn("Paris");
		when(request.getParameter("startStay")).thenReturn("2024-01-01");
		when(request.getParameter("endStay")).thenReturn("2024-01-10");
		when(request.getParameter("capacity")).thenReturn("2");
		when(offerDAO.SearchedOffer(eq("Paris"), any(Calendar.class), any(Calendar.class), eq(2))).thenReturn(offers);
		when(displaySearchServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/home.jsp")).thenReturn(dispatcher);
		
		displaySearchServlet.doGet(request, response);
		
		verify(offerDAO).SearchedOffer(eq("Paris"), any(Calendar.class), any(Calendar.class), eq(2));
		verify(request).setAttribute("offers", offers);
		verify(request).setAttribute("searchType", "new");
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_MissingParameters_RedirectsToHome() throws ServletException, IOException {
		when(request.getParameter("ville")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		displaySearchServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/home");
	}
	
	@Test
	void testDoGet_InvalidCapacity_HandlesException() throws ServletException, IOException {
		when(request.getParameter("ville")).thenReturn("Paris");
		when(request.getParameter("startStay")).thenReturn("2024-01-01");
		when(request.getParameter("endStay")).thenReturn("2024-01-10");
		when(request.getParameter("capacity")).thenReturn("invalid");
		when(request.getContextPath()).thenReturn("/app");
		
		displaySearchServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/home");
	}
	
	@Test
	void testDoGet_InvalidDateFormat_HandlesException() throws ServletException, IOException {
		when(request.getParameter("ville")).thenReturn("Paris");
		when(request.getParameter("startStay")).thenReturn("invalid-date");
		when(request.getParameter("endStay")).thenReturn("2024-01-10");
		when(request.getParameter("capacity")).thenReturn("2");
		when(offerDAO.SearchedOffer(eq("Paris"), any(Calendar.class), any(Calendar.class), eq(2))).thenReturn(offers);
		when(displaySearchServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/home.jsp")).thenReturn(dispatcher);
		
		displaySearchServlet.doGet(request, response);
		
		// Should still forward to home.jsp even with parse exception
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testConstructor() {
		DisplaySearch displaySearch = new DisplaySearch();
		assertNotNull(displaySearch);
	}
}
