package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
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
import dao.UserDAO;
import function.Hash;
import model.User;

/**
 * Additional edge case tests for Profile controller
 */
class ProfileEdgeCaseTest {

	@Mock
	private UserDAO userDAO;
	
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
	
	@Mock
	private ServletContext servletContext;
	
	@Mock
	private ServletConfig servletConfig;
	
	@InjectMocks
	private Profile profileServlet;
	
	private User testUser;
	
	@BeforeEach
	void setUp() throws ServletException {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", Hash.sha256("password"), "John", "Doe", "0123456789", "Client", 100.0);
		
		when(request.getSession()).thenReturn(session);
		when(servletConfig.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		profileServlet.init(servletConfig);
	}
	
	@Test
	void testDoPost_UpdateAllInfoFields() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("info");
		when(request.getParameter("name")).thenReturn("NewName");
		when(request.getParameter("firstname")).thenReturn("NewFirstname");
		when(request.getParameter("phone")).thenReturn("9999999999");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO).changeName("test@test.com", "NewName");
		verify(userDAO).changeFirstname("test@test.com", "NewFirstname");
		verify(userDAO).changePhoneNumber("test@test.com", "9999999999");
	}
	
	@Test
	void testDoPost_NoChanges_DoesNotUpdate() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("info");
		when(request.getParameter("name")).thenReturn("Doe");
		when(request.getParameter("firstname")).thenReturn("John");
		when(request.getParameter("phone")).thenReturn("0123456789");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO, never()).changeName(anyString(), anyString());
		verify(userDAO, never()).changeFirstname(anyString(), anyString());
		verify(userDAO, never()).changePhoneNumber(anyString(), anyString());
	}
	
	@Test
	void testDoPost_UnknownType_DoesNothing() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("unknown");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO, never()).changeName(anyString(), anyString());
		verify(userDAO, never()).changePassword(anyString(), anyString());
	}
	
	@Test
	void testDoGet_ExternalUserNotFound_HandlesGracefully() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("mailAddress")).thenReturn("nonexistent@test.com");
		when(userDAO.getUser("nonexistent@test.com")).thenReturn(null);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/externalProfile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doGet(request, response);
		
		verify(request).setAttribute("extUser", null);
		verify(dispatcher).forward(request, response);
	}
}
