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
 
    function activeInvite(){
 
        if(document.getElementById('buttonPublic').checked){
 
            document.getElementById('listAuthors').disabled = 'disabled';
            document.getElementById('listAuthors').selectedIndex = -1;
 
        }
 
        else{
 
            document.getElementById('listAuthors').disabled = '';
 
        }
 
    }
    
    function EraseSelect(){
            document.getElementById('listAuthors').selectedIndex = -1;
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
		  <form method="post" action="createStory" accept-charset="UTF-8">
		    <p>
                        <br>
                        <br>
		     Nom d'histoire <input type="text" name="title"/><br>
		     Confidentialité de l'histoire : <label><input type="radio" onclick="activeInvite();" name="confident" checked="checked" id="buttonPublic"/>Publique</label>
			   								 <label><input type="radio" onclick="activeInvite();" name="confident"/>Privée</label> <br>
			Personnes invitées pour l'écriture
			<select name="auteurs"  id="listAuthors" multiple disabled="disabled" size=2>
				<c:forEach items="${user}" var="user">
	            	<option value="${user.id}">${user.nom} ${user.prenom}</option>
	            </c:forEach>
			</select> 
			 <input type="button" value="Effacer la sélection" onclick="EraseSelect()"> <br>
			  Nom du premier paragraphe <input type="text" name="titreParagraphe"/><br>
			  Premier paragraphe<TEXTAREA name="story" rows=4 cols=80></TEXTAREA><br>
                          Nombre de choix <input type="number" name="nbChoix" min="0" max="100"><br>
			  </p>
			  <input type="submit" name="Créer l'histoire" />
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
                        Vous avez déja un paragraphe en cours de rédaction : <a href="write_story?idHist=${histoireDejaCommence.id}" class="story">${histoireDejaCommence.titre}</a>
                    </c:if><br>
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