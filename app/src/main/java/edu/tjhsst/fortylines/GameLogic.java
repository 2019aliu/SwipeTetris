package edu.tjhsst.fortylines;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import edu.tjhsst.fortylines.views.GameView;

public class GameLogic {
    private static char[] pieces = {'i', 'j', 'l', 'o', 's', 't', 'z'}; //Possible pieces
    private static Piece active;
    private static boolean canHold = true;
    private static char pieceInHold;
    private static int[][] gameBoard;
    private static int mWidth;
    private static int mHeight;
    private static GameView gv;
    private static HashMap<Character, Integer> pieceColors = new HashMap<Character, Integer>() {{
        put('i', Color.CYAN);
        put('j', Color.BLUE);
        put('l', Color.rgb(255, 128, 0));
        put('o', Color.YELLOW);
        put('s', Color.GREEN);
        put('t', Color.rgb(153, 51, 255));
        put('z', Color.RED);
    }};
    private static HashMap<Character, Integer> ghostColors = new HashMap<Character, Integer>() {{
        put('i', Color.argb(63,0,255,255));
        put('j', Color.argb(63,0,0,255));
        put('l', Color.argb(63,255, 128, 0));
        put('o', Color.argb(63,255,255,0));
        put('s', Color.argb(63,0,255,0));
        put('t', Color.argb(63,153, 51, 255));
        put('z', Color.argb(63,255,0,0));
    }};
    private static HashMap<Character, Integer> pieceNumbers = new HashMap<Character, Integer>() {{
        put('i', 1);
        put('j', 2);
        put('l', 3);
        put('o', 4);
        put('s', 5);
        put('t', 6);
        put('z', 7);
    }};
    private static Character[] tempBag = {'i', 'j', 'l', 'o', 's', 't', 'z'};
    private static ArrayList<Character> bag;
    private static int piecePositionX;
    private static int piecePositionY;
    private static Queue<Character> pieceQueue = new LinkedList<Character>();

    public GameLogic(GameView g, int w, int h) {
        gv = g;
        mWidth = w;
        mHeight = h;
        pieceInHold = '.';
        bag = new ArrayList<Character>(Arrays.asList(tempBag));
        gameBoard = new int[22][10];
    }

    public static void fillQueue() {
        for(int k = 0; k < 5; k++) {
            pieceQueue.add(pickPiece());
        }
    }

    public static char pickPiece() {
        if(bag.size() == 0){
            bag.add('i');
            bag.add('j');
            bag.add('l');
            bag.add('o');
            bag.add('s');
            bag.add('t');
            bag.add('z');
        }
        int bagPick = (int) (Math.random() * bag.size());
        char p = bag.get(bagPick);
        bag.remove((Character) (p));
        return p;
    }

    public static void newPiece() {
        char p = pieceQueue.poll();
        if(p == 'i') {
            piecePositionX = 2;
            piecePositionY = -1;
        }
        else {
            piecePositionX = 3;
            piecePositionY = 0;
        }
        pieceQueue.add(pickPiece());
        canHold = true;

        delayedDrop();
        delayedDrop();
        delayedDrop();


        active = new Piece(p);
    }

    public static void newPieceSpec(char pt) {
        char p = pt;
        if(p == 'i') {
            piecePositionX = 2;
            piecePositionY = -1;
        }
        else {
            piecePositionX = 3;
            piecePositionY = 0;
        }
        pieceQueue.add(pickPiece());

        delayedDrop();
        delayedDrop();
        delayedDrop();


        active = new Piece(p);
    }

    public static void delayedDrop() {
        piecePositionY++;
    }

    public static void drawActivePiece(Canvas canvas, int width, int height) {
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();
        Paint paint = new Paint();
        paint.setColor(pieceColors.get(pt));

        for(int k = 0; k < minos.length; k++) {
            fillCell(canvas, width, height, piecePositionY + minos[k][0], piecePositionX + minos[k][1], paint);
        }
    }


