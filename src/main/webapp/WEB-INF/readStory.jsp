<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>InteractiveStory</title>
<link rel="stylesheet" type="text/css" href="readStory.css" />
<script type="text/javascript" src="scriptReadStory.js"></script>
</head>
<body>
	<script>
		window.onload=function() {
			if('${StoryFirst}'){
				document.getElementById('HistoricBox').style.display = 'none';
			}
			else{
				document.getElementById('StoryBox').style.display = 'none';
			}
		}
		</script>
                
        <c:if test="${user == null}">
     	<ul class='menu'>
     	  <li class='menu'><a href="accueil" class="active">Histoire à lire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=login">Se connecter</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=register">S'enregistrer</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=historique" <c:if test="${param.bouton == 'historique'}"> class="active" </c:if>>Historique</a></li>
		</ul> 
		
     </c:if>
     <c:if test="${user != null}">
     	<ul class='menu'>
	      <li class='menu'><a href="accueil" class="active">Histoire à lire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=storyToWrite">Histoire à écrire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=histoireAPublier">Histoires à publier</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=histoireDepubliable">Histoires dépubliables</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=historique">Historique</a></li>
                  <li style="float:right" class='menu'><a href="accueil?action=bouton&bouton=logout">Se déconnecter</a></li>
		</ul>
     </c:if>
	<input type="button" id='changeBouton' onclick="changeDisplay();" <c:if test="${StoryFirst}"> value="Historique" </c:if>>
	<div id='StoryBox'>
	<div id='paragraphStory'>
	<c:forEach items="${paragsToRead}" var="paragToRead">
             <p>
                 ${paragToRead.texte}
             </p>
	</c:forEach>
	</div>
	<c:if test="${warning}">
	<script>window.onload=function() {
		if('${StoryFirst}'){
			document.getElementById('HistoricBox').style.display = 'none';
		}
		else{
			document.getElementById('StoryBox').style.display = 'none';
		}
		window.alert("Pour changer vos choix, utilisez l'historique.");
	};</script>
     </c:if>
	<c:forEach items="${choixParag}" var="choix" varStatus="vs">
    	<div class="choixSuite"><a class="choixSuite" href="read_story?idHist=${choix.idHist}&choix=${vs.index}&numParagPere=${current.numParag}" class="active">${choix.titre}</a></div>
    	<br>
	</c:forEach>
	</div>
	<div id='HistoricBox'>
	<c:set var="haveSuite" value="true" scope="page" />
	<c:forEach items="${historique}" var="block" varStatus="varstat">
    	<div class="choixHistorique">
    	<c:forEach items="${block}" var="choix" varStatus="varstat2">
    	<c:if test="${varstat2.index == 0}">
    		<a class="TitreHistorique" href="read_story?idHist=${choix.idHist}&goBackTo=${varstat.index}" class="active">${choix.titre}</a>
    	</c:if>
    	<p>${choix.texte}</p>
    	</c:forEach>
    	</div>
    	<br>
	</c:forEach>
	</div>
	<c:if test="${(user != null) and isModified}">
		<a href="accueil?action=save" id="boutonSauvegarde">Sauvegarder et revenir à l'accueil</a>
	</c:if>
</body>
</html>