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

import dao.UserDAO;
import model.User;

@WebServlet("/wallet")
public class Wallet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private UserDAO userDAO;
       
    public Wallet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login?redirect=wallet");
			return;
		}
		
		RequestDispatcher vue = getServletContext().getRequestDispatcher("/WEB-INF/account/wallet.jsp");
		vue.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if (user == null) {
			return;
		}
		
		String am = request.getParameter("amount");
		
		if (am != null && !am.trim().isEmpty()) {
			try {
				double amount = Double.parseDouble(am);
				userDAO.credit(user.getMailAddress(), amount);
				session.setAttribute("user", userDAO.getUser(user.getMailAddress()));
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		
		doGet(request, response);
	}
}
