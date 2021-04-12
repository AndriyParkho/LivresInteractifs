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
	
	@Override
	public String toString() {
		return Integer.toString(numParag);
	}
}
