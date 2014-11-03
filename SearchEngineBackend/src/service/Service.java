package service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import compareGraph.compareRDF;
import createGraph.CreationGraphe;
import analysis.ExplorationMatrice;
import annotation.DBpediaSpotlightClient;
import annotation.JsonParser;
import annotation.SearchData;
import search.GoogleSearch;

public class Service {
	public void launchSearch() {
		
	}
	public static ArrayList<SearchData> annotateAndCreateGraph(String inputSearchResults, DBpediaSpotlightClient c,CreationGraphe creator ) throws Exception {
		JsonParser jsonParser = new JsonParser();
		jsonParser.parseJson(inputSearchResults);
		jsonParser.displaySearchResults(jsonParser.searchResults);
		
		for (int i = 0; i < jsonParser.searchResults.searchDatas.size(); i++) {
			try {
				String txt = jsonParser.searchResults.searchDatas.get(i).description
						+ "\n -";
				String inputFile = c.writeInFile(txt);
				File input = new File(inputFile);
				File output = new File("output" + i + ".txt");
				
				c.evaluate(input, output);

				// String url = jsonParser.searchResults.searchData.
				creator.createGraph("output" + i + ".txt",jsonParser.searchResults.searchDatas.get(i).url);
				
				c.deleteFile(inputFile);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jsonParser.searchResults.searchDatas;
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
		explorer.exploreSimiliratyFromCSV("extendedGraph\\matriceSimilarite.csv", 0.4);
	}
}
