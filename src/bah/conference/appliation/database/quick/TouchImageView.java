package bah.conference.appliation.database.quick;

import java.util.ArrayList;

import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.BoothLocation;
import bah.conference.appliation.dataStructures.Corner;
import bah.conference.appliation.dataStructures.Exhibitor;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TouchImageView extends ImageView {

Matrix matrix = new Matrix();
//http://stackoverflow.com/questions/2537238/how-can-i-get-zoom-functionality-for-images
//https://github.com/MikeOrtiz/TouchImageView

// We can be in one of these 3 states
static final int NONE = 0;
static final int DRAG = 1;
static final int ZOOM = 2;
int mode = NONE;

// Remember some things for zooming
PointF last = new PointF();
PointF start = new PointF();
float minScale = 1f;
float maxScale = 20f;
float[] m;

float redundantXSpace, redundantYSpace;

float width, height;
static final int CLICK = 3;
float saveScale = 1f;
float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

ScaleGestureDetector mScaleDetector;
int h = 872;
int w = 720;
int currentHeight;
int currentWidth;
Context context;
/*
    <ScrollView 
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_width="fill_parent">
	    <ImageView 
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:scrollbarStyle="outsideOverlay"
	        android:src="@drawable/ballroom_1664x3324"/>
	</ScrollView>
*/
public TouchImageView(Context context,AttributeSet attr) {
    super(context,attr);
    super.setClickable(true);
    this.context = context;
	if(conf == null)
		 conf = (ConfApp)context.getApplicationContext();
    //this.setBackgroundResource(R.drawable.ballroom_1664x3324);
    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    matrix.setTranslate(1f, 1f);
    m = new float[9];
    setImageMatrix(matrix);
    setScaleType(ScaleType.MATRIX);

    setOnTouchListener(new OnTouchListener() {

        //@Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);
            currentWidth = v.getWidth();
            currentHeight = v.getHeight();
            matrix.getValues(m);
            float x = m[Matrix.MTRANS_X];
            float y = m[Matrix.MTRANS_Y];
            PointF curr = new PointF(event.getX(), event.getY());

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    last.set(event.getX(), event.getY());
                    start.set(last);
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        float deltaX = curr.x - last.x;
                        float deltaY = curr.y - last.y;
                        float scaleWidth = Math.round(origWidth * saveScale);
                        float scaleHeight = Math.round(origHeight * saveScale);
                        if (scaleWidth < width) {
                            deltaX = 0;
                            if (y + deltaY > 0)
                                deltaY = -y;
                            else if (y + deltaY < -bottom)
                                deltaY = -(y + bottom); 
                        } else if (scaleHeight < height) {
                            deltaY = 0;
                            if (x + deltaX > 0)
                                deltaX = -x;
                            else if (x + deltaX < -right)
                                deltaX = -(x + right);
                        } else {
                            if (x + deltaX > 0)
                                deltaX = -x;
                            else if (x + deltaX < -right)
                                deltaX = -(x + right);

                            if (y + deltaY > 0)
                                deltaY = -y;
                            else if (y + deltaY < -bottom)
                                deltaY = -(y + bottom);
                        }
                        matrix.postTranslate(deltaX, deltaY);
                        last.set(curr.x, curr.y);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    mode = NONE;
                    int xDiff = (int) Math.abs(curr.x - start.x);
                    int yDiff = (int) Math.abs(curr.y - start.y);
                    if (xDiff < CLICK && yDiff < CLICK){
                        Matrix inverse = new Matrix();
                        if(matrix.invert(inverse)){
                        	float[] touchPoint = new float[] {event.getX(), event.getY()};
                        	inverse.mapPoints(touchPoint);
                        	doWork((int)touchPoint[0],(int)touchPoint[1]);
                        	//getUserInput((int)touchPoint[0],(int)touchPoint[1]);
                        }
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
            }
            setImageMatrix(matrix);
            invalidate();
            return true; // indicate event was handled
        }

    });
}

@Override
public void setImageBitmap(Bitmap bm) { 
    super.setImageBitmap(bm);
    bmWidth = bm.getWidth();
    bmHeight = bm.getHeight();
}

