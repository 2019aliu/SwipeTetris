package edu.tjhsst.fortylines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import edu.tjhsst.fortylines.views.GameView;
import edu.tjhsst.fortylines.GameLogic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    private static Context context;

    private int width;
    private int height;

//    Context mContext = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        GameActivity.context = getApplicationContext();

        // Instantiate the gesture detector with the application context and an implementation of GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this, new MyGestureListener(this));
        GameView gv = findViewById(R.id.gameview);



        // Screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        Log.d(DEBUG_TAG, "Width: " + width + " , Height: " + height);
    }

    public static Context getAppContext() {
        return GameActivity.context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);

        this.mDetector.onTouchEvent(event);
        switch(action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG,"Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(DEBUG_TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(DEBUG_TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(DEBUG_TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }

    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        private static final int SWIPE_THRESHOLD = 270;
        private static final int SWIPE_VELOCITY_THRESHOLD = 150;


        private GameActivity g;
        public MyGestureListener (GameActivity ga) {
            g = ga;
        }

        // required to implement, so make a filler
        @Override
        public boolean onDown(MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            if (action==MotionEvent.ACTION_MOVE) {
//                onDragDown();
                Log.d(DEBUG_TAG,"Action was MOVE");
                return true;
            }
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = event2.getY() - event1.getY();
                float diffX = event2.getX() - event1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                    else {
                        if (diffX > 0) {
                            onDragRight();
                        } else {
                            onDragLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeDown();
                    } else {
                        onSwipeUp();
                    }
                    result = true;
                }
                else if(diffY > 0) {
                    onDragDown();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            return result;
        }

        // process left (CCW) vs. right (CW) rotation
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            if (event.getX() <= width/2) {
                onTapLeft();
            }
            else {
                onTapRight();
            }
            Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
            return true;
        }

        // rotate 180
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            GameLogic.rotate180();
            Log.d(DEBUG_TAG, "You double tapped!");
            Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
//            GameLogic.drawGhostPiece();
            return true;
        }

        // shift piece left
        public void onDragLeft() {
            GameLogic.moveLeft();
            Log.d(DEBUG_TAG, "You dragged left!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "Move left", Toast.LENGTH_SHORT).show();
        }

        // shift piece right
        public void onDragRight() {
            GameLogic.moveRight();
            Log.d(DEBUG_TAG, "You dragged right!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "Move right", Toast.LENGTH_SHORT).show();
        }

        // rotate CCW
        public void onTapLeft() {
            GameLogic.gameRotateCCW();
            Log.d(DEBUG_TAG, "You tapped the left!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "Rotate CCW", Toast.LENGTH_SHORT).show();
        }

        // rotate CW
        public void onTapRight() {
            GameLogic.gameRotateCW();
            Log.d(DEBUG_TAG, "You tapped the right!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "Rotate CW", Toast.LENGTH_SHORT).show();
        }


        public void onDragDown() {
            GameLogic.softDrop();
            Log.d(DEBUG_TAG, "You dragged down!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(GameActivity.this, "Soft Drop", Toast.LENGTH_SHORT).show();
        }

        // Hard drop
        public void onSwipeDown() {
            GameLogic.hardDrop();
            Log.d(DEBUG_TAG, "You swiped down!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "Hard drop", Toast.LENGTH_SHORT).show();
        }

        // Hold
        public void onSwipeUp() {
            GameLogic.hold();
            Log.d(DEBUG_TAG, "You swiped up!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "Hold", Toast.LENGTH_SHORT).show();
        }

        // DAS right
        public void onSwipeRight() {
            GameLogic.dasRight();
            Log.d(DEBUG_TAG, "You swiped right!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "DAS right", Toast.LENGTH_SHORT).show();
        }

        // DAS left
        public void onSwipeLeft() {
            GameLogic.dasLeft();
            Log.d(DEBUG_TAG, "You swiped left!");
//            GameLogic.drawGhostPiece();
//            Toast.makeText(context, "DAS left", Toast.LENGTH_SHORT).show();
        }


    }

}
