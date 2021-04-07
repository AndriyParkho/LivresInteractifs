<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>  
     <c:if test="${user == null}">
     	<ul>
		  <li><a href="accueil?action=bouton&bouton=login">Se connecter</a></li>
		  <li><a href="accueil?action=bouton&bouton=register">S'enregistrer</a></li>
		  <li><a href="accueil?action=bouton&bouton=historique">Historique</a></li>
		</ul> 
     </c:if>
     <c:if test="${user != null}">
		  <c:if test="${param.bouton == 'createStory'}">
		  <ul>
	      <li><a href="accueil?action=bouton&bouton=storyToRead">Histoire à lire</a></li>
		  <li><a href="accueil?action=bouton&bouton=storyToWrite">Histoire à écrire</a></li>
		  <li><a href="accueil?action=bouton&bouton=createStory" class="active">Créer une histoire</a></li>
		  <li><a href="accueil?action=bouton&bouton=historique">Historique</a></li>
		  <li style="float:right"><a href="controleur?action=bouton&bouton=logout">Se déconnecter</a></li>
		</ul>
		  <form method="post" action="createStory" accept-charset="UTF-8">
		    <p>
		     Nom d'histoire <input type="text" name="title"/><br>
		     Confidentialité de l'histoire : <label><input type="radio" name="confident" value="0" />Publique</label>
			   								 <label><input type="radio" name="confident" value="1" />Privée</label> <br>
			Personnes invitées pour l'écriture
			<select name="auteurs" multiple size="4">
		      <option value="Chien">Chien</option>
		      <option value="chat">Chat</option>
		      <option value="perroquet">Perroquet</option>
		      <option value="macaw">Macaw</option>
		      <option value="albatros">Albatros</option>
			</select> <br>
			  Nom du premier paragraphe <input type="text" name="paragraphe"/><br>
			  Premier paragraphe<TEXTAREA name="nom" rows=4 cols=80></TEXTAREA><br>
			  </p>
			  <input type="submit" name="Créer l'histoire" />
		  </form>
		  </c:if>
		  <c:if test="${param.bouton == 'storyToWrite'}">
		  	<ul>
		      <li><a href="accueil?action=bouton&bouton=storyToRead">Histoire à lire</a></li>
			  <li><a href="accueil?action=bouton&bouton=storyToWrite" class="active">Histoire à écrire</a></li>
			  <li><a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a></li>
			  <li><a href="accueil?action=bouton&bouton=historique">Historique</a></li>
			  <li style="float:right"><a href="controleur?action=bouton&bouton=logout">Se déconnecter</a></li>
			</ul>
		  	<table>
            <tr>
                <th>Titre</th>
            </tr>
            <c:forEach items="${histoires}" var="histoire">
                <tr>
                    <td><a href="write_story?idHist=${histoire.id}">${histoire.titre}</a></td>
                </tr>
            </c:forEach>
        </table>
	 	  </c:if>
		  
     </c:if>
     
     <c:if test="${param.bouton == 'historique'}">
		 <ul>
		      <li><a href="accueil?action=bouton&bouton=storyToRead">Histoire à lire</a></li>
			  <li><a href="accueil?action=bouton&bouton=storyToWrite">Histoire à écrire</a></li>
			  <li><a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a></li>
			  <li><a href="accueil?action=bouton&bouton=historique" class="active">Historique</a></li>
			  <li style="float:right"><a href="controleur?action=bouton&bouton=logout">Se déconnecter</a></li>
		</ul>
	 </c:if>
	 
     <c:if test="${empty param.bouton}">
     	<ul>
		      <li><a href="accueil?action=bouton&bouton=storyToRead" class="active">Histoire à lire</a></li>
			  <li><a href="accueil?action=bouton&bouton=storyToWrite">Histoire à écrire</a></li>
			  <li><a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a></li>
			  <li><a href="accueil?action=bouton&bouton=historique">Historique</a></li>
			  <li style="float:right"><a href="controleur?action=bouton&bouton=logout">Se déconnecter</a></li>
		</ul>
     	<table>
            <tr>
                <th>Titre</th>
            </tr>
            <c:forEach items="${histoires}" var="histoire">
                <tr>
                    <td><a href="read_story?idHist=${histoire.id}">${histoire.titre}</a></td>
                </tr>
            </c:forEach>
        </table>
	 </c:if>
  </body>
</html>