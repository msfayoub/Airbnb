package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

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

class ProfileTest {

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
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getParameter("mailAddress")).thenReturn(null);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/login.jsp")).thenReturn(dispatcher);
		
		profileServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_UserInSession_ShowsProfile() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("mailAddress")).thenReturn(null);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doGet(request, response);
		
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_ExternalUser_ShowsExternalProfile() throws ServletException, IOException {
		User externalUser = new User("external@test.com", "hash", "External", "User", "9876543210", "Client", 50.0);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("mailAddress")).thenReturn("external@test.com");
		when(userDAO.getUser("external@test.com")).thenReturn(externalUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/externalProfile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doGet(request, response);
		
		verify(request).setAttribute("extUser", externalUser);
		verify(offerDAO).getUserOffer(externalUser);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_UpdateInfo_ChangesName() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("info");
		when(request.getParameter("name")).thenReturn("NewName");
		when(request.getParameter("firstname")).thenReturn("John");
		when(request.getParameter("phone")).thenReturn("0123456789");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO).changeName("test@test.com", "NewName");
		verify(request).setAttribute("alertType", "alert-success");
		verify(request).setAttribute(eq("alertMessage"), anyString());
	}
	
	@Test
	void testDoPost_UpdateInfo_ChangesFirstname() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("info");
		when(request.getParameter("name")).thenReturn("Doe");
		when(request.getParameter("firstname")).thenReturn("NewFirstname");
		when(request.getParameter("phone")).thenReturn("0123456789");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO).changeFirstname("test@test.com", "NewFirstname");
		verify(request).setAttribute("alertType", "alert-success");
	}
	
	@Test
	void testDoPost_UpdateInfo_ChangesPhone() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("info");
		when(request.getParameter("name")).thenReturn("Doe");
		when(request.getParameter("firstname")).thenReturn("John");
		when(request.getParameter("phone")).thenReturn("9999999999");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO).changePhoneNumber("test@test.com", "9999999999");
		verify(request).setAttribute("alertType", "alert-success");
	}
	
	@Test
	void testDoPost_ChangePassword_Successful() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("password");
		when(request.getParameter("pass")).thenReturn("password");
		when(request.getParameter("newPass")).thenReturn("newpassword");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO).changePassword(eq("test@test.com"), anyString());
		verify(request).setAttribute("alertType", "alert-success");
	}
	
	@Test
	void testDoPost_ChangePassword_IncorrectCurrentPassword() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("type")).thenReturn("password");
		when(request.getParameter("pass")).thenReturn("wrongpassword");
		when(request.getParameter("newPass")).thenReturn("newpassword");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(profileServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/profile/profile.jsp")).thenReturn(dispatcher);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO, never()).changePassword(anyString(), anyString());
		verify(request).setAttribute("alertType", "alert-warning");
	}
	
	@Test
	void testDoPost_MissingParameters_HandlesException() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		
		profileServlet.doPost(request, response);
		
		verify(userDAO, never()).changeName(anyString(), anyString());
	}
	
	@Test
	void testConstructor() {
		Profile profile = new Profile();
		assertNotNull(profile);
	}
}
