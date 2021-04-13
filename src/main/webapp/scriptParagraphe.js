	var nbChoixRedige = 0;
	var nbChoixJs = 0;
	
    function choixRedige(numChoix, bool){
		 var choix = document.getElementById("choix" + numChoix.toString());
		 if((bool == 1) && (choix.disabled == false)){
		 	choix.disabled = true;
		 	nbChoixRedige++;
		 }
		 else if ((bool == 0) && (choix.disabled == true)){
		 	choix.disabled = false;
		 	nbChoixRedige--;
		 }
		var select = document.getElementById('choixRemplis');
		if((nbChoixRedige == 0) && (select.disabled == false)){
			select.disabled = true;
		}
		else if((nbChoixRedige != 0) && (select.disabled == true)){
			select.disabled = false;
		}
		else if(nbChoixRedige == 1){
			select.size = 1;
			select.multiple = false;
		}
		else if(nbChoixRedige != 0){
			select.size = 4;
			select.multiple = true;
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
    
    function changeChoice(){
    	var newNbChoice = document.getElementById("nbChoix").value;
    	var div = document.getElementById("choice");
        while (newNbChoice > nbChoixJs){
        	nbChoixJs++;
        	div.insertAdjacentHTML('beforeend', '<tr class="formulaire"><td class="formulaire"><input type="text"  id="choix'+nbChoixJs.toString()+'" name="choix'+nbChoixJs.toString()+'" value="Choix numéro '+nbChoixJs.toString()+'" required/></td>\
        	<td>\
        	<p> Choisir un choix déjà rédigé : \
        	<label><input type="radio" onclick="choixRedige(' + nbChoixJs.toString() + ', 1);" name="choixRedige' + nbChoixJs.toString() +'"/>Oui</label>\
			<label><input type="radio" onclick="choixRedige(' + nbChoixJs.toString() + ', 0)" name="choixRedige' + nbChoixJs.toString() +'" checked="checked"/>Non</label>\
        	</td>\
        	</tr>');
        }
        var choix;
    	 while (newNbChoice < nbChoixJs){
    		choix = document.getElementById("choix" + nbChoixJs.toString());
    		if(choix.disabled == true){
    			nbChoixRedige--;
    		}
    		nbChoixJs--;
 	        div.deleteRow(nbChoixJs);
 	      } 
    }
    
    function submitForm(){
    	if(checkRequired()){
    		if(checkChoice()){
    			if(checkChoiceRedige()){
    				document.getElementById("formCreate").submit();
    			}
    		}
    		else{
    			alert("Appuyez sur le bouton 'Affichez les choix' pour mettre à jour vos choix.");
    		}
    	}
    	else{
    		alert("Tous les champs doivent être remplis.");
    	} 
    }
    
    function checkRequired(){
    	if(document.getElementById("titreParagraphe").value == ""){
    		return false;
    	}
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
    
    function checkChoiceRedige(){
    	if(document.getElementById("choixRemplis").disabled == false){
    		var select = document.getElementById("choixRemplis");
			var selected = 0;
			for (var i=0;i < select.options.length;i++){
				if(select[i].selected){
					selected++;
				}
			}
			if(selected == nbChoixRedige){
				return true;
			}
			else{
    			alert("Nombre de choix rédigés à sélectionner : " + nbChoixRedige.toString() + ", nombre actuellement sélectionné : " + selected.toString());
				return false;
			}
    	}
    	else{
    	return true;
    	}
    }
    
    function resetChoixRedige(){
    	nbChoixRedige = 0;
    	var select = document.getElementById('choixRemplis');
    	select.disabled = true;
    	select.size = 1;
		select.multiple = false;
    }
    
    
    
    