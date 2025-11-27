package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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

import dao.UserDAO;
import model.User;

class WalletTest {

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
	
	@Mock
	private ServletContext servletContext;
	
	@InjectMocks
	private Wallet walletServlet;
	
	private User testUser;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 100.0);
		
		when(request.getSession()).thenReturn(session);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher(anyString())).thenReturn(dispatcher);
	}
	
	@Test
	void testDoGet_NoUserInSession_RedirectsToLogin() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getContextPath()).thenReturn("/app");
		
		walletServlet.doGet(request, response);
		
		verify(response).sendRedirect("/app/login?redirect=wallet");
	}
	
	@Test
	void testDoGet_UserInSession_ShowsWallet() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doGet(request, response);
		
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_ValidAmount_CreditsAccount() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("50.0");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO).credit("test@test.com", 50.0);
		verify(session).setAttribute("user", testUser);
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_InvalidAmount_HandlesException() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("invalid");
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO, never()).credit(anyString(), anyDouble());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void testDoPost_NegativeAmount_CreditsNegative() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("-25.0");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO).credit("test@test.com", -25.0);
	}
	
	@Test
	void testDoPost_ZeroAmount_Credits() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("0.0");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO).credit("test@test.com", 0.0);
	}
	
	@Test
	void testConstructor() {
		Wallet wallet = new Wallet();
		assertNotNull(wallet);
	}
}
