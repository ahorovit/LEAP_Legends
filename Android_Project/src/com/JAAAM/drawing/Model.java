package com.JAAAM.drawing;

import java.util.List;

import com.authorwjf.gamedevtut05.R;
//import com.JAAAM.drawing.Tile;
import com.JAAAM.pieces.*;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;










import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;



//#########################################################################
//								class Model:    
//							  -----------------
//#########################################################################
public class Model{

	//List of gameObj's
	public List<GameObj> objList = new ArrayList<GameObj>();	
	//List of Tiles
	public List<Tile> tileList = new ArrayList<Tile>();
	//List of Pieces
	public List<Piece> pcList = new ArrayList<Piece>();
	//List of Corpses
	public List<Corpse> corpseList = new ArrayList<Corpse>();
	//List of highlighted tiles
	public List<Tile> hiTiles = new ArrayList<Tile>();	

	
	// Grid dimensions
	private int numRows, numCols;
	public int team_turn; // Keep track of the turn
	public boolean valid_sel; // highlight differently if selecting valid piece

	
	//  ----------------------------------
	//				Constructors:    
	//  ----------------------------------
	public Model(int x, int y, int numTeams, int numElite, int numGrunts){

		// Create Grid
		numRows = x;
		numCols = y;	
		
		int numTiles = 0; // counter for System.output
		
		//-----------------
		// Create tiles
		//-----------------		
		for (int i = 0; i < x; i++){
			for (int j = 0; j < y; j++){
				
				tileList.add(new Tile(new Point(i,j)));
				numTiles++;
			}
		}
		System.out.println("Game Model ("+numTiles+" Tiles) Constructed");
		
		//-----------------
		// Create Pieces
		//-----------------
		
		// Each team gets the same number of Grunts and Elite
		for (int this_team = 1; this_team <= numTeams; this_team++){
		
			// Make elite first
			for (int team_ctr = 0; team_ctr < numElite; team_ctr++)
			{
				// Pieces get int_IDs in the order of their instantiation
				Piece this_piece = new Elite(new Point(getRandomHex()), pcList.size(), this_team);
				pcList.add(this_piece);
				
				// set tiles to occupied
				this_piece.getTile(tileList).isOccupied = true;			
			}
			
			// Now make the grunts
			for (int team_ctr = 0; team_ctr < numGrunts; team_ctr++)
			{
				// Pieces get int_IDs in the order of their instantiation
				Piece this_piece = new Grunt(new Point(getRandomHex()), pcList.size(), this_team);
				pcList.add(this_piece);
				
				// set tiles to occupied
				this_piece.getTile(tileList).isOccupied = true;			
			}
		}

		// Load all objects into objList
		objList.clear();
		objList.addAll(tileList);
		objList.addAll(pcList);

		// Give model to all gameObjects
		GameObj this_obj = null;
		for (ListIterator<GameObj> iter = objList.listIterator(); iter.hasNext();){
			this_obj = iter.next();
			this_obj.setObjModel(this);
		}
	}

	// Getters/Setters
	public int getNumRows() {
		return numRows;
	}
	public int getNumCols() {
		return numCols;
	}
    
	
    //#########################################################################
    //  						Touch Processing:    
    //							-----------------
    //#########################################################################


	
	// get the selected tile
	public Tile nearestHex(float x, float y) {

		Point tile_center = null;
		float min_distance = 999999999;  // Simply a large starting number so ALL tiles will be closer.
		Tile select_tile = null;
		Tile this_tile = null;
		
		// Iterate through all tiles and find the closest to touch event
		for (ListIterator<Tile> iter = tileList.listIterator(); iter.hasNext();)
		{
			this_tile = iter.next(); 
			
			//tile_center = this_tile.of_to_SpriteCoord(this_tile.)getTile_center());
			tile_center = this_tile.getTile_center();
			
			float x_dist = (tile_center.x - x);
			float y_dist = (tile_center.y - y);
			
			float dist = (float) Math.sqrt(Math.pow(x_dist,2.0) 
					+ Math.pow(y_dist,2.0));
			
			if (dist < min_distance)
			{	
				min_distance = dist;
				select_tile = this_tile;
			}
		}
		return select_tile;
	}

	
	// Determine if Piece Should be moved
	public boolean isConfirmed(Tile in_hex){
		
		Tile this_tile = null;
		
		// Iterate through all highlighted tiles to check if move was confirmed
		for (ListIterator<Tile> iter = hiTiles.listIterator(); iter.hasNext();)
		{
			this_tile = iter.next(); 
			// If match is found, return true
			if (this_tile.getOfCoord().x == in_hex.getOfCoord().x
					&& this_tile.getOfCoord().y == in_hex.getOfCoord().y)
			{
				return true;
			}
		}
		return false;
	}
	
	// Random Hex Method
	private Point getRandomHex() {
		Random r = new Random();
		boolean isVacant = false;
		
		do{
			Tile this_tile = tileList.get(r.nextInt(tileList.size()));
			
			if (!this_tile.isOccupied){
				return this_tile.getOfCoord();
			}
			
		}while(isVacant == false);
		
		System.out.println("getRandomHex() failed");
		return new Point(-1,-1);
	}
	
	// retrieve a piece with ID char and #
	public GameObj getPc_byID(char char_ID, int id_num){
		GameObj this_piece = null;
		for (ListIterator<Piece> iter = pcList.listIterator(); iter.hasNext();){
			this_piece = iter.next(); 
			
			if (this_piece.getChar_ID() == char_ID 
					&& this_piece.getInt_ID() == id_num)
			{
				return this_piece;
			}
		}
		System.out.println("ERROR: Model.getPc_byID() has failed");
		return new Corpse();
	}
	
	
	// Update pieces
	public void update(){
		
		Piece this_piece = null;
		for (ListIterator<Piece> iter = pcList.listIterator(); iter.hasNext();){
			this_piece = iter.next(); 
			this_piece.update();
		}
	}
	
	
	public boolean checkVictory(){
		
		int teamMin = 99, teamMax = 0;
		Piece this_piece = null;
		for (ListIterator<Piece> iter = pcList.listIterator(); iter.hasNext();){
			this_piece = iter.next();
			
			if (!this_piece.isDead){
				if (this_piece.getTeam() < teamMin)
					teamMin = this_piece.getTeam();
				if (this_piece.getTeam() > teamMax)
					teamMax = this_piece.getTeam();
			}
		}
		
		if (teamMax-teamMin == 0){
			return true;
		}
		else{
			return false;
		}
	}

}