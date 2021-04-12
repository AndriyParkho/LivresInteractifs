package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueModele {
	private Map<Integer, ArrayList<ArrayList<Paragraphe>>> historique;
	
	private boolean isModified;
	
	public HistoriqueModele() {
		historique = new HashMap<Integer, ArrayList<ArrayList<Paragraphe>>> ();
		isModified = false;
	}
	
	public void addParagraph(int histId, ArrayList<Paragraphe> paragraph) {
		ArrayList<ArrayList<Paragraphe>> listParagraph = historique.get(histId);
		if(listParagraph  == null) {
			listParagraph = new ArrayList<ArrayList<Paragraphe>>();
			historique.put(histId, listParagraph);
		}
		listParagraph.add(paragraph);
		setModified(true);
	}
	
	public List<Integer> getStories(){
		List<Integer> listStories = new ArrayList<Integer>();
		for(Integer histId : historique.keySet()) {
			listStories.add(histId);
		}
		return listStories;
	}
	
	public List<ArrayList<Paragraphe>> getTree(int histId){
		return historique.get(histId);
	}
	
	public void addStory(int histId, ArrayList<ArrayList<Paragraphe>> treeOfStory) {
		historique.put(histId, treeOfStory);
	}

	public boolean isModified() {
		return isModified;
	}

	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	
	@Override
	public String toString() {
		return historique.toString();
	}
	
	
}
