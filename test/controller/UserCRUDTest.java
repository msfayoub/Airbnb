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
import model.User;

class UserCRUDTest {

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
	private UserCRUD userCRUDServlet;
	
	private User adminUser;
	private User clientUser;
	private List<User> usersList;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		adminUser = new User("admin@test.com", "hash", "Admin", "User", "1111111111", "Admin", 0.0);
		clientUser = new User("client@test.com", "hash", "Client", "User", "2222222222", "Client", 100.0);
		usersList = Arrays.asList(adminUser, clientUser);
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		userCRUDServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=userCRUD");
	}
	
	@Test
	void testDoGet_NonAdminUser_RedirectsToHome() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(clientUser);
		when(request.getContextPath()).thenReturn("/app");
		
		userCRUDServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/home");
	}
	
	@Test
	void testDoGet_AdminUser_ShowsUserList() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(userDAO.getAllUser()).thenReturn(usersList);
		when(request.getRequestDispatcher("/WEB-INF/crud/userCRUD.jsp")).thenReturn(dispatcher);
		
		userCRUDServlet.doGet(request, response);
		
		verify(userDAO).getAllUser();
		verify(request).setAttribute("users", usersList);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		userCRUDServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=userCRUD");
	}
	
	@Test
	void testDoPost_DeleteAction_DeletesUser() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(request.getParameter("action")).thenReturn("delete");
		when(request.getParameter("index")).thenReturn("1");
		when(userDAO.getAllUser()).thenReturn(usersList);
		when(request.getRequestDispatcher("/WEB-INF/crud/userCRUD.jsp")).thenReturn(dispatcher);
		
		// Simulate initial GET to populate users list
		userCRUDServlet.doGet(request, response);
		
		userCRUDServlet.doPost(request, response);
		
		verify(userDAO).deleteUser(clientUser.getMailAddress());
	}
	
	@Test
	void testDoPost_EditAction_RedirectsToRegister() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(adminUser);
		when(request.getParameter("action")).thenReturn("edit");
		when(request.getParameter("index")).thenReturn("0");
		when(request.getContextPath()).thenReturn("/app");
		when(userDAO.getAllUser()).thenReturn(usersList);
		when(request.getRequestDispatcher("/WEB-INF/crud/userCRUD.jsp")).thenReturn(dispatcher);
		
		// Simulate initial GET to populate users list
		userCRUDServlet.doGet(request, response);
		
		userCRUDServlet.doPost(request, response);
		
		verify(response).sendRedirect("/app/register?edit=" + adminUser.getMailAddress());
	}
	
	@Test
	void testConstructor() {
		UserCRUD userCRUD = new UserCRUD();
		assertNotNull(userCRUD);
	}
}
