import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {


	public static Player player; 
	public static Player AI1;
	public static Player AI2;
	public static Player AI3;
	public static ArrayList<Player> playerOrder = new ArrayList<>();

	public static Deck[] deck = new Deck[24];
	public static ArrayList<Deck> newDeck = new ArrayList<>();
	public static ArrayList<Hand> currentTrick = new ArrayList<>();
	public static int deckCount;
	public static int cardLimit = 5;
	public static String trumpSuit;
	public static String currentSuit;
	public static Scanner sc;


	public static void initPlayers()
	{		
		player = new Player("Player", 1);
		playerOrder.add(player);

		AI1 = new Player("AI1", 2);
		playerOrder.add(AI1);

		AI2 = new Player("AI2", 1);
		playerOrder.add(AI2);

		AI3 = new Player("AI3", 2);
		playerOrder.add(AI3);

		Random rand = new Random();
		int start = rand.nextInt(playerOrder.size());

		rotateTurn(start);

	}

	public static void shuffle()
	{
		deckCount = 0;
		//face, suit, rank
		Object[][] tempDeck = {
				{"9", "C", 0},
				{"9", "S", 0},
				{"9", "D", 0},
				{"9", "H", 0},
				{"10", "C", 1},
				{"10", "S", 1},
				{"10", "D", 1},
				{"10", "H", 1},
				{"J", "C", 2},
				{"J", "S", 2},
				{"J", "D", 2},
				{"J", "H", 2},
				{"Q", "C", 3},
				{"Q", "S", 3},
				{"Q", "D", 3},
				{"Q", "H", 3},
				{"K", "C", 4},
				{"K", "S", 4},
				{"K", "D", 4},
				{"K", "H", 4},
				{"A", "C", 5},
				{"A", "S", 5},
				{"A", "D", 5},
				{"A", "H", 5}
		};

		for(int n = 0; n < tempDeck.length; n++)
		{
			deck[deckCount] = new Deck(tempDeck[n][0].toString(), tempDeck[n][1].toString(), Integer.parseInt(tempDeck[n][2].toString()));
			newDeck.add(deck[deckCount]);
			deckCount++;
		}

		Collections.shuffle(newDeck);

	}

	public static void deal()
	{
		for(int i = 0; i < cardLimit; i++)
		{
			player.dealCard(newDeck.get(i).getFace(), newDeck.get(i).getSuit(), newDeck.get(i).getRank());
			newDeck.remove(i);
		}

		for(int i = 0; i < cardLimit; i++)
		{
			AI1.dealCard(newDeck.get(i).getFace(), newDeck.get(i).getSuit(), newDeck.get(i).getRank());
			newDeck.remove(i);
		}

		for(int i = 0; i < cardLimit; i++)
		{
			AI2.dealCard(newDeck.get(i).getFace(), newDeck.get(i).getSuit(), newDeck.get(i).getRank());
			newDeck.remove(i);
		}

		for(int i = 0; i < cardLimit; i++)
		{
			AI3.dealCard(newDeck.get(i).getFace(), newDeck.get(i).getSuit(), newDeck.get(i).getRank());
			newDeck.remove(i);
		}

		currentSuit = newDeck.get(0).getSuit();


	}

	public static void printCards() throws InterruptedException
	{
		System.out.println("");
		System.out.println("");
		System.out.println("| Player |");
		player.showHand();
		System.out.println("");

		System.out.println("| AI1 |");
		AI1.showHand();
		System.out.println("");

		System.out.println("| AI2 |");
		AI2.showHand();
		System.out.println("");

		System.out.println("| AI3 |");
		AI3.showHand();
		System.out.println("");


		System.out.println("Trump suit: " + trumpSuit);
		System.out.println("");
		System.out.println("");

		Thread.sleep(3000); // pause for each player



	}

	public static boolean acceptSuit(Player player)
	{
		String answer = null;

		if(player.getName().equalsIgnoreCase("player"))
		{
			sc = new Scanner(System.in);
			System.out.println("Would you like to draw this card? (yes/no):");
			answer = sc.nextLine();
		}
		else
		{
			answer = player.chooseSuit(currentSuit);
		}


		if(answer.equalsIgnoreCase("yes"))
		{			
			if(player.getName().equalsIgnoreCase("player"))
			{
				System.out.print("Select which card to swap: ");
				player.showHand();
				System.out.print("\n");
				answer = sc.nextLine();

				player.pickUp(newDeck.get(0).getFace(), newDeck.get(0).getSuit(), newDeck.get(0).getRank());
				player.swapCard(answer);
			}
			else
			{
				player.pickUp(newDeck.get(0).getFace(), newDeck.get(0).getSuit(), newDeck.get(0).getRank());
				player.placeCard(answer);
			}



			trumpSuit = newDeck.get(0).getSuit();
			setTrumpCards(trumpSuit);

			return true;
		}
		else
		{
			//pass onto AI
			//else select suit manually

			return false;
		}

	}

	public static void setTrumpCards(String suit)
	{
		for(int i = 0; i < playerOrder.size(); i++)
		{
			playerOrder.get(i).setTrumpSuit(suit);			
		}
	}

	public static void drawCard(String swapCard, Player player, boolean acceptingSuit)
	{

		if(acceptingSuit)
		{
			player.pickUp(newDeck.get(0).getFace(), newDeck.get(0).getSuit(), newDeck.get(0).getRank());
		}
		else
		{
			for(int i = 0; i < cardLimit; i++)
			{
				if(player.selectCard(i).equalsIgnoreCase(swapCard))
				{
					player.pickUp(newDeck.get(0).getFace(), newDeck.get(0).getSuit(), newDeck.get(0).getRank());
					player.removeCardFromHand(i);
				}
			}
		}




		//		player.showHand();

	}

	public static void startTurn(Player player)
	{
		System.out.println("*** " + player.getName() + "'s TURN ***");

		sc = new Scanner(System.in);
		System.out.print("Place a card: ");
		String answer = sc.nextLine();

		for(int i = 0; i < player.getHandSize(); i++)
		{
			if(player.selectCard(i).equalsIgnoreCase(answer))
			{
				if(currentTrick.size() == 0)
				{
					currentSuit = player.getSuit(i);
				}

				if(player.hasSuit(currentSuit, "Playable"))
				{
					System.out.println("Playing: " + player.selectCard(i));
				}
				else
				{
					System.out.println(player.getName() + " Cant follow suit!..");
					System.out.println(player.getName() + " is playing " +  player.selectCard(i));

				}

				currentTrick.add(player.playCard(i));


				System.out.println("Playing: " + player.selectCard(i) + " rank: " + player.getCardRank(i));
				player.removeCardFromHand(i);
			}
		}

		System.out.println("");
		//		printTrick();

	}



	public static void turn(Player player, int turnPosition)
	{
		if(player.getName().equalsIgnoreCase("player"))
		{
			startTurn(player);
		}
		else
		{
			System.out.println("*** " + player.getName() + "'s TURN ***");
			boolean canFollow = false, canBeat = false;

			if(turnPosition == 0 && currentTrick.size() == 0)
			{
				currentTrick.add(player.playCard());

				if(currentTrick.get(0).isBower())
				{
					currentSuit = trumpSuit;
				}
				else
				{
					currentSuit = currentTrick.get(0).getSuit();
				}



				System.out.println("Current Suit: " + currentSuit);

			}
			else
			{
				if(player.hasSuit(currentSuit, "Playable"))
				{

					if(player.canBeat(trickHighestRank())) //if player can beat the highest rank card
					{
						canBeat = true;

					}


					if(canBeat)
					{
						System.out.println("Can beat using: strongest");
						currentTrick.add(player.playStrongest());
					}
					else
					{
						System.out.println("Cant beat using: weakest");
						currentTrick.add(player.playWeakest());
					}

				}
				else
				{
					//check for trump cards
					//check ranking of all trump cards
					if(player.hasSuit(trumpSuit, "Trump"))
					{

						if(player.canBeat(trickHighestRank())) //if player can beat the highest rank card
						{
							canBeat = true;
						}

						if(canBeat)
						{
							System.out.println("Trumping!..");
							System.out.print("Can beat using: strongest trump\n");
							currentTrick.add(player.playStrongest());

						}
						else
						{
							System.out.println("Cant beat using: weakest off suit");
							currentTrick.add(player.playWeakestOffSuit(trumpSuit));	
						}


					}
					else
					{
						System.out.println(player.getName() + " Cant follow suit!..");
						currentTrick.add(player.playWeakestOffSuit(currentSuit));		
					}
				}
			}
		}

		System.out.println("");
		//		printTrick();
	}

	public static int trickHighestRank()
	{
		int currentHighest = -1;


		for(int i = 0; i < currentTrick.size(); i++)
		{
//			System.out.println("TEMP HIGHEST RANK: " + currentTrick.get(i).getRank());
			
			int tempHighest = currentTrick.get(i).getRank();

			if(tempHighest > currentHighest && 
					currentTrick.get(i).getSuit().equalsIgnoreCase(currentSuit) || 
					currentTrick.get(i).getSuit().equalsIgnoreCase(trumpSuit) || 
					currentTrick.get(i).isBower())
			{
				currentHighest = tempHighest;

//				System.out.println("CURRENT HIGHEST RANK: " + currentTrick.get(i).getRank());
			}
		}

		return currentHighest;
	}

	public static void checkWinner() throws InterruptedException
	{
		int currentWinner = -1;
		int playerCurrent = -1;

		for(int i = 0; i < currentTrick.size(); i++)
		{
			int tempWinner = currentTrick.get(i).getRank();

			if(tempWinner > currentWinner && currentTrick.get(i).getSuit().equalsIgnoreCase(currentSuit))
			{
				currentWinner = tempWinner;
				playerCurrent = i;
			}
			else  if(tempWinner > currentWinner && currentTrick.get(i).getSuit().equalsIgnoreCase(trumpSuit))
			{
				currentWinner = tempWinner;
				playerCurrent = i;
			}
			else if(tempWinner > currentWinner && currentTrick.get(i).isBower())
			{
				currentWinner = tempWinner;
				playerCurrent = i;
			}

		}


		System.out.println(playerOrder.get(playerCurrent).getName() + " Wins Trick with " + currentTrick.get(playerCurrent).getCard() 
				+ " Team: " + playerOrder.get(playerCurrent).getTeam());
		awardWinners(playerOrder.get(playerCurrent).getTeam());

		rotateTurn(playerCurrent);
		//System.out.println("Winner: " + currentTrick.get(playerCurrent).getCard());

		Thread.sleep(5000); // pause for each player



		currentTrick.clear();
	}

	public static void awardWinners(int team)
	{
		for(int i = 0; i < playerOrder.size(); i++)
		{
			if(playerOrder.get(i).getTeam() == team)
			{
				//				System.out.println("Add point to " + playerOrder.get(i).getName());
				playerOrder.get(i).addPoint();
			}
		}
	}

	public static void printTrick()
	{
		for(int i = 0; i < currentTrick.size(); i++)
		{
			System.out.println(currentTrick.get(i).getFace() + currentTrick.get(i).getSuit());

		}
	}

	public static void rotateTurn(int winner)
	{		
		for(int i = 0; i < winner; i++)
		{
			{
				playerOrder.add(playerOrder.size(), playerOrder.get(i));		
			}
		}

		for(int i = 0; i < winner; i++)
		{
			{
				playerOrder.remove(0);
			}
		}

		printPlayerOrder();

	}

	public static Player returnPlayer(int i)
	{
		Player temp = new Player(playerOrder.get(i).getName(), playerOrder.get(i).getTeam(), playerOrder.get(i).getSeatPosition(), playerOrder.get(i).getHand());

		return temp;
	}

	public static void gamePlay() throws InterruptedException
	{
		for(int i = 0; i < playerOrder.size(); i++)
		{
			turn(returnPlayer(i), i);
			Thread.sleep(2000); // pause for each player
		}
	}	

	public static void printPlayerOrder()
	{
		System.out.println("\n*** Player Order ***");

		for(int i = 0; i < playerOrder.size(); i++)
		{
			System.out.println(playerOrder.get(i).getName() +  " P: " + playerOrder.get(i).getPoints());

		}

		System.out.println("********************");
	}

	public static void main(String[] args) throws InterruptedException {


		int turnCount = 0;
		boolean suitAccepted = false;

		initPlayers();
		shuffle();
		deal();
		printCards();
		System.out.println("Current Card: " + newDeck.get(0).getFace() + currentSuit);


		for(int i = 0; i < playerOrder.size(); i++)
		{
			suitAccepted = acceptSuit(playerOrder.get(i));

			if(suitAccepted)
			{
				break;
			}

			if(!suitAccepted)
			{

			}
		}



		while(turnCount < 5)
		{
			printTrick();
			printCards();
			gamePlay();
			checkWinner();


			turnCount++;

		}

		System.out.println("Finish..");











	}



















}
