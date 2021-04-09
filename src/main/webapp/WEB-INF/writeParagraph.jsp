<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
  <script>
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
	<c:if test="${user != null}">
		<form method="post" id="formCreate" action="createStory" accept-charset="UTF-8">
		    <p>
            <br>
            <br>
		     Nom du paragraphe <input type="text" name="title" value="${param.title} disabled"/><br>
			 Premier paragraphe<TEXTAREA name="story" id="story" rows=4 cols=80 <c:if test="${not empty param.texte}"> value="param.texte" </c:if> required></TEXTAREA><br>
             Nombre de choix <input type="number" id="nbChoix" name="nbChoix" value="0" min="0" max="100" required>
             <input type="button" value="Afficher les choix" onclick="changeChoice();">
             <br>
			 </p>
			 <table id="choice" class="formulaire"></table>
			 <br>
			 <input type="button" value="Créer l'histoire" onclick="submitForm();">
	         <p id="errorMessage"> </p>
		</form>
	</c:if>
  </body>