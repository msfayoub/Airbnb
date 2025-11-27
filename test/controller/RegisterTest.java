package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

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

import dao.UserDAO;
import function.Hash;
import model.User;

class RegisterTest {

	@Mock
	private UserDAO userDAO;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private HttpSession session;
	
	@Mock
	private RequestDispatcher dispatcher;
	
	@InjectMocks
	private Register registerServlet;
	
	private User testUser;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", Hash.sha256("password"), "John", "Doe", "0123456789", "Client", 100.0);
	}
	
	@Test
	void testDoGet_NoUserInSession_ShowsRegisterPage() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getParameter("edit")).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/account/register.jsp")).thenReturn(dispatcher);
		
		registerServlet.doGet(request, response);
		
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_UserInSession_RedirectsToHome() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		registerServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/home");
	}
	
	@Test
	void testDoGet_EditUserNotFound_ShowsWarning() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("nonexistent@test.com");
		when(userDAO.getUser("nonexistent@test.com")).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/account/register.jsp")).thenReturn(dispatcher);
		
		registerServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_EditUserNotAuthorized_ShowsError() throws ServletException, IOException {
		User otherUser = new User("other@test.com", "hash", "Jane", "Smith", "9876543210", "Client", 50.0);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("other@test.com");
		when(userDAO.getUser("other@test.com")).thenReturn(otherUser);
		when(request.getRequestDispatcher("/WEB-INF/account/register.jsp")).thenReturn(dispatcher);
		
		registerServlet.doGet(request, response);
		
		verify(request).setAttribute("alertType", "alert-danger");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoGet_AdminCanEditAnyUser() throws ServletException, IOException {
		User admin = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		User otherUser = new User("other@test.com", "hash", "Jane", "Smith", "9876543210", "Client", 50.0);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(admin);
		when(request.getParameter("edit")).thenReturn("other@test.com");
		when(userDAO.getUser("other@test.com")).thenReturn(otherUser);
		when(request.getRequestDispatcher("/WEB-INF/account/register.jsp")).thenReturn(dispatcher);
		
		registerServlet.doGet(request, response);
		
		verify(request).setAttribute("user", otherUser);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_NewUser_CreatesSuccessfully() throws ServletException, IOException {
		when(request.getParameter("mail")).thenReturn("newuser@test.com");
		when(request.getParameter("pass")).thenReturn("password123");
		when(request.getParameter("firstname")).thenReturn("New");
		when(request.getParameter("name")).thenReturn("User");
		when(request.getParameter("phone")).thenReturn("5555555555");
		when(userDAO.getUser("newuser@test.com")).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/account/login.jsp")).thenReturn(dispatcher);
		
		registerServlet.doPost(request, response);
		
		verify(userDAO).createUser(eq("newuser@test.com"), anyString(), eq("New"), eq("User"), eq("5555555555"), eq("Client"), eq(0.0));
		verify(request).setAttribute("alertType", "alert-success");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_UserAlreadyExists_ShowsWarning() throws ServletException, IOException {
		when(request.getParameter("mail")).thenReturn("test@test.com");
		when(request.getParameter("pass")).thenReturn("password");
		when(request.getParameter("firstname")).thenReturn("John");
		when(request.getParameter("name")).thenReturn("Doe");
		when(request.getParameter("phone")).thenReturn("0123456789");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(request.getRequestDispatcher("/WEB-INF/account/register.jsp")).thenReturn(dispatcher);
		
		registerServlet.doPost(request, response);
		
		verify(request).setAttribute("alertType", "alert-warning");
		verify(request).setAttribute(eq("alertMessage"), anyString());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_MissingParameters_HandlesException() throws ServletException, IOException {
		when(request.getParameter("mail")).thenReturn(null);
		
		registerServlet.doPost(request, response);
		
		verify(userDAO, never()).createUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyDouble());
	}
	
	@Test
	void testConstructor() {
		Register register = new Register();
		assertNotNull(register);
	}
}
