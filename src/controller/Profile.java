package controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.OfferDAO;
import dao.UserDAO;
import function.Hash;
import model.User;


@WebServlet("/profile")
public class Profile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private UserDAO userDAO;
	
	@EJB
	private OfferDAO offerDAO;
	
       
    public Profile() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		User extUser = (User) userDAO.getUser(request.getParameter("mailAddress"));
		
		RequestDispatcher vue;
		if (user == null && extUser == null) {
			request.setAttribute("alertType", "alert-warning");
			request.setAttribute("alertMessage", "Vous devez d'abord �tre connect� avant de pouvoir acc�der � votre profil !");
			vue = getServletContext().getRequestDispatcher("/WEB-INF/login.jsp");
			vue.forward(request, response);
		} else if(user != null && extUser == null) {
			vue = getServletContext().getRequestDispatcher("/WEB-INF/profile/profile.jsp");
			vue.forward(request, response);
		} else {
			request.setAttribute("extUser", extUser);
			request.setAttribute("extUserOffers", offerDAO.getUserOffer(extUser));
			vue = getServletContext().getRequestDispatcher("/WEB-INF/profile/externalProfile.jsp");
			vue.forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		try {
			final HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			
			if (user == null) {
				return;
			}
			
			String mail = user.getMailAddress();
			String type = request.getParameter("type");
			
			if (type == null) {
				doGet(request, response);
				return;
			}
			
			switch(type) {
				
			case "info":
				String name = request.getParameter("name");
				String firstname = request.getParameter("firstname");
				String phoneNumber = request.getParameter("phone");
				if (name != null && !user.getName().equals(name) && !name.isEmpty()) {
					userDAO.changeName(mail, name);
					request.setAttribute("alertType", "alert-success");
					request.setAttribute("alertMessage", "Le nom a �t� chang� avec succ�s !");
				}
				if (firstname != null && !user.getFirstname().equals(firstname) && !firstname.isEmpty()) {
					userDAO.changeFirstname(mail, firstname);
					request.setAttribute("alertType", "alert-success");
					request.setAttribute("alertMessage", "Le pr�nom a �t� chang� avec succ�s !");
				}
				if (phoneNumber != null && !user.getPhoneNumber().equals(phoneNumber) && !phoneNumber.isEmpty()) {
					userDAO.changePhoneNumber(mail, phoneNumber);
					request.setAttribute("alertType", "alert-success");
					request.setAttribute("alertMessage", "Le num�ro de t�l�phone a �t� chang� avec succ�s !");
				}
				break;
				
			case "password":
				String password = request.getParameter("pass");
				String newPassword = request.getParameter("newPass");
				if (password != null && newPassword != null) {
					String hashedPassword = Hash.sha256(password);
					String hashedNewPassword = Hash.sha256(newPassword);
					if (user.getHashedPassword().equals(hashedPassword)) {
						userDAO.changePassword(mail, hashedNewPassword);
						request.setAttribute("alertType", "alert-success");
						request.setAttribute("alertMessage", "Le mot de passe a �t� chang� avec succ�s !");
					} else {
						request.setAttribute("alertType", "alert-warning");
						request.setAttribute("alertMessage", "Le mot de passe actuel est incorrect !");
					}
				}
				break;
			}
			
			session.setAttribute("user", userDAO.getUser(mail));
			doGet(request, response);
			
		} catch (NullPointerException npe) {
			System.err.println("Missing params");
			npe.printStackTrace();
		}
	}

}
