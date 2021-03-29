package modele;

public class Utilisateur {
	private int id;
	private String nom;
	private String prenom;
	private String email;
	private String password;
	// private HashMap<integer, ArrayList<Paragraphe>> = new HashMap<integer, ArrayList<Paragraphe>>(); pour l'historique ? A la place de List peut-être un Tree 
	
	public Utilisateur(int id, String nom, String prenom, String email, String password) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	
	
}
