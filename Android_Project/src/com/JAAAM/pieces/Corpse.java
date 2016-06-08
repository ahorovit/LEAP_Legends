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
//								class Corpse:    
//							  -----------------
//#########################################################################
public class Corpse extends GameObj{
	

	

		
	//  ----------------------------------
	//				Constructors:    
	//  ----------------------------------
	
	// Default Constructor --> in case there's a failed find pc by ID
	public Corpse(){}

	
	//  Primary Constructor	
	public Corpse(Point ofCoord, char char_ID, String string_ID){

		this.ofCoord = ofCoord;
		updateCoords(ofCoord);
		this.char_ID = char_ID;
		this.string_ID = string_ID;	
	}
}