public void setMaxZoom(float x)
{
    //maxScale = x;
}

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mode = ZOOM;
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
    	float mScaleFactor = (float)Math.min(Math.max(.85f, detector.getScaleFactor()), 1.25);
        float origScale = saveScale;
        saveScale *= mScaleFactor;
        if (saveScale > maxScale) {
            saveScale = maxScale;
            mScaleFactor = maxScale / origScale;
        } else if (saveScale < minScale) {
            saveScale = minScale;
            mScaleFactor = minScale / origScale;
        }
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        if (origWidth * saveScale <= width || origHeight * saveScale <= height) {
            matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
            if (mScaleFactor < 1) {
                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (mScaleFactor < 1) {
                    if (Math.round(origWidth * saveScale) < width) {
                        if (y < -bottom)
                            matrix.postTranslate(0, -(y + bottom));
                        else if (y > 0)
                            matrix.postTranslate(0, -y);
                    } else {
                        if (x < -right) 
                            matrix.postTranslate(-(x + right), 0);
                        else if (x > 0) 
                            matrix.postTranslate(-x, 0);
                    }
                }
            }
        } else {
            matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
            matrix.getValues(m);
            float x = m[Matrix.MTRANS_X];
            float y = m[Matrix.MTRANS_Y];
            if (mScaleFactor < 1) {
                if (x < -right) 
                    matrix.postTranslate(-(x + right), 0);
                else if (x > 0) 
                    matrix.postTranslate(-x, 0);
                if (y < -bottom)
                    matrix.postTranslate(0, -(y + bottom));
                else if (y > 0)
                    matrix.postTranslate(0, -y);
            }
        }
        return true;

    }
}

@Override
protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
{
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    width = MeasureSpec.getSize(widthMeasureSpec);
    height = MeasureSpec.getSize(heightMeasureSpec);
    //Fit to screen.
    float scale;
    float scaleX =  (float)width / (float)bmWidth;
    float scaleY = (float)height / (float)bmHeight;
    scale = Math.min(scaleX, scaleY);
    matrix.setScale(scale, scale);
    setImageMatrix(matrix);
    saveScale = 1f;

    // Center the image
    redundantYSpace = (float)height - (scale * (float)bmHeight) ;
    redundantXSpace = (float)width - (scale * (float)bmWidth);
    redundantYSpace /= (float)2;
    redundantXSpace /= (float)2;

    matrix.postTranslate(redundantXSpace, redundantYSpace);

    origWidth = width - 2 * redundantXSpace;
    origHeight = height - 2 * redundantYSpace;
    right = width * saveScale - width - (2 * redundantXSpace * saveScale);
    bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
    setImageMatrix(matrix);
}
































ArrayList<BoothLocation> boothLocations = new ArrayList<BoothLocation>();
BoothLocation booth = null;
ConfApp conf = null;
public void getUserInput(final float x, final float y){
	AlertDialog.Builder alert = new AlertDialog.Builder(context);

	alert.setTitle("Title");
	alert.setMessage("Message");

	// Set an EditText view to get user input 
	final EditText input = new EditText(context);
	alert.setView(input);

	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	public void onClick(DialogInterface dialog, int whichButton) {
		if(booth == null){
			booth = new BoothLocation();
			booth.mapName = "Exhibition Hall";
		}
		String value = input.getText().toString();		
		booth.addPoint((int)x, (int)y);
		if(value.length()>0){
			booth.setBoothName(value);
			conf.local.insertBooth(booth);
			String output = "";
			for(Corner corner : booth.getCorners())
				output += "x:"+corner.x + " y:"+corner.y + ", ";
			Log.wtf("test", output);
			booth = new BoothLocation();
		}
	  }
	});

	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	  public void onClick(DialogInterface dialog, int whichButton) {
		  booth = new BoothLocation();
	  }
	});

	alert.show();
}



//save points
//cancel
//save booth and restart
boolean interact = false;
public void enableInteraction(boolean a){
	interact = a;
}
Toast toast = null;
public void doWork(int x, int y){
	if(interact){
		ArrayList<BoothLocation> clicked = new ArrayList<BoothLocation>();
		x = (int) ((w/currentWidth)*x);
		y = (int) ((h/currentHeight)*y);
		ArrayList<BoothLocation> booths = conf.local.getBoothClicked(x, y);
		for(BoothLocation booth : booths){
			if(booth.contains(x, y)){
				Exhibitor exhibitor = conf.local.getExhibitor(booth);
				if(exhibitor !=null){
					if(toast==null)
						toast = toast.makeText(context, exhibitor.company, Toast.LENGTH_SHORT);
					toast.setText(exhibitor.company);
					toast.show();
					break;
				}
			}
		}
	}
}





















}