package com.JAAAM.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import com.authorwjf.gamedevtut05.R;
//import com.JAAAM.actions.*;
import com.JAAAM.drawing.*;

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
//  						   class Piece:    
//							-----------------
//#########################################################################
public class Piece extends GameObj{
		
	public boolean isDead = false;
	public boolean isHurt = false;
	public boolean enemyInRange = false;
	
	private int energy;
	
	//  ----------------------------------
	//			Constructors:    
	//  ----------------------------------
	
	// Generic constructor  
	public Piece(){
		super();
	}
	
/*	
	// Constructor with offSet coord input
	public Piece(Point ofCoord, char char_ID, String string_ID){
		super(ofCoord, char_ID, string_ID);
	}
*/
	// coord string_ID and int_ID Constructor (Used by Model when generating Pieces)
	public Piece(Point ofCoord, char char_ID, String string_ID, int int_ID, int team){
		//super(ofCoord, char_ID, string_ID, team);
		super();
		
		this.ofCoord = ofCoord;
		this.char_ID = char_ID;
		this.string_ID = string_ID;
		this.int_ID = int_ID;
		this.team = team;
		
		
		setEnergy(1); // generic pieces have only 1 HP
		
		// At some point, make ability_list static among pieces of a type
		
    	//System.out.println("Piece Constructed at ofCoord: " + ofCoord.x + "," + ofCoord.y);
	}
	
	//  ----------------------------------
	//				Methods:    
	//  ----------------------------------
	
	
	// Update currently only does anything for Grunts
	public void update(){
		
	}
	
	
	// Retrieve Tile piece is on
	public Tile getTile(List <Tile> tiles){
		
		Tile this_tile = null;

		for (ListIterator<Tile> iter = tiles.listIterator(); iter.hasNext();)
		{
			this_tile = iter.next(); 
			
			if (this_tile.getOfCoord().x == ofCoord.x && this_tile.getOfCoord().y == ofCoord.y)
			{
				return this_tile;
			}
		}
		System.out.println("Piece not found by Tile.getTile()");
		return this_tile;
	}
	
	
	
	// Generate list of target tiles for Ability
	public List<Tile> getMovTargets(){

		// enemyInRange has no impact on the Piece moving. It is the derived classes
		// that need to do different things based on this variable
				
		
		// get R1 tiles
		System.out.println("Piece.cuCoord: ("+cuCoord.x+","+cuCoord.y+","+cuCoord.z+")");
		List<Tile> r1_tiles = getRange('1');
		List<Tile> target_tiles = new ArrayList<Tile>();

		Tile this_tile = null;
		// Remove any that are occupied
		for (ListIterator<Tile> iter = r1_tiles.listIterator(); iter.hasNext();)
		{
			this_tile = iter.next(); 

			if(this_tile.isOccupied == false)
			{
				target_tiles.add(this_tile);
				
			}	
		}
		return target_tiles;
	}

	
	// Implement move
	public void movePc(Tile tile_o, Tile tile_f){
		
		// Set origin tile to unoccupied
		tile_o.isOccupied = false;
		
		// Update piece coordinates
		ofCoord = tile_f.getOfCoord();
		updateCoords(ofCoord);
		
		// Set destination file to occupied
		tile_f.isOccupied = true;

	}
	
	protected void takeHit(int damage){
		isHurt = true;
		energy -= damage;
		if (energy <= 0){
			die();
		}
	}
	
	// kill piece
	protected void die(){
		
		// Create proxy Corpse
		objModel.corpseList.add(new Corpse(ofCoord, 'X', string_ID));
		System.out.println("# Corpses: "+ objModel.corpseList.size());
		
		getTile(objModel.tileList).isOccupied = false;
		
		// Remove Piece from pcList
		int index = 0; 
		int saveIndex = -1;
		Piece this_piece = null;
		for (ListIterator<Piece> iter = objModel.pcList.listIterator(); iter.hasNext();)
		{
			this_piece = iter.next();

			if (this_piece.char_ID == char_ID && this_piece.int_ID == int_ID){
				saveIndex = index;
				objModel.pcList.remove(saveIndex);		
				break;
			}
			index++;
		}
			System.out.println(getstring_ID() + " "+getInt_ID()+" has died.");
	}


	public int getEnergy() {
		return energy;
	}


	public void setEnergy(int energy) {
		this.energy = energy;
	}


}
