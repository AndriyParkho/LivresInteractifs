<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>InteractiveStory</title>
</head>
<body>
	<c:forEach items="${paragsToRead}" var="paragToRead">
             <p>
                 ${paragToRead.texte}
             </p>
	</c:forEach>
	<c:forEach items="${choixParag}" var="choix" varStatus="vs">
		<ul>
             <li><a href="read_story?idHist=${choix.idHist}&choix=${vs.index}&numParag=${choix.numParag}" class="active">${choix.titre}</a></li>
    	</ul>
	</c:forEach>
</body>
</html>