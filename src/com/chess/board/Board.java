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
        int b;
        ChessPiece.Knight knight = new ChessPiece.Knight();
        ChessPiece.Rook rook = new ChessPiece.Rook();
        ChessPiece.Bishop bishop = new ChessPiece.Bishop();
        ChessPiece.Queen queen = new ChessPiece.Queen();
        ChessPiece.King king= new ChessPiece.King();
        ChessPiece.Pawn pawn = new ChessPiece.Pawn();


        boardSquares[0][0].setPieceOccupied(rook.chessPieceName); boardSquares[0][0].iconData = new ImageIcon(rook.iconDestinationBlack);

        boardSquares[0][1].setPieceOccupied(knight.chessPieceName); boardSquares[0][1].iconData = new ImageIcon(knight.iconDestinationBlack);
//        boardSquares[0][1].setOccupied(true); boardSquares[0][1].setTeamOccupied("black");

        boardSquares[0][2].setPieceOccupied(bishop.chessPieceName); boardSquares[0][2].iconData = new ImageIcon(bishop.iconDestinationBlack);
//        boardSquares[0][2].setOccupied(true); boardSquares[0][2].setTeamOccupied("black");

        boardSquares[0][3].setPieceOccupied(queen.chessPieceName); boardSquares[0][3].iconData = new ImageIcon(queen.iconDestinationBlack);
//        boardSquares[0][3].setOccupied(true); boardSquares[0][3].setTeamOccupied("black");

        boardSquares[0][4].setPieceOccupied(king.chessPieceName); boardSquares[0][4].iconData = new ImageIcon(king.iconDestinationBlack);
//        boardSquares[0][4].setOccupied(true); boardSquares[0][4].setTeamOccupied("black");

        boardSquares[0][5].setPieceOccupied(bishop.chessPieceName); boardSquares[0][5].iconData = new ImageIcon(bishop.iconDestinationBlack);
//        boardSquares[0][5].setOccupied(true); boardSquares[0][5].setTeamOccupied("black");

        boardSquares[0][6].setPieceOccupied(knight.chessPieceName); boardSquares[0][6].iconData = new ImageIcon(knight.iconDestinationBlack);
//        boardSquares[0][6].setOccupied(true); boardSquares[0][6].setTeamOccupied("black");

        boardSquares[0][7].setPieceOccupied(rook.chessPieceName); boardSquares[0][7].iconData = new ImageIcon(rook.iconDestinationBlack);
//        boardSquares[0][7].setOccupied(true); boardSquares[0][7].setTeamOccupied("black");

        for(b=0;b<8;b++){
            boardSquares[1][b].setPieceOccupied(pawn.chessPieceName); boardSquares[1][b].iconData = new ImageIcon(pawn.iconDestinationBlack);
            boardSquares[1][b].setOccupied(true); boardSquares[1][b].setTeamOccupied("black"); //first 2 lines of the loop are for the pawns
            boardSquares[0][b].setOccupied(true); boardSquares[0][b].setTeamOccupied("black");
        }
    }

    private void addWhiteTeam() {
        int w;
        ChessPiece.Knight knight = new ChessPiece.Knight();
        ChessPiece.Rook rook = new ChessPiece.Rook();
        ChessPiece.Bishop bishop = new ChessPiece.Bishop();
        ChessPiece.Queen queen = new ChessPiece.Queen();
        ChessPiece.King king = new ChessPiece.King();
        ChessPiece.Pawn pawn = new ChessPiece.Pawn();


        boardSquares[7][0].setPieceOccupied(rook.chessPieceName); boardSquares[7][0].iconData = new ImageIcon(rook.iconDestinationWhite);

        boardSquares[7][1].setPieceOccupied(knight.chessPieceName); boardSquares[7][1].iconData = new ImageIcon(knight.iconDestinationWhite);

        boardSquares[7][2].setPieceOccupied(bishop.chessPieceName); boardSquares[7][2].iconData = new ImageIcon(bishop.iconDestinationWhite);

        boardSquares[7][3].setPieceOccupied(queen.chessPieceName); boardSquares[7][3].iconData = new ImageIcon(queen.iconDestinationWhite);

        boardSquares[7][4].setPieceOccupied(king.chessPieceName); boardSquares[7][4].iconData = new ImageIcon(king.iconDestinationWhite);

        boardSquares[7][5].setPieceOccupied(bishop.chessPieceName); boardSquares[7][5].iconData = new ImageIcon(bishop.iconDestinationWhite);

        boardSquares[7][6].setPieceOccupied(knight.chessPieceName); boardSquares[7][6].iconData = new ImageIcon(knight.iconDestinationWhite);

        boardSquares[7][7].setPieceOccupied(rook.chessPieceName); boardSquares[7][7].iconData = new ImageIcon(rook.iconDestinationWhite);

        for(w=0;w<8;w++){
            boardSquares[6][w].setPieceOccupied(pawn.chessPieceName); boardSquares[6][w].iconData = new ImageIcon(pawn.iconDestinationWhite);
            boardSquares[6][w].setOccupied(true); boardSquares[6][w].setTeamOccupied("white"); //first 2 lines of the loop are for the pawns
            boardSquares[7][w].setOccupied(true); boardSquares[7][w].setTeamOccupied("white");
        }
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



