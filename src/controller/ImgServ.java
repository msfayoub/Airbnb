package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImgServ
 */
@WebServlet("/imgServ")
public class ImgServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String IMAGES_FOLDER = "/upload_img";
       
    public ImgServ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   // Get the path of the image from request parameter
	   String filename = request.getParameter( "name" );
	   
	   if (filename == null || filename.trim().isEmpty()) {
		   return; 
	   }
	   
	   String fullPath = getServletContext().getRealPath(IMAGES_FOLDER) + File.separator + filename;
	      
	   // Retrieve mimeType dynamically
	   String mime = getServletContext().getMimeType(filename);
	      
	   if (mime == null) {
		   return; 
	   }

	   response.setContentType(mime);
	   File file = new File(fullPath);
	   
	   response.setContentLength((int)file.length());

	   FileInputStream inputStream = new FileInputStream(file);
	   OutputStream outputStream = response.getOutputStream();

	   // Copy the contents of the file to the output stream
	   byte[] buffer = new byte[1024];
	   int count = 0;
	       
	   while ((count = inputStream.read(buffer)) >= 0) {   
		   outputStream.write(buffer, 0, count);
	   }
	       
	   outputStream.close();    
	   inputStream.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
