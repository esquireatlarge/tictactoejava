//Matt Sguerri
//CECS 261
//Tic Tac Toe Project :  An attractive tic tac toe
//game where the computer makes a valid random move after
//the user moves.

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class main {
	public static void main (String[] args){
		//Creating new frame to store panel in.
		JFrame frame = new JFrame("Tic Tac Toe:  Terminator Edition");
		//Assign default close operation.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//JPanel panel = new JPanel();
		//panel.setLayout(new BorderLayout());
		//Instantiate a new panel.
		TicTacToe tic = new TicTacToe();
		//radiobuttonsfield rpanel = new radiobuttonsfield();
		
		//panel.add(tic, BorderLayout.CENTER);
		//panel.add(rpanel, BorderLayout.NORTH);

		frame.getContentPane().add(tic);
		frame.pack();
		frame.setVisible(true);

	}

}