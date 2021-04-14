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
    
    
     