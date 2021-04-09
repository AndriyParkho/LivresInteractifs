<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
  <script type="text/javascript">

  
    function displayInvite(){
    	document.getElementById('listPersons').style.display = 'block';
    }
    
    function hideInvite(){
    	document.getElementById('listPersons').style.display = 'none';
    	document.getElementById('listAuthors').selectedIndex = -1;
    }
    
    function EraseSelect(){
            document.getElementById('listAuthors').selectedIndex = -1;
    }
    
    var nbChoixJs = 0;
    
    function changeChoice(){
    	document.getElementById("errorMessage").innerHTML ="";
    	var newNbChoice = document.getElementById("nbChoix").value;
    	var div = document.getElementById("choice");
        while (newNbChoice > nbChoixJs){
        	nbChoixJs++;
        	div.insertAdjacentHTML('beforeend', '<tr class="formulaire"><td class="formulaire"><input type="text"  id="choix'+nbChoixJs.toString()+'" value="Choix numéro '+nbChoixJs.toString()+'" required/></td></tr>');
        }
    	 while (newNbChoice < nbChoixJs){
    		nbChoixJs--;
 	        div.deleteRow(nbChoixJs);
 	      } 
    }
    
    function checkRequired(){
    	if(document.getElementById("title").value == ""){
    		return false;
    	}
    	if(document.getElementById("titreParagraphe").value == ""){
    		return false;
    	}
    	if(document.getElementById("story").value == ""){
    		return false;
    	}
    	if(document.getElementById("nbChoix").value == ""){
    		return false;
    	}
    	return true;
    } 
    
    function checkChoice(){
    	var nbChoiceSupposed = document.getElementById("nbChoix").value;
    	var test = document.getElementById("choix" + nbChoiceSupposed.toString());
    	if(nbChoiceSupposed == 0){
    		if(test == null){
    			return true;
    		}
    		else{
    			return false;
    		}
    	}
    	else{
    		if(test == null){
    			return false;
    		}
    		else{
    			return true;
    		}
    	}
    }
    
    function submitForm(){
    	if(checkRequired()){
    		if(checkChoice()){
    			document.getElementById("formCreate").submit();
    		}
    		else{
    			document.getElementById("errorMessage").innerHTML = "Appuyez sur le bouton valider pour mettre à jour vos choix";
    		}
    	}
    	else{
    		document.getElementById("errorMessage").innerHTML = "Tous les champs doivent être remplis";
    	} 
    } 
    
</script>

     <c:if test="${user == null}">
     	<ul>
		  <li><a href="accueil?action=bouton&bouton=login">Se connecter</a></li>
		  <li><a href="accueil?action=bouton&bouton=register">S'enregistrer</a></li>
		  <li><a href="accueil?action=bouton&bouton=historique" <c:if test="${param.bouton == 'historique'}"> class="active" </c:if>>Historique</a></li>
		</ul> 
		
     </c:if>
     <c:if test="${user != null}">
     	<ul>
	      <li><a href="accueil" <c:if test="${empty param.bouton}"> class="active" </c:if>>Histoire à lire</a></li>
		  <li><a href="accueil?action=bouton&bouton=storyToWrite" <c:if test="${param.bouton == 'storyToWrite'}"> class="active" </c:if>>Histoire à écrire</a></li>
		  <li><a href="accueil?action=bouton&bouton=createStory" <c:if test="${param.bouton == 'createStory'}"> class="active" </c:if>>Créer une histoire</a></li>
		  <li><a href="accueil?action=bouton&bouton=historique" <c:if test="${param.bouton == 'historique'}"> class="active" </c:if>>Historique</a></li>
		  <li style="float:right"><a href="accueil?action=bouton&bouton=logout">Se déconnecter</a></li>
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
              <p>Nombre de choix :</p><input type="number" id="nbChoix" name="nbChoix" value="0" min="0" max="100" required>
              <input type="button" value="Afficher les choix" onclick="changeChoice();">
              <br>
			  
			  <table id="choice" class="formulaire"></table>
			  <br>
			  <input type="button" value="Créer l'histoire" onclick="submitForm();">
	          <p id="errorMessage"> </p>
		  </form>
		  </c:if>
		  <c:if test="${param.bouton == 'storyToWrite'}">
                      <br>
                      <br>
                    <table>
	            <tr>
	                <th>Titre</th>
	            </tr>
                    
                    <c:if test="${histoireDejaCommence != null}">
                        <div class='alreadyWritting'>Vous avez déja un paragraphe en cours de rédaction : <a href="write_story?idHist=${histoireDejaCommence.id}" class='alreadyWritting'>${histoireDejaCommence.titre}</a></div>
                    </c:if>
	            <c:forEach items="${histoires}" var="histoire">
	                <tr>
	                    <td><a href="write_story?idHist=${histoire.id}" class="story">${histoire.titre}</a></td>
	                </tr>
	            </c:forEach>
	        	</table>

	 	  </c:if>
		  
     </c:if>
     
     <c:if test="${param.bouton == 'historique'}">
	 </c:if>
	 
     <c:if test="${empty param.bouton}">
     	<table>
            <tr>
                <th>Titre</th>
            </tr>
            <c:forEach items="${histoires}" var="histoire">
                <tr>
                    <td><a href="read_story?idHist=${histoire.id}&numParag=1">${histoire.titre}</a></td>
                </tr>
            </c:forEach>
        </table>
	 </c:if>
  </body>
</html>