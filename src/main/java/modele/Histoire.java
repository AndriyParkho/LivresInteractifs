package modele;

import java.sql.Date;

public class Histoire {
	private int id;
	private String titre;
	private Date datePubli;
	private Utilisateur auteur; // Ou juste int idAuteur ?
	private Paragraphe firstParag; // Ou un arbre de paragraphe qui constitue toute l'histoire ?
	
	public Histoire(int id, String titre, Date datePubli, Utilisateur auteur, Paragraphe firstParag) {
		super();
		this.id = id;
		this.titre = titre;
		this.datePubli = datePubli;
		this.auteur = auteur;
		this.firstParag = firstParag;
	}
	
	public int getId() {
		return id;
	}
	public String getTitre() {
		return titre;
	}
	public Date getDatePubli() {
		return datePubli;
	}
	public Utilisateur getAuteur() {
		return auteur;
	}
	public Paragraphe getFirstParag() {
		return firstParag;
	}
	
	
}
