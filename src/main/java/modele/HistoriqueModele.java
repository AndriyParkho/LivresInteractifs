package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueModele {
	private Map<Integer, ArrayList<Paragraphe>> historique;
	
	public HistoriqueModele() {
		historique = new HashMap<Integer, ArrayList<Paragraphe>> ();
	}
	
	public void addParagraph(int histId, Paragraphe paragraph) {
		ArrayList<Paragraphe> listParagraph = historique.get(histId);
		if(listParagraph  == null) {
			listParagraph = new ArrayList<Paragraphe>();
			historique.put(histId, listParagraph);
		}
		listParagraph.add(paragraph);
	}
	
	public List<Integer> getStories(){
		List<Integer> listStories = new ArrayList<Integer>();
		for(Integer histId : historique.keySet()) {
			listStories.add(histId);
		}
		return listStories;
	}
	
}
