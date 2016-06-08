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
//							 class EliteGrunt:    
//							 -----------------
//#########################################################################
public class Elite extends Grunt{
	
	//  ----------------------------------
	//			Constructors:    
	//  ----------------------------------

	// Standard EliteGrunt
	public Elite(Point ofCoord, int int_ID, int team){
		super();
		
		this.ofCoord = ofCoord;
		updateCoords(ofCoord);			// VERY IMPORTANT FOR ALL UNITS!!!
		this.int_ID = int_ID;
		this.team = team;
		char_ID = 'E';
		string_ID = "Elite"; 
		
		setEnergy(20);
    	System.out.println("Grunt Constructed at ofCoord: " + ofCoord.x + "," + ofCoord.y);
	}



	// Methods:

	// Update currently only does anything for Grunts
	public void update(){
		
	}
	
	// Elite can move anywhere within two tiles
	public List<Tile> getMovTargets(){

		// get R1 tiles
		System.out.println("Piece.cuCoord: ("+cuCoord.x+","+cuCoord.y+","+cuCoord.z+")");
		
		//Here is the only difference from the base class function:
		List<Tile> range_tiles = getRange('1');
		range_tiles.addAll(getRange('2'));			// Radius = 2 is added to list of targets
				
		List<Tile> target_tiles = new ArrayList<Tile>();

		Tile this_tile = null;
		// Remove any that are occupied
		for (ListIterator<Tile> iter = range_tiles.listIterator(); iter.hasNext();)
		{
			this_tile = iter.next(); 

			if(this_tile.isOccupied == false)
			{
				target_tiles.add(this_tile);
			}	
		}
		return target_tiles;
	}
	
	// Implement move: Elite executes stab when moving
	public void movePc(Tile tile_o, Tile tile_f){
		
		// Set origin tile to unoccupied
		tile_o.isOccupied = false;
		
		// Update piece coordinates
		ofCoord = tile_f.getOfCoord();
		updateCoords(ofCoord);
		
		// Set destination file to occupied
		tile_f.isOccupied = true;
		
		// Only difference from GameObj function
		stab(tile_o, tile_f);
	}
	
	// Implement Stab ability: New ability for this class
	protected void stab(Tile tile_o, Tile tile_f){
		
		Tile this_tile = null;
		
		// Get radius = 1 tiles for initial and final tiles
		List<Tile> tileO_R1 = tile_o.getRange('1');
		List<Tile> tileF_R1 = tile_f.getRange('1');

		// Now find the subset of tiles that will get stabbed
		List<Tile> stabTiles = new ArrayList<Tile>();
		
		// Loop through first list and save tile if this_tile is on second list too
		for (ListIterator<Tile> iter = tileO_R1.listIterator(); iter.hasNext();){
			this_tile = iter.next();

			if (tileF_R1.contains(this_tile)){
				stabTiles.add(this_tile);
			}
		}
		
		//Now that we have the Affected tiles, stab any victims
		for (ListIterator<Tile> iter = stabTiles.listIterator(); iter.hasNext();){
			this_tile = iter.next();

			if (this_tile.isOccupied){
				Piece victim = this_tile.getPiece(objModel.pcList);
				victim.takeHit(8);  //  Elite stab does 10 dmg
			}
		}
	}
	
}
