package com.JAAAM.pieces;

import java.util.Random;

import com.JAAAM.drawing.Model;
import com.authorwjf.gamedevtut05.R;






//import com.JAAAM.drawing.CubeCoord;
import android.graphics.Point;






import java.lang.Math;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;



//#########################################################################
//								class GameObj:    
//							  -----------------
//#########################################################################
public class GameObj{
	
	protected Model objModel = null;			//gets handle on gameModel

	
	// Distinguish various subtypes
	protected int int_ID;
	protected int team;
	protected char char_ID;	
	protected String string_ID;	

	// GameObj image
	protected Bitmap sprite = null; // naked dude or hex
	
	// png print coordinates 
	//protected Point spriteCoord;
	//private Point tile_center = null;;
	//protected int spriteWidth;
	//protected int spriteHeight;

	// Hex Coordinates (Cubic, Axial, Offset systems) 
	protected CubeCoord cuCoord;
	protected Point axCoord, ofCoord;
	
		
	//  ----------------------------------
	//				Constructors:    
	//  ----------------------------------
	
	// Default Constructor 
	public GameObj(){
		
		int_ID = -1;
		team = -1;
		char_ID = '0';	
		
		string_ID = new String();	
		ofCoord = new Point(-1,-1);
		cuCoord = new CubeCoord(-1,-1,-1);
//		spriteCoord = new Point(-1,-1);

		// Convert offset coordinates to Sprite coordinates
//		spriteCoord = new Point(of_to_SpriteCoord(ofCoord));
	}

/*	
	//  Primary Constructor	
	public GameObj(Point ofCoord, char char_ID, String string_ID, int team){

		// I want GameObject to get a handle on the gameModel 
		// here. For now, it's in the Gameboard with the sprite
		// loading.
		
		this.ofCoord = ofCoord;	
		this.char_ID = char_ID;
		this.string_ID = string_ID;	
		this.team = team;	
		
		// Offset Cubic Disambiguation variables
		int q = ofCoord.x;
		int r = ofCoord.y;
		
		// Convert Offset coordinates to Cube coordinates
		cuCoord = new CubeCoord(q,  -q-(r-((q-(q%2))/2)),  r-((q-(q%2))/2));	
		// Convert offset coordinates to Axial coordinates
		axCoord = new Point(cuCoord.x, cuCoord.z);	
		// Convert offset coordinates to Sprite coordinates
		spriteCoord = new Point(of_to_SpriteCoord(ofCoord));
		setSprite_center(new Point(-1,-1));
	}
*/	
	
	
	//  ----------------------------------
	//				Methods:    
	//  ----------------------------------
		
	

	
	// Convert Offset Coords into other coord systems
	protected void updateCoords(Point ofCoord){
		
		// Offset Cubic Disambiguation variables
		int q = ofCoord.x;
		int r = ofCoord.y;
		
		// Convert Offset coordinates to Cube coordinates
		cuCoord.x = q;
		cuCoord.y = -q-(r-((q-(q%2))/2));
		cuCoord.z = r-((q-(q%2))/2);
	}
	
	
	
	
	
