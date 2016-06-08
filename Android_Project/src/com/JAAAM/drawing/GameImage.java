package com.JAAAM.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import com.authorwjf.gamedevtut05.R;
//import com.JAAAM.actions.*;
import com.JAAAM.pieces.*;

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
//  						 class GameBoard:    
//							-----------------
//#########################################################################
public class GameImage extends View{
	
	// Get a handle on the canvas
	private Canvas canvas = null;
	
	// GameImage needs access to the gameModel
	public Model gbModel = null;
	public int numCols = 5;
	public int numRows = 5;
	public int numTeams = 2;
	public int numGrunts = 4;
	public int numElite = 1;
	
	// Sprites to load
	private Bitmap kill_X = null; // kill image
	private Bitmap dude_1 = null; // grunt image
	private Bitmap dude_2 = null;
	private Bitmap phalanx1 = null; // phalanx image
	private Bitmap phalanx2 = null; // grunt image
	private Bitmap elite_1 = null;
	private Bitmap elite_2 = null;
	private Bitmap hex_01 = null; 
	private Bitmap hex_02 = null; 
	private Bitmap hex_03 = null; 
	private Bitmap hi_hex = null; // translucent blue hex
	private Bitmap inv_sel = null; // translucent blue hex
	private Bitmap team_1 = null; // translucent blue hex
	private Bitmap team_2 = null; // translucent blue hex
	private Bitmap corpse = null; // bones

	
	
