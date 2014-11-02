package model;

import java.util.ArrayList;

public class SearchResults {

	protected String searchQuery;
	public ArrayList<SearchData> searchData;
	
	public SearchResults(){
	
		searchQuery = "";
		searchData = new ArrayList<SearchData>();
	}
	

}
