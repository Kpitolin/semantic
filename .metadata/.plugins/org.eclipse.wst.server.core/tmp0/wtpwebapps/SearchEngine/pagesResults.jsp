<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList,annotation.SearchData"  %>

<%
ArrayList<SearchData> pages = (ArrayList<SearchData>) request.getAttribute("pages");

for(SearchData onePage : pages) {
%>
<div class="result-box">
	<div class="result-body">
		<h2 class="result-title">
			<a href="${onePage.url}">
				<b>${onePage.title}</b>
				courte description titre
			</a>
		</h2>
		<div class="result-description">
			${onePage.description}
		</div>
		<div class="result-extras">

		</div>
	</div>
</div>
<%
}
%>