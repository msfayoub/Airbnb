package controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HomeTest {

    private Home homeServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    public void setUp() {
        homeServlet = new Home();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void testDoGet() throws Exception {
        when(mockRequest.getRequestDispatcher("/WEB-INF/home.jsp")).thenReturn(mockDispatcher);

        homeServlet.doGet(mockRequest, mockResponse);

        verify(mockRequest).getRequestDispatcher("/WEB-INF/home.jsp");
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPost() throws Exception {
        when(mockRequest.getRequestDispatcher("/WEB-INF/home.jsp")).thenReturn(mockDispatcher);

        homeServlet.doPost(mockRequest, mockResponse);

        verify(mockRequest).getRequestDispatcher("/WEB-INF/home.jsp");
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }
}
