package controller;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AccommodationDAO;
import model.Accommodation;
import model.User;

/**
 * Servlet implementation class Accommodation
 */
@WebServlet("/accommodationCRUD")
public class AccommodationCRUD extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	private AccommodationDAO accommodationDAO;
	
    public AccommodationCRUD() {
        super();
    }
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login?redirect=accommodationCRUD");
			return;
		}
		
		List<Accommodation> accommodations;
		if (user.getUserType().equals("Admin")) {
			accommodations = accommodationDAO.getAllAccommodation();
		} else {
			accommodations = accommodationDAO.getUserAccommodation(user);
		}
		
		request.setAttribute("accommodations", accommodations);
		request.getRequestDispatcher("/WEB-INF/crud/accommodationCRUD.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login?redirect=accommodationCRUD");
			return;
		}
		
		List<Accommodation> accommodations;
		if (user.getUserType().equals("Admin")) {
			accommodations = accommodationDAO.getAllAccommodation();
		} else {
			accommodations = accommodationDAO.getUserAccommodation(user);
		}
		
		String index = request.getParameter("index");
		Accommodation accommodation = accommodations.get(Integer.valueOf(index));
		
		switch (request.getParameter("action")) {
			case "delete":
				accommodationDAO.deleteAccommodation(accommodation);
				break;
				
			case "edit":
				response.sendRedirect(request.getContextPath() + "/addAccommodation?edit=" + accommodation.getId()); 
				return;
				
			default:
				break;
		}
		
		doGet(request, response);
	}

}