    public static void drawGrid(Canvas canvas, int width, int height) {
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(53, 53, 53));
        paint.setStrokeWidth(4);
        for(int k = 0; k < 10; k++) {
            canvas.drawLine(k * width / 10.0f, 0, k * width / 10.0f, height, paint);
        }
        for(int k = 0; k < 20; k++) {
            canvas.drawLine(0, k * height / 20.0f, width, k * height / 20.0f, paint);
        }
    }

    public static void drawBoard(Canvas canvas, int width, int height) {
        Paint paint = new Paint();

        for(int r = 2; r < gameBoard.length; r++) {
            for(int c = 0; c < gameBoard[0].length; c++) {
                if(gameBoard[r][c] != 0) {
                    paint.setColor(pieceColors.get(pieces[gameBoard[r][c] - 1]));
                    fillCell(canvas, width, height, r, c, paint);
                }
            }
        }

    }

    public static void redrawGame(Canvas canvas) {
        Log.d("Drawing stuff", "re");
        canvas.drawColor(Color.BLACK);
        Log.d("Drawing stuff", "ree");
        drawGrid(canvas, mWidth, mHeight);
        Log.d("Drawing stuff", "reee");
        drawBoard(canvas, mWidth, mHeight);
        Log.d("Drawing stuff", "reeee");
        drawActivePiece(canvas, mWidth, mHeight);
        Log.d("Drawing stuff", "reeeee");
        drawGhostPiece(canvas, mWidth, mHeight);
        Log.d("Drawing stuff", "ultimate reeeeeeeeeee");
    }

    public static void fillCell(Canvas canvas, int width, int height, int r, int c, Paint paint) {
        float left = c * width / 10f;
        float top = (r - 2) * height / 20f;
        canvas.drawRect(left, top, left + width / 10f, top + height / 20f, paint);
    }

    // function to draw the ghost piece
    public static void drawGhostPiece(Canvas canvas, int width, int height) {
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();
        Paint paint = new Paint();
        paint.setColor(ghostColors.get(pt));
        int hdY = piecePositionY;
        if(piecePositionY < 0) {
            hdY = 0;
        }

        boolean[] m = new boolean[4];

        do {
            for(int k = 0; k < m.length; k++) {
                m[k] = ((hdY + minos[k][0]) < 21) && (gameBoard[hdY + minos[k][0] + 1][piecePositionX + minos[k][1]] == 0);
            }
            hdY++;
        }while(m[0] && m[1] && m[2] && m[3]);

        for(int k = 0; k < minos.length; k++) {
            fillCell(canvas, width, height, hdY + minos[k][0]-1, piecePositionX + minos[k][1], paint);
        }
    }



    public static void moveLeft() {
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();

        int tpx = piecePositionX - 1;
        boolean[] m = new boolean[4];
        for(int k = 0; k < m.length; k++) {
            m[k] = ((tpx + minos[k][1] >= 0) && (gameBoard[piecePositionY + minos[k][0] + 2][tpx + minos[k][1]] == 0));
        }
        if(m[0] && m[1] && m[2] && m[3]) {
            piecePositionX = tpx;
        }
        gv.invalidate();
    }

    public static void moveRight() {
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();

        int tpx = piecePositionX + 1;
        boolean[] m = new boolean[4];
        for(int k = 0; k < m.length; k++) {
            m[k] = ((tpx + minos[k][1] < 10) && (gameBoard[piecePositionY + minos[k][0] + 2][tpx + minos[k][1]] == 0));
        }
        if(m[0] && m[1] && m[2] && m[3]) {
            piecePositionX = tpx;
        }
        gv.invalidate();
    }

    public static void gameRotateCCW() {
        active.rotateCCW();
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();
        boolean[] m = new boolean[4];
        int[][] kickValues = active.getKickValues();
        int kickIndex = 0;
        int tpy = piecePositionY + kickValues[kickIndex][0];
        int tpx = piecePositionX + kickValues[kickIndex][1];

        do {
            tpy = piecePositionY + kickValues[kickIndex][0];
            tpx = piecePositionX + kickValues[kickIndex][1];

            for(int k = 0; k < m.length; k++) {
                m[k] = ((tpx + minos[k][1] < 10 && tpx + minos[k][1] >= 0) && (tpy + minos[k][0] + 1 < 21 && tpy + minos[k][0] + 1 >= 0) && (gameBoard[tpy + minos[k][0] + 2][tpx + minos[k][1]] == 0));
            }
            kickIndex++;
        } while(!(m[0] && m[1] && m[2] && m[3]) && kickIndex < kickValues.length);
        if(m[0] && m[1] && m[2] && m[3]) {
            piecePositionX = tpx;
            piecePositionY = tpy;
        }
        else {
            active.rotateCW();
        }
        gv.invalidate();
    }

    public static void gameRotateCW() {
        active.rotateCW();
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();
        boolean[] m = new boolean[4];
        int[][] kickValues = active.getKickValues();
        int kickIndex = 0;
        int tpy = piecePositionY + kickValues[kickIndex][0];
        int tpx = piecePositionX + kickValues[kickIndex][1];

        do {
            tpy = piecePositionY + kickValues[kickIndex][0];
            tpx = piecePositionX + kickValues[kickIndex][1];

            for(int k = 0; k < m.length; k++) {
                m[k] = ((tpx + minos[k][1] < 10 && tpx + minos[k][1] >= 0) && (tpy + minos[k][0] + 1 < 21 && tpy + minos[k][0] + 1 >= 0) && (gameBoard[tpy + minos[k][0] + 2][tpx + minos[k][1]] == 0));
            }
            kickIndex++;
        } while(!(m[0] && m[1] && m[2] && m[3]) && kickIndex < kickValues.length);
        if(m[0] && m[1] && m[2] && m[3]) {
            piecePositionX = tpx;
            piecePositionY = tpy;
        }
        else {
            active.rotateCCW();
        }
        Log.d("GameLogic", active.getRotationState() + "");
        gv.invalidate();
    }

    public static void rotate180() {
        gameRotateCW();
        gameRotateCW();
        gv.invalidate();
    }

    public static void hardDrop() {
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();
        int hdY = piecePositionY;
        if(piecePositionY < 0) {
            hdY = 0;
        }
        boolean[] m = new boolean[4];

        do {
            for(int k = 0; k < m.length; k++) {
                m[k] = ((hdY + minos[k][0]) < 21) && (gameBoard[hdY + minos[k][0] + 1][piecePositionX + minos[k][1]] == 0);
            }
            hdY++;
        }while(m[0] && m[1] && m[2] && m[3]);
        for(int k = 0; k < minos.length; k++) {
            gameBoard[hdY + minos[k][0] - 1][piecePositionX + minos[k][1]] = pieceNumbers.get(pt);
        }
        newPiece();
        clearLines();


        gv.invalidate();
    }

    public static void softDrop() {
        int[][] minos = active.getPieceState();
        int pt = active.getPieceType();

        int tpy = piecePositionY + 1;
        boolean[] m = new boolean[4];
        for(int k = 0; k < m.length; k++) {
            m[k] = ((tpy + minos[k][0]) < 22) && (gameBoard[tpy + minos[k][0]][piecePositionX + minos[k][1]] == 0);
        }
        if (m[0] && m[1] && m[2] && m[3]) {
            piecePositionY = tpy;
        }
        Log.d("SOFT DROP TESTING", "POSITION: " + piecePositionY);
        gv.invalidate();

    }

    public static void hold() {
        if(pieceInHold == '.') {
            pieceInHold = active.getPieceType();
            newPiece();
        }
        else {
            char tempHold = active.getPieceType();
            newPieceSpec(pieceInHold);
            pieceInHold = tempHold;
        }
        canHold = false;
        gv.invalidate();
    }

    public static void dasRight() {
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();
        int dasX = piecePositionX;

        boolean[] m = new boolean[4];

        do {
            for(int k = 0; k < m.length; k++) {
                m[k] = ((dasX + minos[k][1] + 1) < 10) && (gameBoard[piecePositionY + minos[k][0]][dasX + minos[k][1] + 1] == 0);
            }
            dasX++;
        }while(m[0] && m[1] && m[2] && m[3]);
        piecePositionX = dasX - 1;
        gv.invalidate();
    }

    public static void dasLeft() {
        int[][] minos = active.getPieceState();
        char pt = active.getPieceType();
        int dasX = piecePositionX;

        boolean[] m = new boolean[4];

        do {
            for(int k = 0; k < m.length; k++) {
                m[k] = ((dasX + minos[k][1] - 1) >= 0) && (gameBoard[piecePositionY + minos[k][0]][dasX + minos[k][1] - 1] == 0);
            }
            dasX--;
        }while(m[0] && m[1] && m[2] && m[3]);
        piecePositionX = dasX + 1;
        gv.invalidate();
    }

    public static void clearLines() {
        for(int row = gameBoard.length - 1; row >= 0; row--) {
            boolean rowFilled = true;
            for (int cell = 0; cell < gameBoard[0].length; cell++) {
                if(gameBoard[row][cell] == 0) {
                    rowFilled = false;
                    break;
                }
            }
            if(rowFilled) {
                for(int rowShift = row; rowShift > 0; rowShift--) {
                    gameBoard[rowShift] = gameBoard[rowShift - 1];

                }
                row++;
            }
        }
    }
}
