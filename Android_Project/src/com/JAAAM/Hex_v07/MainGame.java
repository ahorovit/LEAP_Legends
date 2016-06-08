package com.JAAAM.Hex_v07;

//import com.JAAAM.actions.*;
import com.JAAAM.drawing.*;
import com.JAAAM.pieces.*;
import com.authorwjf.gamedevtut05.R;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Point;




//#########################################################################
//								Main Activity:    
//							  -----------------
//#########################################################################
public class MainGame extends Activity implements OnTouchListener {

	private int debugOn = 1;
	
	// gbModel
	public GameImage gameBoard = null;
	public Model mainModel = null;
	public int main_turn;
	public boolean turnComplete;
	public Tile sel_hex0 = null; // Tile selected in turnState 0
	public Tile sel_hex1 = null; // Tile selected in turnState 1
	

	private int turnState = 0;
	private boolean victoryFlag = false;
	
	// From Tutorial: Handler for onTouchListener (I think...)
	private Handler frame = new Handler();

	// This is from tutorial
	private static final int FRAME_RATE = 20; // 1000/FRAME_RATE = 50 frames per second

	//  ----------------------------------
	//		  Board Touch Processing:    
	//  ----------------------------------	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Main.onCreate() called"); // Logcat message

        // Not completely sure what this does
        setContentView(R.layout.main);
        
        // Access the gameImage
        gameBoard = ((GameImage)findViewById(R.id.the_canvas));
        // Access the model instantiated by the GameImage
        mainModel = gameBoard.gbModel;
        mainModel.update(); // Switches Grunts to phalanx
        
        // Randomize First player
        Random firstTeam = new Random();
       
        main_turn = firstTeam.nextInt(gameBoard.getNumTeams())+1;
        mainModel.team_turn = main_turn;
        
        
        // Ideally, gameModel should be initialized here!
        // As of this version, it is instantiated in GameBoard constructor
		// because I can't figure out how to pass the gameModel into
		// the GameImage constructor.......
/*
		// Instantiate model here
		mainModel = new Model (5,5,2);
		// hand model to gameBoard
		gameBoard.gbModel = mainModel;
*/
        // Create handler for onTouchListener.....not sure what that means
        Handler h = new Handler();
        
        // Set listener focus on the Reset button and the gameImage
        (findViewById(R.id.the_canvas)).setOnTouchListener(this);
        (findViewById(R.id.the_button)).setOnTouchListener(this);
 
