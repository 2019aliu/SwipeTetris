package edu.tjhsst.fortylines.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import edu.tjhsst.fortylines.GameLogic;

public class GameView extends View {
    private int test = 0;
    public GameView(Context context) {
        super(context);

        init(null);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        new GameLogic(this, this.getWidth(), this.getHeight());
        GameLogic.fillQueue();
        GameLogic.newPiece();
        Log.d("GAMEVIEW", "REEEEEEEEEEEEEE");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
//        Paint paint = new Paint();
//        paint.setColor(Color.rgb(53, 53, 53));
//        paint.setStrokeWidth(4);
//        for(int k = 0; k < 10; k++) {
//            canvas.drawLine(k * this.getWidth() / 10.0f, 0, k * this.getWidth() / 10.0f, this.getHeight(), paint);
//        }
//        for(int k = 0; k < 20; k++) {
//            canvas.drawLine(0, k * this.getHeight() / 20.0f, this.getWidth(), k * this.getHeight() / 20.0f, paint);
//        }
//        test += 100;
//        paint.setColor(Color.RED);
//        canvas.drawLine(0, 0, test, test, paint);

//        GameLogic.redrawGame(canvas);
        GameLogic.drawGrid(canvas, this.getWidth(), this.getHeight());
        GameLogic.drawBoard(canvas, this.getWidth(), this.getHeight());
        GameLogic.drawActivePiece(canvas, this.getWidth(), this.getHeight());
        GameLogic.drawGhostPiece(canvas, this.getWidth(), this.getHeight());
//        GameLogic.drawBoard(canvas, this.getWidth(), this.getHeight());
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            default:
//                break;
//        }
//        // Invalidate the whole view. If the view is visible.
//        invalidate();
//        return true;
//    }

    public void testing(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        canvas.drawLine(0, 0, this.getWidth()/2f, this.getHeight(), p);
    }
}
