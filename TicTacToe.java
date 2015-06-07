//Matt Sguerri
//CECS 261
//Tic Tac Toe Project :  An attractive tic tac toe
//game where the computer makes a valid random move after
//the user moves.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class TicTacToe extends JPanel{
	//User selects his piece.
	private JRadioButton x, y;
	private JLabel introduction, whoisplaying, whatpiece;
	//The buttons represent positions on the board, except
	//move_ex which executes the move within the text field.
	private JButton topleft, top, topright, left, middle, right;
	private JButton bottomleft, bottom, bottomright, move_ex, undo;
	private boolean computerTurn = false, victory = false;
	private String user_piece, computer_piece;
	
	//Count the computer's moves, count the player's moves.
	private int block_location, computer_count, player_count;
	private undostack s;
	
	//Measure the occupied spaces and which spaces
	//are occupied.  Based on the array, if the
	//first element is true, then the first box 
	//would be occupied, meaning the top left box is filled.
	//The array accounts for boxes numerically going from left to right
	//and top to bottom.
	private boolean occupied[] = {false, false, false, false, false, 
			false, false, false, false};
	
	private JTextField move_input;
	
	public TicTacToe(){
		
		//Instantiate almost all components...
		s = new undostack();
		x = new JRadioButton("Play as X");
		y = new JRadioButton("Play as O");
		topleft = new JButton();
		top = new JButton();
		topright = new JButton();
		left = new JButton();
		middle = new JButton();
		right = new JButton();
		bottomleft = new JButton();
		bottom = new JButton();
		bottomright = new JButton();
		topleft.setEnabled(false);
		top.setEnabled(false);
		topright.setEnabled(false);
		left.setEnabled(false);
		middle.setEnabled(false);
		right.setEnabled(false);
		bottomleft.setEnabled(false);
		bottom.setEnabled(false);
		bottomright.setEnabled(false);
		move_input = new JTextField(2);
		move_ex = new JButton("Move!");
		undo = new JButton("Undo");
		
		//Reset the computer moves.
		computer_count = 0;
		//Reset the player moves.
		player_count = 0;
		
		//Set the fonts for all the buttons so the pieces can be bigger...
		topleft.setFont(new Font("Helvetica", Font.BOLD, 36));
		top.setFont(new Font("Helvetica", Font.BOLD, 36));
		topright.setFont(new Font("Helvetica", Font.BOLD, 36));
		left.setFont(new Font("Helvetica", Font.BOLD, 36));
		middle.setFont(new Font("Helvetica", Font.BOLD, 36));
		right.setFont(new Font("Helvetica", Font.BOLD, 36));
		bottomleft.setFont(new Font("Helvetica", Font.BOLD, 36));
		bottom.setFont(new Font("Helvetica", Font.BOLD, 36));
		bottomright.setFont(new Font("Helvetica", Font.BOLD, 36));

		//Initialize listeners
		PieceListener plisten = new PieceListener();
		
		//Add the listeners for the radio buttons
		x.addActionListener(plisten);
		y.addActionListener(plisten);
		
		//Add the listener for the move button
		move_ex.addActionListener(plisten);
		move_input.addActionListener(plisten);
		undo.addActionListener(plisten);
		
		//Instantiate child panels
		UserPlayer player = new UserPlayer();
		ContainerPanel conpanel = new ContainerPanel();
		//TicTacToeBoard board = new TicTacToeBoard();
		Statistics stats = new Statistics();
		
		//Set the layout of the parent panel
		setLayout(new BorderLayout());
		
		//Add child panels to respective locations in
		//parent panel.
		add(player, BorderLayout.NORTH);
		add(conpanel, BorderLayout.CENTER);
		add(stats, BorderLayout.SOUTH);
		
		//Make the window fairly large...
		setPreferredSize(new Dimension(600,500));
	}
	
	private class PieceListener implements ActionListener{
		
		//Listener for the x or y radio buttons and move button...
		
		public void actionPerformed(ActionEvent event){
			//If the user is playing as X...
			if(event.getSource() == x){
				//Make sure the user is playing as X...
				user_piece = "X";
				
				//Make sure the computer is playing as Y...
				computer_piece = "O";
				
				//Make sure to disable buttons so user
				//cannot change mid-game.
				x.setEnabled(false);
				y.setEnabled(false);
				
				//First do a coin toss with the computer...
				
				//Enable button so they can execute a move...
				//Maybe disable the move_ex button while the computer is moving..
				move_input.setEnabled(true);
				move_ex.setEnabled(true);

				//Display what the user is playing as...
				whatpiece.setText("Playing as X");
				
				//The computer makes the first move....
				if(computerFirstMove() == true){
					move_ex.setEnabled(false);
					whoisplaying.setText("::Computer gets first turn!");
					block_location = computerMove();
					determineBlock(block_location, computer_piece);
					System.out.print("Computer first turn: X");
					move_ex.setEnabled(true);
					move_input.grabFocus();
					computerTurn = true;
					undo.setEnabled(true);
				}
				else{
					computerTurn = false;
					move_input.grabFocus();
					whoisplaying.setText("::Your turn!");
				}
			}
			
			//Y is selected...
			else if(event.getSource() == y){
				//Make sure the user is playing as Y...
				user_piece = "O";
				
				//Make sure the user is playing as X...
				computer_piece = "X";
				
				//Make sure to disable buttons so user
				//cannot change mid-game.
				x.setEnabled(false);
				y.setEnabled(false);
				
				//Enable button so they can execute a move...
				//Disable button while the computer is taking its turn...
				move_input.setEnabled(true);
				move_ex.setEnabled(true);
				whatpiece.setText("Playing as O");
				
				//The computer makes the first move....
				if(computerFirstMove() == true){
					move_ex.setEnabled(false);
					computerTurn = true;
					whoisplaying.setText("::Computer gets first turn!");
					block_location = computerMove();
					determineBlock(block_location, computer_piece);
					System.out.print("Computer first turn: Y");
					move_ex.setEnabled(true);
					move_input.grabFocus();
					computer_count++;
					undo.setEnabled(true);
				}
				else{
					computerTurn = false;
					move_input.grabFocus();
					whoisplaying.setText("::Your turn!");
				}
			}
			
			//If the user pressed the execute move button
			if((event.getSource() == move_ex) || (event.getSource() == move_input)){
				computerTurn = false;
				String text = move_input.getText();
				block_location = convertToBlock(text);
				if((block_location > 9) && (block_location < 1))
					whoisplaying.setText("Invalid option.");
				else{
					if(occupied[block_location-1] == true){
						whoisplaying.setText("That piece is occupied already!");
					}
					else{
						determineBlock(block_location-1, user_piece);
						
						//Checks to see if there are three in a row, 
						//but only if the game is still open
						if(victory == false)
							checkBlocks(user_piece);
						else
							undo.setEnabled(false);
						
						computerTurn = true;
						player_count++;	
						
						//The computer takes its turn...
						block_location = computerMove();
						//Make sure that computerMove returned a valid
						//value, because if it doesn't it means that
						//there is only one slot left reserved for the user..
						if(block_location <= 8)
							determineBlock(block_location, computer_piece);
						
						//Checks to see if there are three in a row but only if
						//the game is still open
						if(victory == false)
							checkBlocks(computer_piece);
						else
							undo.setEnabled(false);
						
						computerTurn = false;
					}
					if(computerTurn == true)
						System.out.println("move_ex:computerTurn is true");
					else
						System.out.println("move_ex:computerTurn is false");
				}
				move_input.setText("");
				move_input.grabFocus();
				undo.setEnabled(true);
			}
			
			if(event.getSource() == undo){
				undoMove();
			}
		}
	}
	
	private class UserPlayer extends JPanel{
		private JLabel move;
		public UserPlayer(){
			move =  new JLabel("Type where to put the piece: ");
			
			ButtonGroup piece = new ButtonGroup();
			piece.add(x);
			piece.add(y);
			
			//Initially, the components must be disabled
			//until the user picks a piece.
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			
			add(x);
			add(y);
			add(move);
			add(move_input);
			add(move_ex);
			add(undo);
			
		}
	}//Ends UserPlayer subpanel
	
	private class TicTacToeBoard extends JPanel{
		public TicTacToeBoard(){
			setLayout( new GridLayout(3,3));
			add(topleft);
			add(top);
			add(topright);
			add(left);
			add(middle);
			add(right);
			add(bottomleft);
			add(bottom);
			add(bottomright);
		}
	}//Ends board subpanel
	
	private class VerticalCoords extends JPanel{
		private JLabel a = new JLabel("A");
		private JLabel b = new JLabel("B");
		private JLabel c = new JLabel("C");
		public VerticalCoords(){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createRigidArea(new Dimension(20, 40)));
			add(a);
			add(Box.createRigidArea(new Dimension(20, 170)));
			add(b);
			add(Box.createRigidArea(new Dimension(20, 130)));
			add(c);
			add(Box.createRigidArea(new Dimension(20, 170)));
		}
	}
	
	private class HorizontalCoords extends JPanel{
		private JLabel one = new JLabel("1");
		private JLabel two = new JLabel("2");
		private JLabel three = new JLabel("3");
		public HorizontalCoords(){
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(Box.createRigidArea(new Dimension(60, 20)));
			add(one);
			add(Box.createRigidArea(new Dimension(190, 20)));
			add(two);
			add(Box.createRigidArea(new Dimension(170, 20)));
			add(three);
			add(Box.createRigidArea(new Dimension(190, 20)));
		}
	}
	
	//This panel contains statistics about the game
	//such as who went first and who is playing as what...
	private class Statistics extends JPanel{
		
		public Statistics(){
			whatpiece = new JLabel();
			whoisplaying = new JLabel();
			whatpiece.setText("No piece selected!");
			add(whatpiece);
			add(whoisplaying);
		}
	}
	
	//Contains the board, as well as coordinates
	//all in separate panels arranged in a border layout.
	private class ContainerPanel extends JPanel{
		public ContainerPanel(){
			TicTacToeBoard board = new TicTacToeBoard();
			VerticalCoords vcoord = new VerticalCoords();
			HorizontalCoords hcoord = new HorizontalCoords();
			
			setLayout(new BorderLayout());
			add(hcoord, BorderLayout.NORTH);
			add(vcoord, BorderLayout.WEST);
			add(board, BorderLayout.CENTER);
		}
	}
	
	public boolean computerFirstMove(){
		Random generator = new Random();
		boolean num;
		
		//Generate either true or false...
		num = generator.nextBoolean();
		
		return num;
	}
	
	public int computerMove(){
		Random move_gen = new Random();
		int block = 9001, slots_left = 9;
		
		//This will become an infinite loop if there is only
		//one block left, so this for loop
		//scans the occupied array to find
		//out how many slots are left...
		for(int i = 0; i <= 8; i++){
			if(occupied[i] == true)
				--slots_left;
		}
		
		if(slots_left >= 1){ //Should prevent infinite loop
			do{
				block = move_gen.nextInt(9);
			}while(occupied[block] == true);
			computer_count++;
		}
	
		return block;
	}
	
	public void determineBlock(int loc, String piece){
		switch(loc){
			case 0:
				topleft.setText(piece);
				occupied[0] = true;
				s.push(loc);
				break;
			case 1:
				top.setText(piece);
				occupied[1] = true;
				s.push(loc);
				break;
			case 2:
				topright.setText(piece);
				occupied[2] = true;
				s.push(loc);
				break;
			case 3:
				left.setText(piece);
				occupied[3] = true;
				s.push(loc);
				break;
			case 4:
				middle.setText(piece);
				occupied[4] = true;
				s.push(loc);
				break;
			case 5:
				right.setText(piece);
				occupied[5] = true;
				s.push(loc);
				break;
			case 6:
				bottomleft.setText(piece);
				occupied[6] = true;
				s.push(loc);
				break;
			case 7:
				bottom.setText(piece);
				occupied[7] = true;
				s.push(loc);
				break;
			case 8:
				bottomright.setText(piece);
				occupied[8] = true;
				s.push(loc);
				break;
			default:
				whoisplaying.setText("Error in placement!");
				break;
		}
	}
	
	public void determineWin(){
		//If the computer might be winning...
		if(computerTurn == true){
			if(computer_count >= 3)
				checkBlocks(computer_piece);
		}
		else{
			if(player_count >= 3)
				checkBlocks(user_piece);
		}
	}
	
	public void checkBlocks(String piece){
		if((topleft.getText() == piece) && (top.getText() == piece)
				&& (topright.getText() == piece)){
			topleft.setBackground(Color.orange);
			top.setBackground(Color.orange);
			topright.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
		else if((left.getText() == piece) && (middle.getText() == piece)
				&& (right.getText() == piece)){
			left.setBackground(Color.orange);
			middle.setBackground(Color.orange);
			right.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
		else if((bottomleft.getText() == piece) && (bottom.getText() == piece)
				&& (bottomright.getText() == piece)){
			bottomleft.setBackground(Color.orange);
			bottom.setBackground(Color.orange);
			bottomright.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
		else if((topleft.getText() == piece) && (middle.getText() == piece)
				&& (bottomright.getText() == piece)){
			topleft.setBackground(Color.orange);
			middle.setBackground(Color.orange);
			bottomright.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
		else if((topright.getText() == piece) && (middle.getText() == piece)
				&& (bottomleft.getText() == piece)){
			topright.setBackground(Color.orange);
			middle.setBackground(Color.orange);
			bottomleft.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
		else if((topleft.getText() == piece) && (left.getText() == piece)
				&& (bottomleft.getText() == piece)){
			topleft.setBackground(Color.orange);
			left.setBackground(Color.orange);
			bottomleft.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
		else if((top.getText() == piece) && (middle.getText() == piece)
				&& (bottom.getText() == piece)){
			top.setBackground(Color.orange);
			middle.setBackground(Color.orange);
			bottom.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
		else if((topright.getText() == piece) && (right.getText() == piece)
				&& (bottomright.getText() == piece)){
			topright.setBackground(Color.orange);
			right.setBackground(Color.orange);
			bottomright.setBackground(Color.orange);
			move_ex.setEnabled(false);
			move_input.setEnabled(false);
			undo.setEnabled(false);
			victory = true;
		}
	}
	
	public int convertToBlock(String str){
		int location;
		str = str.toUpperCase();
		if(str.compareTo("A1") == 0)
			location = 1;
		else if(str.compareTo("A2") == 0)
			location = 2;
		else if(str.compareTo("A3") == 0)
			location = 3;
		else if(str.compareTo("B1") == 0)
			location = 4;
		else if(str.compareTo("B2") == 0)
			location = 5;
		else if(str.compareTo("B3") == 0)
			location = 6;
		else if(str.compareTo("C1") == 0)
			location = 7;
		else if(str.compareTo("C2") == 0)
			location = 8;
		else if(str.compareTo("C3") == 0)
			location = 9;
		else
			location = -1;
		
		return location;
	}
	
	public void undoMove(){
		int moveToUndo;
		if(!s.isEmpty()){
			moveToUndo = s.pop();
			//Because stack stores
			//from zero to eight, so the 
			//increment helps with understanding.
			if(moveToUndo == 8){
				bottomright.setText("");
				occupied[moveToUndo] = false;
			}
			else if(moveToUndo == 7){
				bottom.setText("");
				occupied[moveToUndo] = false;
			}
			else if(moveToUndo == 6){
				bottomleft.setText("");
				occupied[moveToUndo] = false;
			}
			else if(moveToUndo == 5){
				right.setText("");
				occupied[moveToUndo] = false;
			}
			else if(moveToUndo == 4){
				middle.setText("");
				occupied[moveToUndo] = false;
			}
			else if(moveToUndo == 3){
				left.setText("");
				occupied[moveToUndo] = false;
			}
			else if(moveToUndo == 2){
				topright.setText("");
				occupied[moveToUndo] = false;
			}
			else if(moveToUndo == 1){
				top.setText("");
				occupied[moveToUndo] = false;
			}
			else{
				topleft.setText("");
				occupied[moveToUndo] = false;
			}
		}
	}
	

}//Ends class
