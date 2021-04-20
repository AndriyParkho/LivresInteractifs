<%@ page contentType="text/html; charset=utf-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<html>

		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<title>InteractiveStory</title>
			<link rel="stylesheet" type="text/css" href="styles/writeParagraph.css" />
			<script type="text/javascript" src="scripts/scriptWriteParagraphe.js"></script>
		</head>

		<body>
			<select name="paragrapheCondition" id="paragrapheCondition" size=1 disabled>
				<c:forEach items="${paragrapheCondition}" var="paragrapheCondition">
					<option value="${paragrapheCondition.numParag}">${paragrapheCondition.titre}</option>
				</c:forEach>
			</select>
			<select name="paragrapheRedige" id="paragrapheRedige" size=1 disabled>
				<c:forEach items="${paragrapheRedige}" var="paragrapheRedige">
					<option value="${paragrapheRedige.numParag}">${paragrapheRedige.titre}</option>
				</c:forEach>
			</select>
			<c:if test="${user != null}">
				<c:if test="${modify == null}">
					<form method="post" id="formCreate"
						action="write_paragraph?idHist=${idHist}&numParag=${numParag}&titre=${titreParag}"
						accept-charset="UTF-8">
						<p>
							<br>
							<br>
							Nom du paragraphe :<input type="text" name="title" id="titreParagraphe"
								value="${titreParag}" disabled /><br>
							Paragraphe :<TEXTAREA name="story" id="story" rows=4
								cols=80><c:if test="${not empty texte}"> ${texte} </c:if></TEXTAREA><br>
							<c:if test="${nbChoix == 0}">
								<p>Mon paragraphe est une conclusion :</p> <label><input type="radio"
										onclick="hideChoice();" name="isConclusion" id="isConclusionId" value='1'
										checked="checked" />Oui</label>
								<label><input type="radio" onclick="displayChoice();" name="isConclusion"
										value='0' />Non</label> <br>
							</c:if>
							<c:if test="${nbChoix != 0}">
								<p>Mon paragraphe est une conclusion :</p> <label><input type="radio"
										onclick="hideChoice();" name="isConclusion" id="isConclusionId"
										value='1' />Oui</label>
								<label><input type="radio" onclick="displayChoice();" name="isConclusion" value='0'
										checked="checked" />Non</label> <br>
							</c:if>
						<div id="listeDesChoix">
							<c:if test="${not empty ancienChoix}">
								<p>Nombre de nouveaux choix :</p>
							</c:if>
							<c:if test="${empty ancienChoix}">
								<p>Nombre de choix :</p>
							</c:if>
							<br>
							<input type="number" id="nbChoix" name="nbChoix" value="0" min="0" max="100" required>
							<input type="button" value="Afficher les choix" onclick="changeChoice();">
							<br>
							<hr>
							<div id="blocDesChoix">
								<div id="divAncienChoix">
									<c:set var="compteurChoix" value="0" scope="page" />
									<c:forEach items="${ancienChoix}" var="oldChoice">
										<c:set var="compteurChoix" value="${compteurChoix + 1}" scope="page" />
										<div class='introChoix'>
											<table>
												<tr>
													<th>Ancien choix ${compteurChoix}</th>
												</tr>
												<tr>
													<td>
														<p> Supprimer ce choix : </p>
														<label><input type="radio" onclick='lock(${compteurChoix}, 1);'
																name="supressOldChoix${compteurChoix}"
																value='1' />Oui</label>
														<label><input type="radio" onclick='lock(${compteurChoix}, 0);'
																name="supressOldChoix${compteurChoix}" value='0'
																id="supressOldChoix${compteurChoix}"
																checked="checked" />Non</label>
													</td>
												</tr>
											</table>
										</div>
										<table id="tableOldChoice${compteurChoix}" class="formulaire">
											<tr class="formulaire">
												<td class="formulaire">
													<input type="text" id="oldChoix${compteurChoix}"
														name="oldChoix${compteurChoix}" <c:if
														test="${not oldChoice.valide}"> value="${oldChoice.titre}"
							</c:if>
							<c:if test="${oldChoice.valide}">value="Ancien choix numéro ${compteurChoix}" disabled</c:if> required/>
							</td>
							</tr>
							<tr class="formulaire">
								<td>
									<p> Choisir un choix déjà rédigé : </p>
									<label><input type="radio" onclick="oldChoixRedige(${compteurChoix}, 1);"
											name="oldChoixRedige${compteurChoix}" id="oldChoixRedige${compteurChoix}" value='1'
											<c:if test="${oldChoice.valide}">checked="checked"
						</c:if>/>Oui</label>
						<label><input type="radio" onclick="oldChoixRedige(${compteurChoix}, 0)"
								name="oldChoixRedige${compteurChoix}" id="oldChoixRedige${compteurChoix}" value='0' <c:if
								test="${not oldChoice.valide}">checked="checked"</c:if>
							/>Non</label>
						</td>
						</tr>
						<tr>
							<td>
								<select name="oldParagrapheRedige${compteurChoix}" id="oldParagrapheRedige${compteurChoix}" size=1
									<c:if test="${not oldChoice.valide}">disabled</c:if>>
									<c:forEach items="${paragrapheRedige}" var="paragrapheRedige">
										<option value="${paragrapheRedige.numParag}" <c:if
											test="${oldChoice.numParag == paragrapheRedige.numParag}">selected="selected"</c:if>
											>${paragrapheRedige.titre}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<p> Choisir un paragraphe conditionnel : </p>
								<label><input type="radio" onclick="oldChoixConditionnel(${compteurChoix}, 1);"
										name="oldChoixConditionnel${compteurChoix}" value='1' <c:if
										test="${not empty oldChoice.condition}">checked="checked"</c:if>/>Oui</label>
								<label><input type="radio" onclick="oldChoixConditionnel(${compteurChoix}, 0)"
										name="oldChoixConditionnel${compteurChoix}" id="oldIsNotConditionnal${compteurChoix}"
										value='0' <c:if test="${empty oldChoice.condition}">checked="checked"</c:if>/>Non</label>
							</td>
						</tr>
						<tr>
							<td>
								<select name="oldParagrapheCondition${compteurChoix}" id="oldParagrapheCondition${compteurChoix}"
									size=1 <c:if test="${oldChoice.condition == null}">disabled</c:if>>
									<c:forEach items="${paragrapheCondition}" var="paragrapheCondition">
										<option value="${paragrapheCondition.numParag}" <c:if
											test="${(not empty oldChoice.condition) and (oldChoice.condition.numParag == paragrapheCondition.numParag)}">
											selected="selected"</c:if>>${paragrapheCondition.titre}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						</table>
						</c:forEach>
						</div>
						<hr>
						<table id="choice" class="formulaire">
						</table>

						</div>
						</div>
			<div id="divBouton">
				<input type="button" value="Valider le paragraphe" onclick="submitForm();">
				<input type="number" id="nbOldChoix" name="nbOldChoix" value="${compteurChoix}" style="display: none">
				<input class="button" onclick="submitSave();" value="Sauvegarder la rédaction du paragraphe"
					type="button" />
				<a href="write_paragraph?action=erase&idHist=${idHist}&numParag=${numParag}">Annuler la rédaction du
					paragraphe</a>
			</div>
			</form>
			<c:if test="${nbChoix == 0}">
				<script type="text/javascript">
					window.onload = function () {
						hideChoice();
					}
				</script>
			</c:if>
			</c:if>



			<c:if test="${modify != null}">
				<form method="post" id="formModify"
					action="write_paragraph?idHist=${idHist}&numParag=${numParag}&titre=${titreParag}&action=modify"
					accept-charset="UTF-8">
					<p>
						<br>
						<br>
						Nom du paragraphe :<input type="text" name="title" id="titreParagraphe" value="${titreParag}"
							disabled /><br>
						Paragraphe :<TEXTAREA name="story" id="story" rows=4 cols=80 <c:if
							test="${not author}"> disabled </c:if>>${texte}</TEXTAREA><br>
					<div id="listeDesChoix">
						<p>Nombre de nouveaux choix :</p>
						<br>
						<input type="number" id="nbChoix" name="nbChoix" value="0" min="0" max="100" required>
						<input type="button" value="Afficher les choix" onclick="changeChoice();">
						<br>
						<hr>
						<div id="blocDesChoix">
							<div id="divAncienChoix">
								<c:set var="compteurChoix" value="0" scope="page" />
								<c:forEach items="${ancienChoix}" var="oldChoice">
									<c:set var="compteurChoix" value="${compteurChoix + 1}" scope="page" />
									<div class='introChoix'>
										<table>
											<tr>
												<th>Ancien choix ${compteurChoix}</th>
											</tr>
										</table>
									</div>
									<table id="tableOldChoice${compteurChoix}" class="formulaire">
										<tr class="formulaire">
											<td class="formulaire">
												<input type="text" id="oldChoix${compteurChoix}"
													name="oldChoix${compteurChoix}" value="${oldChoice.titre}"
													disabled />
											</td>
										</tr>
										<c:if test="${not empty oldChoice.condition}">
											<tr>
												<td>
													<p> Paragraphe conditionnel : </p>
												</td>
											</tr>
											<tr>
												<td>
													<input type="text" id="oldChoixCond${compteurChoix}"
														name="oldChoix${compteurChoix}"
														value="${oldChoice.condition.titre}" disabled />
												</td>
											</tr>
										</c:if>
									</table>
								</c:forEach>
							</div>
							<hr>
							<table id="choice" class="formulaire">
							</table>
						</div>
					</div>
					<div id="divBouton">
						<c:if test="${author == null}">
							<input type="button" value="Valider les modifications" onclick="submitModify(0);">
						</c:if>
						<c:if test="${author != null}">
							<input type="button" value="Valider les modifications" onclick="submitModify(1);">
						</c:if>
						<input type="button" value="Annulez les modifications" onclick="deleteModif();">
					</div>
				</form>
			</c:if>
			</c:if>
		</body>