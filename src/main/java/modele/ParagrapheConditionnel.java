package modele;

public class ParagrapheConditionnel extends Paragraphe{
	
	private Paragraphe condition;
	
	public ParagrapheConditionnel(int idHist, int numParag, String titre, int valide, Paragraphe condition) {
		super(idHist, numParag, titre, valide);
		this.condition = condition;
	}
	
	public ParagrapheConditionnel(int idHist, int numParag, String titre, int valide) {
		super(idHist, numParag, titre, valide);
		condition = null;
	}

	public Paragraphe getCondition() {
		return condition;
	}
}
