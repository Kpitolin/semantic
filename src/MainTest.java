
public class MainTest {

	public static void main(String[] args) throws Exception {

		GoogleSearch gcse = new GoogleSearch();
		gcse.GenerateJsonFile("test.json", "Obama Care", "", 10L);
		System.out.println(gcse.getSearchUrl());
		DuckDuckGoSearch ddg = new DuckDuckGoSearch();
		ddg.GenerateJsonFile("duckduckgo.json", "coco");
		System.out.println(ddg.getSearchUrl());
	}

}
