package annotation;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that annotates text with DBpedia spotlight
 */

public class DBpediaSpotlightClient extends AnnotationClient {

	private final static String API_URL = "http://spotlight.dbpedia.org/";
	private static final double CONFIDENCE = 0.2;
	private static final int SUPPORT = 20;

	@Override
	public List<DBpediaResource> extract(Text text) throws AnnotationException {

		LOG.info("Querying API.");
		String spotlightResponse;
		try {
			GetMethod getMethod = new GetMethod(API_URL + "rest/annotate/?"
					+ "confidence=" + CONFIDENCE + "&support=" + SUPPORT
					+ "&text=" + URLEncoder.encode(text.text(), "utf-8"));
			//System.out.println(URLEncoder.encode(text.text(), "utf-8"));
			//GetMethod getMethod = new GetMethod("http://spotlight.dbpedia.org/rest/annotate?text=In%202004%2C%20Obama%20received&confidence=0.2&support=20&types=Person,Organisation");
			//GetMethod getMethod = new GetMethod("http://spotlight.dbpedia.org/rest/annotate?text=President%20Obama%20called%20Wednesday%20on%20Congress%20to%20extend%20a%20tax%20break%20for%20students%20included%20in%20last%20year%27s%20economic%20stimulus%20package,%20arguing%20that%20the%20policy%20provides%20more%20generous%20assistance.&confidence=0.2&support=20");
			getMethod.addRequestHeader(new Header("Accept", "application/json"));

			spotlightResponse = request(getMethod);
		} catch (UnsupportedEncodingException e) {
			throw new AnnotationException("Could not encode text.", e);
		}

		assert spotlightResponse != null;

		JSONObject resultJSON = null;
		JSONArray entities = null;

		try {
			resultJSON = new JSONObject(spotlightResponse);
			entities = resultJSON.getJSONArray("Resources");
		} catch (JSONException e) {
			throw new AnnotationException(
					"Received invalid response from DBpedia Spotlight API.");
		}

		LinkedList<DBpediaResource> resources = new LinkedList<DBpediaResource>();
		for (int i = 0; i < entities.length(); i++) {
			try {
				JSONObject entity = entities.getJSONObject(i);
				resources.add(new DBpediaResource(entity.getString("@URI"),
						Integer.parseInt(entity.getString("@support"))));

			} catch (JSONException e) {
				LOG.error("JSON exception " + e);
			}

		}

		return resources;
	}

	/**
	 * Function that writes text in a file 
	 * @param text : the text to write in the file
	 * @return : the name of the file
	 */
	public String writeInFile(String text) {

		try {

			File file = new File("filename.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ("filename.txt");

	}

	public void deleteFile(String path) {

		try {

			File file = new File(path);
			file.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		DBpediaSpotlightClient c = new DBpediaSpotlightClient();
		JsonParser jsonParser = new JsonParser();
		jsonParser.parseJson("test.json");
		jsonParser.displaySearchResults(jsonParser.searchResults);
		
		for(int i = 0; i < jsonParser.searchResults.searchData.size(); i++){
			String txt = jsonParser.searchResults.searchData.get(i).description + "\n -";
			String inputFile = c.writeInFile(txt);
			File input = new File(inputFile);
			File output = new File("output" + i + ".txt");

			c.evaluate(input, output);
			c.deleteFile(inputFile);
		}
		
	}

}