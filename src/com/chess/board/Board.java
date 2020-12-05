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
        ChessPiece.Bishop bishop = new ChessPiece.Bishop();
        ChessPiece.Queen queen = new ChessPiece.Queen();
        ChessPiece.King king= new ChessPiece.King();
        boardSquares[0][1].setPieceOccupied(knight.chessPieceName); boardSquares[0][1].iconData = new ImageIcon(knight.iconDestinationBlack);
        boardSquares[0][1].setOccupied(true); boardSquares[0][1].setTeamOccupied("black");


        boardSquares[0][6].setPieceOccupied(knight.chessPieceName); boardSquares[0][6].iconData = new ImageIcon(knight.iconDestinationBlack);
        boardSquares[0][6].setOccupied(true); boardSquares[0][6].setTeamOccupied("black");


        boardSquares[0][0].setPieceOccupied(rook.chessPieceName); boardSquares[0][0].iconData = new ImageIcon(rook.iconDestinationBlack);
        boardSquares[0][0].setOccupied(true); boardSquares[0][0].setTeamOccupied("black");

        boardSquares[0][7].setPieceOccupied(rook.chessPieceName); boardSquares[0][7].iconData = new ImageIcon(rook.iconDestinationBlack);
        boardSquares[0][7].setOccupied(true); boardSquares[0][7].setTeamOccupied("black");

        boardSquares[0][2].setPieceOccupied(bishop.chessPieceName); boardSquares[0][2].iconData = new ImageIcon(bishop.iconDestinationBlack);
        boardSquares[0][2].setOccupied(true); boardSquares[0][2].setTeamOccupied("black");

        boardSquares[0][5].setPieceOccupied(bishop.chessPieceName); boardSquares[0][5].iconData = new ImageIcon(bishop.iconDestinationBlack);
        boardSquares[0][5].setOccupied(true); boardSquares[0][5].setTeamOccupied("black");

        boardSquares[0][4].setPieceOccupied(queen.chessPieceName); boardSquares[0][4].iconData = new ImageIcon(queen.iconDestinationBlack);
        boardSquares[0][4].setOccupied(true); boardSquares[0][4].setTeamOccupied("Black");

        boardSquares[0][3].setPieceOccupied(king.chessPieceName); boardSquares[0][3].iconData = new ImageIcon(king.iconDestinationBlack);
        boardSquares[0][3].setOccupied(true); boardSquares[0][3].setTeamOccupied("Black");

    }

    private void addWhiteTeam() {
        ChessPiece.Knight knight = new ChessPiece.Knight();
        ChessPiece.Rook rook = new ChessPiece.Rook();
        ChessPiece.Bishop bishop = new ChessPiece.Bishop();
        ChessPiece.Queen queen = new ChessPiece.Queen();
        ChessPiece.King king= new ChessPiece.King();
        boardSquares[7][1].setPieceOccupied(knight.chessPieceName); boardSquares[7][1].iconData = new ImageIcon(knight.iconDestinationWhite);
        boardSquares[7][1].setOccupied(true); boardSquares[7][1].setTeamOccupied("white");

        boardSquares[7][6].setPieceOccupied(knight.chessPieceName); boardSquares[7][6].iconData = new ImageIcon(knight.iconDestinationWhite);
        boardSquares[7][6].setOccupied(true); boardSquares[7][6].setTeamOccupied("white");

        boardSquares[7][0].setPieceOccupied(rook.chessPieceName); boardSquares[7][0].iconData = new ImageIcon(rook.iconDestinationWhite);
        boardSquares[7][0].setOccupied(true); boardSquares[7][0].setTeamOccupied("white");

        boardSquares[7][7].setPieceOccupied(rook.chessPieceName); boardSquares[7][7].iconData = new ImageIcon(rook.iconDestinationWhite);
        boardSquares[7][7].setOccupied(true); boardSquares[7][7].setTeamOccupied("white");

        boardSquares[7][2].setPieceOccupied(bishop.chessPieceName); boardSquares[7][2].iconData = new ImageIcon(bishop.iconDestinationWhite);
        boardSquares[7][2].setOccupied(true); boardSquares[7][2].setTeamOccupied("white");

        boardSquares[7][5].setPieceOccupied(bishop.chessPieceName); boardSquares[7][5].iconData = new ImageIcon(bishop.iconDestinationWhite);
        boardSquares[7][5].setOccupied(true); boardSquares[7][5].setTeamOccupied("white");

        boardSquares[7][3].setPieceOccupied(queen.chessPieceName); boardSquares[7][3].iconData = new ImageIcon(queen.iconDestinationWhite);
        boardSquares[7][3].setOccupied(true); boardSquares[7][3].setTeamOccupied("white");

        boardSquares[7][4].setPieceOccupied(king.chessPieceName); boardSquares[7][4].iconData = new ImageIcon(king.iconDestinationWhite);
        boardSquares[7][4].setOccupied(true); boardSquares[7][4].setTeamOccupied("white");

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