	//  ----------------------------------
	//				Constructor:    
	//  ----------------------------------
	public GameImage(Context context, AttributeSet aSet) {
		super(context, aSet);
		System.out.println("Initiating GameBoard constructor");

		// Instantiate gbModel --> I would like this to be in Main!!
		// >>This would require passing gameModel into GameImage Constructor
		gbModel = new Model(numCols,numRows,numTeams, numElite, numGrunts);
	
		// Load All sprites
		kill_X = BitmapFactory.decodeResource(getResources(), R.drawable.kill_x);
		dude_1 = BitmapFactory.decodeResource(getResources(), R.drawable.dude_01);
		dude_2 = BitmapFactory.decodeResource(getResources(), R.drawable.dude_2);
		phalanx1 = BitmapFactory.decodeResource(getResources(), R.drawable.phalanx_01);
		phalanx2 = BitmapFactory.decodeResource(getResources(), R.drawable.phalanx_02);
		elite_1 = BitmapFactory.decodeResource(getResources(), R.drawable.elite_1);
		elite_2 = BitmapFactory.decodeResource(getResources(), R.drawable.elite_2);
		hex_01 = BitmapFactory.decodeResource(getResources(), R.drawable.hex_01);
		hex_02 = BitmapFactory.decodeResource(getResources(), R.drawable.hex_02);
		hex_03 = BitmapFactory.decodeResource(getResources(), R.drawable.hex_03);
		hi_hex = BitmapFactory.decodeResource(getResources(), R.drawable.hi_hex);
		inv_sel = BitmapFactory.decodeResource(getResources(), R.drawable.hi_hex_02);
		team_1 = BitmapFactory.decodeResource(getResources(), R.drawable.player_blue);
		team_2 = BitmapFactory.decodeResource(getResources(), R.drawable.player_red);
		corpse = BitmapFactory.decodeResource(getResources(), R.drawable.corpse_02);		
	}

	
	// DRAWING function
	@Override
	synchronized public void onDraw(Canvas canvas) {
		
		// logCat outputs for debugging
		System.out.println("GameImage.onDraw() called");
		System.out.println("# highlighted tiles in onDraw(): " + gbModel.hiTiles.size());
		
		// get a handle on the canvas
		this.canvas = canvas;
		
		// Print gameObjects in layers. First Tiles, then Corpses, then Pieces, then Highlighting
		GameObj this_obj = null;
		//First Tiles
		for (ListIterator<Tile> iter = gbModel.tileList.listIterator(); iter.hasNext();)
		{
			this_obj = iter.next();

			drawSprite(this_obj);
		}
		
		//Second, Corpses
		for (ListIterator<Corpse> iter = gbModel.corpseList.listIterator(); iter.hasNext();)
		{
			this_obj = iter.next();
			drawSprite(this_obj);
		}		
		
		
		//Third, Pieces
		Bitmap team_sprite = null; // Local handle for selected sprite
		for (ListIterator<Piece> iter = gbModel.pcList.listIterator(); iter.hasNext();)
		{
			this_obj = iter.next();
			drawSprite(this_obj);
		}
		
		//Last, Highlighting

		// Highlight any tiles in hiTiles
		//Tile this_tile = null; // local tile handle for iteration
		for (ListIterator<Tile> HI_iter = gbModel.hiTiles.listIterator(); HI_iter.hasNext();)
		{
			this_obj = HI_iter.next(); 
			
			Point spriteCoord = of_to_SpriteCoord(this_obj.getOfCoord());

			// Draw highlight hexes
			if (gbModel.valid_sel == true){
				canvas.drawBitmap(hi_hex,spriteCoord.x ,spriteCoord.y ,null);
			}else{
				canvas.drawBitmap(inv_sel,spriteCoord.x ,spriteCoord.y ,null);
			}
		}
	}

	
	// Find appropriate sprite and draw it to the right place
	private void drawSprite(GameObj this_obj){

		Bitmap sprite = null;
		Bitmap team_sprite = null;
				
		// Set Piece Sprite
		switch (this_obj.getChar_ID()){
		case 'T':
			// choose correct tile sprite
			switch (this_obj.getInt_ID()){
			case 1:
				sprite = hex_01; 	break;				
			case 2:
				sprite = hex_02; 	break;				
			case 3:
				sprite = hex_03; 	break;
			}
			break;
		case 'G':
			if(this_obj.getTeam() == 1)
			{
				if (((Grunt)this_obj).isPhalanx){
					sprite = phalanx1;
				}else{
					sprite = dude_1;}
			}
			else 
			{
				if (((Grunt)this_obj).isPhalanx){
					sprite = phalanx2;
				}else{
					sprite = dude_2; 	
				}
			}
			break; 		
		case 'E': 
			if(this_obj.getTeam() == 1)
			{
			sprite = elite_1; break;
			} else {
			sprite = elite_2; break;
			}
		case 'X':
			sprite = corpse;	break;
		}

		// Translate object's ofCoord into SpriteCoords
		Point spriteCoord = of_to_SpriteCoord(this_obj.getOfCoord());

		
		// For touch location in Model, record all tile centers
		if(this_obj instanceof Tile){
			((Tile) this_obj).setTile_center(new Point(spriteCoord.x + sprite.getWidth()/2
												, spriteCoord.y + sprite.getHeight()/2));
		}
		
		// Add Team ring sprite if appropriate
		if (this_obj.getTeam() > 0){
			switch (this_obj.getTeam()){
			case 1:
				team_sprite = team_1; break;
			case 2:
				team_sprite = team_2; break;
			}
			canvas.drawBitmap(team_sprite,spriteCoord.x ,spriteCoord.y ,null);
		}

		// Draw sprite to the canvas
		canvas.drawBitmap(sprite,spriteCoord.x ,spriteCoord.y ,null);

		
		// Print X if piece is hurt
		if(this_obj instanceof Piece && ((Piece)this_obj).isHurt){
			canvas.drawBitmap(kill_X,spriteCoord.x ,spriteCoord.y ,null);
			((Piece)this_obj).isHurt = false;
		}
	}
	
	
	
	// Translate offset Coords (x,y) to Sprite coordinates for drawing	
	private Point of_to_SpriteCoord(Point ofCoord) {
		
		// minimum pixel boundary for Galaxy S3/Hex_v04 setup
	    int minX = 5; // old 5
	    int minY = 3; // old 3

	    // Pixel spacing
	    int gridSpacing_X = findViewById(R.id.the_canvas).getWidth()/6; // old 109
	    int gridSpacing_Y = findViewById(R.id.the_canvas).getHeight()/6; // old 128
	    int oddRow_offset = gridSpacing_Y/2; // old 63
		
  
	    // Generic Conversion:
		int x = minX + ofCoord.x * gridSpacing_X;
 	    int y = minY + (ofCoord.y * gridSpacing_Y) + (ofCoord.x%2)*(oddRow_offset);
 	    
		return new Point (x,y);
	}
	
	
	

	


	public int getNumTeams() {
		return numTeams;
	}

}
