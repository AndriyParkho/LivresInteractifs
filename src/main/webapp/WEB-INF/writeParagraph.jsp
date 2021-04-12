<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="writeParagraph.css" />
    <script type="text/javascript" src="scriptParagraphe.js"></script>
  </head>
  <body>
	<c:if test="${user != null}">
		<form method="post" id="formCreate" action="write_paragraph" accept-charset="UTF-8">
		    <p>
            <br>
            <br>
		     Nom du paragraphe :<input type="text" name="title" id="titreParagraphe" value="${titreParag}" disabled/><br>
			 Paragraphe :<TEXTAREA name="story" id="story" rows=4 cols=80 <c:if test="${not empty param.texte}"> value="param.texte" </c:if> required></TEXTAREA><br>
             <p>Mon paragraphe est une conclusion :</p> <label><input type="radio" onclick="hideChoice();" name="isConclusion" checked="checked"/>Oui</label>
			   								 <label><input type="radio" onclick="displayChoice();" name="isConclusion" checked="checked"/>Non</label> <br>
			  <div id="listeDesChoix">
              <p>Nombre de choix :</p><input type="number" id="nbChoix" name="nbChoix" value="1" min="1" max="100" required>
              <input type="button" value="Afficher les choix" onclick="changeChoice();">
              <br>
              <select name="choixRemplis"  id="choixRemplis" size=1 disabled>
				<c:forEach items="${paragrapheRedige}" var="paragrapheRedige">
	            	<option value="${paragrapheRedige.numParag}">${paragrapheRedige.titre}</option>
	            </c:forEach>
	            </select>
			  <table id="choice" class="formulaire"></table>
			  </div>
			 <input type="button" value="Valider le paragraphe" onclick="submitForm();">
		</form>
		<a href="write_paragraph?action=erase&idHist=${idHist}&numParag=${numParag}" id="boutonErase">Annuler la rédaction du paragraphe</a>
	</c:if>
  </body>