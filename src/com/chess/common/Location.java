package com.chess.common;

import java.util.Objects;

public class Location {
    private int col;
    private int row;

    public Location(Integer row, int col){
        this.col = col;
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }
    public int getcol() {
        return col;
    }

    public int getrow() {
        return row;
    }


}
