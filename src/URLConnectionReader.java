import java.net.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class URLConnectionReader {

	// Google APIs URL
	final private String GOOGLE_SEARCH_URL = "https://www.googleapis.com/customsearch/v1?";

	// API Key
	final private String API_KEY = "AIzaSyCZ9DFQ-zFw3NQgGIyonK0fw8ic_U4vR40";

	// Custom Search Engine ID
	final private String SEARCH_ENGINE_ID = "006463225579681566849:zklgocryvzk";

	// Basic Google search request URL
	final private String FINAL_URL = GOOGLE_SEARCH_URL + "key=" + API_KEY
			+ "&cx=" + SEARCH_ENGINE_ID + "&q=";

	// Complete request URL
	private URL searchUrl;

	// Object of the query
	private String query;

	// Number of results by query (max = 10)
	private long NumberOfResults;

	// Constructor
	public URLConnectionReader() throws MalformedURLException {

		super();
		this.NumberOfResults = (long) 10;
		this.query = "";

	}

	public void GenerateJsonFile(String filename, String query, String fields,
			Long NumberOfResults) throws IOException {

		File file = new File(filename);
		FileWriter fw;
		fw = new FileWriter(file);
		String stringUrl = FINAL_URL + query.replaceAll(" ", "+");

		// Selected fields in the final JSon file, if empty (""), every fields
		// will be selected
		if (!fields.isEmpty()) {

			stringUrl += "&fields=" + fields;

			// Default value = 10
			if (NumberOfResults != null) {

				this.setNumberOfResults((long) NumberOfResults);
				stringUrl += "&num=" + NumberOfResults.toString();
			}
		}

		// URL to be sent
		URL tempUrl = new URL(stringUrl);
		this.searchUrl = tempUrl;

		// Connection initialization
		URLConnection urlConnection = this.searchUrl.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()));

		// Writing in the previously created file
		String inputLine;
		while ((inputLine = in.readLine()) != null) {

			fw.write(inputLine + "\n");
			System.out.println(inputLine);

		}
		in.close();
		fw.close();
	}

	public void setNumberOfResults(long numberOfResults) {

		NumberOfResults = numberOfResults;
	}

	public URL getSearchUrl() {
		
		return searchUrl;
	}

	public void setSearchUrl(URL searchUrl) {

		this.searchUrl = searchUrl;
	}

	public static void main(String[] args) throws Exception {

		URLConnectionReader ucr = new URLConnectionReader();
		ucr.GenerateJsonFile(
				"test.json",
				"obama",
				"",
				10L);
	}
}