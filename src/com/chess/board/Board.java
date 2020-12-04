package com.chess.board;
import com.chess.squares.Square;
import com.chess.chesspieces.ChessPiece;

import javax.swing.*;

//The class Board contains the data for the chessboardGrid
public class Board {

    public Square[][] boardSquares = new Square[8][8];

    public Board() {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardSquares[row][col] = new Square();
            }
        }

        addBlackTeam();

        addWhiteTeam();
    }

    private void addBlackTeam() {
        ChessPiece.Knight knight = new ChessPiece.Knight();
        ChessPiece.Rook rook = new ChessPiece.Rook();
        boardSquares[0][1].setPieceOccupied(knight.chessPieceName); boardSquares[0][1].iconData = new ImageIcon(knight.iconDestinationBlack);
        boardSquares[0][1].setOccupied(true); boardSquares[0][1].setTeamOccupied("black");

        boardSquares[0][6].setPieceOccupied(knight.chessPieceName); boardSquares[0][6].iconData = new ImageIcon(knight.iconDestinationBlack);
        boardSquares[0][6].setOccupied(true); boardSquares[0][6].setTeamOccupied("black");


        boardSquares[0][0].setPieceOccupied(rook.chessPieceName); boardSquares[0][0].iconData = new ImageIcon(rook.iconDestinationBlack);
        boardSquares[0][0].setOccupied(true); boardSquares[0][0].setTeamOccupied("black");


    }

    private void addWhiteTeam() {
        ChessPiece.Knight knight = new ChessPiece.Knight();
        ChessPiece.Rook rook = new ChessPiece.Rook();

        boardSquares[7][1].setPieceOccupied(knight.chessPieceName); boardSquares[7][1].iconData = new ImageIcon(knight.iconDestinationWhite);
        boardSquares[7][1].setOccupied(true); boardSquares[7][1].setTeamOccupied("white");

        boardSquares[7][6].setPieceOccupied(knight.chessPieceName); boardSquares[7][6].iconData = new ImageIcon(knight.iconDestinationWhite);
        boardSquares[7][6].setOccupied(true); boardSquares[7][6].setTeamOccupied("white");

        boardSquares[7][0].setPieceOccupied(rook.chessPieceName); boardSquares[7][0].iconData = new ImageIcon(rook.iconDestinationWhite);
        boardSquares[7][0].setOccupied(true); boardSquares[0][0].setTeamOccupied("white");

    }
}
//    public void printBoard(){
//        for(Square[] row: boardSquares){
//            for(Square square : row){
//                System.out.print(square);
//            }
//            System.out.println();
//        }
//    }



