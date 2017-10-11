/*
 * This program written by Michael Morrisey 10/10/2017
 * as practice with game making.
 * This is a recreation of the board game Battleship
 * which features two 10x10 squares where each player
 * hides 5 ships and then hunts for the other player's
 * ships.
 * There is a text file, "winners.txt", included with this project.
This program reads text from that file, places it in the leaderboard array,
and later writes to the file when the leaderboard has been updated with 
the newest game winner.


To Do:  -GUI
        
 */

import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Battleship implements ActionListener
{
    //list of winners
    private static String[] leaderboard;
    private static Player player1;
    private static Player player2;
    
    
    public static void main(String[] args) throws Exception
    {
    	//create a new game
    	Battleship myGame;
            //run Battleship constructor, which will create a GUI
            myGame = new Battleship();
    	
    	//initialize the leaderboard by reading from the file
        leaderboard = new String[]{"","",""};
        //call readTextFile() to fill the scoreboard with the previous winners,
        //which were stored in the text file
        readTextFile(leaderboard);
        
        //print stats about the old winners
        printWinners();
        
        //initialize the new players (prompt input to set up the board)    
        player1 = new Player(1);
        player2 = new Player(2);
  /*    //a 2d array to use in hardcoding the ship locations for testing
        int[][] myTempBoard;
        //one hardcoded board
        myTempBoard = new int[][]{
                        {1,1,1,1,1,0,0,0,0,0},
			{1,1,1,1,0,0,0,0,0,0},
			{1,1,1,0,1,1,1,0,0,0},
			{1,1,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0}
        };
        player1 = new Player(1, myTempBoard);
                        
        myTempBoard = new int[][]{
                        {1,1,1,1,0,0,0,0,0,0},
			{1,1,1,1,0,0,0,0,0,0},
			{1,1,1,1,0,0,0,0,0,0},
			{1,1,1,1,0,0,0,0,0,0},
			{1,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0}
        };
        player2 = new Player(2, myTempBoard);
   */
    
	
        
        
        //play the game and announce a new winner
        myGame.play();
        //store the winner's name in a text file
        writeTextFile(leaderboard);
		
    }//end main()
	
    //constructor
    //This constructor does nothing
    public Battleship() 
    {
        //construct a GUI
        //create a JFrame to hold everything
    	JFrame myFrame = new JFrame("Battleship");
    	//set the frame to exit on close
    	myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	//add stuff to the frame
    	//specifically, create a 10x10 grid of buttons, a menu bar at the top, and instructions (click to attack)
    	//create a panel with 
    	JPanel contentPanel = new JPanel(new GridLayout(10,10));
    	for (char i = 'A'; i < 'K'; i++)
    	{
    		for (int j = 1; j < 11; j++)
    		{
    			JButton button = new JButton(i + " " + j);
    			button.addActionListener(new ActionListener() 
    			{
    				public void actionPerformed(ActionEvent e) 
    				{
    					//this will detail what to do when the button is pushed. 
    					//we are attacking
    					
    					
    					
    				}//end actionPerformed()
    			}//end ActionListener()
    			);//end addActionListener()
    			contentPanel.add(button);
    			
    		}//end for(j)
    	}//end for(i)
    	myFrame.getContentPane().add(contentPanel);
    	
    	
    	//size the frame
    	myFrame.pack();
    	//set it as visible
    	myFrame.setVisible(true);
        
    
        
        
    }//end constructor Battleship()
	
    //****************************************************************
    
    public static void printWinners()
    {
        System.out.println("Winners:"
                + "\n1: " + leaderboard[0]
                + "\n2: " + leaderboard[1]
                + "\n3: " + leaderboard[2]);
    }//end printWinners()
    //*****************************************************************
    
    //this method allows gameplay and determines the winner. The method
    //returns the winning player
    public void play()
    {
        
        
        
        //update leaderboard in anticipation of a new victor
        leaderboard[2] = leaderboard[1];
        leaderboard[1] = leaderboard[0];
        
        
        //declare an int array to represent an attack as x,y coordinates
        int[] xy;
        
        //TESTING: set player 1 up to lose right away
    //    player1.shipBitsRemaining = 1;
        
        //continue playing as long as both players have some ship bits left
        while (player1.shipBitsRemaining > 0 && player2.shipBitsRemaining > 0)
        {
            //begin turn; first player 1 attacks
            xy = player1.attack();
            player1.analyzeDefense(player2.defend(xy), xy);
            //then player 2 attacks. 
            //Note that player 2 could have just lost their last ship bit, so test for that
            if (player2.shipBitsRemaining > 0)
            {
                xy = player2.attack();
                player2.analyzeDefense(player1.defend(xy), xy);
            }//end if
        }//end while
        
        //one of the players has no remaining ship bits. Congratulate the winner
        if (player1.shipBitsRemaining > 0)
        {
            System.out.println("Congratulations Player 1! You sunk my battleship.");
            leaderboard[0] = player1.initials;
        }//end if(Player 1 won)
        else 
        {
            System.out.println("Congratulations Player 2! You sunk my battleship.");
            leaderboard[0] = player2.initials;
        }//end else(Player 2 won)
        
        //Print the updated leaderboard
        printWinners();
        
        
    }//end play()
	
    //**************************************************************************

   //this method writes the winner's initials to a TXT file
    public static void writeTextFile(String[] winners) throws Exception
    {
        java.io.File myFile = new java.io.File("winners.txt");
        
        java.io.PrintWriter myPrintWriter = new java.io.PrintWriter(myFile);
            String CSVstring = winners[0] + "\n"
                    + winners[1] + "\n"
                    + winners[2];
            myPrintWriter.write(CSVstring);

        myPrintWriter.close();
        
    }//end writeTextFile()
    
    
	
    //***********************************************************************
    
    //this method reads the winner's initials from a TXT file
    //This method should read the text using a Scanner 
    //and save each successive line to the next spot on the Leaderboard array
    //and the number of elements of the array should be returned.
    
    public static void readTextFile(String[] winners) throws Exception
    {
        //a pointer to the next available spot in the array
        int count = 0;
        //make a new File object and associate it with the text file
        File myTextFile = new File("winners.txt");
        //make a new Scanner object and associate it with the File object
        Scanner myScanner = new Scanner(myTextFile);
        
        //step through the entire File line by line
        while (myScanner.hasNextLine() )
        {
            winners[count] = myScanner.nextLine();
            count++;
        }//end while
        
        myScanner.close();
       
    }//end readTextFile
    
    //****************************************************************************************
    //this method is necessary for implementing ActionListener on the buttons
    public void actionPerformed(ActionEvent e) {
    	
    }//end actionPerformed()
    
}//end class Battleship


