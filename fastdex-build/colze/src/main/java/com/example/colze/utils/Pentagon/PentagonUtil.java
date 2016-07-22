package com.example.colze.utils.Pentagon;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class PentagonUtil {
	private Context mContext;
	public PentagonUtil(Context context)
	{
		mContext = 	context;
	}
	public void drawPentagonWithPointArr(List<CGPoint> points,ImageView iv_canvas,int imageId){
    	Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.GRAY);
        Resources rec = mContext.getResources();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) rec.getDrawable(imageId);
        Bitmap baseBitmap = bitmapDrawable.getBitmap();
        float multiple = (0.6f)*90;
        float center = (1.0f - 0.6f)*200/2;
        float scale =(float)(180.00/baseBitmap.getWidth());
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newBitmap = Bitmap.createBitmap(baseBitmap, 0, 0, baseBitmap.getWidth(), baseBitmap.getHeight(),matrix, true);
        Canvas tempCanvas = new Canvas(newBitmap);
    	if(points.size()==5){
    		for(int i=0; i<5; i++){
    			if(i<4){
    				tempCanvas.drawLine((float)points.get(i).x*multiple + center, (float)points.get(i).y*multiple + center, (float)points.get(i+1).x*multiple + center, (float)points.get(i+1).y*multiple + center, paint);
    			}else if(i==4){
    				tempCanvas.drawLine((float)points.get(i).x*multiple + center, (float)points.get(i).y*multiple + center, (float)points.get(0).x*multiple + center, (float)points.get(0).y*multiple + center, paint);
    			}
    		}
    	}
    	iv_canvas.setImageBitmap(newBitmap);
    }
	public List<CGPoint> getPentagonWithPointArr(Double psOldFloat[]){
    	List<CGPoint> pantage = new ArrayList<CGPoint>();
    	for (int i=0;i<5; i++) {
    		pantage.add(new CGPoint());
    	}
    	double psFloat[] = new double[5];
    	psFloat[0] = psOldFloat[0]/100;
    	psFloat[1] = psOldFloat[1]/100;
    	psFloat[2] = psOldFloat[2]/100;
    	psFloat[3] = psOldFloat[3]/100;
    	psFloat[4] = psOldFloat[4]/100;
        for (int i=0;i<5; i++) {
            if (psFloat[i]<0.1){
            	psFloat[i]=0.1;
            }
        }
        pantage.get(0).x=1;
        pantage.get(0).y=1-psFloat[0];
        pantage.get(1).x=psFloat[1]*Math.sin(72*Math.PI/180)+1;
        pantage.get(1).y=1-psFloat[1]*Math.cos(72*Math.PI/180);
        pantage.get(2).x=1+psFloat[2]*Math.cos(54*Math.PI/180);
        pantage.get(2).y=1+psFloat[2]*Math.sin(54*Math.PI/180);
        pantage.get(3).x=1-psFloat[3]*Math.cos(54*Math.PI/180);
        pantage.get(3).y=1+psFloat[3]*Math.sin(54*Math.PI/180);
        pantage.get(4).x=1-psFloat[4]*Math.sin(72*Math.PI/180);
        pantage.get(4).y=1-psFloat[4]*Math.cos(72*Math.PI/180);
        return pantage;
    }
	public class CGPoint {
    	public double x;
    	public double y;
    	public CGPoint(){
    	this.x = 0;
    	this.y = 0;
    	}
    };
}
