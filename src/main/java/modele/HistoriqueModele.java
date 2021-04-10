package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueModele {
	private Map<Integer, ArrayList<Integer>> historique;
	
	public HistoriqueModele() {
		historique = new HashMap<Integer, ArrayList<Integer>> ();
	}
	
	public void addParagraph(int histId, int paragraphId) {
		ArrayList<Integer> listParagraph = historique.get(histId);
		if(listParagraph  == null) {
			listParagraph = new ArrayList<Integer>();
			historique.put(histId, listParagraph);
		}
		listParagraph.add(paragraphId);
	}
	
	public List<Integer> getStories(){
		List<Integer> listStories = new ArrayList<Integer>();
		for(Integer histId : historique.keySet()) {
			listStories.add(histId);
		}
		return listStories;
	}
	
}
