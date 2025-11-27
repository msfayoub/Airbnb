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

import dao.UserDAO;
import function.Hash;
import model.User;

/**
 * Additional edge case tests for Register controller
 */
class RegisterEdgeCaseTest {

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
	private User adminUser;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", Hash.sha256("password"), "John", "Doe", "0123456789", "Client", 100.0);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
	}
	
	@Test
	void testDoPost_EmptyPassword_CreatesUser() throws ServletException, IOException {
		when(request.getParameter("mail")).thenReturn("newuser@test.com");
		when(request.getParameter("pass")).thenReturn("");
		when(request.getParameter("firstname")).thenReturn("New");
		when(request.getParameter("name")).thenReturn("User");
		when(request.getParameter("phone")).thenReturn("5555555555");
		when(userDAO.getUser("newuser@test.com")).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/account/login.jsp")).thenReturn(dispatcher);
		
		registerServlet.doPost(request, response);
		
		verify(userDAO).createUser(eq("newuser@test.com"), anyString(), eq("New"), eq("User"), eq("5555555555"), eq("Client"), eq(0.0));
	}
	
	@Test
	void testDoPost_SpecialCharactersInName_CreatesUser() throws ServletException, IOException {
		when(request.getParameter("mail")).thenReturn("special@test.com");
		when(request.getParameter("pass")).thenReturn("password");
		when(request.getParameter("firstname")).thenReturn("José");
		when(request.getParameter("name")).thenReturn("O'Brien-Smith");
		when(request.getParameter("phone")).thenReturn("123-456-7890");
		when(userDAO.getUser("special@test.com")).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/account/login.jsp")).thenReturn(dispatcher);
		
		registerServlet.doPost(request, response);
		
		verify(userDAO).createUser(eq("special@test.com"), anyString(), eq("José"), eq("O'Brien-Smith"), eq("123-456-7890"), eq("Client"), eq(0.0));
	}
	
	@Test
	void testDoGet_EditOwnProfile_ShowsEditForm() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("edit")).thenReturn("test@test.com");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(request.getRequestDispatcher("/WEB-INF/account/register.jsp")).thenReturn(dispatcher);
		
		registerServlet.doGet(request, response);
		
		verify(request).setAttribute("user", testUser);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_VeryLongEmail_CreatesUser() throws ServletException, IOException {
		String longEmail = "verylongemailaddress.with.many.dots.and.characters@verylongdomainname.com";
		when(request.getParameter("mail")).thenReturn(longEmail);
		when(request.getParameter("pass")).thenReturn("password");
		when(request.getParameter("firstname")).thenReturn("Long");
		when(request.getParameter("name")).thenReturn("Email");
		when(request.getParameter("phone")).thenReturn("1234567890");
		when(userDAO.getUser(longEmail)).thenReturn(null);
		when(request.getRequestDispatcher("/WEB-INF/account/login.jsp")).thenReturn(dispatcher);
		
		registerServlet.doPost(request, response);
		
		verify(userDAO).createUser(eq(longEmail), anyString(), eq("Long"), eq("Email"), eq("1234567890"), eq("Client"), eq(0.0));
	}
}
