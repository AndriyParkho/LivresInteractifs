<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
    <script type="text/javascript" src="script.js"></script>
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
		  <li class='menu'><a href="accueil?action=bouton&bouton=historique" <c:if test="${param.bouton == 'historique'}"> class="active" </c:if>>Historique</a></li>
		  <li style="float:right" class='menu'><a href="accueil?action=bouton&bouton=logout">Se déconnecter</a></li>
		</ul>
		  <c:if test="${param.bouton == 'createStory'}">
		  <form method="post" id="formCreate" action="createStory" accept-charset="UTF-8">
		    <p>
            <br>
            <br>
		     Nom d'histoire :<input type="text" name="title" id="title"/><br>
		     Confidentialité de l'histoire : <label><input type="radio" onclick="hideInvite();" name="confident" checked="checked" id="buttonPublic"/>Publique</label>
			   								 <label><input type="radio" onclick="displayInvite();" name="confident"/>Privée</label> <br>
			<div id='listPersons'>Personnes invitées pour l'écriture : <br><br>
		
			<select name="auteurs"  id="listAuthors" size=2 multiple >
				<c:forEach items="${user}" var="user">
	            	<option value="${user.id}">${user.nom} ${user.prenom}</option>
	            </c:forEach>
			</select> 
			<input type="button" value="Effacer la sélection" onclick="EraseSelect()">
			 <br>
			 </div>
			 <p>
			  Nom du premier paragraphe :</p><input type="text" name="titreParagraphe" id="titreParagraphe"/>
			  <br>
			  <p>Premier paragraphe :</p><TEXTAREA name="story" id="story" rows=4 cols=80 required></TEXTAREA>
			  <br>
			  <p>Mon paragraphe est une conclusion :</p> <label><input type="radio" onclick="hideChoice();" name="isConclusion" checked="checked"/>Oui</label>
			   								 <label><input type="radio" onclick="displayChoice();" name="isConclusion" checked="checked"/>Non</label> <br>
			  <div id="listeDesChoix">
              <p>Nombre de choix :</p><input type="number" id="nbChoix" name="nbChoix" value="0" min="0" max="100" required>
              <input type="button" value="Afficher les choix" onclick="changeChoice();">
              <br>
			  <table id="choice" class="formulaire"></table>
			  </div>
			  <br>
			  <input type="button" value="Créer l'histoire" onclick="submitForm();">
	          <p id="errorMessage"> </p>
		  </form>
		  </c:if>
		  <c:if test="${param.bouton == 'storyToWrite'}">
                      <br>
                      <br>
                    
                    <c:if test="${paragEnCours != null}">
                        <div class='alreadyWritting'>Vous avez déja un paragraphe en cours de rédaction : <a href="write_paragraph?idHist=${paragEnCours.idHist}&numParag=${paragEnCours.numParag}" class='alreadyWritting'>${paragEnCours.titre}</a></div>
                    </c:if>
	                <c:if test="${paragEnCours == null}">
	                 	<table>
	            			<tr>
	                			<th>Titre</th>
	            			</tr>
			           		 <c:forEach items="${histoires}" var="histoire">
			                	<tr>
			                    	<td class="click"><a href="write_story?idHist=${histoire.id}&numParagPere=1" class="story">${histoire.titre}</a></td>
			                	</tr>
			            	</c:forEach>
			            </table>
		            </c:if>
	        	

	 	  </c:if>
		  
     </c:if>
     
     <c:if test="${param.bouton == 'historique'}">
     <table>
            <tr>
                <th>Liste des histoires commencées</th>
            </tr>
            <c:forEach items="${histoires}" var="histoire">
                <tr>
                    <td class="click"><a href="read_story?idHist=${histoire.id}">${histoire.titre}</a></td>
                </tr>
            </c:forEach>
        </table>
	 </c:if>
	 
     <c:if test="${empty param.bouton}">
     	<table>
            <tr>
                <th>Titre</th>
            </tr>
            <c:forEach items="${histoires}" var="histoire">
                <tr>
                    <td class="click"><a href="read_story?idHist=${histoire.id}&numParagPere=1">${histoire.titre}</a></td>
                </tr>
            </c:forEach>
        </table>
	 </c:if>
  </body>
</html>