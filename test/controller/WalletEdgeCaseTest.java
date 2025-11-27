package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

import dao.UserDAO;
import model.User;

/**
 * Additional edge case tests for Wallet controller
 */
class WalletEdgeCaseTest {

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
	
	@Mock
	private ServletConfig servletConfig;
	
	@InjectMocks
	private Wallet walletServlet;
	
	private User testUser;
	
	@BeforeEach
	void setUp() throws ServletException {
		MockitoAnnotations.openMocks(this);
		testUser = new User("test@test.com", "hash", "John", "Doe", "0123456789", "Client", 100.0);
		
		when(request.getSession()).thenReturn(session);
		when(servletConfig.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		walletServlet.init(servletConfig);
	}
	
	@Test
	void testDoPost_VeryLargeAmount_Credits() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("999999.99");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO).credit("test@test.com", 999999.99);
	}
	
	@Test
	void testDoPost_DecimalAmount_CreditsExactly() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("123.45");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO).credit("test@test.com", 123.45);
	}
	
	@Test
	void testDoPost_NullAmount_HandlesException() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn(null);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO, never()).credit(anyString(), anyDouble());
	}
	
	@Test
	void testDoPost_EmptyAmount_HandlesException() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("");
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		verify(userDAO, never()).credit(anyString(), anyDouble());
	}
	
	@Test
	void testDoPost_AmountWithSpaces_HandlesException() throws ServletException, IOException {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(testUser);
		when(request.getParameter("amount")).thenReturn("  50.0  ");
		when(userDAO.getUser("test@test.com")).thenReturn(testUser);
		when(walletServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRequestDispatcher("/WEB-INF/account/wallet.jsp")).thenReturn(dispatcher);
		
		walletServlet.doPost(request, response);
		
		// Double.parseDouble should handle leading/trailing spaces
		verify(userDAO).credit("test@test.com", 50.0);
	}
}
