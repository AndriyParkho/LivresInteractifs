package modele;

import java.util.ArrayList;

public class Paragraphe {
	private int idHist;
	private int numParag;
	private String titre;
	private String texte;
	private boolean valide;
	private int nbChoix;
	private ArrayList<Paragraphe> paragSuiv;
	private int idWritter;
	
	public Paragraphe(int idHist, int numParag, String titre, String texte, boolean valide, int nbChoix,
			ArrayList<Paragraphe> paragSuiv, int idWritter) {
		super();
		this.idHist = idHist;
		this.numParag = numParag;
		this.titre = titre;
		this.texte = texte;
		this.valide = valide;
		this.nbChoix = nbChoix;
		this.paragSuiv = paragSuiv;
		this.idWritter = idWritter;
	}
	
	public Paragraphe(int idHist, int numParag, String titre, String texte, int nbChoix) {
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
	public int getNbChoix() {
		return nbChoix;
	}
	public ArrayList<Paragraphe> getParagSuiv() {
		return paragSuiv;
	}
	public int getIdWritter() {
		return idWritter;
	}
	
	
}
