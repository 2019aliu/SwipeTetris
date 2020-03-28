package edu.tjhsst.fortylines;

public class Piece {
    private int[][][] mSrsStates;
    private int[][][] mOffsetTables;
    private int mRotationState;
    private char mPieceType;
    private int[][] mKickValues;

    public Piece(char pt) {
        this.mPieceType = pt;
        this.mRotationState = 0;
        if(pt == 'i') {
            int[][][] a = { {{2, 1}, {2, 2}, {2, 3}, {2, 4}},
                            {{1, 2}, {2, 2}, {3, 2}, {4, 2}},
                            {{2, 0}, {2, 1}, {2, 2}, {2, 3}},
                            {{0, 2}, {1, 2}, {2, 2}, {3, 2}} };
            this.mSrsStates = a;
            int[][][] b = { {{0, 0}, {0, -1}, {0, 2}, {0, -1}, {0, 2}},
                            {{0, -1}, {0, 0}, {0, 0}, {1, 0}, {-2, 0}},
                            {{1, -1}, {1, 1}, {1, -2}, {0, 1}, {0, -2}},
                            {{1, 0}, {1, 0}, {1, 0}, {-1, 0}, {2, 0}} };
            this.mOffsetTables = b;
        }
        else if(pt == 'o') {
            int[][][] a = { {{1, 1}, {0, 1}, {0, 2}, {1, 2}},
                            {{1, 1}, {1, 2}, {2, 1}, {2, 2}},
                            {{1, 1}, {1, 0}, {2, 0}, {2, 1}},
                            {{1, 1}, {0, 0}, {0, 1}, {1, 0}} };
            this.mSrsStates = a;
            int[][][] b = { {{0, 0}}, {{-1, 0}}, {{-1, -1}}, {{0, -1}}};
            this.mOffsetTables = b;
        }
        else {
            int[][][] b = { {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
                            {{0, 0}, {0, 1}, {-1, 1}, {2, 0}, {2, 1}},
                            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
                            {{0, 0}, {0, -1}, {-1, -1}, {2, 0}, {2, -1}} };
            this.mOffsetTables = b;
            if(pt == 's') {
                int[][][] a = { {{0, 1}, {0, 2}, {1, 0}, {1, 1}},
                                {{0, 1}, {1, 1}, {1, 2}, {2, 2}},
                                {{1, 2}, {1, 1}, {2, 1}, {2, 0}},
                                {{0, 0}, {1, 0}, {1, 1}, {2, 1}} };
                this.mSrsStates = a;
            }
            else if(pt == 'z') {
                int[][][] a = { {{0, 0}, {0, 1}, {1, 1}, {1, 2}},
                                {{0, 2}, {1, 2}, {1, 1}, {2, 1}},
                                {{1, 0}, {1, 1}, {2, 1}, {2, 2}},
                                {{0, 1}, {1, 1}, {1, 0}, {2, 0}} };
                this.mSrsStates = a;
            }
            else if(pt == 'j') {
                int[][][] a = { {{0, 0}, {1, 0}, {1, 1}, {1, 2}},
                                {{0, 2}, {0, 1}, {1, 1}, {2, 1}},
                                {{2, 2}, {1, 0}, {1, 1}, {1, 2}},
                                {{2, 0}, {0, 1}, {1, 1}, {2, 1}} };
                this.mSrsStates = a;
            }
            else if(pt == 'l') {
                int[][][] a = { {{0, 2}, {1, 0}, {1, 1}, {1, 2}},
                                {{2, 2}, {0, 1}, {1, 1}, {2, 1}},
                                {{2, 0}, {1, 0}, {1, 1}, {1, 2}},
                                {{0, 0}, {0, 1}, {1, 1}, {2, 1}} };
                this.mSrsStates = a;
            }
            else if(pt == 't') {
                int[][][] a = { {{0, 1}, {1,1}, {1, 0}, {1, 2}},
                                {{0, 1}, {1,1}, {2, 1}, {1, 2}},
                                {{2, 1}, {1,1}, {1, 0}, {1, 2}},
                                {{0, 1}, {1,1}, {2, 1}, {1, 0}} };
                this.mSrsStates = a;
            }
        }
        mKickValues = new int[5][2];
    }

    public int[][][] getSrsStates() {
        return mSrsStates;
    }

    public int[][][] getOffsetTables() {
        return mOffsetTables;
    }

    public int getRotationState() {
        return mRotationState;
    }

    public char getPieceType() {
        return mPieceType;
    }

    public int[][] getKickValues() {
        return mKickValues;
    }


    public int[][] getPieceState() {
        return this.mSrsStates[this.mRotationState];
    }

    public void rotateCCW() {
        int[][] kickA = this.mOffsetTables[this.mRotationState];
        this.mRotationState = (this.mRotationState + 3) % 4;
        int[][] kickB = this.mOffsetTables[this.mRotationState];
        for(int k = 0; k < this.mKickValues.length; k++) {
            this.mKickValues[k][0] = kickA[k][0] - kickB[k][0];
            this.mKickValues[k][1] = kickA[k][1] - kickB[k][1];
        }
    }

    public void rotateCW() {
        int[][] kickA = this.mOffsetTables[this.mRotationState];
        this.mRotationState = (this.mRotationState + 1) % 4;
        int[][] kickB = this.mOffsetTables[this.mRotationState];
        for(int k = 0; k < this.mKickValues.length; k++) {
            this.mKickValues[k][0] = kickA[k][0] - kickB[k][0];
            this.mKickValues[k][1] = kickA[k][1] - kickB[k][1];
        }
    }
}