//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************



class GameBoard 
{
    //a 2d array to represent where the player has placed their own ships to start the game
	public int[][] board;
	//a 2d array to represent where the player has attempted to attack the opposing ships
	public int[][] opposingBoard;
	
        
	
	public GameBoard()
	{
		board = new int[][] {
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0}
		};
		
		opposingBoard = new int[][] {
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0}
		};
	}//end constructor GameBoard()

	
	
	//****************************************************************************************************************************
	
	
	
	/*	//this method will print the gameboard
	public void printBoard(int[][] thisBoard)
	{	System.out.println("    0, 1, 2, 3, 4, 5, 6, 7, 8, 9");
		int i = 0;
		for (int[] eachArr : thisBoard)
			{
				System.out.print(i + " - "); 
				for (int eachInt : eachArr)
					System.out.print(eachInt + ", ");
				System.out.print("\n");
				i++;
			}//end for
	}//end printBoard()
	
	*/
	
	//this method will print the gameboard
		public void printBoard(int[][] thisBoard)
	{
	//construct a GUI
    //create a JFrame to hold everything
	JFrame myFrame = new JFrame("Attack History");
	//set the frame to exit on close
	myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	//add stuff to the frame
	//specifically, create a 10x10 grid of buttons, a menu bar at the top, and instructions (click to attack)
	//create a panel with 
	JPanel contentPanel = new JPanel(new GridLayout(10,10));
	JButton myButton;
	for (int i = 0; i < 10; i++)
	{
		for (int j = 0; j < 10; j++)
		{
			//1 means hit, 9 means miss
			if (thisBoard[j][i] == 1)
				myButton = new JButton("HIT!");
			else if (thisBoard[j][i] == 9)
				myButton = new JButton("miss.");
			else myButton = new JButton("?");
			contentPanel.add(myButton);		
		}//end for(j)
	}//end for(i)
	myFrame.getContentPane().add(contentPanel);
	
	
	//size the frame
	myFrame.pack();
	//set it as visible
	myFrame.setVisible(true);

	}//end printBoard()
		
}//end class GameBoard;




