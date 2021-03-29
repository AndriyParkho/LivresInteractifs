package modele;

import java.util.ArrayList;

public class Paragraphe {
	private Histoire hist; // Ou int idHist ?
	private int numParag;
	private String texte;
	private boolean valide;
	private int nbChoix;
	private ArrayList<Paragraphe> paragSuiv;
	private Utilisateur writter; // Ou int idWritter ?
	
	public Paragraphe(Histoire hist, int numParag, String texte, boolean valide, int nbChoix,
			ArrayList<Paragraphe> paragSuiv, Utilisateur writter) {
		super();
		this.hist = hist;
		this.numParag = numParag;
		this.texte = texte;
		this.valide = valide;
		this.nbChoix = nbChoix;
		this.paragSuiv = paragSuiv;
		this.writter = writter;
	}
	
	public Histoire getHist() {
		return hist;
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
	public int getNbChoix() {
		return nbChoix;
	}
	public ArrayList<Paragraphe> getParagSuiv() {
		return paragSuiv;
	}
	public Utilisateur getWritter() {
		return writter;
	}
	
	
}
