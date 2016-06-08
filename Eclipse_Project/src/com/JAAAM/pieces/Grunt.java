package com.JAAAM.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import com.authorwjf.gamedevtut05.R;
//import com.JAAAM.actions.*;





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
//								class Grunt:    
//							 -----------------
//#########################################################################
public class Grunt extends Piece{
	
	public boolean isPhalanx;
	//private boolean mustAttack;
	
	//  ----------------------------------
	//			Constructors:    
	//  ----------------------------------

	
	// Standard Grunt
	public Grunt(Point ofCoord, int int_ID, int team){
		super();
		
		this.ofCoord = ofCoord;
		updateCoords(ofCoord);			// VERY IMPORTANT FOR ALL UNITS!!!
		this.int_ID = int_ID;
		this.team = team;
		char_ID = 'G';
		string_ID = "Grunt"; 
		
		setEnergy(10);
    	System.out.println("Grunt Constructed at ofCoord: " + ofCoord.x + "," + ofCoord.y);
	}

	// Elite Grunt chain constructor
	public Grunt(){
		super();
	}
	
	
	
	// Methods:

	
	public void update(){
	
		// Set isPhalanx to false by default
		isPhalanx = false;
		
		// get R1 tiles
		List<Tile> r1_tiles = getRange('1');

		Tile this_tile = null;
		Piece this_piece = null;
		// Check for any enemies in neighboring tiles
		for (ListIterator<Tile> iter = r1_tiles.listIterator(); iter.hasNext();)
		{
			this_tile = iter.next(); 

			if(this_tile.isOccupied == true){
				this_piece = this_tile.getPiece(objModel.pcList);
				
				if (this_piece.team == this.team ){
					isPhalanx = true;
					break; // No need to keep iterating
				}
			}	
		}
	}
	
	
	// If Grunt neighbors a tile, it can only attack.
	// Else, it can move within Radius = 1
	public List<Tile> getMovTargets(){
			
		// get R1 tiles
		List<Tile> r1_tiles = getRange('1');
		List<Tile> target_tiles = new ArrayList<Tile>();
		Tile this_tile = null;
		Piece this_piece = null;
		// Check for any enemies in neighboring tiles
		for (ListIterator<Tile> iter = r1_tiles.listIterator(); iter.hasNext();)
		{
			this_tile = iter.next(); 
				if(this_tile.isOccupied == true){
				this_piece = this_tile.getPiece(objModel.pcList);
				
				if (this_piece.team != this.team ){
					target_tiles.add(this_tile);
				}
			}	
		}

		// If no enemies were found, look for unoccupied tiles to move to:
		if (target_tiles.size() == 0){
			enemyInRange = false;
			
			for (ListIterator<Tile> iter = r1_tiles.listIterator(); iter.hasNext();)
			{
				this_tile = iter.next(); 

				if(this_tile.isOccupied == false)
				{
					target_tiles.add(this_tile);
				}	
			}
		}else{
			enemyInRange = true;
		}
		return target_tiles;
	}
	
	// Implement move
	public void movePc(Tile tile_o, Tile tile_f){
		
		
		// If there's no enemy, move piece
		if (!enemyInRange){
			// Set origin tile to unoccupied
			tile_o.isOccupied = false;
		
			// Update piece coordinates
			ofCoord = tile_f.getOfCoord();
			updateCoords(ofCoord);
		
			// Set destination file to occupied
			tile_f.isOccupied = true;
		}else{
			// If there is an enemy
			jab(tile_f);
			
		}
	}
	
	
	protected void jab(Tile tile_f){
		Piece victim = tile_f.getPiece(objModel.pcList);
		victim.takeHit(5);  //  Elite stab does 10 dmg	
	}
	
	// Enable Phalanx ability
	protected void takeHit(int damage){
		isHurt = true;
		
		// If grunt is in phalanx mode, cut damage it takes by half
		if(isPhalanx){
			damage /= 2;
		}
		
		setEnergy(getEnergy() - damage);
		if (getEnergy() <= 0){
			die();
		}
	}
}
