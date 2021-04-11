<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>InteractiveStory</title>
<link rel="stylesheet" type="text/css" href="styles.css" />
</head>
<body>
	<div id='paragraphStory'>
         <p>
             ${currentParag.texte}
         </p>
	</div>
	<h1>Choix déjà rédigé :</h1>
	<c:forEach items="${choixParagSuite}" var="choix" varStatus="vs">
    	<div class="choixSuite"><a class="choixSuite" href="write_story?idHist=${choix.idHist}&choix=${vs.index}&numParagPere=${currentParag.numParag}" class="active">${choix.titre}</a></div>
    	<br>
	</c:forEach>
	<h1>Choix à rédiger :</h1>
	<c:forEach items="${choixParagAEcrire}" var="choix" varStatus="vs">
    	<div class="choixSuite"><a class="choixSuite" href="write_paragraph?&idHist=${choix.idHist}&numParag=${choix.numParag}&titreParag=${choix.titre}" class="active">${choix.titre}</a></div>
    	<br>
	</c:forEach>
</body>
</html>