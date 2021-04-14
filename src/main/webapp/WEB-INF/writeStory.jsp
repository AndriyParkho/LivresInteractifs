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
    <c:if test="${user == null}">
     	<ul class='menu'>
     	  <li class='menu'><a href="accueil" <c:if test="${empty param.bouton}"> class="active" </c:if>>Histoire à lire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=login">Se connecter</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=register">S'enregistrer</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=historique" <c:if test="${param.bouton == 'historique'}"> class="active" </c:if>>Historique</a></li>
		</ul> 
		
     </c:if>
     <c:if test="${user != null}">
     	<ul class='menu'>
	      <li class='menu'><a href="accueil" <c:if test="${empty param.bouton}"> class="active" </c:if>>Histoire à lire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=storyToWrite" <c:if test="${param.bouton == 'storyToWrite'}"> class="active" </c:if>>Histoire à écrire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=createStory" <c:if test="${param.bouton == 'createStory'}"> class="active" </c:if>>Créer une histoire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=histoireAPublier" <c:if test="${param.bouton == 'histoireAPublier'}"> class="active" </c:if>>Histoires à publier</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=histoireDepubliable" <c:if test="${param.bouton == 'histoireDepubliable'}"> class="active" </c:if>>Histoires dépubliables</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=historique" <c:if test="${param.bouton == 'historique'}"> class="active" </c:if>>Historique</a></li>
                  <li style="float:right" class='menu'><a href="accueil?action=bouton&bouton=logout">Se déconnecter</a></li>
		</ul>
     </c:if>
    
        
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