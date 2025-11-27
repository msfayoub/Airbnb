package controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.AccommodationDAO;
import dao.AddressDAO;
import dao.HouseRulesDAO;
import dao.PictureDAO;
import model.Accommodation;
import model.Address;
import model.HouseRules;
import model.Picture;
import model.User;


@WebServlet("/addAccommodation")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class AddAccommodation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private AccommodationDAO accommodationDAO;
	
	@EJB
	private AddressDAO addressDAO;
	
	@EJB
	private HouseRulesDAO houseRulesDAO;
	
	@EJB
	private PictureDAO pictureDAO;
	
	private Accommodation editAccommodation;
	private Accommodation tempAccommodation;
       
    private static final String IMAGES_FOLDER = "/upload_img";
    private String uploadPath;
    
    public AddAccommodation() {
        super();
    }

    /*
     * Si le dossier de sauvegarde de l'image n'existe pas, on demande sa cr�ation.
     */ 
    @Override
    public void init() throws ServletException {
        uploadPath = getServletContext().getRealPath(IMAGES_FOLDER);
        File uploadDir = new File( uploadPath );
        if ( ! uploadDir.exists() ) { uploadDir.mkdir(); }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		editAccommodation = null;
		tempAccommodation = null;
		
		User user = (User) request.getSession().getAttribute("user");
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login?redirect=addAccommodation");
			return;
		}
		
		if (request.getParameter("edit") != null) {
			String accommodationId = request.getParameter("edit");
			
			editAccommodation = accommodationDAO.getAccommodation(Integer.parseInt(accommodationId));

			if (editAccommodation == null) {
				request.setAttribute("alertType", "alert-warning");
				request.setAttribute("alertMessage", "Ce logement n'existe pas !");
				request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp").forward(request, response);
				return;
				
			} else if ( !editAccommodation.getUser().getMailAddress().equals(user.getMailAddress()) && !user.getUserType().equals("Admin") ) {
				editAccommodation = null;
				
				request.setAttribute("alertType", "alert-danger");
				request.setAttribute("alertMessage", "Vous n'�tes pas autoris� � modifier ce logement !");
				request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp").forward(request, response);
				return;
			}
			
			request.setAttribute("accommodation", editAccommodation);
		}
		
		request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = (User)request.getSession().getAttribute("user");
			
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/login?redirect=addAccommodation");
				return;
			}
			
			String form = request.getParameter("form");
			
			if ( "picture".equals(form) && tempAccommodation != null && request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data") ) {
				Collection<Part> parts = request.getParts();
				Collection<String> filesName = new ArrayList<>();
				
				// Check parts size
				if (parts.size() == 0) {
					request.setAttribute("alertType", "alert-warning");
					request.setAttribute("alertMessage", "Aucune photo s�lection�e !");
					request.setAttribute("pictureForm", true);
					request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp").forward(request, response);
					return;
				}
				
				for (Part part : parts) {
					
					// Check part size
					if (getFileName(part).length() == 0) {
						
						// Send a notification warning to the user
						request.setAttribute("alertType", "alert-warning");
						request.setAttribute("alertMessage", "Aucune photo s�lection�e ou il y a une erreur avec l'une des photos !");
						request.setAttribute("pictureForm", true);
						request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp").forward(request, response);
						return;
					}
					
					String extension = getFileName(part).substring(getFileName(part).lastIndexOf('.'));
					
					// Check if the extension if valid
					if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")) {
						
						// Create unique file name with current time and random string
						String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID() + extension;
						
						// Get file full path according to IMAGES_FOLDER
						String fullPath = uploadPath + File.separator + fileName;
				        
				        // Save files in server
				        part.write(fullPath);
				        
				        // Add file name in Collection in order to persist them later
				        filesName.add(fileName);
				        
					} else {
						// Send a notification warning to the user
						request.setAttribute("alertType", "alert-warning");
						request.setAttribute("alertMessage", "L'extension des photos doit �tre en .png, .jpg ou .jpeg !");
						request.setAttribute("pictureForm", true);
						request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp").forward(request, response);
						return;
					}
			    }
				
				
				for (String fileName : filesName) {
			        // Persist file name
			        // pictureDAO.createPicture(tempAccommodation, fileName);
					tempAccommodation.addPicture(new Picture(fileName));
				}
				
				// Persist temporary accommodation in order to persist pictures
				accommodationDAO.createAccommodation(tempAccommodation);
				
				// Delete temporary accommodation
				tempAccommodation = null;
				
				response.sendRedirect(request.getContextPath() + "/addRooms");
				return;
				
			} else if ("info".equals(form) || (request.getContentType() != null && !request.getContentType().toLowerCase().contains("multipart/form-data"))) {
				
				String name = request.getParameter("name");
				String type = request.getParameter("type");
				String capacity = request.getParameter("capacity");
				String numberOfRooms = request.getParameter("numberOfRooms");
				String description = request.getParameter("description");
				
				String streetAndNumber = request.getParameter("address");
				String addressComplement = request.getParameter("addressComplement");
				String city = request.getParameter("city");
				String postalCode = request.getParameter("zipCode");
				String country = request.getParameter("country");
				
			String arrivalHourStr = request.getParameter("arrivalHour");
			String departureHourStr = request.getParameter("departureHour");
			String petAllowed = request.getParameter("petAllowed");
			String partyAllowed = request.getParameter("partyAllowed");
			String smokeAllowed = request.getParameter("smokeAllowed");
			
			// Add null checks for time parameters
			if (arrivalHourStr == null || departureHourStr == null) {
				throw new NullPointerException("Missing time parameters");
			}
			
			LocalTime arrivalHour = LocalTime.parse(arrivalHourStr);
			LocalTime departureHour = LocalTime.parse(departureHourStr);				if (editAccommodation != null) {
					Accommodation newAccommodation = accommodationDAO.getAccommodation(editAccommodation.getId());
					newAccommodation.setPictures(null);
					
					newAccommodation.update(name, type, Integer.parseInt(capacity), Integer.parseInt(numberOfRooms), description);
					newAccommodation.updateAddress(streetAndNumber, addressComplement, postalCode, city, country);
					newAccommodation.updateHouseRules(arrivalHour, departureHour, getBoolFromCheckbox(petAllowed), getBoolFromCheckbox(partyAllowed), getBoolFromCheckbox(smokeAllowed));
					accommodationDAO.updateAccommodation(newAccommodation);
					
					editAccommodation = null;
					
					response.sendRedirect(request.getContextPath() + "/accommodationCRUD");
					return;
				}
		
				// Create a temporary accommodation
				this.tempAccommodation = new Accommodation(user, name, new Address(streetAndNumber, addressComplement, postalCode, city, country),
						new HouseRules(arrivalHour, departureHour, getBoolFromCheckbox(petAllowed), getBoolFromCheckbox(partyAllowed), getBoolFromCheckbox(smokeAllowed)), 
						type, Integer.parseInt(capacity), Integer.parseInt(numberOfRooms), description);
				
				// Get the information enter in the first page, and redirect at picture upload form
				request.setAttribute("pictureForm", true);	
			}
			
			request.getRequestDispatcher("/WEB-INF/accommodation/addAccommodation.jsp").forward(request, response);
			return;
			
		} catch(NullPointerException npe) {
			npe.printStackTrace();
			System.err.println("Missing params: " + npe.getMessage());
			response.sendRedirect(request.getContextPath() + "/addAccommodation");
		}
	}
	
	protected void treaInformationsForm(HttpServletRequest request, HttpServletResponse response) {

	}
	
	protected void treatPicturesForm(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	protected void treatEdition(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	private boolean getBoolFromCheckbox(String checkboxValue) {
		if (checkboxValue == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/*
    * R�cup�ration du nom du fichier dans la requ�te.
    */
    private String getFileName( Part part ) {
        for ( String content : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( content.trim().startsWith( "filename" ) ) {
            	return content.substring( content.indexOf( "=" ) + 2, content.length() - 1 );
            } 
        }
        
        return "Default.file";
    }
}
