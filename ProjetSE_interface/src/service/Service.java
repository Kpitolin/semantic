package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.CreationGraphe;
import model.DBpediaSpotlightClient;
import model.GoogleSearch;
import model.JsonParser;
import model.compareRDF;

import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.hp.hpl.jena.rdf.model.Model;

import au.com.bytecode.opencsv.CSVReader;

public class Service {
	private final static char SEPARATOR = ';';

	@SuppressWarnings({ "unchecked", "unused" })
	public JSONObject exploreSimiliratyFromCSV(String pathnameCSV,
			double seuilJaccard) throws IOException {

		File file = new File(pathnameCSV);
		FileReader fr = new FileReader(file);
		CSVReader csvReader = new CSVReader(fr, SEPARATOR);

		List<String[]> data = new ArrayList<String[]>();
		String[] nextLine = null;
		String[] firstLine = null;
		// parcours du fichier csv ligne � ligne
		while ((nextLine = csvReader.readNext()) != null) {
			int size = nextLine.length;

			if (size == 0) { // ligne vide
				continue;
			}

			String debut = nextLine[0].trim();

			// recupere en particulier la premiere ligne contenant juste le noms
			// de chaque page
			// et on insere les autres ligne dans la liste data
			if (debut.startsWith("#")) {
				firstLine = nextLine;
			} else {
				if (debut.length() == 0 && size == 1) {
					continue;
				}
				data.add(nextLine);
			}
		}

		JSONObject objJSON = new JSONObject(); // objet JSON final contenant les
												// deux sous objets nodes et
												// links

		JSONArray listNodes = new JSONArray();
		JSONArray listLinks = new JSONArray();

		// traitement des donn�es
		for (String[] oneData : data) {
			int i = 1;
			double indiceJaccard = Double.parseDouble(oneData[i]);

			String noeud1 = oneData[0]; // premiere colonne du csv contenant
										// egalement que le nom de chaque page

			// remplissage de notre liste de noeud
			JSONObject aNode = new JSONObject();
			aNode.putIfAbsent("name", noeud1);
			aNode.putIfAbsent("theme", new Integer(0)/*
													 * ici on met l'element de
													 * la derniere colonne
													 * correspondant au theme
													 */);
			if (!listNodes.contains(aNode)) {
				listNodes.add(aNode);
			}

			// remplissage de la liste de lien en fonction de l'indice de
			// similarit� entre deux noeud
			while (indiceJaccard != 1) {
				String noeud2 = firstLine[i];

				if (indiceJaccard >= seuilJaccard) {
					JSONObject aLink = new JSONObject();

					int indexNoeud1 = data.indexOf(oneData) - 1;
					int indexNoeud2 = i - 1;

					aLink.put("source", indexNoeud1);
					aLink.put("target", indexNoeud2);
					listLinks.add(aLink);
				}
				i++;
				indiceJaccard = Double.parseDouble(oneData[i]);
			}
		}
		// merge des deux liste(deux objets) nodes et links dans l'objet json
		// final objJSON
		objJSON.put("links", listLinks);
		objJSON.put("nodes", listNodes);

		csvReader.close();

		// ecriture d'un fichier (au cas o�)
		File output = new File("similarity_graph.json");
		FileWriter fileWriter = new FileWriter(output);
		fileWriter.write(objJSON.toJSONString());
		fileWriter.flush();
		fileWriter.close();

		return objJSON;
	}

	public void search(String requete) throws IOException {
		GoogleSearch gcse = new GoogleSearch();
		gcse.GenerateJsonFile("searchResults.json", requete, "", 10L);

		//Trace
		System.out.println(gcse.getSearchUrl());
	}

	public void annotate() {
		DBpediaSpotlightClient c = new DBpediaSpotlightClient();
		JsonParser jsonParser = new JsonParser();
		jsonParser.parseJson("searchResults.json");
		jsonParser.displaySearchResults(jsonParser.searchResults);

		String txt = "";

		for (int i = 0; i < jsonParser.searchResults.searchData.size(); i++) {
			txt += jsonParser.searchResults.searchData.get(0).description
					+ "\n";
		}
		String inputFile = c.writeInFile(txt);

		File input = new File(inputFile);
		File output = new File("output.txt");

		try {
			c.evaluate(input, output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.deleteFile(inputFile);
	}

	public void createGraph(String filePath, String rootUrl) {
		ArrayList arrayOfWords = new ArrayList();
		CreationGraphe cg = new CreationGraphe();

		try {
			cg.extractText(filePath, arrayOfWords);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Model creation
		BasicConfigurator.configure(); // necessary
		Model m = cg.modelCreation(arrayOfWords, rootUrl);

		try {
			cg.writeInFile(m);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void semanticSearch(String requete) throws IOException{
		search(requete);
		annotate();
		
		createGraph();
		compareRDF cRDF = new compareRDF();
		cRDF.creerMatriceSimilarite();
		
	}
}
