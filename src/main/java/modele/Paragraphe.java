package modele;

import java.util.ArrayList;

public class Paragraphe {
	private int idHist;
	private int numParag;
	private String titre;
	private String texte;
	private boolean valide;
	private Integer nbChoix;
	private ArrayList<Paragraphe> paragSuiv = new ArrayList<Paragraphe>();
	private Integer idWritter;
	private Integer conditionParag;
	
	public Paragraphe(int idHist, int numParag, String titre, String texte, Integer nbChoix, Integer conditionParag) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
		this.nbChoix = nbChoix;
		this.conditionParag = conditionParag;
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
	
	public Paragraphe(int idHist, int numParag, String titre, String texte, boolean valide, Integer nbChoix, Integer idWritter, Integer conditionParag) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
		this.valide = valide;
		this.nbChoix = nbChoix;
		this.idWritter = idWritter;
		this.conditionParag = conditionParag;
	}
	
	public Paragraphe(int idHist, int numParag, String titre, String texte, Integer nbChoix) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
		this.nbChoix = nbChoix;
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

	public void addParagSuiv(Paragraphe parag) {
		this.paragSuiv.add(parag);
	}
	
	public Paragraphe findParag(int numParag) {
		if(this.getNumParag() == numParag) return this;
		else if(this.getParagSuiv().size() == 0) return null;
		else {
			Paragraphe result = null;
			Paragraphe temp = null;
			for(Paragraphe parag: this.getParagSuiv()) {
				temp = parag.findParag(numParag);
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
	
	public Integer getConditionParag() {
		return conditionParag;
	}

	@Override
	public String toString() {
		return Integer.toString(numParag);
	}
}
