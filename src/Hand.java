import java.util.ArrayList;

public class Hand {
	
	private String face;
	private String suit;
	private int rank;
	private boolean isBower;
	
	public Hand(String face, String suit, int rank)
	{
		this.face = face;
		this.suit = suit;
		this.rank = rank;
	}
	
	public Hand(String face, String suit, int rank, boolean isBower)
	{
		this.face = face;
		this.suit = suit;
		this.rank = rank;
		this.isBower = isBower;
	}
	
	public boolean isBower() {
		return isBower;
	}

	public void setBower(boolean isBower) {
		this.isBower = isBower;
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
		rank += n;
//		System.out.println("Rank now: " + face + suit + " " + rank);
	}
	
	public String getCard()
	{
		return face + suit;
	}
	
	
}
