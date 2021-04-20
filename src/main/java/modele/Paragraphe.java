package modele;

import java.util.ArrayList;
import java.util.HashMap;

public class Paragraphe {
	private int idHist;
	private int numParag;
	private String titre;
	private String texte;
	private boolean valide;
	private Integer nbChoix;
	private ArrayList<Paragraphe> paragSuiv = new ArrayList<Paragraphe>();
	private ArrayList<Integer> condParagSuiv = new ArrayList<Integer>();
	private Integer idWritter;
	private Integer idModifier;
	
	
	public Paragraphe(int idHist, int numParag, String titre, String texte, boolean valide, Integer nbChoix, Integer idWritter, Integer idModifier) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
		this.valide = valide;
		this.nbChoix = nbChoix;
		this.idWritter = idWritter;
		this.idModifier = idModifier;
	}
	
	public Paragraphe(int idHist, int numParag, String titre, String texte, boolean valide, Integer nbChoix, Integer idWritter) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
		this.valide = valide;
		this.nbChoix = nbChoix;
		this.idWritter = idWritter;
	}

	public Paragraphe(int idHist, int numParag, String titre, String texte, Integer nbChoix) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
		this.nbChoix = nbChoix;
	}
	
	public Paragraphe(int idHist, int numParag, String titre, int valide) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.valide = (valide == 1);
	}
	
	public Paragraphe(int idHist, int numParag, String titre, String texte) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
	}
	
	public Paragraphe(int idHist, int numParag, String titre) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
	}
	
	public Paragraphe(int idHist, int numParag) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
	}
	
	public int getIdHist() {
		return idHist;
	}
	public int getNumParag() {
		return numParag;
	}
	public String getTexte() {
		return texte;
	}
	public boolean isValide() {
		return valide;
	}
	public Integer getNbChoix() {
		return nbChoix;
	}
	public ArrayList<Paragraphe> getParagSuiv() {
		return paragSuiv;
	}
	public Integer getIdWritter() {
		return idWritter;
	}
	
	public String getTitre() {
		return titre;
	}

	public Integer getIdModifier() {
		return idModifier;
	}

	public void addParagSuiv(Paragraphe parag) {
		this.paragSuiv.add(parag);
		this.condParagSuiv.add(null);
	}
	
	
	public void addParagSuiv(Paragraphe parag, Integer conditionParag) {
		this.paragSuiv.add(parag);
		this.condParagSuiv.add(conditionParag);
	}
	
	public Paragraphe findParag(int numParag, HashMap<Integer, Paragraphe> dicoParag) {
		if(this.getNumParag() == numParag) return this;
		else if(dicoParag.get(this.getNumParag()) != null) return null;
		else if(this.getParagSuiv().size() == 0) return null;
		else {
			dicoParag.put(this.getNumParag(), this);
			Paragraphe result = null;
			Paragraphe temp = null;
			for(Paragraphe parag: this.getParagSuiv()) {
				temp = parag.findParag(numParag, dicoParag);
				if(temp != null) {
					result = temp;
				}
			}
			return result;
		}
	}
	
	public Paragraphe getParagSuiv(int numParag) {
		for(Paragraphe para : paragSuiv) {
			if(para.getNumParag() == numParag) {
				return para;
			}
		}
		return null;
	}
	
	public ArrayList<Integer> getCondParagSuiv() {
		return condParagSuiv;
	}

	@Override
	public String toString() {
		return Integer.toString(numParag);
	}
}
