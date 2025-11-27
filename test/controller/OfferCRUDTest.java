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

import dao.OfferDAO;
import model.Offer;
import model.User;

class OfferCRUDTest {

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
	private OfferCRUD offerCRUDServlet;
	
	private User clientUser;
	private User adminUser;
	private List<Offer> offers;
	private Offer testOffer;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		clientUser = new User("client@test.com", "hash", "Client", "User", "2222222222", "Client", 100.0);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		testOffer = new Offer();
		testOffer.setId(1);
		offers = Arrays.asList(testOffer, new Offer());
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		offerCRUDServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=offerCRUD");
	}
	
	@Test
	void testDoGet_ClientUser_ShowsOwnOffers() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(offerDAO.getUserOffer(clientUser)).thenReturn(offers);
		when(request.getRequestDispatcher("/WEB-INF/crud/offerCRUD.jsp")).thenReturn(dispatcher);
		
		offerCRUDServlet.doGet(request, response);
		
		verify(offerDAO).getUserOffer(clientUser);
		verify(request).setAttribute("offers", offers);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_AdminUser_ShowsAllOffers() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(offerDAO.getAllOffer()).thenReturn(offers);
		when(request.getRequestDispatcher("/WEB-INF/crud/offerCRUD.jsp")).thenReturn(dispatcher);
		
		offerCRUDServlet.doGet(request, response);
		
		verify(offerDAO).getAllOffer();
		verify(request).setAttribute("offers", offers);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_DeleteAction_DeletesOffer() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(request.getParameter("action")).thenReturn("delete");
		when(request.getParameter("index")).thenReturn("0");
		when(offerDAO.getUserOffer(clientUser)).thenReturn(offers);
		when(request.getRequestDispatcher("/WEB-INF/crud/offerCRUD.jsp")).thenReturn(dispatcher);
		
		// Populate offers list first
		offerCRUDServlet.doGet(request, response);
		
		offerCRUDServlet.doPost(request, response);
		
		verify(offerDAO).deleteOffer(testOffer);
	}
	
	@Test
	void testDoPost_EditAction_RedirectsToAddOffer() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(request.getParameter("action")).thenReturn("edit");
		when(request.getParameter("index")).thenReturn("0");
		when(request.getContextPath()).thenReturn("/app");
		when(offerDAO.getUserOffer(clientUser)).thenReturn(offers);
		when(request.getRequestDispatcher("/WEB-INF/crud/offerCRUD.jsp")).thenReturn(dispatcher);
		
		offerCRUDServlet.doGet(request, response);
		offerCRUDServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/addOffer?edit=1");
	}
	
	@Test
	void testDoPost_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		offerCRUDServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=offerCRUD");
	}
	
	@Test
	void testConstructor() {
		OfferCRUD offerCRUD = new OfferCRUD();
		assertNotNull(offerCRUD);
	}
}
