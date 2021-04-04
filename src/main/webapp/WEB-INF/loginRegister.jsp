<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>InteractiveStory</title>
    <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
<body>
<c:if test="${login}">
	<h2>Connexion</h2>
	<form method="post" action="login" accept-charset="UTF-8">
	    <p>
	     Email : <input type="text" name="email" required/><br>
	     Mot de passe : <input type="password" name="password" required/><br>
	    </p>
	    <input type="submit" name="Se connecter" />
	    <input type="button"
	    	   onclick="self.location.href='register.html'"
	       	   value="S'enregister">
	  </form>
</c:if>
<c:if test="${!login}">
	<h1>Création d'un nouveau compte</h1>
	<form action="register" method="post" accept-charset="UTF-8">
		<p>
		Nom : <input type="text" name="nom" required/> <br>
		Prénom : <input type="text" name="prenom" required/> <br>
		<hr>
		<p>
		Email : <input type="text" name="email" required/> <br>
		Mot de passe : <input type="password" name="password" required/> <br>
		</p>
		<input type="submit" value="Créer" />
	</form>
</c:if>
</body>
</html>