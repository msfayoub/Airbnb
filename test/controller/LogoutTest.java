package controller;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogoutTest {

    private Logout logoutServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;

    @BeforeEach
    public void setUp() {
        logoutServlet = new Logout();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
    }

    @Test
    public void testDoGet() throws Exception {
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getContextPath()).thenReturn("/app");

        logoutServlet.doGet(mockRequest, mockResponse);

        verify(mockSession).setAttribute("user", null);
        verify(mockResponse).sendRedirect("/app/home");
    }

    @Test
    public void testLogoutClearsUserSession() throws Exception {
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getContextPath()).thenReturn("");

        logoutServlet.doGet(mockRequest, mockResponse);

        verify(mockSession).setAttribute("user", null);
    }
}
