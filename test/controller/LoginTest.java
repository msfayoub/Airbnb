package controller;

import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

class LoginTest {
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private RequestDispatcher dispatcher;
    
    @Mock
    private UserDAO userDAO;
    
    private Login loginServlet;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginServlet = new Login();
    }
    
    @Test
    void testSuccessfulLogin() throws Exception {
        User user = new User();
        user.setMailAddress("test@test.com");
        user.setHashedPassword("hashedPassword");
        
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getSession()).thenReturn(session);
        
        verify(session, never()).setAttribute(eq("user"), any(User.class));
    }
}