//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************
//*********************************************************************************************



//this class will allow the game to have two players,
//with a gameboard representing each player's ships
//and 
class Player
{
    //declare an int to use as primary key identifying player (Player 1, etc)
    public int playerNumber;
    //declare a String to store the player's initials
    public String initials;
    //declare a GameBoard to store the location of this player's ships
    GameBoard myGameBoard;
    //declare an int to use in determining when the game is over;
    //when this number dwindles to zero, the player has lost
    int shipBitsRemaining;
    
    //******************************************************************
    
    //constructor inputs user initials and adds ships to board
    Player(int playerNum){
        playerNumber = playerNum;
        //initialize shipBitsRemaining to 17 (5+4+3+3+2)
        shipBitsRemaining = 17;
        //prompt the user to input their initials
        initials = JOptionPane.showInputDialog("Player "+ playerNumber +", enter your initials:");
    //    Scanner myscanner = new Scanner(System.in);
    //    System.out.println("Player "+ playerNumber +", enter your initials:");
    //    initials = myscanner.next();
        
        //prompt the user to place their ships
        myGameBoard = new GameBoard();
        addShips(myGameBoard);
        
    }//end constructor Player()
    
    //a testing constructor so that i can hardcode the gameboard (not input it each time)
    Player(int playerNum, int[][] hardcodedBoard){
        playerNumber = playerNum;
        //initialize shipBitsRemaining to 17 (5+4+3+3+2)
        shipBitsRemaining = 17;
        //prompt the user to input their initials
        //prompt the user to place their ships
        myGameBoard = new GameBoard();
        myGameBoard.board = hardcodedBoard;
        
    }//end constructor Player()
    //*********************************************************************
    
    //This method represents launching an attack against an opposing player.
    //This method prompts the user for input and returns the x,y coordinates
    //of the attack.
    //This method validates that the player has selected a valid location
    //and that the location has not already been attacked
    public int[] attack()
    {
        System.out.println("\n\nPlayer " + playerNumber + "'s turn.");
            Scanner myScanner = new Scanner(System.in);
                //Print the record of attacks before each new attack
                System.out.println("Here is the record of your attacks.\nLegend: 1 = Hit, 9 = Miss, 0 = Untried.");
		myGameBoard.printBoard(myGameBoard.opposingBoard);
               
        
        
        
        //declare a flag to use to exit the loop
        boolean flag;
        //declare a flag to use to loop until the user inputs a valid number (0-9)
        boolean numFlag;
        //declare an x and y to use as longitude and latitude of the attack
        int[] xy = new int[2];
    //    Scanner myScanner = new Scanner(System.in);
        int myRecord;
        
        //initialize flag to false
        flag = false;
        //initialize numFlag to false
        numFlag = false;
        //loop until the exit condition is met, i.e. the user selects a spot to attack
        //that hasn't been attacked yet.
        while (!flag)
        {
            //initialize numFlag to false
            numFlag = false;
            //input x coordinate, loop until valid value is entered
            while (!numFlag)
            {
            System.out.println("Player " + playerNumber + ", input attack x coordinate: ");
            xy[0] = myScanner.nextInt();
            if (xy[0] < 0 || xy[0] > 10)
                System.out.println("Error: must choose a number from 0 to 9. Please try again.");
            else numFlag = true;
            }//end while(!numFlag)
            
            //input y coordinate, loop until valid value is entered
            numFlag = false;
            while (!numFlag)
            {
            System.out.println("Player " + playerNumber + ", input attack y coordinate: ");
            xy[1] = myScanner.nextInt();
            if (xy[1] < 0 || xy[1] > 10)
                System.out.println("Error: must choose a number from 0 to 9. Please try again.");
            else numFlag = true;
            }//end while(!numFlag)
            
            //check out the attack history, stored as "opposingBoard"
            myRecord = myGameBoard.opposingBoard[xy[1]][xy[0]];
            if (myRecord == 1)
                System.out.println("You have already gone there, it was a hit. Please try again.");
            else if (myRecord == 9)
                System.out.println("You have already gone there, it was a miss. Please try again.");
            else if (myRecord == 0)
                flag = true;
        }//end while(!flag)
        
        System.out.println("Player " + playerNumber + " is attacking at x,y coordinate ("+xy[1]+", "+xy[0]+").");
        return xy;
    }//end attack()
    