	// Get tiles in Radius = 1
	public List <Tile> getRange(char range_code){
		
		List <CubeCoord> targetCoords = new ArrayList<CubeCoord>();
		List <Tile> range_tiles = new ArrayList<Tile>();
		
		switch (range_code){
		
		case '1':
			
			// all 6 cuCoord targetCoordss for R1 calculation
			targetCoords.add(new CubeCoord(cuCoord.x+1, cuCoord.y-1, cuCoord.z));
			targetCoords.add(new CubeCoord(cuCoord.x+1, cuCoord.y, cuCoord.z-1));
			targetCoords.add(new CubeCoord(cuCoord.x, cuCoord.y+1, cuCoord.z-1));
			targetCoords.add(new CubeCoord(cuCoord.x-1, cuCoord.y+1, cuCoord.z));
			targetCoords.add(new CubeCoord(cuCoord.x-1, cuCoord.y, cuCoord.z+1));
			targetCoords.add(new CubeCoord(cuCoord.x, cuCoord.y-1, cuCoord.z+1));

			break;
			
		case '2':
			// all 12 cuCoord targetCoordss for R1 calculation
		//Direct tiles:
			targetCoords.add(new CubeCoord(cuCoord.x+2, cuCoord.y-2, cuCoord.z));
			targetCoords.add(new CubeCoord(cuCoord.x+2, cuCoord.y, cuCoord.z-2));
			targetCoords.add(new CubeCoord(cuCoord.x, cuCoord.y+2, cuCoord.z-2));
			targetCoords.add(new CubeCoord(cuCoord.x-2, cuCoord.y+2, cuCoord.z));
			targetCoords.add(new CubeCoord(cuCoord.x-2, cuCoord.y, cuCoord.z+2));
			targetCoords.add(new CubeCoord(cuCoord.x, cuCoord.y-2, cuCoord.z+2));
		//Diagonal tiles:	
			targetCoords.add(new CubeCoord(cuCoord.x+1, cuCoord.y+1, cuCoord.z-2));
			targetCoords.add(new CubeCoord(cuCoord.x+2, cuCoord.y-1, cuCoord.z-1));
			targetCoords.add(new CubeCoord(cuCoord.x-1, cuCoord.y+2, cuCoord.z-1));
			targetCoords.add(new CubeCoord(cuCoord.x-2, cuCoord.y+1, cuCoord.z+1));
			targetCoords.add(new CubeCoord(cuCoord.x+1, cuCoord.y-2, cuCoord.z+1));
			targetCoords.add(new CubeCoord(cuCoord.x-1, cuCoord.y-1, cuCoord.z+2));
			
			break;
	
		}
		
		// for all targets, find tiles with matching coords
		CubeCoord this_coord = null;
		Tile this_tile = null;
		for (ListIterator<CubeCoord> iter = targetCoords.listIterator(); iter.hasNext();)
		{
			this_coord = iter.next(); 		

			// Look through tiles for matching coords
			for (ListIterator<Tile> tile_iter = objModel.tileList.listIterator(); tile_iter.hasNext();){
				this_tile = tile_iter.next(); 

				if (this_tile.getCuCoord().x == this_coord.x && this_tile.getCuCoord().y == this_coord.y
							&& this_tile.getCuCoord().z == this_coord.z)
				{
					range_tiles.add(this_tile);
				}
			}
		}
		return range_tiles;
	}
	
	
	
	
	
	// getPiece occupying tile
	public Piece getPiece(List <Piece> pieces){
		
		Piece this_piece = null;

		for (ListIterator<Piece> iter = pieces.listIterator(); iter.hasNext();)
		{
			this_piece = iter.next(); 
			
			if (this_piece.getOfCoord().x == ofCoord.x && this_piece.getOfCoord().y == ofCoord.y)
			{
				return this_piece;
			}
		}
		System.out.println("Piece not found by Tile.getPiece()");
		return this_piece;

	}
	
	
	// This function is not working properly!!!  Or is it??
		// Translate offset Coords (x,y) to Sprite coordinates for drawing	
	public Point of_to_SpriteCoord(Point in_hex) {
		
		// minimum pixel boundary for Galaxy S3/Hex_v04 setup
	    int minX = 5;
	    int minY = 3;

	    // Pixel spacing for Galaxy S3/Hex_v04 setup
	    int gridSpacing_X = 109;
	    int gridSpacing_Y = 128;
	    int oddRow_offset = 63;
		
	    // We need to figure out Relative Layout so these values
	    // are calculated dynamically --> layout file/emulator independent
	    
	    // ...The following would then work for any input:	    
	    // Generic Conversion:
		int x = minX + in_hex.x * gridSpacing_X;
 	    int y = minY + (in_hex.y * gridSpacing_Y) + (in_hex.x%2)*(oddRow_offset);
 	    
		return new Point (x,y);
	}

	
	
	//  ----------------------------------
	//			  Getters/Setters:    
	//  ----------------------------------
	
	public char getChar_ID() {
		return char_ID;
	}
	
	public String getstring_ID() {
		return string_ID;
	}
	
	public CubeCoord getCuCoord() {
		return cuCoord;
	}
	
	public Point getAxCoord() {
		return axCoord;
	}
	
	public Point getOfCoord() {
		return ofCoord;
	}

//	public Point getSpriteCoord() {
//		return spriteCoord;
//	}
	
	public Bitmap getSprite() {
		return sprite;
	}

	public void setSprite(Bitmap sprite) {
		this.sprite = sprite;
	}
	
//	public void setSpriteCoord(Point spriteCoord) {
//		this.spriteCoord = spriteCoord;
//	}
	
	public void setObjModel(Model objModel) {
		this.objModel = objModel;
	}
	
	
	//expose sprite bounds to controller
	// Find out why synchronizing this makes it work.
//	public int getSpriteWidth() {
//		return spriteWidth;
//	}
	
///	public void setSpriteWidth(int in_width) {
//		spriteWidth = in_width;		
//	}
	
//	synchronized public int getSpriteHeight() {
//		return spriteHeight;
//	}
	
//	public void setSpriteHeight(int in_height) {
//		spriteHeight = in_height;		
//	}
	
	public int getInt_ID() {
		return int_ID;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}



}





