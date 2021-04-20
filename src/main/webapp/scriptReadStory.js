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
    
    
     function goBack(idHist, index){
		 if (confirm("Etes vous sûr de vouloir retourner en arrière ?")) {
 			 window.location.href = "read_story?idHist=" + idHist.toString() + "&goBackTo=" + index.toString();
		}
	 }