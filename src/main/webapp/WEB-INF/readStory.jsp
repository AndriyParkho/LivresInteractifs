<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>InteractiveStory</title>
<link rel="stylesheet" type="text/css" href="styles.css" />
<script type="text/javascript" src="script.js"></script>
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
		
		function changeDisplay(){
	    	if(document.getElementById('HistoricBox').style.display == 'none'){
	    		document.getElementById('HistoricBox').style.display = 'block';
	    		document.getElementById('StoryBox').style.display = 'none';
	    		document.getElementById('changeBouton').value = "Histoire";
	    	}
	    	else{
	    		document.getElementById('HistoricBox').style.display = 'none';
	    		document.getElementById('StoryBox').style.display = 'block';
	    		document.getElementById('changeBouton').value = "Historique";
	    	}
	    }
		</script>
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
</body>
</html>