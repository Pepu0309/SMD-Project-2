package oh_heaven.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class Oh_Heaven extends CardGame {

	public static void main(String[] args)
	{
		// System.out.println("Working Directory = " + System.getProperty("user.dir"));
		final Properties properties;
		if (args == null || args.length == 0) {
			//properties = PropertiesLoader.loadPropertiesFile(null);
		} else {
			//properties = PropertiesLoader.loadPropertiesFile(args[0]);
		}
		new Oh_Heaven();
	}

	public Oh_Heaven()
	{
		super(700, 700, 30);
		setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");
		initScores();
		initScore();
		initPlayers();
		for (int i=0; i <nbRounds; i++) {
			initTricks();
			initRound();
			playRound();
			updateScores();
		};
		for (int i=0; i <nbPlayers; i++) updateScore(i);
		int maxScore = 0;
		for (int i = 0; i <nbPlayers; i++) if (scores[i] > maxScore) maxScore = scores[i];
		Set <Integer> winners = new HashSet<Integer>();
		for (int i = 0; i <nbPlayers; i++) if (scores[i] == maxScore) winners.add(i);
		String winText;
		if (winners.size() == 1) {
			winText = "Game over. Winner is player: " +
					winners.iterator().next();
		}
		else {
			winText = "Game Over. Drawn winners are players: " +
					String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
		}
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText(winText);
		refresh();
	}

	public enum Suit
  {
    SPADES, HEARTS, DIAMONDS, CLUBS
  }

  public enum Rank
  {
    // Reverse order of rank importance (see rankGreater() below)
	// Order of cards is tied to card images
	ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
  }
  
  final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

  static public final int seed = 30006;
  static final Random random = new Random(seed);
  
  // return random Enum value
  public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
      int x = random.nextInt(clazz.getEnumConstants().length);
      return clazz.getEnumConstants()[x];
  }

  // return random Card from Hand
  public static Card randomCard(Hand hand){
      int x = random.nextInt(hand.getNumberOfCards());
      return hand.get(x);
  }
 
  // return random Card from ArrayList
  public static Card randomCard(ArrayList<Card> list){
      int x = random.nextInt(list.size());
      return list.get(x);
  }
  
  public boolean rankGreater(Card card1, Card card2) {
	  return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
  }
	 
  private final String version = "1.0";
  public final int nbPlayers = 4;
  public final int nbStartCards = 13;
  public final int nbRounds = 3;
  public final int madeBidBonus = 10;
  private final int handWidth = 400;
  private final int trickWidth = 40;
  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private final Location[] handLocations = {
			  new Location(350, 625),
			  new Location(75, 350),
			  new Location(350, 75),
			  new Location(625, 350)
	  };
  private final Location[] scoreLocations = {
			  new Location(575, 675),
			  new Location(25, 575),
			  new Location(575, 25),
			  // new Location(650, 575)
			  new Location(575, 575)
	  };
  private Actor[] scoreActors = {null, null, null, null };
  private final Location trickLocation = new Location(350, 350);
  private final Location textLocation = new Location(350, 450);
  private final int thinkingTime = 2000;
  //private Hand[] hands;
  private Location hideLocation = new Location(-500, - 500);
  private Location trumpsActorLocation = new Location(50, 50);
  private boolean enforceRules=false;

  // Game always has 4 players according to the spec.
  private Player[] players = new Player[4];

  public void setStatus(String string) { setStatusText(string); }
  
	private int[] scores = new int[nbPlayers];
	// Note: tricks is the "cards on the board", so this shouldn't be stored by the players.
	private int[] tricks = new int[nbPlayers];
	private int[] bids = new int[nbPlayers];

	Font bigFont = new Font("Serif", Font.BOLD, 36);

	private void initPlayers() {
		// Modify later to create players via properties loader.
		players[0] = new InteractivePlayer(0);
		for(int i = 1; i < nbPlayers; i++) {
			players[i] = new NPC(i);
		}
		// Debugging code for checking type of players.
//		for(int i = 0; i < nbPlayers; i++) {
//			if(players[i] instanceof InteractivePlayer) {
//				System.out.println("player " + i + " is an instance of InteractivePlayer");
//			} else if (players[i] instanceof NPC) {
//				System.out.println("player " + i + " is an instance of NPC");
//			}
//		}
	}

	private void initScore() {
		 for (int i = 0; i < nbPlayers; i++) {
			 // scores[i] = 0;
			 String text = "[" + String.valueOf(scores[i]) + "]" + String.valueOf(tricks[i]) + "/" + String.valueOf(bids[i]);
			 scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
			 addActor(scoreActors[i], scoreLocations[i]);
		 }
	  }

	private void updateScore(int player) {
		removeActor(scoreActors[player]);
		String text = "[" + String.valueOf(scores[player]) + "]" + String.valueOf(tricks[player]) + "/" + String.valueOf(bids[player]);
		scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[player], scoreLocations[player]);
	}

	private void initScores() {
		 for (int i = 0; i < nbPlayers; i++) {
			 scores[i] = 0;
		 }
	}

	private void updateScores() {
		 for (int i = 0; i < nbPlayers; i++) {
			 scores[i] += tricks[i];
			 if (tricks[i] == bids[i]) scores[i] += madeBidBonus;
		 }
	}

	private void initTricks() {
		 for (int i = 0; i < nbPlayers; i++) {
			 tricks[i] = 0;
		 }
	}

	private void initBids(Suit trumps, int nextPlayer) {
		int total = 0;
		for (int i = nextPlayer; i < nextPlayer + nbPlayers; i++) {
			 int iP = i % nbPlayers;
			 bids[iP] = nbStartCards / 4 + random.nextInt(2);
			 total += bids[iP];
		 }
		 if (total == nbStartCards) {  // Force last bid so not every bid possible
			 int iP = (nextPlayer + nbPlayers) % nbPlayers;
			 if (bids[iP] == 0) {
				 bids[iP] = 1;
			 } else {
				 bids[iP] += random.nextBoolean() ? -1 : 1;
			 }
		 }
		// for (int i = 0; i < nbPlayers; i++) {
		// 	 bids[i] = nbStartCards / 4 + 1;
		//  }
	 }

	private void initRound() {
		//hands = new Hand[nbPlayers];
//		for (int i = 0; i < nbPlayers; i++) {
//			   hands[i] = new Hand(deck);
//		}
		// Initialise each player's hand this round using the deck.
		for (int i = 0; i < nbPlayers; i++) {
			players[i].initialisePlayerHand(deck);
		}

		dealingOut(nbPlayers, nbStartCards);
		for (int i = 0; i < nbPlayers; i++) {
			players[i].sortHand();
		}
		// Set up human player for interaction
//		CardListener cardListener = new CardAdapter()  // Human Player plays card
//				{
//				  public void leftDoubleClicked(Card card) { selected = card; hands[0].setTouchEnabled(false); }
//				};
//		hands[0].addCardListener(cardListener);
		// graphics
		RowLayout[] layouts = new RowLayout[nbPlayers];
		for (int i = 0; i < nbPlayers; i++) {
			layouts[i] = new RowLayout(handLocations[i], handWidth);
			layouts[i].setRotationAngle(90 * i);
			//layouts[i].setStepDelay(10);
//			hands[i].setView(this, layouts[i]);
//			hands[i].setTargetArea(new TargetArea(trickLocation));
//			hands[i].draw();
			// Game assigns each player a location and a layout and tells players to display their hand at the
			// location specified. Player stores their own information.
			players[i].displayHand(this, layouts[i], new TargetArea(trickLocation));
		}
//	    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
//	      hands[i].setVerso(true);			// You do not need to use or change this code.
		// End graphics
	 }
	private void dealingOut(int nbPlayers, int nbCardsPerPlayer) {
		Hand pack = deck.toHand(false);
		// pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
		for (int i = 0; i < nbCardsPerPlayer; i++) {
			for (int j=0; j < nbPlayers; j++) {
				if (pack.isEmpty()) return;
				Card dealt = randomCard(pack);
				// System.out.println("Cards = " + dealt);
				dealt.removeFromHand(false);
				//hands[j].insert(dealt, false);
				players[j].dealCard(dealt);
				// dealt.transfer(hands[j], true);
			}
		}
	}

	// An attribute storing the card that the current player selected.
	private Card curPlayerSelected;

	private void playRound() {
		// Select and display trump suit
		final Suit trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);
		// End trump suit
		Hand trick;
		int winner;
		Card winningCard;
		Suit lead;
		int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
		initBids(trumps, nextPlayer);

		Player interactivePlayer = players[0];

		// initScore();
		for (int i = 0; i < nbPlayers; i++) updateScore(i);
		for (int i = 0; i < nbStartCards; i++) {
			trick = new Hand(deck);
			curPlayerSelected = null;
			// if (false) {
			if (players[nextPlayer] instanceof InteractivePlayer) {  // Select lead depending on player type
				getInteractivePlayerMove();
			// Random player plays a random card
			} else {
				getNPCMove(nextPlayer);
			}
			// Lead with curPlayerSelected card
				trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
				trick.draw();
				curPlayerSelected.setVerso(false);
				// No restrictions on the card being lead
				lead = (Suit) curPlayerSelected.getSuit();
				curPlayerSelected.transfer(trick, true); // transfer to trick (includes graphic effect)
				winner = nextPlayer;
				winningCard = curPlayerSelected;
			// End Lead
			for (int j = 1; j < nbPlayers; j++) {
				if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
				curPlayerSelected = null;
				// if (false) {
				if (players[nextPlayer] instanceof InteractivePlayer) {
					getInteractivePlayerMove();
				} else {
					getNPCMove(nextPlayer);
				}
				// Follow with curPlayerSelected card
					trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
					trick.draw();
					curPlayerSelected.setVerso(false);  // In case it is upside down
					// Check: Following card must follow suit if possible
						if (curPlayerSelected.getSuit() != lead && players[nextPlayer].getPlayerHand().getNumberOfCardsWithSuit(lead) > 0) {
							 // Rule violation
							 String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + curPlayerSelected;
							 System.out.println(violation);
							 if (enforceRules)
								 try {
									 throw(new BrokeRuleException(violation));
									} catch (BrokeRuleException e) {
										e.printStackTrace();
										System.out.println("A cheating player spoiled the game!");
										System.exit(0);
									}
						 }
					// End Check
					 curPlayerSelected.transfer(trick, true); // transfer to trick (includes graphic effect)
					 System.out.println("winning: " + winningCard);
					 System.out.println(" played: " + curPlayerSelected);
					 // System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
					 // System.out.println(" played: suit = " +    curPlayerSelected.getSuit() + ", rank = " + (13 -    curPlayerSelected.getRankId()));
					 if ( // beat current winner with higher card
						 (curPlayerSelected.getSuit() == winningCard.getSuit() && rankGreater(curPlayerSelected, winningCard)) ||
						  // trumped when non-trump was winning
						 (curPlayerSelected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
						 System.out.println("NEW WINNER");
						 winner = nextPlayer;
						 winningCard = curPlayerSelected;
					 }
				// End Follow
			}
			delay(600);
			trick.setView(this, new RowLayout(hideLocation, 0));
			trick.draw();
			nextPlayer = winner;
			setStatusText("Player " + nextPlayer + " wins trick.");
			tricks[nextPlayer]++;
			updateScore(nextPlayer);
		}
		removeActor(trumpsActor);
	}

	private void getInteractivePlayerMove() {
		// This is the interactive player, delay and setStatus are methods of GameGrid class so unlikely
		// we can move them around.

		// Interactive player is player 0.
		Player interactivePlayer = players[0];
		// Tell the interactive player to reset their previous move as it will store its move from last turn.
		// It needs to be set to null, so it can constantly poll their player.
		interactivePlayer.resetMove();
		interactivePlayer.getPlayerHand().setTouchEnabled(true);
		setStatus("Player 0 double-click on card to lead.");
		// This while loop constantly checks for the interactive playing double-clicking to play a move.
		do {
			curPlayerSelected = interactivePlayer.playMove();
			delay(100);
		} while (curPlayerSelected == null);
	}

	private void getNPCMove(int curNPCPlayerNum) {
		setStatusText("Player " + curNPCPlayerNum + " thinking...");
		delay(thinkingTime);
		// This is NPC player picking their next move
		curPlayerSelected = players[curNPCPlayerNum].playMove();
	}
}