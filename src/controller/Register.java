package controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import function.Hash;
import model.User;

@WebServlet("/register")
public class Register extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	@EJB
	private UserDAO userDAO;
	
	private User editUser;
	
    public Register() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	User user = (User) request.getSession().getAttribute("user");
    	

		if (request.getParameter("edit") != null) {
			String userMail = request.getParameter("edit");
			
			editUser = userDAO.getUser(userMail);

			if (editUser == null) {
				request.setAttribute("alertType", "alert-warning");
				request.setAttribute("alertMessage", "Cette utilisateur n'existe pas !");
				request.getRequestDispatcher("/WEB-INF/account/register.jsp").forward(request, response);
				return;
				
			} else if ( !editUser.getMailAddress().equals(user.getMailAddress()) && !user.getUserType().equals("Admin") ) {
				editUser = null;
				
				request.setAttribute("alertType", "alert-danger");
				request.setAttribute("alertMessage", "Vous n'�tes pas autoris� � modifier cet utilisateur !");
				request.getRequestDispatcher("/WEB-INF/account/register.jsp").forward(request, response);
				return;
			}
			
			request.setAttribute("user", editUser);
			
		} else if (user != null) {
    		response.sendRedirect(request.getContextPath() + "/home");
    		return;	
    	}
		
		request.getRequestDispatcher("/WEB-INF/account/register.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (editUser != null) {
			String firstname = request.getParameter("firstname");
			String name = request.getParameter("name");
			String phoneNumber = request.getParameter("phone");
			
			editUser.update(firstname, name, phoneNumber);
			userDAO.updateUser(editUser);
			
			response.sendRedirect(request.getContextPath() + "/userCRUD");
			return;
		}
		
		try {	
			String mailAddress = request.getParameter("mail");
			String password = request.getParameter("pass");
			String firstname = request.getParameter("firstname");
			String name = request.getParameter("name");
			String phoneNumber = request.getParameter("phone");
			
			// Validate required fields
			if (mailAddress == null || mailAddress.trim().isEmpty()) {
				System.err.println("Missing params: mail address is required");
				return;
			}
			
			if (userDAO.getUser(mailAddress) != null) {
				request.setAttribute("alertType", "alert-warning");
				request.setAttribute("alertMessage", "Cette adresse de courriel est d�j� utilis�e !");
				request.getRequestDispatcher("/WEB-INF/account/register.jsp").forward(request, response);
				
			} else {
				String hpass = Hash.sha256(password == null ? "" : password);
				userDAO.createUser(mailAddress, hpass, 
						firstname == null ? "" : firstname, 
						name == null ? "" : name, 
						phoneNumber == null ? "" : phoneNumber, 
						"Client", 0);
				request.setAttribute("alertType", "alert-success");
				request.setAttribute("alertMessage", "Le compte a �t� cr�� avec succ�s !");
				request.getRequestDispatcher("/WEB-INF/account/login.jsp").forward(request, response);
			}
			
		} catch(NullPointerException npe) {
			System.err.println("Missing params");
			npe.printStackTrace();
		}
	}

}
