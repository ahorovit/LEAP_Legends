package com.JAAAM.pieces;

import java.util.Random;

import com.authorwjf.gamedevtut05.R;



//import com.JAAAM.drawing.CubeCoord;
import android.graphics.Point;



import java.lang.Math;
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
//								class Tile:    
//							  -----------------
//#########################################################################
public class Tile extends GameObj{
	
	// Class specific variables
	public boolean isOccupied;
	protected Point tile_center;

	//  ----------------------------------
	//			Constructors:    
	//  ----------------------------------
	
/*	
	public Tile(){
		super('T');
	}
*/	
	// Tile Offset Coord input Constructor (Used by Model when generating tiles)
	public Tile(Point ofCoord){
		super();

		this.ofCoord = ofCoord;
		updateCoords(ofCoord);

		this.char_ID = 'T';
		this.string_ID = "Tile";
		
		// Randomize tile sprite
		Random r = new Random();
		int_ID = r.nextInt(3)+1;

		//setTile_center(new Point(-1,-1));

	}

	//  ----------------------------------
	//			Methods:    
	//  ----------------------------------

		
	// Retrieve Piece occupying tile
	public Piece getPiece(List <Piece> pieces){
		
		Piece this_piece = null;

		for (ListIterator<Piece> iter = pieces.listIterator(); iter.hasNext();)
		{
			this_piece = iter.next(); 
			
			if (this_piece.getChar_ID() != 'X' && this_piece.getOfCoord().x == ofCoord.x 
					&& this_piece.getOfCoord().y == ofCoord.y)
			{
				return this_piece;
			}
		}
		System.out.println("Piece not found by Tile.getPiece()");
		return this_piece;

	}
	
	
	
	public Point getTile_center() {
		return tile_center;
	}
	
	public void setTile_center(Point tile_center) {
		this.tile_center = tile_center;
	}
	
	
}