        // I don't know what this is doing exactly
        h.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Runs initial Gameboard Graphics
				gen_gameImg();
			}
        }, 1000);
    }

	
    // Adapted from tutorial code (vaguely understood)
    // Implemented in absence of user input (ie upon launch/reset)
    synchronized public void gen_gameImg() {
    	((Button)findViewById(R.id.the_button)).setEnabled(true);
    	
    	// No Idea what this is
    	frame.removeCallbacks(frameUpdate);
    	
    	// Force GameImage to draw to canvas --> invalidate() does this somehow
    	((GameImage)findViewById(R.id.the_canvas)).invalidate();
    	frame.postDelayed(frameUpdate, FRAME_RATE);
    }
    
    //#########################################################################
    //  						   ON TOUCH:    
    //							-----------------
    //#########################################################################
    
    @Override
    synchronized public boolean onTouch(View v, MotionEvent event) {
 
    	if (debugOn == 1)	{System.out.println("Input Event Occurred");}
    	
    	// Exit Function if event is action_up or action_move
    	if (event.getAction() != MotionEvent.ACTION_DOWN){
    		return false;
    	}

    	// Check to see if the Reset Button was pushed
    	if (v.getId() == R.id.the_button){
        	System.out.println("Reset Button Clicked");

        	// Reset all fields
        	reset_All();
    		return true;    		
    	}
    	
    	//  ----------------------------------
    	//		  Board Touch Processing:    
    	//  ----------------------------------
    	
    	// Identify tile touched
    	Tile select_Hex = mainModel.nearestHex(event.getX(), event.getY());
    	
		//Print Tile Coords to text output
    	if(main_turn == 1)
    	{
    		((TextView)findViewById(R.id.line_1)).setText("Robots' Turn..."); 
    	} else {
    		((TextView)findViewById(R.id.line_1)).setText("Humans' Turn..."); 

    	}
    	if (debugOn == 1){
    		System.out.println("DEBUG: Tile Selected ("+ select_Hex.getOfCoord().x 
    				+ ","+select_Hex.getOfCoord().y +")");
    	}
		
		//		  turnState 0: Awaiting highlight command   
    	//  ------------------------------------------------------
    	if (turnState == 0){
    		
	    	if (debugOn == 1)	{System.out.println("DEBUG: TurnState 0....");}
    		
    		// Record initial position
    		mainModel.hiTiles.clear(); 

    		sel_hex0 = select_Hex;

    		// IF hex is Occupied....
    		if (select_Hex.isOccupied){
    			Piece select_Pc = select_Hex.getPiece(mainModel.pcList);

    	    	if (debugOn == 1)	{System.out.println("DEBUG: Piece Identified");}
    			
    			//Print piece ID to text output
    			((TextView)findViewById(R.id.line_2)).setText(select_Pc.getstring_ID()
    					+ " " + select_Pc.getInt_ID() + "; Energy = " + select_Pc.getEnergy());

    			// If the piece belongs to the current player, access move
    			if (select_Pc.getTeam() == main_turn){
    				
    				mainModel.valid_sel = true; // Use valid highlighting sprite
    				
    				System.out.println("Turn initiated..");
    				
            		// update highlight tiles based on selected tile
        			// NOTE: I want to somehow make this generic enough to 
        			// Work for any abilities being used.    				
    				mainModel.hiTiles = select_Pc.getMovTargets();
    				turnState = 1;
    			}else{  // If the piece doesn't belong to current player, just highlight
        			mainModel.hiTiles.add(select_Hex);
    				mainModel.valid_sel = false; // Use invalid highlighting sprite
    			}
    			
    	    	if (debugOn == 1)	{System.out.println("DEBUG: Targets Identified");}
    		}
    		else{  // If tile is unoccupied..
    			mainModel.hiTiles.add(select_Hex);
				mainModel.valid_sel = false; // Use invalid highlighting sprite
    			
    	    	if (debugOn == 1)	{System.out.println("DEBUG: Vacant tile highlighted");}
    		}
    		// Generate Game Image
        	gen_gameImg();
        	
        	//turnState = 1;
        	System.out.println("TurnState =  " + turnState);

    		return true;
    	}
    	
		//		  turnState 1: Awaiting deselect/move choice   
    	//  ------------------------------------------------------
    	else{

	    	if (debugOn == 1)	{System.out.println("DEBUG: TurnState 1....");}
    		
    		sel_hex1 = select_Hex; // record hex selection
    		
    		if (sel_hex0.isOccupied){
    			Piece select_Pc = sel_hex0.getPiece(mainModel.pcList);
    		    		
    			// If user confirmed move, execute move
    			if (select_Pc.getTeam() == main_turn && mainModel.isConfirmed(select_Hex)){
    				sel_hex0.getPiece(mainModel.pcList).movePc(sel_hex0, sel_hex1); 
    				turnComplete = true;
    				if (debugOn == 1)	{System.out.println("DEBUG: Piece Moved");}
    			}
    		}
    		
    		// Move Complete: unhighlight all tiles
    		mainModel.hiTiles.clear(); 
    		sel_hex0 = null;
    		sel_hex1 = null;
    		
    		// Check for Victory Condition
    		if (mainModel.checkVictory()){
    			victoryFlag = true;
    		}
    		// If Game is over, don't increment player, otherwise...
    		else{
    			if (turnComplete){
    				// Cycle through players
    				if (main_turn == gameBoard.getNumTeams()){
    					main_turn = 1;	// if this is max teamNum, reset to 1
    				}else{
    					main_turn++;
    				}
    			turnComplete = false; // reset for next turn
    			}
    		}
    		
    		// Update Pieces (right now, only affects Grunt
    		mainModel.update();
    		
    		// Update GameImage   		
        	gen_gameImg();
        	
        	turnState = 0;
	    	if (debugOn == 1)	{System.out.println("DEBUG: Turn Complete");}

    		return true;
    	}
    }

  // from tutorial code (no idea what this is doing)
  private Runnable frameUpdate = new Runnable() {
	@Override
	synchronized public void run() {
		System.out.println("run() executed");
		
		if (victoryFlag){
			
			Piece victor = mainModel.pcList.get(0);
			
			// Update Victory text
			((TextView)findViewById(R.id.line_1)).setText("VICTORY");
			((TextView)findViewById(R.id.line_2)).setText(victor.getstring_ID() 
					+ " " + victor.getInt_ID());
		}
		
		return;
	}
  };

  private void reset_All(){
    	mainModel = new Model(5,5,gameBoard.numTeams, gameBoard.numElite
    			, gameBoard.numGrunts);
  		gameBoard.gbModel = mainModel;
  		
  		mainModel.update();
  		
        // Randomize First player
        Random firstTeam = new Random();
       
        main_turn = firstTeam.nextInt(gameBoard.getNumTeams())+1;
        mainModel.team_turn = main_turn;
  		
  		turnState = 0;
  		sel_hex0 = null;
  		sel_hex1 = null;
  		victoryFlag = false;
  		
			// Update Victory text
			((TextView)findViewById(R.id.line_1)).setText("To the DEATH!");
			((TextView)findViewById(R.id.line_2)).setText(" ");
  		gen_gameImg();
  }
}
