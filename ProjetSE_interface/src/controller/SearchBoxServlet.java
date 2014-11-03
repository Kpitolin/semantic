package controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import service.Service;


public class SearchBoxServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	          throws ServletException, IOException {
		
//		String commandStr = request.getParameter("action");
//		if(!commandStr.isEmpty()){
//			Action action = null;
//			if(commandStr.equals("SearchEngine")){
//				action = new SearchEngineAction();
//			}
//			action.execute(request);
//		}
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
		  
		    Service s = new Service();
		    
		    String requete = request.getParameter("requete");
		    
		    s.semanticSearch(requete);
		    JSONObject jsonObj = s.exploreSimiliratyFromCSV("C:\\Users\\Thomas\\Documents\\4IF\\semantic\\ProjetSE\\matrice_jaccard.csv", 0.6);
		    
			response.setContentType("application/json");
			response.getWriter().write(jsonObj.toJSONString());
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
	    try {
	    } catch (Exception e) {
	      throw new ServletException(e.getMessage());
	    }
	  }
	  
	  public void destroy() {
	    super.destroy();
	    try {
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

}
