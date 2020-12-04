package com.chess.squares;
import com.chess.common.Location;

import javax.swing.*;

public class Square {

    private boolean isOccupied;
    private String pieceOccupied = "empty";
    private String teamOccupied = "empty";
    public ImageIcon iconData = null;
    public boolean isSelected = false;

    public String getTeamOccupied() {
        return teamOccupied;
    }

    public void setTeamOccupied(String teamOccupied) {
        this.teamOccupied = teamOccupied;
    }

    public Square(){
        this.isOccupied = false;
    }


    public ImageIcon getIconData() {
        return iconData;
    }

    public void setIconData(ImageIcon iconData) {
        this.iconData = iconData;
    }

    public void reset(){
            this.isOccupied = false;
        }


    public String getPieceOccupied() {
        return pieceOccupied;
    }

    public void setPieceOccupied(String pieceOccupied) {
        this.pieceOccupied = pieceOccupied;
    }


    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}