    //*********************************************************************
  
    //this method represents defending against an attack.
    //This method accepts the x,y coordinates of the attack
    //and checks the player's GameBoard to see whether there is a ship there,
    //and returns boolean true (hit) or false (miss)
    //Note: x,y coordinates are in the form column, row
    //but 2d array indexing is of the form array[row][column]
    //so indexing an array is reversed to array[y][x]
    
    public boolean defend(int[] xy)
    {
        //declare a boolean to return
        boolean hit;
        hit = false;
        
        //if there is a 1 at this x,y point in the gameboard, then it's a hit
        if (myGameBoard.board[xy[1]][xy[0]] == 1)
        {    
            System.out.println("ALERT! ALERT!\nPlayer " + playerNumber + " has been hit!");
            hit = true;
            //update the board to reflect that this spot has been hit
            myGameBoard.board[xy[1]][xy[0]] = 9;
            //decrement shipBitsRemaining since each hit is one step closer to losing
            shipBitsRemaining--;
        }//end if(hit)
        else
        {
            System.out.println("This is a miss.");
            myGameBoard.board[xy[1]][xy[0]] = 5;
        }//end else
        
        return hit;
    }//end defend()
    
    //*********************************************************************
    
    //This method analyzes the results of the defend() method,
    //and updates the player's attack history
    public void analyzeDefense(boolean hit, int[] xy)
    {
        
        if(hit)
            //on the opposingBoard, a 1 means hit, a 0 means unknown, and a 9 means miss
            myGameBoard.opposingBoard[xy[1]][xy[0]] = 1;
        else
            myGameBoard.opposingBoard[xy[1]][xy[0]] = 9;
    
    }//end analyzeDefense()
    
    
    //********************************************************************
    
    
    //This method accepts a GameBoard as a parameter
    //and adds 5 ships to the board
    //by calling inputShipLocation()
    public void addShips(GameBoard thisBoard){
        //boolean to determine whether a new ship was added
		boolean successfullyAdded;
                //Scanner to allow user to hide their ships when they are ready
		Scanner myScanner;
                //temporary storage of whatever nonsense the user enters before hiding their ship
                String whatever;
		
                //initialize the Scanner to accept keyboard input
                myScanner = new Scanner(System.in);
                
		//put 5 ships on the board. Keep trying to add each ship until it is successfully added
		
		//initialize successfullyAdded to false
		successfullyAdded = false;
		//now allow the user to input the ship location,
		//but do it in a loop so that if the location is invalid they will try again

		while (!successfullyAdded)
			successfullyAdded = inputShipLocation(thisBoard, 5);
		
		successfullyAdded = false;
		while (!successfullyAdded)
			successfullyAdded = inputShipLocation(thisBoard, 4);
		
		successfullyAdded = false;
		while (!successfullyAdded)
			successfullyAdded = inputShipLocation(thisBoard, 3);
		
		successfullyAdded = false;
		while (!successfullyAdded)
			successfullyAdded = inputShipLocation(thisBoard, 3);
		
		successfullyAdded = false;
		while (!successfullyAdded)
			successfullyAdded = inputShipLocation(thisBoard, 2);
                
                //Now that all the ships are placed, print the board one more time
		thisBoard.printBoard(thisBoard.board);
                //Now prompt the user to clear the console
                System.out.println("Press 'Enter' to clear the console and hide your ships.");
                whatever = myScanner.next();
                //according to Joni on StackOverflow, this should clear the console
                System.out.print("\033[H\033[2J");
                System.out.flush();
	
    }//end addShips
    
    
    //********************************************************************************************************************
	
