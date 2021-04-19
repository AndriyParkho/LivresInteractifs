<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="accueil.css" />
    <script type="text/javascript" src="scriptAccueil.js"></script>
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
		  <li class='menu'><a href="accueil?action=bouton&bouton=paragEcrit" <c:if test="${param.bouton == 'paragEcrit'}"> class="active" </c:if>>Paragraphes rédigé</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=histoireAPublier" <c:if test="${param.bouton == 'histoireAPublier'}"> class="active" </c:if>>Histoires à publier</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=histoireDepubliable" <c:if test="${param.bouton == 'histoireDepubliable'}"> class="active" </c:if>>Histoires dépubliables</a></li>
		  <li class='menu'><a href="accueil?action=bouton&bouton=historique" <c:if test="${param.bouton == 'historique'}"> class="active" </c:if>>Historique</a></li>
                  <li style="float:right" class='menu'><a href="accueil?action=bouton&bouton=logout">Se déconnecter</a></li>
		</ul>
		  <c:if test="${param.bouton == 'createStory'}">
			  <c:if test="${paragEnCours != null}">
                       <div class='alreadyWritting'>Vous avez déja un paragraphe en cours de rédaction : <a href="write_paragraph?idHist=${paragEnCours.idHist}&numParag=${paragEnCours.numParag}&titreParag=${paragEnCours.titre}" class='alreadyWritting'>${paragEnCours.titre}</a></div>
	          </c:if>
	          <c:if test="${paragEnCours == null}">	          			
				  <form method="post" id="formCreate" action="createStory" accept-charset="UTF-8">
				    <p>
		
				     Nom d'histoire :<input type="text" name="title" id="title"/><br>
		                     Confidentialité de l'histoire : <label><input type="radio" onclick="hideInvite();" name="confident" checked="checked" id="buttonPublic" value="0"/>Publique</label>
					   								 <label><input type="radio" onclick="displayInvite();" name="confident" value="1"/>Privée</label> <br>
					<div id='listPersons'>Personnes invitées pour l'écriture : <br><br>
				
					<select name="auteurs"  id="auteurs" size=2 multiple >
		
						<c:forEach items="${user}" var="user">
			            	<option value="${user.id}">${user.nom} ${user.prenom}</option>
			            </c:forEach>
					</select> 
					<input type="button" value="Effacer la sélection" onclick="eraseSelect()">
					 <br>
					 </div>
					 <p>
					  Nom du premier paragraphe :</p><input type="text" name="titreParagraphe" id="titreParagraphe"/>
					  <br>
					  <p>Premier paragraphe :</p><TEXTAREA name="story" id="story" rows=4 cols=80 required></TEXTAREA>
					  <br>
					  <p>Mon paragraphe est une conclusion :</p> <label><input type="radio" onclick="hideChoice();" name="isConclusion" value='1'/>Oui</label>
					   								 <label><input type="radio" onclick="displayChoice();" name="isConclusion" checked="checked" value='0'/>Non</label> <br>
					  <div id="listeDesChoix">
		              <p>Nombre de choix :</p><input type="number" id="nbChoix" name="nbChoix" value="1" min="1" max="100" required>
		              <input type="button" value="Afficher les choix" onclick="changeChoice();">
		              <br>
					  <table id="choice" class="formulaire">
					  	<tr class="formulaire">
						  	<td class="formulaire"><input type="text"  id="choix1" name="choix1" value="Choix numéro 1" required/>
						  	</td>
					  	</tr>
		        
					  </table>
					  </div>
					  <br>
					  <input type="button" value="Créer l'histoire" onclick="submitForm();">
				  </form>
	          </c:if>
		  </c:if>
		  <c:if test="${param.bouton == 'paragEcrit'}">
           		 <c:forEach var="entry" items="${paragrapheRedige}">
                	<c:forEach  items="${entry.value}" var="parag">	                		
	                	<div id='paragraphStory'>
	                	<h1>${entry.key} : ${parag.titre}</h1>
					         <p>
					             ${parag.texte}
					         </p>
					         <ul>
						         <c:forEach  items="${parag.paragSuiv}" var="choix">
						         	<li>${choix.titre}</li>		
						         </c:forEach>
					         </ul>
					         <form action="parag_ecrit">
					         	<input type="hidden"
							            name="button"
							            value="modifier">
					         	<input type="hidden"
							            name="idhist"
							            value="${parag.idHist}">
					         	<input type="hidden"
							            name="numparag"
							            value="${parag.numParag}">
    							<input type="submit" value="Modifier" />
							</form>
							<form action="parag_ecrit">
					         	<input type="hidden"
							            name="button"
							            value="supprimer">
					         	<input type="hidden"
							            name="idhist"
							            value="${parag.idHist}">
					         	<input type="hidden"
							            name="numparag"
							            value="${parag.numParag}">
    							<input type="submit" value="Supprimer" />
							</form>
						</div>
                	</c:forEach>
            	</c:forEach>
	 	  </c:if>
		  <c:if test="${param.bouton == 'storyToWrite'}">
		  			<c:if test="${warning == 'paragIndisponible'}">
						<script>window.onload=function() {
							window.alert("Le choix n'est plus disponible, désolé...");
						};</script>
				    </c:if>
                    <c:if test="${paragEnCours != null}">
                        <div class='alreadyWritting'>Vous avez déja un paragraphe en cours de rédaction : <a href="write_paragraph?idHist=${paragEnCours.idHist}&numParag=${paragEnCours.numParag}&titreParag=${paragEnCours.titre}" class='alreadyWritting'>${paragEnCours.titre}</a></div>
                    </c:if>
	                <c:if test="${paragEnCours == null}">
	                	<c:if test="${not empty histoires}">
	                 	<table>
	            			<tr>
	                			<th>Titre</th>
	            			</tr>
			           		 <c:forEach items="${histoires}" var="histoire">
			                	<tr class="activable">
			                    	<td class="click"><a href="write_story?idHist=${histoire.id}&numParagPere=1" class="story">${histoire.titre}</a></td>
			                	</tr>
			            	</c:forEach>
			            </table>
			            </c:if>
			            </c:if>
			        <c:if test="${empty histoires}">
			        <div class="emptyStory">
			        <h2>Il n'y a pas d'histoire à écrire</h2>
			        <a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a>
			        </div>
			        </c:if>
		            </c:if>
	        	

	 	  </c:if>
		  
     
     
     <c:if test="${param.bouton == 'historique'}">
     	<c:if test="${not empty histoires}">
		     <table>
		            <tr>
		                <th>Liste des histoires commencées</th>
		            </tr>
		            <c:forEach items="${histoires}" var="histoire">
		                <tr class="activable">
		                    <td class="click"><a href="read_story?idHist=${histoire.id}">${histoire.titre}</a></td>
		                </tr>
		            </c:forEach>
		        </table>
		        <c:if test="${(user != null) and isModified}">
					<a href="accueil?action=save" id="boutonSauvegarde">Sauvegarder et revenir à l'accueil</a>
				</c:if>
		</c:if>
		<c:if test="${empty histoires}">
			        <div class="emptyStory">
			        <h2>Votre historique est vide</h2>
			        <a href="accueil">Commencer une histoire</a>
			        </div>
			</c:if>
	 </c:if>
	 
     <c:if test="${empty param.bouton}">
     	<c:if test ="${not empty histoires}">
     	<table>
            <tr>
                <th>Titre</th>
            </tr>
            <c:forEach items="${histoires}" var="histoire">
                <tr class="activable">
                    <td class="click"><a href="read_story?idHist=${histoire.id}&numParagPere=1">${histoire.titre}</a></td>
                </tr>
            </c:forEach>
        </table>
        </c:if>
        	<c:if test="${(empty histoires) and (user != null)}">
			        <div class="emptyStory">
			        <h2>Il n'y a pas d'histoire à lire</h2>
			        <a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a>
			        </div>
			</c:if>
			<c:if test="${(empty histoires) and (user == null)}">
			        <div class="emptyStory">
			        <h2>Il n'y a pas d'histoire à lire</h2>
			        <a href="accueil?action=bouton&bouton=login">Connectez-vous pour créer une histoire</a>
			        </div>
			</c:if>
	 	</c:if>
	 	
	 
	  <c:if test="${param.bouton == 'histoireAPublier'}">
	  <c:if test="${not empty histoiresAPublier}">                 
      	<form method="post" class="formValidHistoire" action="publication">
      		<h2>Histoires à publier</h2>
        	<c:forEach items="${histoiresAPublier}" var="histoire">                   
            	<p class="histoirePubli">${histoire.titre} <input type="checkbox" id="${histoire.id}" name="${histoire.id}"></p><br>
            </c:forEach>
            <input type="submit" value="Publier les histoires"> 
        </form>
        </c:if>
        <c:if test="${empty histoiresAPublier}">
        <div class="emptyStory">
        <h2>Vous n'avez pas d'histoire à publier</h2>
        <a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a>
        </div>
        </c:if>
	 </c:if>
	 
	 <c:if test="${param.bouton == 'histoireDepubliable'}">
	 	<c:if test="${not empty histoiresDepubliables}">
	 		<form method="post" class="formValidHistoire" action="depublication">
      		<h2>Histoires à dépublier</h2>
        	<c:forEach items="${histoiresDepubliables}" var="histoire">                   
            	<p class="histoirePubli">${histoire.titre} <input type="checkbox" id="${histoire.id}" name="${histoire.id}"></p><br>
            </c:forEach>
            <input type="submit" value="Dépublier les histoires"> 
        </form>
        </c:if>
        <c:if test="${empty histoiresDepubliables}">
        <div class="emptyStory">
        <h2>Vous n'avez pas d'histoire à dépublier</h2>
        <div id="boutonBlock">
        <a href="accueil?action=bouton&bouton=histoireAPublier">Histoires à publier</a>
        <a href="accueil?action=bouton&bouton=createStory">Créer une histoire</a>
        </div>
        </div>
        </c:if>
	 </c:if>
         
                                        
        <c:if test="${param.bouton == 'supprimer'}$">
            <c:if test="${not suppression}$">
                <script>window.onload=function() {
                    window.alert("Ce paragraphe ne peut pas être supprimé : quelqu'un écrit sur l'un des fils");
		};</script>
            </c:if>
        </c:if>
  </body>
</html>