package controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.OfferDAO;
import model.Offer;

public class HomeTest {

    private Home homeServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockDispatcher;
    private ServletContext mockServletContext;
    private ServletConfig mockServletConfig;
    private OfferDAO mockOfferDAO;

    @BeforeEach
    public void setUp() throws Exception {
        homeServlet = new Home();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockDispatcher = mock(RequestDispatcher.class);
        mockServletContext = mock(ServletContext.class);
        mockServletConfig = mock(ServletConfig.class);
        mockOfferDAO = mock(OfferDAO.class);
        
        // Initialize servlet with mock config
        when(mockServletConfig.getServletContext()).thenReturn(mockServletContext);
        homeServlet.init(mockServletConfig);
        
        // Inject mock OfferDAO using reflection
        Field offerDAOField = Home.class.getDeclaredField("offerDAO");
        offerDAOField.setAccessible(true);
        offerDAOField.set(homeServlet, mockOfferDAO);
    }

    @Test
    public void testDoGet() throws Exception {
        List<Offer> mockOffers = new ArrayList<>();
        when(mockOfferDAO.getLastOffer()).thenReturn(mockOffers);
        when(mockServletContext.getRequestDispatcher("/WEB-INF/home.jsp")).thenReturn(mockDispatcher);

        homeServlet.doGet(mockRequest, mockResponse);

        verify(mockOfferDAO).getLastOffer();
        verify(mockRequest).setAttribute("offers", mockOffers);
        verify(mockRequest).setAttribute("searchType", "default");
        verify(mockServletContext).getRequestDispatcher("/WEB-INF/home.jsp");
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }
}
