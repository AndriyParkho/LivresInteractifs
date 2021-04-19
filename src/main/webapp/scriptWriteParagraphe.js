	var nbChoixJs = 0;
	
    function choixRedige(numChoix, bool){
		 var choix = document.getElementById("choix" + numChoix.toString());
		 var choixRedige = document.getElementById("paragrapheRedige" + numChoix.toString());
		 if((bool == 1) && (choix.disabled == false)){
		 	choix.disabled = true;
			 choixRedige.disabled = false;
		 }
		 else if ((bool == 0) && (choix.disabled == true)){
		 	choix.disabled = false;
			choixRedige.disabled = true;
		 }
    }
	
	function choixConditionnel(numChoix, bool){
		var choix = document.getElementById("paragrapheCondition" + numChoix.toString());
		 if((bool == 0) && (choix.disabled == false)){
		 	choix.disabled = true;
		 }
		 else if ((bool == 1) && (choix.disabled == true)){
		 	choix.disabled = false;
		 }
	}
    
    function displayChoice(){
    	document.getElementById('nbChoix').value = 1;
        changeChoice();
    	document.getElementById('listeDesChoix').style.display = 'block';
    }
    
    function hideChoice(){
    	document.getElementById('listeDesChoix').style.display = 'none';
    	document.getElementById('nbChoix').value = 0;
        changeChoice();
        resetChoixRedige();
    }
    
	function insertHtmlChoice(div){
		div.insertAdjacentHTML('beforeend', '<tr><th>Choix'+ nbChoixJs.toString() + '</th></tr>\
			<tr class="formulaire"><td class="formulaire"><input type="text"  id="choix'+nbChoixJs.toString()+'" name="choix'+nbChoixJs.toString()+'" value="Choix numéro '+nbChoixJs.toString()+'" required/></td>\
			</tr>\
			<tr class="formulaire">\
			<td>\
        	<p> Choisir un choix déjà rédigé : </p>\
        	<label><input type="radio" onclick="choixRedige(' + nbChoixJs.toString() + ', 1);" name="choixRedige' + nbChoixJs.toString() +'"/>Oui</label>\
			<label><input type="radio" onclick="choixRedige(' + nbChoixJs.toString() + ', 0)" name="choixRedige' + nbChoixJs.toString() +'" checked="checked"/>Non</label>\
        	</td>\
			</tr>');
	}

	function insertHtmlChoice2(div){
		div.insertAdjacentHTML('beforeend', '<tr class="formulaire">\
			<td>\
        	<p> Choisir un paragraphe conditionnel : </p>\
        	<label><input type="radio" onclick="choixConditionnel(' + nbChoixJs.toString() +', 1);" name="choixConditionnel' + nbChoixJs.toString() +'"/>Oui</label>\
			<label><input type="radio" onclick="choixConditionnel(' + nbChoixJs.toString() +', 0)" name="choixConditionnel' + nbChoixJs.toString() +'" checked="checked" id="isNotConditionnal' + nbChoixJs.toString() +'" />Non</label>\
        	</td>\
			</tr>');
	}

	function insertCondition(div){
		var exemple = document.getElementById("paragrapheCondition");
		var paragCond = exemple.cloneNode(true);
		paragCond.id = "paragrapheCondition" + nbChoixJs.toString();
		paragCond.name = "paragrapheCondition" + nbChoixJs.toString();
		div.appendChild(paragCond);
	}

	function insertRedige(div){
		var exemple = document.getElementById("paragrapheRedige");
		var paragCond = exemple.cloneNode(true);
		paragCond.id = "paragrapheRedige" + nbChoixJs.toString();
		paragCond.name = "paragrapheRedige" + nbChoixJs.toString();
		div.appendChild(paragCond);
	}
	
    function changeChoice(){
    	var newNbChoice = document.getElementById("nbChoix").value;
    	var div = document.getElementById("choice");
        while (newNbChoice > nbChoixJs){
        	nbChoixJs++;
        	insertHtmlChoice(div);
			insertRedige(div);
			insertHtmlChoice2(div);
			insertCondition(div);
        }
        var choix;
    	 while (newNbChoice < nbChoixJs){
    		choix = document.getElementById("choix" + nbChoixJs.toString());
    		nbChoixJs--;
			div.removeChild(div.lastChild);
			div.removeChild(div.lastChild);
			div.removeChild(div.lastChild);
			div.removeChild(div.lastChild);
 	      } 
    }
    
	function checkRequired(){
    	if(document.getElementById("story").value == ""){
    		return false;
    	}
    	else{
			return true;
		}
    } 
	
	function submitForm(){
    	if(checkRequired()){
			if(document.getElementById("isConclusionId").checked){
				document.getElementById("formCreate").submit();
			} else{
				if(checkChoice()){
					if(document.getElementById("nbOldChoix").value > 0){
						if(checkChoiceRedigeOld()){
							if(checkSupress()){
								if(checkUnconditionnalOld()){
									document.getElementById("formCreate").submit();
								}
								else{
									alert("Au minimum un de vos choix doit être inconditionnel.")
								}
							} else{
								alert("En prenant en compte vos suppression, vous n'avez plus de choix. Mettez un choix valide ou passez votre paragraphe en conclusion.");
							}
    					}
						else{
							alert("Vous avez sélectionné deux fois ou plus le même choix rédigé, ce n'est pas autorisé. Pensé à vérifier vos anciens choix vérifés !");
						}
					}else{
						if(checkChoiceRedige()){
							if(checkUnconditionnal()){
									document.getElementById("formCreate").submit();
								}
								else{
									alert("Au minimum un de vos choix doit être inconditionnel.")
								}
    					}
						else{
							alert("Vous avez sélectionné deux fois ou plus le même choix rédigé, ce n'est pas autorisé.");
						}
					}
    			}
    			else{
    				alert("Appuyez sur le bouton 'Affichez les choix' pour mettre à jour vos choix.");
    			}
			}
    	}
    	else{
    		alert("Tous les champs doivent être remplis.");
    	} 
    }
    
    function checkChoice(){
    	var nbChoiceSupposed = document.getElementById("nbChoix").value;
    	var testChoix = document.getElementById("choix" + nbChoiceSupposed.toString());
    	if(nbChoiceSupposed == 0){
    		if(testChoix == null){
    			return true;
    		}
    		else{
    			return false;
    		}
    	}
    	else{
    		if(testChoix == null){
    			return false;
    		}
    		else{
    			return true;
    		}
    	}
    }
    
	function setChoice(){
		nbChoixJs = document.getElementById("nbChoix").value;
	}

    function checkChoiceRedige(){
    	let listOfChoice = [];
		let choice;
		let value;
		for (let i = 1; i <= nbChoixJs; i++) {
			choice = document.getElementById('paragrapheRedige' + i.toString());
			if(choice.disabled == false){
				value = choice.value;
				if(listOfChoice.includes(value)){
					return false;
				} else{
					listOfChoice.push(value);
				}
			}
		}
		return true;
    }

	function checkChoiceRedigeOld(){
    	let listOfChoice = [];
		let choice;
		let value;
		for (let i = 1; i <= nbChoixJs; i++) {
			choice = document.getElementById('paragrapheRedige' + i.toString());
			if(choice.disabled == false){
				value = choice.value;
				if(listOfChoice.includes(value)){
					return false;
				} else{
					listOfChoice.push(value);
				}
			}
		}
		let buttonSupress;
		let nbOldChoice = document.getElementById("nbOldChoix").value;
		for (let j = 1; j <= nbOldChoice; j++) {
			buttonSupress = document.getElementById("supressOldChoix" + j.toString());
			if(buttonSupress.checked){
				choice = document.getElementById('oldParagrapheRedige' + j.toString());
				if(choice.disabled == false){
					value = choice.value;
					if(listOfChoice.includes(value)){
						return false;
					} else{
						listOfChoice.push(value);
					}
				}
			}
				
		}
		return true;
    }

    function submitSave(){
		if(document.getElementById("isConclusionId").checked){
			var form = document.getElementById('formCreate');
			form.action += "&save=true" ;
			form.submit();
		} else{
			if(checkChoice()){
				if(document.getElementById("nbOldChoix").value > 0){
					if(checkChoiceRedigeOld()){
						if(checkSupress()){
							if(checkUnconditionnalOld()){
								var form = document.getElementById('formCreate');
								form.action += "&save=true" ;
								form.submit();
							}
							else{
								alert("Au minimum un de vos choix doit être inconditionnel.")
							}
						} else{
							alert("En prenant en compte vos suppression, vous n'avez plus de choix. Mettez un choix valide ou passez votre paragraphe en conclusion.");
						}
					}
					else{
						alert("Vous avez sélectionné deux fois ou plus le même choix rédigé, ce n'est pas autorisé. Pensez à vérifier vos anciens choix vérifés !");
					}
				}else{
					if(checkChoiceRedige()){
						if(checkUnconditionnal()){
								var form = document.getElementById('formCreate');
								form.action += "&save=true" ;
								form.submit();
							}
							else{
								alert("Au minimum un de vos choix doit être inconditionnel.")
							}
					}
					else{
						alert("Vous avez sélectionné deux fois ou plus le même choix rédigé, ce n'est pas autorisé.");
					}
				}
			}
			else{
				alert("Appuyez sur le bouton 'Affichez les choix' pour mettre à jour vos choix.");
			}
		}
    }
    
    function oldChoixRedige(numChoix, bool){
		 var choix = document.getElementById("oldChoix" + numChoix.toString());
		 var choixRedige = document.getElementById("oldParagrapheRedige" + numChoix.toString());
		 if((bool == 1) && (choix.disabled == false)){
		 	choix.disabled = true;
			 choixRedige.disabled = false;
		 }
		 else if ((bool == 0) && (choix.disabled == true)){
		 	choix.disabled = false;
			choixRedige.disabled = true;
		 }
    }
	
	function oldChoixConditionnel(numChoix, bool){
		var choix = document.getElementById("oldParagrapheCondition" + numChoix.toString());
		 if((bool == 0) && (choix.disabled == false)){
		 	choix.disabled = true;
		 }
		 else if ((bool == 1) && (choix.disabled == true)){
		 	choix.disabled = false;
		 }
	}

	function lock(num, bool){
		var tableChoix = document.getElementById("tableOldChoice" + num.toString());
		if(bool){
			tableChoix.style.display = 'none';
		} else{
			tableChoix.style.display = 'block';
		}
	}

    function checkSupress(){
		if(!(document.getElementById("isConclusionId").checked)){
			var nbChoice = document.getElementById("nbChoix").value;
			if(nbChoice == 0){
				let nbOldChoice = document.getElementById("nbOldChoix").value;
				for (let j = 1; j <= nbOldChoice; j++) {
					buttonSupress = document.getElementById("supressOldChoix" + j.toString());
					if(buttonSupress.checked){
						return true;
					}
				}
				return false;
			} 
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}

	function checkUnconditionnal(){
		for (let i = 1; i <= nbChoixJs; i++) {
			if(document.getElementById('isNotConditionnal' + i.toString()).checked){
				return true;
			}
		}
		return false;
	}

	function checkUnconditionnalOld(){
		for (let i = 1; i <= nbChoixJs; i++) {
			if(document.getElementById('isNotConditionnal' + i.toString()).checked){
				return true;
			}
		}
		let buttonSupress;
		let nbOldChoice = document.getElementById("nbOldChoix").value;
		for (let j = 1; j <= nbOldChoice; j++) {
			buttonSupress = document.getElementById("supressOldChoix" + j.toString());
			if(buttonSupress.checked){
				if(document.getElementById('oldIsNotConditionnal' + i.toString()).checked){
					return true;
				}
			}
				
		}
		return false;
	}