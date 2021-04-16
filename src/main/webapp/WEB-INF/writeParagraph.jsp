<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="writeParagraph.css" />
    <script type="text/javascript" src="scriptWriteParagraphe.js"></script>
  </head>
  <body>
	<c:if test="${user != null}">
		<select name="paragrapheCondition"  id="paragrapheCondition" size=1 disabled>
				<c:forEach items="${paragrapheCondition}" var="paragrapheCondition">
	            	<option value="${paragrapheCondition.numParag}">${paragrapheCondition.titre}</option>
	            </c:forEach>
	    </select>
	    <select name="paragrapheRedige"  id="paragrapheRedige" size=1 disabled>
				<c:forEach items="${paragrapheRedige}" var="paragrapheRedige">
           			<option value="${paragrapheRedige.numParag}">${paragrapheRedige.titre}</option>
           		</c:forEach>
		    </select>
		<form method="post" id="formCreate" action="write_paragraph?idHist=${idHist}&numParag=${numParag}&titre=${titreParag}" accept-charset="UTF-8">
		    <p>
            <br>
            <br>
		     Nom du paragraphe :<input type="text" name="title" id="titreParagraphe"  value="${titreParag}" disabled/><br>
			 Paragraphe :<TEXTAREA name="story" id="story" rows=4 cols=80 required><c:if test="${not empty texte}"> ${texte} </c:if></TEXTAREA><br>
             <c:if test="${numChoix == 0}">
             <p>Mon paragraphe est une conclusion :</p> <label><input type="radio" onclick="hideChoice();" name="isConclusion" id="isConclusionId" value='1' checked="checked"/>Oui</label>
			   								 <label><input type="radio" onclick="displayChoice();" name="isConclusion"  value='0'/>Non</label> <br>
			  
			  	<script>
					window.onload=function() {
					hideChoice();
				}
			</script>
			  </c:if>
			  <c:if test="${nbChoix != 0}">
             <p>Mon paragraphe est une conclusion :</p> <label><input type="radio" onclick="hideChoice();" name="isConclusion" id="isConclusionId" value='1' />Oui</label>
			   								 <label><input type="radio" onclick="displayChoice();" name="isConclusion"  value='0' checked="checked"/>Non</label> <br>
			  
			  </c:if>
			  
			  <div id="listeDesChoix">
              <p>Nombre de choix :</p><br>
              <input type="number" id="nbChoix" name="nbChoix" <c:if test="${not empty ancienChoix}">value="0" min="0"</c:if> <c:if test="${empty ancienChoix}">value="1" min="1"</c:if> max="100" required>
              
              <input type="button" value="Afficher les choix" onclick="changeChoice();">
              <br>
              <c:if test="${nbChoix != 0}">
              <script>
				window.onload=function() {
					setChoice();
				}
			</script>
			</c:if>
			  <table id="choice" class="formulaire">
			  <c:if test="${empty ancienChoix}">
			  <tr><th>Choix 1</th></tr>
			  <tr class="formulaire">
				  <td class="formulaire">
					  	<input type="text"  id="choix1" name="choix1" value="Choix numéro 1" required/>
				  </td></tr>
			<tr class="formulaire">
			<td>
        	<p> Choisir un choix déjà rédigé : </p>
        	<label><input type="radio" onclick="choixRedige(1, 1);" name="choixRedige1"/>Oui</label>
			<label><input type="radio" onclick="choixRedige(1, 0)" name="choixRedige1" checked="checked"/>Non</label>
        	</td>
			</tr>
        	<tr>
        	<td>
        	<select name="paragrapheRedige1"  id="paragrapheRedige1" size=1 disabled>
				<c:forEach items="${paragrapheRedige}" var="paragrapheRedige">
           			<option value="${paragrapheRedige.numParag}">${paragrapheRedige.titre}</option>
           		</c:forEach>
		    </select>
        	</td>
        	</tr><tr>
        		 <td>
        		  <p> Choisir un paragraphe conditionnel : </p>
        		  <label><input type="radio" onclick="choixConditionnel(1, 1);" name="choixConditionnel1"/>Oui</label>
				  <label><input type="radio" onclick="choixConditionnel(1, 0)" name="choixConditionnel1" checked="checked"/>Non</label>
        		 </td>
        		 </tr>
        	<tr><td>
        	<select name="paragrapheCondition1"  id="paragrapheCondition1" size=1 disabled>
					<c:forEach items="${paragrapheCondition}" var="paragrapheCondition">
	            		<option value="${paragrapheCondition.numParag}">${paragrapheCondition.titre}</option>
	           		 </c:forEach>
	   				 </select>
	   				 </td></tr>
	   				 </c:if>
			  </table>
			 <input type="button" value="Valider le paragraphe" onclick="submitForm();">
			 
			 
			 
			 
			 <c:set var="compteurChoix" value="0" scope="page" />
			<c:forEach items="${ancienChoix}" var="oldChoice">
			<c:set var="compteurChoix" value="${compteurChoix + 1}" scope="page"/>
			<div class='introChoix'>
			<table>
			<tr><th>Ancien choix ${compteurChoix}</th></tr><tr>
			<td>
			<p> Supprimer ce choix : </p>
			  <label><input type="radio" onclick='lock(${compteurChoix}, 1);' name="supressOldChoix${compteurChoix}" value='1'/>Oui</label>
			  <label><input type="radio" onclick='lock(${compteurChoix}, 0);' name="supressOldChoix${compteurChoix}" value='0' id="supressOldChoix${compteurChoix}" checked="checked"/>Non</label>
			  </td></tr>
			  </table>
			  </div>
			<table id="tableOldChoice${compteurChoix}" class="formulaire">
			
			  <tr class="formulaire">
				  <td class="formulaire">
					  	<input type="text"  id="oldChoix${compteurChoix}" name="oldChoix${compteurChoix}" <c:if test="${not oldChoice.valide}"> value="${oldChoice.titre}"</c:if> <c:if test="${oldChoice.valide}">value="Ancien choix numéro ${compteurChoix}" disabled</c:if> required/>
				  </td></tr>
			<tr class="formulaire">
			<td>
        	<p> Choisir un choix déjà rédigé : </p>
        	<label><input type="radio" onclick="oldChoixRedige(${compteurChoix}, 1);" name="oldChoixRedige${compteurChoix}" id="oldChoixRedige${compteurChoix}" value='1'<c:if test="${oldChoice.valide}">checked="checked"</c:if>/>Oui</label>
			<label><input type="radio" onclick="oldChoixRedige(${compteurChoix}, 0)" name="oldChoixRedige${compteurChoix}" id="oldChoixRedige${compteurChoix}" value='0' <c:if test="${not oldChoice.valide}">checked="checked"</c:if>/>Non</label>
        	</td>
			</tr>
        	<tr>
        	<td>
        	<select name="oldParagrapheRedige${compteurChoix}"  id="oldParagrapheRedige${compteurChoix}" size=1 <c:if test="${not oldChoice.valide}">disabled</c:if>>
				<c:forEach items="${paragrapheRedige}" var="paragrapheRedige">
           			<option value="${paragrapheRedige.numParag}" <c:if test="${oldChoice.numParag == paragrapheRedige.numParag}">selected="selected"</c:if>>${paragrapheRedige.titre}</option>
           		</c:forEach>
		    </select>
        	</td>
        	</tr><tr>
        		 <td>
        		  <p> Choisir un paragraphe conditionnel : </p>
        		  <label><input type="radio" onclick="oldChoixConditionnel(${compteurChoix}, 1);" name="oldChoixConditionnel${compteurChoix}" id="oldChoixConditionnel${compteurChoix}" value='1' <c:if test="${not empty oldChoice.condition}">checked="checked"</c:if>/>Oui</label>
				  <label><input type="radio" onclick="oldChoixConditionnel(${compteurChoix}, 0)" name="oldChoixConditionnel${compteurChoix}" id="oldChoixConditionnel${compteurChoix}" value='0' <c:if test="${empty oldChoice.condition}">checked="checked"</c:if>/>Non</label>
        		 </td>
        		 </tr>
        	<tr><td>
        	<select name="oldParagrapheCondition${compteurChoix}"  id="oldParagrapheCondition${compteurChoix}" size=1 <c:if test="${oldChoice.condition == null}">disabled</c:if>>
					<c:forEach items="${paragrapheCondition}" var="paragrapheCondition">
	            		<option value="${paragrapheCondition.numParag}" <c:if test="${(not empty oldChoice.condition) and (oldChoice.condition.numParag == paragrapheCondition.numParag)}">selected="selected"</c:if>>${paragrapheCondition.titre}</option>
	           		 </c:forEach>
	   				 </select>
	   				 </td></tr>
	   				 </table>
	   				 </c:forEach>
	   				 </div>
	   		<input type="number" id="nbOldChoix" name="nbOldChoix" value="${compteurChoix}" style="display: none">
			 </form> 
			 
			 
		<div id="boutonDiv">
		<a onclick="submitSave();" class="bouton" id="bouton">Sauvegarder la rédaction du paragraphe</a>
		<a href="write_paragraph?action=erase&idHist=${idHist}&numParag=${numParag}" class="bouton" id="boutonErase">Annuler la rédaction du paragraphe</a>
		</div>
		
		
			
	   		
	</c:if>
  </body>