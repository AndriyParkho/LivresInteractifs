package modele;

import java.sql.Date;

public class Histoire {
	private int id;
	private String titre;
	private Date datePubli;
	private int idAuteur;
	private Paragraphe firstParag; // Ou un arbre de paragraphe qui constitue toute l'histoire ?
    private String[] auteurs; 
	
	public Histoire(int id, String titre, Date datePubli, int idAuteur) {
		super();
		this.id = id;
		this.titre = titre;
		this.datePubli = datePubli;
		this.idAuteur = idAuteur;
	}
	
	public Histoire(int id, String titre, Date datePubli, int idAuteur, Paragraphe firstParag) {
		super();
		this.id = id;
		this.titre = titre;
		this.datePubli = datePubli;
		this.idAuteur = idAuteur;
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
	public int getIdAuteur() {
		return idAuteur;
	}
	public Paragraphe getFirstParag() {
		return firstParag;
	}

	public void setFirstParag(Paragraphe firstParag) {
		this.firstParag = firstParag;
	}
        
        public String[] getAuteurs(){
            return auteurs;
        }
        
        public void setAuteurs(String[] auteurs){
            this.auteurs = auteurs;
        }
	
	
}
