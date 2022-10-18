import java.util.ArrayList;

public class Deck {

	private String face;
	private String suit;
	private int rank;
	
	public Deck(String face, String suit, int rank)
	{
		this.face = face;
		this.suit = suit;
		this.rank = rank;
	}

	public String getFace() {
		return face;
	}

	public String getSuit() {
		return suit;
	}

	public int getRank() {
		return rank;
	}
	
	public void setRank(int n)
	{
		rank = n;
	}
	
	
	
	
}
