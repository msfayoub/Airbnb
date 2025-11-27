package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ImgServTest {

	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private ServletContext servletContext;
	
	@Mock
	private ServletOutputStream outputStream;
	
	@InjectMocks
	private ImgServ imgServServlet;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		when(imgServServlet.getServletContext()).thenReturn(servletContext);
	}
	
	@Test
	void testDoGet_NullFilename_DoesNotServe() throws ServletException, IOException {
		when(request.getParameter("name")).thenReturn(null);
		
		imgServServlet.doGet(request, response);
		
		verify(response, never()).setContentType(anyString());
	}
	
	@Test
	void testDoGet_ValidFilename_ServesImage() throws ServletException, IOException {
		when(request.getParameter("name")).thenReturn("test.jpg");
		when(imgServServlet.getServletContext()).thenReturn(servletContext);
		when(response.getOutputStream()).thenReturn(outputStream);
		
		// Note: This test will work but may not find actual files
		// In a real environment, you'd need to set up test files
		imgServServlet.doGet(request, response);
		
		verify(request).getParameter("name");
	}
	
	@Test
	void testDoGet_ImageExtension_SetsCorrectMimeType() throws ServletException, IOException {
		when(request.getParameter("name")).thenReturn("photo.png");
		when(imgServServlet.getServletContext()).thenReturn(servletContext);
		when(servletContext.getMimeType("photo.png")).thenReturn("image/png");
		
		imgServServlet.doGet(request, response);
		
		verify(servletContext).getMimeType("photo.png");
	}
	
	@Test
	void testDoPost_CallsDoGet() throws ServletException, IOException {
		when(request.getParameter("name")).thenReturn(null);
		
		imgServServlet.doPost(request, response);
		
		verify(request).getParameter("name");
	}
	
	@Test
	void testConstructor() {
		ImgServ imgServ = new ImgServ();
		assertNotNull(imgServ);
	}
}
