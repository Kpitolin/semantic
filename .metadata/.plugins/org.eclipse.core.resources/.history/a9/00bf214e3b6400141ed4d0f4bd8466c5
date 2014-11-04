package controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import analysis.AnalyseResults;
import service.Service;

class SearchAction extends Action {

	public SearchAction() {
	}
	@Override
	public void execute(HttpServletRequest request) {
		Service s = new Service();
		System.out.println("booom execute");
		
		String requete = request.getParameter("q");
		String label = request.getParameter("label");
		if(label.isEmpty()){
			label = null;
		}
		AnalyseResults results =new AnalyseResults();
		
		try {
			results = s.launchSearch(requete,label);
		} catch (IOException e) {
			System.out.println("Erreur au lancement de la méthode launchSearch");
		}
		
		request.setAttribute("pages", results.pagesResults);
	}

}
