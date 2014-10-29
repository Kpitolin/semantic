package servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class SearchBoxServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	          throws ServletException, IOException {
		
	}
	
	 /**
	   * Handles the HTTP
	   * <code>GET</code> method.
	   *
	   * @param request servlet request
	   * @param response servlet response
	   * @throws ServletException if a servlet-specific error occurs
	   * @throws IOException if an I/O error occurs
	   */
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	          throws ServletException, IOException {
	    processRequest(request, response);
	  }

	  /**
	   * Handles the HTTP
	   * <code>POST</code> method.
	   *
	   * @param request servlet request
	   * @param response servlet response
	   * @throws ServletException if a servlet-specific error occurs
	   * @throws IOException if an I/O error occurs
	   */
	  @Override
	  protected void doPost(HttpServletRequest request, HttpServletResponse response)
	          throws ServletException, IOException {
	    processRequest(request, response);
	  }
	  
	  /**
	   * Returns a short description of the servlet.
	   *
	   * @return a String containing servlet description
	   */
	  @Override
	  public String getServletInfo() {
	    return "Short description";
	  }
	  
	  @Override
	  public void init() throws ServletException {
	    //dao = new FileDao();
	    try {
	      //count = dao.getCount();
	    } catch (Exception e) {
	      getServletContext().log("An exception occurred in FileCounter", e);
	      throw new ServletException("An exception occurred in FileCounter"
	          + e.getMessage());
	    }
	  }
	  
	  public void destroy() {
	    super.destroy();
	    try {
	      //dao.save(count);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

}
