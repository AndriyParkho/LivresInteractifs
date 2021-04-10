    function displayInvite(){
    	document.getElementById('listPersons').style.display = 'block';
    }
    
    function hideInvite(){
    	document.getElementById('listPersons').style.display = 'none';
    	document.getElementById('listAuthors').selectedIndex = -1;
    }
    
    function displayChoice(){
    	document.getElementById('listeDesChoix').style.display = 'block';
    }
    
    function hideChoice(){
    	document.getElementById('listeDesChoix').style.display = 'none';
    	document.getElementById('nbChoix').value = 0;
        changeChoice();
    }
    
    function EraseSelect(){
            document.getElementById('listAuthors').selectedIndex = -1;
    }
    
    var nbChoixJs = 0;
    
    function changeChoice(){
    	document.getElementById("errorMessage").innerHTML ="";
    	var newNbChoice = document.getElementById("nbChoix").value;
    	var div = document.getElementById("choice");
        while (newNbChoice > nbChoixJs){
        	nbChoixJs++;
        	div.insertAdjacentHTML('beforeend', '<tr class="formulaire"><td class="formulaire"><input type="text"  id="choix'+nbChoixJs.toString()+'" value="Choix numéro '+nbChoixJs.toString()+'" required/></td></tr>');
        }
    	 while (newNbChoice < nbChoixJs){
    		nbChoixJs--;
 	        div.deleteRow(nbChoixJs);
 	      } 
    }
    
    function checkRequired(){
    	if(document.getElementById("title").value == ""){
    		return false;
    	}
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
    	var test = document.getElementById("choix" + nbChoiceSupposed.toString());
    	if(nbChoiceSupposed == 0){
    		if(test == null){
    			return true;
    		}
    		else{
    			return false;
    		}
    	}
    	else{
    		if(test == null){
    			return false;
    		}
    		else{
    			return true;
    		}
    	}
    }
    
    function submitForm(){
    	if(checkRequired()){
    		if(checkChoice()){
    			document.getElementById("formCreate").submit();
    		}
    		else{
    			document.getElementById("errorMessage").innerHTML = "Appuyez sur le bouton 'Affichez les choix' pour mettre à jour vos choix.";
    		}
    	}
    	else{
    		document.getElementById("errorMessage").innerHTML = "Tous les champs doivent être remplis.";
    	} 
    } 