	//This method prompts the user for input and attempts to 
        //use this input to call addShip(), or outputs an error message.
	//This method returns a boolean which indicates whether the ship was successfully added
	public boolean inputShipLocation(GameBoard thisBoard, int length)
	{
		//boolean to determine whether ship was successfully added
		boolean added;
		//output the current game board so that the user can decide where to put a new ship
		thisBoard.printBoard(thisBoard.board);
		//declare a Scanner to accept user input
				Scanner myScanner = new Scanner(System.in);
				int x;
				int y;
				int horizontalInput;
				boolean horizontal;
				System.out.println("Please enter the x coordinate to place your battleship (length " + length + "):");
				x = myScanner.nextInt();
				System.out.println("Please enter the y coordinate to place your battleship (length " + length + "):");
				y = myScanner.nextInt();
				System.out.println("Should the ship be placed horizontally? Please enter 1 for Yes or 2 for No:");
				horizontalInput = myScanner.nextInt();
				if (horizontalInput == 1)
					horizontal = true;
				else horizontal = false;
				//now place the ship on the board
				
				added = addShip(thisBoard, y, x, length, horizontal);
				//NOTE: if an error is returned here, this method should restart (give another chance to input valid location)
				//Note: this effect is achieved when this method is called from within a while loop in the Battleship constructor
				
				return added;
	}//end inputShipLocation()
        
        
        //*****************************************************************************
        
        
        //this method will allow a player to add a ship to the game board.
	//parameters for this method represent which GameBoard to add the ships to,
        //where the ship starts, its length, and whether it is horizontal.
	//This method return a boolean representing whether the ship was successfully added
	public boolean addShip(GameBoard thisBoard, int row, int column, int length, boolean horizontal)
	{
		//a boolean to return
		boolean result;
		//a boolean to flag when we are trying to put a ship somewhere another ship already is
		boolean flag;
		//initialize flag to false
		flag = false;
		//initialize result to true
		result = true;
		
		if (horizontal)
		{
			if (length + column > 10)
			{
				System.out.println("Error: attempting to place a ship half off the game board.");
				result = false;
			}
			
			else for (int i = 0; i < length; i++)
			{
				if (thisBoard.board[row][column + i] == 1)
					flag = true;
			}//end for
		}//end if(horizontal)
		else
		{
			if (length + row > 10)
			{
				System.out.println("Error: attempting to place a ship half off the game board.");
				result = false;
			}
			
			else for (int i = 0; i < length; i++)
			{
				if (thisBoard.board[row + i][column] == 1)
					flag = true;
			}//end for
		}//end else
		
		//if there is already a ship, print an error message
		if (flag)
		{
			System.out.println("Error: attempting to place a ship where there is already a ship.");
			result = false;
		}
			
		//otherwise, place the ship
		else if (result)
		{
			if (horizontal)
				for (int i = 0; i < length; i++)
				{
					thisBoard.board[row][column + i] = 1;
				}//end for
			else for (int i = 0; i < length; i++)
				{
					thisBoard.board[row + i][column] = 1;
				}//end for
		}//end else
		

		return result;
		
	}//end addShip()
	
	
	

}//end class Player



