import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

	private String name;
	private int seatPosition;
	private int team;
	private int points;
	
	private ArrayList<Hand> currentHand = new ArrayList<>();
	private ArrayList<Hand> playableCards = new ArrayList<>();
	private Hand card;


	public Player(String name, int team)
	{
		this.name = name;
		this.team = team;
	}

	public Player(String name, int team, int seatPosition, ArrayList<Hand> cards)
	{
		this.name = name;
		this.team = team;
		this.seatPosition = seatPosition;

		this.currentHand = cards;
	}

	public void dealCard(String face, String suit, int rank)
	{
		card = new Hand(face, suit, rank);
		currentHand.add(card);
	}

	public void setTrumpSuit(String suit)
	{
//		System.out.print(name + "\t");

		for(int i = 0; i < currentHand.size(); i++)
		{
			if(currentHand.get(i).getSuit().equalsIgnoreCase(suit))
			{

				if(currentHand.get(i).getFace().equalsIgnoreCase("j"))
				{
					currentHand.get(i).setRank(100);
				} 
				else
				{
					currentHand.get(i).setRank(50);

				}
			}	
			else
			{
				if(currentHand.get(i).getFace().equalsIgnoreCase("j"))
				{
					if(currentHand.get(i).getSuit().equalsIgnoreCase(findSecondBower(suit.toLowerCase())))
					{
//						System.out.println("Bower: " + currentHand.get(i).getCard());
						currentHand.get(i).setRank(80);
						currentHand.get(i).setBower(true);
					}
					
					
				}
			}
		}
	}

	public String findSecondBower(String suit)
	{
		switch(suit)
		{
			case "c":
				return "s";
				
			case "s":
				return "c";
				
			case "d":
				return "h";
				
			case "h":
				return "d";
		}
		
		return suit;
		
		
	}

	public void swapCard(String card)
	{
		for(int i = 0; i < currentHand.size(); i++)
		{
			if(currentHand.get(i).getCard().equalsIgnoreCase(card))
			{
				removeCardFromHand(i);
			}
		}
	}

	public String chooseSuit(String currentSuit)
	{
		String answer = "no";
		int suitCount = 0;

		for(int i = 0; i < currentHand.size(); i++)
		{
			if(currentHand.get(i).getSuit().equalsIgnoreCase(currentSuit))
			{
				suitCount++;
			}
		}


		if(suitCount >= 2)
		{
			answer = "yes";
		}



		return answer;

	}

	public void findMatchingJack()
	{

	}

	public String getName()
	{
		return name;
	}

	public int getTeam()
	{
		return team;
	}

	public void addPoint()
	{
		this.points++;

		//		System.out.println(points);
		//return win true or false
	}

	public int getPoints()
	{
		return points;
	}

	public int getSeatPosition()
	{
		return seatPosition;
	}

	public void setSeatPosition(int turnPosition)
	{
		this.seatPosition = turnPosition;
	}

	public Hand playCard()
	{
		Hand temp = null;
		List<Integer> selection = new ArrayList<>();
		int currentSelection = -1, index = -1;

		//getting the 2 top cards to play first
		for(int i = 0; i < currentHand.size(); i++)
		{
			selection.add(currentHand.get(i).getRank());
		}

		Collections.sort(selection);

		if(selection.size() > 1)
		{
			List<Integer> top2 = new ArrayList<>(selection.subList(selection.size() -2, 
					selection.size()));

			Collections.shuffle(top2);

			currentSelection = top2.get(0);

			//finding card with rank
			for(int i = 0; i < currentHand.size(); i++)
			{
				if(currentHand.get(i).getRank() == currentSelection)
				{					
					System.out.println(name + " is playing " + currentHand.get(i).getCard());
					temp = new Hand(currentHand.get(i).getFace(), 
							currentHand.get(i).getSuit(), 
							currentHand.get(i).getRank(),
							currentHand.get(i).isBower());

					currentHand.remove(i);

					return temp;
				}
			}

		}
		else
		{
			System.out.println(name + " is playing " + currentHand.get(0).getCard());
			temp = new Hand(currentHand.get(0).getFace(), 
					currentHand.get(0).getSuit(), 
					currentHand.get(0).getRank(),
					currentHand.get(0).isBower());

			currentHand.remove(0);
		}

		return temp;	 
	}

	public String selectCard(int i)
	{
		return currentHand.get(i).getFace() + currentHand.get(i).getSuit();
	}

	public String getSuit(int i)
	{
		return currentHand.get(i).getSuit();
	}

	public int findIndex(int playingCurrent)
	{
		int index = 0; 

		for(int i = 0; i < currentHand.size(); i++)
		{
			if(playableCards.get(playingCurrent).getCard().equalsIgnoreCase(currentHand.get(i).getCard()))
			{
				index = i;
			}
		}

		return index;

	}

	public Hand playStrongest()
	{	
		int currentStrongest = 0;
		int playingCurrent = 0; 

		for(int i = 0; i < playableCards.size(); i++)
		{
			//System.out.println(name + " is CHECKING.. " + playableCards.get(i).getCard());
			int tempStrongest = playableCards.get(i).getRank();

			if(tempStrongest > currentStrongest)
			{
				currentStrongest = tempStrongest;
				playingCurrent = i;

				//System.out.println("Current Strongest: " + currentStrongest);
			}
		}

		System.out.println(name + " is playing " + playableCards.get(playingCurrent).getCard() + " rank: " + playableCards.get(playingCurrent).getRank());
		Hand temp = new Hand(playableCards.get(playingCurrent).getFace(), playableCards.get(playingCurrent).getSuit(), playableCards.get(playingCurrent).getRank(),
				playableCards.get(playingCurrent).isBower());
		currentHand.remove(findIndex(playingCurrent));

		playableCards.clear();


		return temp;
	}

	public Hand playWeakest()
	{
		int currentWeakest = 100;
		int playingCurrent = 0;

		for(int i = 0; i < playableCards.size(); i++)
		{
			//System.out.println(name + " is CHECKING.. " + currentHand.get(i).getCard());
			int tempWeakest = playableCards.get(i).getRank();

			if(currentWeakest > tempWeakest)
			{
				currentWeakest = tempWeakest;

				playingCurrent = i;
				//System.out.println("Current Weakest: " + currentWeakest);
			}



		}

		System.out.println(name + " is playing " + playableCards.get(playingCurrent).getCard() + " rank: " + playableCards.get(playingCurrent).getRank());
		currentHand.remove(findIndex(playingCurrent));
		
		Hand temp = new Hand(playableCards.get(playingCurrent).getFace(), playableCards.get(playingCurrent).getSuit(), playableCards.get(playingCurrent).getRank(),
				playableCards.get(playingCurrent).isBower());

		playableCards.clear();

		return temp;	
	}

	public Hand playWeakestOffSuit(String currentSuit)
	{
		int currentWeakest = 100;
		int playingCurrent = 0;

		for(int i = 0; i < currentHand.size(); i++)
		{
			//System.out.println(name + " is CHECKING.. " + currentHand.get(i).getCard());
			int tempWeakest = currentHand.get(i).getRank();

			if(currentWeakest > tempWeakest && !currentHand.get(playingCurrent).getSuit().equalsIgnoreCase(currentSuit))
			{
				currentWeakest = tempWeakest;

				playingCurrent = i;
				//System.out.println("Current Weakest: " + currentWeakest);
			}				
		}

		System.out.println(name + " is playing " + currentHand.get(playingCurrent).getCard() + " rank: " + currentHand.get(playingCurrent).getRank());
		Hand temp = new Hand(currentHand.get(playingCurrent).getFace(), currentHand.get(playingCurrent).getSuit(), currentHand.get(playingCurrent).getRank());
		currentHand.remove(playingCurrent);

		return temp;	
	}

	public String placeCard(String currentSuit)
	{
		int currentWeakest = 100;
		int playingCurrent = 0;

		for(int i = 0; i < currentHand.size(); i++)
		{
			//System.out.println(name + " is CHECKING.. " + currentHand.get(i).getCard());
			int tempWeakest = currentHand.get(i).getRank();

			if(currentWeakest > tempWeakest && !currentHand.get(playingCurrent).getSuit().equalsIgnoreCase(currentSuit))
			{
				currentWeakest = tempWeakest;

				playingCurrent = i;
				//System.out.println("Current Weakest: " + currentWeakest);
			}				
		}


		String temp = currentHand.get(playingCurrent).getFace() + currentHand.get(playingCurrent).getSuit();
		System.out.println("Placing: " + temp);
		currentHand.remove(playingCurrent);

		return temp;	
	}

	public boolean hasSuit(String suit, String playTrump)
	{
		boolean hasSuit = false;

		playableCards.clear();

//				System.out.println(playTrump + " Cards: ");
		for(int i = 0; i < currentHand.size(); i++)
		{
			if(currentHand.get(i).getSuit().equalsIgnoreCase(suit) && !currentHand.get(i).isBower())
			{
				playableCards.add(currentHand.get(i));
				hasSuit = true;

//								System.out.println(playableCards.get(playableCards.size() - 1).getCard());

			}
			else if(playTrump.equalsIgnoreCase("trump") && currentHand.get(i).isBower())
			{
				playableCards.add(currentHand.get(i));
				hasSuit = true;
			}
		}

		return hasSuit;
	}

	//	public boolean canTrump(String suit)
	//	{
	//		boolean canTrump = false;
	//		
	//		playableCards.clear();
	//		
	//		System.out.println("Trump Cards: ");
	//		for(int i = 0; i < currentHand.size(); i++)
	//		{
	//			if(currentHand.get(i).getSuit().equalsIgnoreCase(suit))
	//			{
	//				playableCards.add(currentHand.get(i));
	//				canTrump = true;
	//				
	//				System.out.println(playableCards.get(playableCards.size() - 1).getCard());
	//				
	//			}
	//		}
	//		
	//		return canTrump;
	//	}

	public boolean canBeat(int rank)
	{
		for(int i = 0; i < playableCards.size(); i++)
		{
			if(playableCards.get(i).getRank() > rank)
			{
				return true;
			}
		}

		return false;
	}

	public int highestRankOnSuit(String suit)
	{
		int currentHighest = -1;

		for(int i = 0; i < currentHand.size(); i++)
		{
			int tempHighest = currentHand.get(i).getRank();

			if(tempHighest > currentHighest)
			{
				currentHighest = tempHighest;
			}
		}

//		System.out.println("Can beat with rank: " + currentHighest);
		return currentHighest;
	}

	public void pickUp(String face, String suit, int rank)
	{
		card = new Hand(face, suit, rank);
		addCardToHand(card);

		System.out.println(face + suit + " has been picked up..");
		System.out.println(suit + "'s are!\n");
	}

	public int getCardRank(int i)
	{
		return currentHand.get(i).getRank();
	}

	public void addCardToHand(Hand card)
	{
		this.currentHand.add(card);
	}

	public void removeCardFromHand(int i)
	{
		currentHand.remove(i);
	}

	public int getHandSize()
	{
		return currentHand.size();
	}

	public ArrayList<Hand> getHand()
	{
		return currentHand;
	}

	public void showHand()
	{
		for(int i = 0; i < currentHand.size(); i++)
		{
			System.out.print(currentHand.get(i).getFace() + currentHand.get(i).getSuit() + "\t");
		}

	}

	public Hand playCard(int i)
	{
		return currentHand.get(i);
	}



}
