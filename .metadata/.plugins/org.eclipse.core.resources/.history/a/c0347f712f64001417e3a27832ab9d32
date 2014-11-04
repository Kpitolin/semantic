package service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import compareGraph.compareRDF;
import createGraph.CreationGraphe;
import analysis.AnalyseResults;
import analysis.ExplorationMatrice;
import annotation.DBpediaSpotlightClient;
import annotation.JsonParser;
import annotation.SearchData;
import search.GoogleSearch;

public class Service {
	public final static String macSeparator = "/";
	public final static String windowsSeparator = "\\"; 
	public final static String separator = windowsSeparator;
	private final static double SEUILJACCARD = 0.6;
	
	public AnalyseResults launchSearch(String query,String label) throws IOException {
		GoogleSearch search = new GoogleSearch();
		String jsonGenere = search.search(query, label);
		
		DBpediaSpotlightClient annotation = new DBpediaSpotlightClient();
		CreationGraphe creator = new CreationGraphe();
		
		ArrayList<SearchData> searchDatas = new ArrayList<SearchData>();
		
		try {
			searchDatas = annotateAndCreateGraph(jsonGenere,annotation,creator);
		} catch (Exception e) {
			System.out.println("probleme d'annotation ou de creation des graphes rdf");
		}
		
		compareRDF compare = new compareRDF();
		compare.creerMatriceSimilarite("."+separator+"extendedGraph");
		
		ExplorationMatrice explorer = new ExplorationMatrice();
		JSONObject jsonObj = explorer.exploreSimiliratyFromCSV("extendedGraph"+separator+"matriceSimilarite.csv", SEUILJACCARD);
		
		AnalyseResults results = new AnalyseResults(searchDatas,jsonObj);
		
		return results;
	}
	
	public static ArrayList<SearchData> annotateAndCreateGraph(String inputSearchResults, DBpediaSpotlightClient c,CreationGraphe creator ) throws Exception {
		JsonParser jsonParser = new JsonParser();
		jsonParser.parseJsonDBpediaFile(inputSearchResults);
		jsonParser.displaySearchResults(jsonParser.searchResults);
		
		for (int i = 0; i < jsonParser.searchResults.searchDatas.size(); i++) {
			try {
				String txt = jsonParser.searchResults.searchDatas.get(i).description
						+ "\n -";
				String inputFile = c.writeInFile(txt);
				File input = new File(inputFile);
				File output = new File("."+separator+"output" + i + ".txt");
				
				c.evaluate(input, output);

				creator.createGraph("."+separator+"output" + i + ".txt",jsonParser.searchResults.searchDatas.get(i).url);
				
				c.deleteFile(inputFile);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jsonParser.searchResults.searchDatas;
	}
	
	public String exportJSON(){
		ExplorationMatrice explorer = new ExplorationMatrice();
		JSONObject jsonObj = null;
		try {
			jsonObj = explorer.exploreSimiliratyFromCSV("extendedGraph"+separator+"matriceSimilarite.csv", SEUILJACCARD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj.toJSONString();
	}
	
	public static void main (String args[]) throws IOException{
		GoogleSearch search = new GoogleSearch();
		String jsonGenere = search.search("obama", null);
		
		DBpediaSpotlightClient annotation = new DBpediaSpotlightClient();
		CreationGraphe creator = new CreationGraphe();
		
		try {
			annotateAndCreateGraph(jsonGenere,annotation,creator);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		compareRDF compare = new compareRDF();
		compare.creerMatriceSimilarite("extendedGraph");
		
		ExplorationMatrice explorer = new ExplorationMatrice();
		explorer.exploreSimiliratyFromCSV("extendedGraph"+separator+"matriceSimilarite.csv", 0.4);
	}
}
