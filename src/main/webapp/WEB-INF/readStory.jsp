<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>InteractiveStory</title>
<link rel="stylesheet" type="text/css" href="styles.css" />
</head>
<body>
	<div id='paragraphStory'>
	<c:forEach items="${paragsToRead}" var="paragToRead">
             <p>
                 ${paragToRead.texte}
             </p>
	</c:forEach>
	</div>
	<c:forEach items="${choixParag}" var="choix" varStatus="vs">
    	<div class="choixSuite"><a class="choixSuite" href="read_story?idHist=${choix.idHist}&choix=${vs.index}&numParagPere=${current.numParag}" class="active">${choix.titre}</a></div>
    	<br>
	</c:forEach>
</body>
</html>