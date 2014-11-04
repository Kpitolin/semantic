package createGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.jena.riot.Lang;
import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;

public class CreationGraphe {
	public final static String macSeparator = "/";
	public final static String windowsSeparator = "\\"; 
	public final static String separator = macSeparator;
	public static int increment = 0;

	public void extractText(String filePath, ArrayList arrayOfWords)
			throws FileNotFoundException {
		// Recuperation fichier txt
		Scanner scanner = new Scanner(new File(filePath));
		// On boucle sur chaque champ detecté
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			System.out.println(line + "-------");
			if(!(line.contains("%") ) && !line.equals("")){
				arrayOfWords.add(line);
	
			}
		}

		scanner.close();
	}
	public void extractFromJSON (JSONObject job , ArrayList arrayOfarrayOfWords, int iter){
		 JSONObject res=(JSONObject)job.get(iter);
		 JSONObject keyword=(JSONObject)res.get("");
		// JSONArray array=(JSONArray)keyword;
		
	}
	
	public Model modelCreation(ArrayList arrayOfWords, String rootUrl) {
		Model m = ModelFactory.createDefaultModel();
		String dbRootUri = "http://dbpedia.org/resource";
		Resource r = m.createResource(rootUrl);
		Property P;
		Resource res;

		for (int i = 0; i < arrayOfWords.size(); i++) {
			if(arrayOfWords.get(i) instanceof ArrayList){
				// is a list
				modelCreation2levels( (ArrayList) arrayOfWords.get(i),  m,  r);
			}
			else
			{
				P = m.createProperty(dbRootUri + "/" + arrayOfWords.get(i));
				res = m.createResource(dbRootUri + "/" + arrayOfWords.get(i));
				m.add(r, P, res);
			}
			
		}

		return m;
	}
	
	public void modelCreation2levels(ArrayList arrayOfWords, Model m, Resource r) {
		String dbRootUri = "http://dbpedia.org/resource";
		Property P;
		Resource res;
		// the first element of the list will be the root keyword
		// the others, the children

		for (int i = 0; i < arrayOfWords.size(); i++) {
			P = m.createProperty(dbRootUri + "/" + arrayOfWords.get(i));
			res = m.createResource(dbRootUri + "/" + arrayOfWords.get(i));
			m.add(r, P, res);
		}

	}

	public void writeInFile(Model m) throws IOException {
		// now write the model in XML form to a file
		RDFWriter writer = m.getWriter();
		// writer.setErrorHandler(myErrorHandler);
		writer.setProperty("showXmlDeclaration", "true");
		writer.setProperty("tab", "8");
		writer.setProperty("relativeURIs", "same-document,relative");
		OutputStream outStream = new FileOutputStream("."+separator+"extendedGraph"+separator+"foo_" + increment
				+ ".rdf");
		writer.write(m, outStream, "RDF/XML-ABBREV");
		outStream.close();

	}

	public void createGraph(String filePath, String rootUrl) {
		ArrayList arrayOfWords = new ArrayList();
		try {
			extractText(filePath, arrayOfWords);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Model creation
		BasicConfigurator.configure(); // necessary
		Model m = modelCreation(arrayOfWords, rootUrl);

		try {
			writeInFile(m);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {

		

	}

}
