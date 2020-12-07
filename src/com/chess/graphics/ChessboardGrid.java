package com.chess.graphics;
import com.chess.board.Board;
import com.chess.squares.Square;
import com.chess.squares.SquareColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;


public class ChessboardGrid extends JFrame {

    private JButton[][] squares = new JButton[8][8];
    private Board boardData = new Board();
    private int currentSelectedX = -1, currentSelectedY = -1;

    boolean selected = false;
    Color black = new Color(102, 51, 0);
    Color white = new Color(255, 204, 153);

    SquareColor currentColor = SquareColor.WHITE;

    public ChessboardGrid() {
        setTitle("Chess: fite me nub"); //Previous title was "This is a title"

        Container contents = getContentPane();
        contents.setLayout(new GridLayout(8, 8));

        ButtonHandler buttonHandler = new ButtonHandler();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                if (currentColor == SquareColor.WHITE) {
                    squares[row][col].setBackground(white);
                } else {
                    squares[row][col].setBackground(black);
                }
                contents.add(squares[row][col]);
                squares[row][col].addActionListener(buttonHandler);

                currentColor = (currentColor == SquareColor.WHITE) ? SquareColor.BLACK : SquareColor.WHITE;

            }
            currentColor = (currentColor == SquareColor.WHITE) ? SquareColor.BLACK : SquareColor.WHITE;
        } //this generates the board

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(boardData.boardSquares[row][col].iconData != null){
                    squares[row][col].setIcon(boardData.boardSquares[row][col].iconData);
                }
            }
        }



        setSize(600, 600);
        setResizable(false);
        setLocationRelativeTo(null); //centers window
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (source == squares[row][col]) {
                        processClick(row, col);
                        return;
                    }
                }
            }

        }
    }

    //
    private void processClick(int destRow, int destCol) {

            if(!selected){
                showSelected(destRow,destCol);
                currentSelectedY = destRow;
                currentSelectedX = destCol;
                selected = true;
                System.out.printf("Team: %s ,Piece: %s at y: %d, x: %d is selected\n", boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied(), boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied(), currentSelectedY, currentSelectedX);
            }
            else{
                if (!isValidMove(destRow, destCol)) {
                    System.out.println("invalid move");
                }
                else{

                    boardData.boardSquares[destRow][destCol].setOccupied(true);
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setOccupied(false);

                    boardData.boardSquares[destRow][destCol].setTeamOccupied(boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied());
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setTeamOccupied("Empty");

                    boardData.boardSquares[destRow][destCol].setIconData(boardData.boardSquares[currentSelectedY][currentSelectedX].getIconData());
                    boardData.boardSquares[destRow][destCol].setPieceOccupied(boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied());
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setIconData(null);
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setPieceOccupied("Empty");

                    squares[destRow][destCol].setIcon(squares[currentSelectedY][currentSelectedX].getIcon());
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setIconData(null);
                    squares[currentSelectedY][currentSelectedX].setIcon(null);

                }
                squares[currentSelectedY][currentSelectedX].setBorder(null);
                selected = false;
            }





    }

    private boolean isValidMove(int destRow, int destCol) {

        if(boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied() != boardData.boardSquares[destRow][destCol].getTeamOccupied()){

            if ((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "knight") {
                int rowDelta = Math.abs(destRow - currentSelectedY);
                int colDelta = Math.abs(destCol - currentSelectedX);
                if ((rowDelta == 1) && (colDelta == 2)) {
                    return true;
                }
                if ((rowDelta == 2) && (colDelta == 1)) {
                    return true;
                }
            }

            if((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "rook"){
               String direction="none";
                if(currentSelectedY<destRow&&currentSelectedX==destCol){
                    direction="south";
                }
                if(currentSelectedY>destRow&&currentSelectedX==destCol){
                    direction="north";
                }
                if(currentSelectedX<destCol&&currentSelectedY==destRow){
                    direction="east";
                }
                if(currentSelectedX>destCol&&currentSelectedY==destRow){
                    direction="west";
                }
                if(direction=="south") {
                    int spacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p = new Square();
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY + i][currentSelectedX];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }
                if(direction=="north") {
                    int spacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p = new Square();
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY - i][currentSelectedX];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }
                if(direction=="east") {
                    int spacesToMove = Math.abs(destCol - currentSelectedX);
                    Square p = new Square();
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY][currentSelectedX+i];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }
                if(direction=="west") {
                    int spacesToMove = Math.abs(destCol - currentSelectedX);
                    Square p = new Square();
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY][currentSelectedX-i];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }

            }
            /// ///
            if((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "bishop"){
                int XspacesToMove = Math.abs(destCol - currentSelectedX);
                int YspacesToMove = Math.abs(destRow - currentSelectedY);
                if(XspacesToMove==YspacesToMove) {
                    String direction = "none";
                    if (currentSelectedY < destRow && currentSelectedX < destCol) {
                        direction = "southeast";
                    }
                    if (currentSelectedY < destRow && currentSelectedX > destCol) {
                        direction = "southwest";
                    }
                    if (currentSelectedY > destRow && currentSelectedX > destCol) {
                        direction = "northwest";
                    }
                    if (currentSelectedY > destRow && currentSelectedX < destCol) {
                        direction = "northeast";
                    }
                    if (direction == "southeast") {
                        int spacesToMove = Math.abs(destCol - currentSelectedX);
                        Square p = new Square();
                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY + i][currentSelectedX + i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;
                    }
                    if (direction == "southwest") {

                        Square p = new Square();
                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY + i][currentSelectedX - i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;
                    }
                    if (direction == "northwest") {

                        Square p = new Square();
                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY - i][currentSelectedX - i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;
                    }
                    if (direction == "northeast") {

                        Square p = new Square();

                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY - i][currentSelectedX + i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;

                    }
                }

            }
            if((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "queen"){
                String direction="none";
                if(currentSelectedY<destRow&&currentSelectedX<destCol){
                    direction="southeast";
                }
                if(currentSelectedY<destRow&&currentSelectedX>destCol){
                    direction="southwest";
                }
                if(currentSelectedY>destRow&&currentSelectedX>destCol){
                    direction="northwest";
                }
                if(currentSelectedY>destRow&&currentSelectedX<destCol){
                    direction="northeast";
                } if(currentSelectedY<destRow&&currentSelectedX==destCol){
                    direction="south";
                }
                if(currentSelectedY>destRow&&currentSelectedX==destCol){
                    direction="north";
                }
                if(currentSelectedX<destCol&&currentSelectedY==destRow){
                    direction="east";
                }
                if(currentSelectedX>destCol&&currentSelectedY==destRow){
                    direction="west";
                }
                if(direction=="south") {
                    int spacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p = new Square();
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY + i][currentSelectedX];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }
                if(direction=="north") {
                    int spacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p;
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY - i][currentSelectedX];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }
                if(direction=="east") {
                    int spacesToMove = Math.abs(destCol - currentSelectedX);
                    Square p = new Square();
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY][currentSelectedX+i];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }
                if(direction=="west") {
                    int spacesToMove = Math.abs(destCol - currentSelectedX);
                    Square p = new Square();
                    for (int i = 1; i < spacesToMove; i++) {
                        p = boardData.boardSquares[currentSelectedY][currentSelectedX-i];
                        if(p.isOccupied()==true){
                            return false;
                        }

                    }

                    return true;
                }
                if(direction=="southeast") {
                    int XspacesToMove = Math.abs(destCol - currentSelectedX);
                    int YspacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p = new Square();
                    if(XspacesToMove==YspacesToMove) {
                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY + i][currentSelectedX + i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;
                    }
                }
                if(direction=="southwest") {
                    int XspacesToMove = Math.abs(destCol - currentSelectedX);
                    int YspacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p = new Square();
                    if(XspacesToMove==YspacesToMove) {
                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY + i][currentSelectedX - i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;
                    }
                }
                if(direction=="northwest") {
                    int XspacesToMove = Math.abs(destCol - currentSelectedX);
                    int YspacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p = new Square();
                    if(XspacesToMove==YspacesToMove) {
                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY - i][currentSelectedX - i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;
                    }
                }
                if(direction=="northeast") {
                    int XspacesToMove = Math.abs(destCol - currentSelectedX);
                    int YspacesToMove = Math.abs(destRow - currentSelectedY);
                    Square p = new Square();
                    if(XspacesToMove==YspacesToMove) {
                        for (int i = 1; i < XspacesToMove; i++) {
                            p = boardData.boardSquares[currentSelectedY - i][currentSelectedX + i];
                            if (p.isOccupied() == true) {
                                return false;
                            }

                        }

                        return true;
                    }
                }

            }

            if ((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "king") {
                int xDistance = Math.abs(destCol - currentSelectedX);
                int yDistance = Math.abs(destRow - currentSelectedY);
                if(xDistance<=1&&yDistance<=1){
                    return true;
                }
            }

//            if ((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "pawn") {
//                String direction = "none";
//                if(currentSelectedY>destRow&&currentSelectedX==destCol){
//                    direction="north";
//                }
//            }
//            NOTE: DOH KAYATA AT FIRST AKO GI BASE ANG MOVEMENT SA PAWNS SA KING ((except nga pwede siya maka take 2 steps kung iya first move kay going forward and en passant))
        }

       return false;


    }



    private void showSelected( int curRow, int curCol){
        squares[curRow][curCol].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
    }





}
