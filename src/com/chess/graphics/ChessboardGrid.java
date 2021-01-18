package com.chess.graphics;
import com.chess.board.Board;
import com.chess.runner.Game;
import com.chess.squares.Square;
import com.chess.squares.SquareColor;
import com.chess.logger.GameLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;


public class ChessboardGrid extends JFrame {


    private JButton[][] squares = new JButton[8][8];
    private Board boardData = new Board();
    private Board tempBoard = new Board();

    private int currentSelectedX = -1, currentSelectedY = -1;

    boolean selected = false;
    Color black = new Color(102, 51, 0);
    Color white = new Color(255, 204, 153);

    private int bKingY = 0;  private int bKingX = 4;
    private int wKingY = 7;  private int wKingX = 4;


    SquareColor currentColor = SquareColor.WHITE;

    private String currentTeam = "white";

    public ChessboardGrid() {
        setTitle("Chess: fite me nub"); //Previous title was "This is a title"
        GameLog logger= new GameLog();
        logger.createfile();
        logger.newgame();
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

    private void processClick(int destRow, int destCol) {
            copyData();

            if(!selected){
                showSelected(destRow,destCol);
                currentSelectedY = destRow;
                currentSelectedX = destCol;
                selected = true;
                System.out.printf("Team: %s Piece: %s at y: %d, x: %d is selected\n", boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied(), boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied(), currentSelectedY, currentSelectedX);
                if(currentTeam != boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied()){
                    selected = false;
                    squares[currentSelectedY][currentSelectedX].setBorder(null);
                    System.out.printf("IT IS %s's TURN, NOT YOURS\n", currentTeam);
                }
            }
            else{
                if (!isValidMove(destRow, destCol)) {
                    System.out.println("invalid move");
                }
                else{

                    boardData.boardSquares[destRow][destCol].setOccupied(true);
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setOccupied(false);

                    boardData.boardSquares[destRow][destCol].setTeamOccupied(boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied());
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setTeamOccupied("empty");

                    boardData.boardSquares[destRow][destCol].setIconData(boardData.boardSquares[currentSelectedY][currentSelectedX].getIconData());
                    boardData.boardSquares[destRow][destCol].setPieceOccupied(boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied());
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setIconData(null);
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setPieceOccupied("empty");

                    squares[destRow][destCol].setIcon(squares[currentSelectedY][currentSelectedX].getIcon());
                    boardData.boardSquares[currentSelectedY][currentSelectedX].setIconData(null);
                    squares[currentSelectedY][currentSelectedX].setIcon(null);
                    GameLog logger= new GameLog();
                    logger.log(currentTeam,boardData.boardSquares[destRow][destCol].getPieceOccupied(),currentSelectedX,currentSelectedY,destCol,destRow);
                    if(checkIfChecked()){
                        revertToPrevious();
                        currentTeam = (currentTeam.equals("white"))? "black": "white";
                        squares[currentSelectedY][currentSelectedX].setIcon(squares[destRow][destCol].getIcon());
                        squares[destRow][destCol].setIcon(null);
                        JFrame notification = new JFrame();
                        JPanel panel = new JPanel();
                        JLabel label = new JLabel();
                        label.setText("YOU CANT PLACE YOURSELF IN A CHECK");
                        panel.add(label);
                        notification.add(panel);

                        notification.setVisible(true);
                        notification.setSize(450,100);
                        notification.setLocationRelativeTo(null);
                        notification.setResizable(false);
                    }
                    currentTeam = (currentTeam.equals("white"))? "black": "white";

                    if(checkIfChecked()){
                        System.out.println("CHECK");
                        if(isCheckmate()){
                            JFrame endScreen = new JFrame();

                            endScreen.setSize(450, 100);
                            endScreen.setResizable(false);
                            endScreen.setVisible(true);
                            endScreen.setLocationRelativeTo(null);
                            endScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                            JPanel panel = new JPanel();
                            JLabel winnerText = new JLabel();
                            if(currentTeam == "black"){
                                winnerText.setText("CHECKMATE, WHITE WINS!!!");
                            }
                           else{
                                winnerText.setText("CHECKMATE, BLACK WINS!!!");
                            }

                            panel.add(winnerText);
                            endScreen.add(panel);
                        }
                    }

                } //proceeds with moving the pieces

                squares[currentSelectedY][currentSelectedX].setBorder(null);
                selected = false;


            } //moves the image icons of the buttons and changes the board data



    }

    private boolean checkIfChecked(){
        int kingPosY = (currentTeam == "white")? wKingY: bKingY;
        int kingPosX = (currentTeam == "white")? wKingX: bKingX;

        for(int kingLeft = kingPosX-1; kingLeft >= 0; kingLeft--){
            if(boardData.boardSquares[kingPosY][kingLeft].isOccupied()) {
                if (boardData.boardSquares[kingPosY][kingLeft].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY][kingLeft].getPieceOccupied() == "rook" || boardData.boardSquares[kingPosY][kingLeft].getPieceOccupied() == "queen")) {
                    return true;
                }
                else{
                    break;
                }
            }
        } //checks the left side of the king
        for(int kingRight = kingPosX+1; kingRight < 8; kingRight++){
            if(boardData.boardSquares[kingPosY][kingRight].isOccupied()){
                if(boardData.boardSquares[kingPosY][kingRight].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY][kingRight].getPieceOccupied() == "rook" || boardData.boardSquares[kingPosY][kingRight].getPieceOccupied() == "queen")){
                    return true;
                }
                else{
                    break;
                }
            }
        } //checks the right side of the king
        for(int kingUp = kingPosY-1; kingUp >= 0; kingUp--){
            if(boardData.boardSquares[kingUp][kingPosX].isOccupied()){
                if(boardData.boardSquares[kingUp][kingPosX].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingUp][kingPosX].getPieceOccupied() == "rook" || boardData.boardSquares[kingUp][kingPosX].getPieceOccupied() == "queen")){
                    return true;
                }
                else{
                    break;
                }
            }
        } //checks the up side of the king
        for(int kingDown = kingPosY+1; kingDown < 8; kingDown++){
            if(boardData.boardSquares[kingDown][kingPosX].isOccupied()){
                if(boardData.boardSquares[kingDown][kingPosX].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingDown][kingPosX].getPieceOccupied() == "rook" || boardData.boardSquares[kingDown][kingPosX].getPieceOccupied() == "queen")){
                    return true;
                }
                else{
                    break;
                }
            }
        } //checks the down side of the king

        //this checks for upper left
        for(int kingDiagonalY = kingPosY - 1, kingDiagonalX = kingPosX - 1; kingDiagonalY >= 0 && kingDiagonalX >= 0; kingDiagonalY--, kingDiagonalX--){
            if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].isOccupied()){
                if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "bishop" || boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "queen")){
                    return true;
                }
                else{

                    break;
                }
            }
        }
        //this checks for lower left
        for(int kingDiagonalY = kingPosY + 1, kingDiagonalX = kingPosX - 1; kingDiagonalY < 8 && kingDiagonalX >= 0; kingDiagonalY++, kingDiagonalX--){
            if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].isOccupied()){
                if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "bishop" || boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "queen")){
                    return true;
                }
                else{

                    break;
                }
            }
        }
        //this checks for upper right
        for(int kingDiagonalY = kingPosY - 1, kingDiagonalX = kingPosX + 1; kingDiagonalY >= 0 && kingDiagonalX < 8; kingDiagonalY--, kingDiagonalX++){
            if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].isOccupied()){
                if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "bishop" || boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "queen")){
                    return true;
                }
                else{
                    break;
                }
            }
        }
        //this checks for lower right
        for(int kingDiagonalY = kingPosY + 1, kingDiagonalX = kingPosX + 1; kingDiagonalY < 8  && kingDiagonalX < 8; kingDiagonalY++, kingDiagonalX++){
            if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].isOccupied()){
                if(boardData.boardSquares[kingDiagonalY][kingDiagonalX].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "bishop" || boardData.boardSquares[kingDiagonalY][kingDiagonalX].getPieceOccupied() == "queen")){
                    return true;
                }
                else{
                    break;
                }
            }
        }

        //checks if there are knights
        if(kingPosY-2 >= 0 && kingPosX-1 >= 0){
            if(boardData.boardSquares[kingPosY-2][kingPosX-1].isOccupied()){
                if(boardData.boardSquares[kingPosY-2][kingPosX-1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY-2][kingPosX-1].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }
        if(kingPosY-2 >= 0 && kingPosX+1 < 8){
            if(boardData.boardSquares[kingPosY-2][kingPosX+1].isOccupied()){
                if(boardData.boardSquares[kingPosY-2][kingPosX+1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY-2][kingPosX+1].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }
        if(kingPosY-1 >= 0 && kingPosX-2 < 0){
            if(boardData.boardSquares[kingPosY-1][kingPosX-2].isOccupied()){
                if(boardData.boardSquares[kingPosY-1][kingPosX-2].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY-1][kingPosX-2].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }
        if(kingPosY-1 >= 0 && kingPosX+2 > 8){
            if(boardData.boardSquares[kingPosY-1][kingPosX+2].isOccupied()){
                if(boardData.boardSquares[kingPosY-1][kingPosX+2].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY-1][kingPosX+2].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }
        if(kingPosY+1 < 8 && kingPosX-2 >= 0){
            if(boardData.boardSquares[kingPosY+1][kingPosX-2].isOccupied()){
                if(boardData.boardSquares[kingPosY+1][kingPosX-2].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY+1][kingPosX-2].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }
        if(kingPosY+1 < 8 && kingPosX+2 < 8){
            if(boardData.boardSquares[kingPosY+1][kingPosX+2].isOccupied()){
                if(boardData.boardSquares[kingPosY+1][kingPosX+2].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY+1][kingPosX+2].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }
        if(kingPosY+2 < 8 && kingPosX-1 >= 0){
            if(boardData.boardSquares[kingPosY+2][kingPosX-1].isOccupied()){
                if(boardData.boardSquares[kingPosY+2][kingPosX-1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY+2][kingPosX-1].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }
        if(kingPosY+2 < 8 && kingPosX+1 >= 0){
            if(boardData.boardSquares[kingPosY+2][kingPosX+1].isOccupied()){
                if(boardData.boardSquares[kingPosY+2][kingPosX+1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY+2][kingPosX+1].getPieceOccupied() == "knight")){
                    return true;
                }
            }
        }

        //checks if there are pawns
        if(currentTeam == "white"){
            if(kingPosY+1 < 8 && kingPosX+1 < 8){
                if(boardData.boardSquares[kingPosY+1][kingPosX+1].isOccupied()){
                    if(boardData.boardSquares[kingPosY+1][kingPosX+1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY+1][kingPosX+1].getPieceOccupied() == "pawn")){
                        return true;
                    }
                }
            }
            if(kingPosY+1 < 8 && kingPosX-1 >= 0){
                if(boardData.boardSquares[kingPosY+1][kingPosX-1].isOccupied()){
                    if(boardData.boardSquares[kingPosY+1][kingPosX-1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY+1][kingPosX-1].getPieceOccupied() == "pawn")){
                        return true;
                    }
                }
            }
        }
        else{
            if(kingPosY-1 >= 0 && kingPosX+1 < 8){
                if(boardData.boardSquares[kingPosY-1][kingPosX+1].isOccupied()){
                    if(boardData.boardSquares[kingPosY-1][kingPosX+1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY-1][kingPosX+1].getPieceOccupied() == "pawn")){
                        return true;
                    }
                }
            }
            if(kingPosY-1 >= 0 && kingPosX-1 >= 0){
                if(boardData.boardSquares[kingPosY-1][kingPosX-1].isOccupied()){
                    if(boardData.boardSquares[kingPosY-1][kingPosX-1].getTeamOccupied() != currentTeam && (boardData.boardSquares[kingPosY-1][kingPosX-1].getPieceOccupied() == "pawn")){
                        return true;
                    }
                }
            }
        }

        return false;

    }

    private void copyData(){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                tempBoard.boardSquares[y][x].setPieceOccupied(boardData.boardSquares[y][x].getPieceOccupied());
                tempBoard.boardSquares[y][x].setIconData(boardData.boardSquares[y][x].getIconData());
                tempBoard.boardSquares[y][x].setOccupied(boardData.boardSquares[y][x].isOccupied());
                tempBoard.boardSquares[y][x].setTeamOccupied(boardData.boardSquares[y][x].getTeamOccupied());
            }
        }
    }

    private void revertToPrevious(){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                boardData.boardSquares[y][x].setPieceOccupied(tempBoard.boardSquares[y][x].getPieceOccupied());
                boardData.boardSquares[y][x].setIconData(tempBoard.boardSquares[y][x].getIconData());
                boardData.boardSquares[y][x].setOccupied(tempBoard.boardSquares[y][x].isOccupied());
                boardData.boardSquares[y][x].setTeamOccupied(tempBoard.boardSquares[y][x].getTeamOccupied());
            }
        }
    }
    private boolean isCheckmate(){
        copyData();
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(boardData.boardSquares[y][x].getTeamOccupied() == currentTeam){
                    if(boardData.boardSquares[y][x].getPieceOccupied() == "rook"){
                        for(int left = x - 1; left >= 0 && boardData.boardSquares[y][left].getTeamOccupied() != currentTeam; left--){
                            if(boardData.boardSquares[y][left].isOccupied() && boardData.boardSquares[y][left].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y][left].setOccupied(true);
                                boardData.boardSquares[y][left].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y][left].setPieceOccupied("rook");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,left);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y][left].setOccupied(true);
                            boardData.boardSquares[y][left].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y][left].setPieceOccupied("rook");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,left);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int right = x + 1; right < 8 && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam;right++){
                            if(boardData.boardSquares[y][right].isOccupied() && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y][right].setOccupied(true);
                                boardData.boardSquares[y][right].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y][right].setPieceOccupied("rook");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,right);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[y][right].setOccupied(true);
                            boardData.boardSquares[y][right].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y][right].setPieceOccupied("rook");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,right);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int up = y - 1; up >= 0 && boardData.boardSquares[up][x].getTeamOccupied() != currentTeam; up--){
                            if(boardData.boardSquares[y][x].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[up][x].setOccupied(true);
                                boardData.boardSquares[up][x].setTeamOccupied(currentTeam);
                                boardData.boardSquares[up][x].setPieceOccupied("rook");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,y);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[up][x].setOccupied(true);
                            boardData.boardSquares[up][x].setTeamOccupied(currentTeam);
                            boardData.boardSquares[up][x].setPieceOccupied("rook");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,x);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int down = y + 1; down < 8 && boardData.boardSquares[down][x].getTeamOccupied() != currentTeam; down++){
                            if(boardData.boardSquares[down][x].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[down][x].setOccupied(true);
                                boardData.boardSquares[down][x].setTeamOccupied(currentTeam);
                                boardData.boardSquares[down][x].setPieceOccupied("rook");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,x);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[down][x].setOccupied(true);
                            boardData.boardSquares[down][x].setTeamOccupied(currentTeam);
                            boardData.boardSquares[down][x].setPieceOccupied("rook");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,x);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                    }
                    if(boardData.boardSquares[y][x].getPieceOccupied() == "bishop"){
                        for(int left = x - 1, up = y - 1; left >= 0 && up >= 0 && boardData.boardSquares[y][left].getTeamOccupied() != currentTeam; left--, up--){
                            if(boardData.boardSquares[up][left].isOccupied() && boardData.boardSquares[up][left].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[up][left].setOccupied(true);
                                boardData.boardSquares[up][left].setTeamOccupied(currentTeam);
                                boardData.boardSquares[up][left].setPieceOccupied("bishop");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,left);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[up][left].setOccupied(true);
                            boardData.boardSquares[up][left].setTeamOccupied(currentTeam);
                            boardData.boardSquares[up][left].setPieceOccupied("bishop");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,left);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int right = x + 1, up = y - 1; right < 8 && up >= 0 && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam;right++, up--){
                            if(boardData.boardSquares[y][right].isOccupied() && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[up][right].setOccupied(true);
                                boardData.boardSquares[up][right].setTeamOccupied(currentTeam);
                                boardData.boardSquares[up][right].setPieceOccupied("bishop");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,right);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[y][right].setOccupied(true);
                            boardData.boardSquares[y][right].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y][right].setPieceOccupied("bishop");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,right);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int down = y + 1, left = x - 1; down < 8 && left >= 0 && boardData.boardSquares[down][left].getTeamOccupied() != currentTeam; down++, left--){
                            if(boardData.boardSquares[y][x].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[down][left].setOccupied(true);
                                boardData.boardSquares[down][left].setTeamOccupied(currentTeam);
                                boardData.boardSquares[down][left].setPieceOccupied("bishop");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,left);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[down][left].setOccupied(true);
                            boardData.boardSquares[down][left].setTeamOccupied(currentTeam);
                            boardData.boardSquares[down][left].setPieceOccupied("bishop");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,left);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int down = y + 1, right = x + 1; down < 8 && right < 8&& boardData.boardSquares[down][right].getTeamOccupied() != currentTeam; down++, right++){
                            if(boardData.boardSquares[down][right].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[down][right].setOccupied(true);
                                boardData.boardSquares[down][right].setTeamOccupied(currentTeam);
                                boardData.boardSquares[down][right].setPieceOccupied("bishop");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,right);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[down][x].setOccupied(true);
                            boardData.boardSquares[down][x].setTeamOccupied(currentTeam);
                            boardData.boardSquares[down][x].setPieceOccupied("bishop");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,right);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                    }
                    if(boardData.boardSquares[y][x].getPieceOccupied() == "knight"){
                        if(y-2 >= 0 && x-1 >=0){
                            if(boardData.boardSquares[y-2][x-1].isOccupied() && boardData.boardSquares[y-2][x-1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y-2][x-1].setOccupied(true);
                                boardData.boardSquares[y-2][x-1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y-2][x-1].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-2,x-1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y-2][x-1].setOccupied(true);
                            boardData.boardSquares[y-2][x-1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y-2][x-1].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-2,x-1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y-2 >= 0 && x+1 < 8){
                            if(boardData.boardSquares[y-2][x+1].isOccupied() && boardData.boardSquares[y-2][x+1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y-2][x+1].setOccupied(true);
                                boardData.boardSquares[y-2][x+1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y-2][x+1].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-2,x+1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y-2][x+1].setOccupied(true);
                            boardData.boardSquares[y-2][x+1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y-2][x+1].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-2,x+1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y-1 >= 0 && x-2 >=0){
                            if(boardData.boardSquares[y-1][x-2].isOccupied() && boardData.boardSquares[y-1][x-2].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y-1][x-2].setOccupied(true);
                                boardData.boardSquares[y-1][x-2].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y-1][x-2].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x-2);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y-1][x-2].setOccupied(true);
                            boardData.boardSquares[y-1][x-2].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y-1][x-2].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x-2);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y-1 >= 0 && x+2 < 8){
                            if(boardData.boardSquares[y-1][x+2].isOccupied() && boardData.boardSquares[y-1][x+2].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y-1][x+2].setOccupied(true);
                                boardData.boardSquares[y-1][x+2].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y-1][x+2].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x+2);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y-1][x+2].setOccupied(true);
                            boardData.boardSquares[y-1][x+2].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y-1][x+2].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x+2);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }

                        if(y+1 < 8 && x-2 >= 0){
                            if(boardData.boardSquares[y+1][x-2].isOccupied() && boardData.boardSquares[y+1][x-2].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y+1][x-2].setOccupied(true);
                                boardData.boardSquares[y+1][x-2].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y+1][x-2].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x-2);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y+1][x-2].setOccupied(true);
                            boardData.boardSquares[y+1][x-2].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y+1][x-2].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x-2);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y+1 < 8 && x+2 < 8){
                            if(boardData.boardSquares[y+1][x+2].isOccupied() && boardData.boardSquares[y+1][x+2].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y+1][x+2].setOccupied(true);
                                boardData.boardSquares[y+1][x+2].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y+1][x+2].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x+2);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y+1][x+2].setOccupied(true);
                            boardData.boardSquares[y+1][x+2].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y+1][x+2].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x+2);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y+2 < 8 && x-1 >= 0){
                            if(boardData.boardSquares[y+2][x-1].isOccupied() && boardData.boardSquares[y+2][x-1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y+2][x-1].setOccupied(true);
                                boardData.boardSquares[y+2][x-1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y+2][x-1].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+2,x-1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y+2][x-1].setOccupied(true);
                            boardData.boardSquares[y+2][x-1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y+2][x-1].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+2,x-1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y+2 < 8 && x+1 < 8){
                            if(boardData.boardSquares[y+2][x+1].isOccupied() && boardData.boardSquares[y+2][x+1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y+2][x+1].setOccupied(true);
                                boardData.boardSquares[y+2][x+1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y+2][x+1].setPieceOccupied("knight");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+2,x+1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y+2][x+1].setOccupied(true);
                            boardData.boardSquares[y+2][x+1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y+2][x+1].setPieceOccupied("knight");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+2,x+1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                    }
                    if(boardData.boardSquares[y][x].getPieceOccupied() == "queen"){
                        for(int left = x - 1; left >= 0 && boardData.boardSquares[y][left].getTeamOccupied() != currentTeam; left--){
                            if(boardData.boardSquares[y][left].isOccupied() && boardData.boardSquares[y][left].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y][left].setOccupied(true);
                                boardData.boardSquares[y][left].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y][left].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,left);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y][left].setOccupied(true);
                            boardData.boardSquares[y][left].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y][left].setPieceOccupied("queen");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,left);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int right = x + 1; right < 8 && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam;right++){
                            if(boardData.boardSquares[y][right].isOccupied() && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y][right].setOccupied(true);
                                boardData.boardSquares[y][right].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y][right].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,right);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[y][right].setOccupied(true);
                            boardData.boardSquares[y][right].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y][right].setPieceOccupied("queen");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y,right);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int up = y - 1; up >= 0 && boardData.boardSquares[up][x].getTeamOccupied() != currentTeam; up--){
                            if(boardData.boardSquares[y][x].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[up][x].setOccupied(true);
                                boardData.boardSquares[up][x].setTeamOccupied(currentTeam);
                                boardData.boardSquares[up][x].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,y);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[up][x].setOccupied(true);
                            boardData.boardSquares[up][x].setTeamOccupied(currentTeam);
                            boardData.boardSquares[up][x].setPieceOccupied("queen");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,x);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int down = y + 1; down < 8 && boardData.boardSquares[down][x].getTeamOccupied() != currentTeam; down++){
                            if(boardData.boardSquares[down][x].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[down][x].setOccupied(true);
                                boardData.boardSquares[down][x].setTeamOccupied(currentTeam);
                                boardData.boardSquares[down][x].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,x);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[down][x].setOccupied(true);
                            boardData.boardSquares[down][x].setTeamOccupied(currentTeam);
                            boardData.boardSquares[down][x].setPieceOccupied("queen");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,x);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }

                        for(int left = x - 1, up = y - 1; left >= 0 && up >= 0 && boardData.boardSquares[y][left].getTeamOccupied() != currentTeam; left--, up--){
                            if(boardData.boardSquares[up][left].isOccupied() && boardData.boardSquares[up][left].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[up][left].setOccupied(true);
                                boardData.boardSquares[up][left].setTeamOccupied(currentTeam);
                                boardData.boardSquares[up][left].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,left);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[up][left].setOccupied(true);
                            boardData.boardSquares[up][left].setTeamOccupied(currentTeam);
                            boardData.boardSquares[up][left].setPieceOccupied("queen");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,left);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int right = x + 1, up = y - 1; right < 8 && up >= 0 && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam;right++, up--){
                            if(boardData.boardSquares[y][right].isOccupied() && boardData.boardSquares[y][right].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[up][right].setOccupied(true);
                                boardData.boardSquares[up][right].setTeamOccupied(currentTeam);
                                boardData.boardSquares[up][right].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,right);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[y][right].setOccupied(true);
                            boardData.boardSquares[y][right].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y][right].setPieceOccupied("queen");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,up,right);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int down = y + 1, left = x - 1; down < 8 && left >= 0 && boardData.boardSquares[down][left].getTeamOccupied() != currentTeam; down++, left--){
                            if(boardData.boardSquares[y][x].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[down][left].setOccupied(true);
                                boardData.boardSquares[down][left].setTeamOccupied(currentTeam);
                                boardData.boardSquares[down][left].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,left);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[down][left].setOccupied(true);
                            boardData.boardSquares[down][left].setTeamOccupied(currentTeam);
                            boardData.boardSquares[down][left].setPieceOccupied("queen");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,left);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        for(int down = y + 1, right = x + 1; down < 8 && right < 8&& boardData.boardSquares[down][right].getTeamOccupied() != currentTeam; down++, right++){
                            if(boardData.boardSquares[down][right].isOccupied() && boardData.boardSquares[y][x].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[down][right].setOccupied(true);
                                boardData.boardSquares[down][right].setTeamOccupied(currentTeam);
                                boardData.boardSquares[down][right].setPieceOccupied("queen");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,right);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();
                                    break;
                                }
                            }

                            boardData.boardSquares[down][x].setOccupied(true);
                            boardData.boardSquares[down][x].setTeamOccupied(currentTeam);
                            boardData.boardSquares[down][x].setPieceOccupied("queen");


                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");


                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,down,right);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                    }
                    /* if(boardData.boardSquares[y][x].getPieceOccupied() == "king"){
                        if(y-1 >= 0 && x-1 >=0){
                            if(boardData.boardSquares[y-1][x-1].isOccupied() && boardData.boardSquares[y-1][x-1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y-1][x-1].setOccupied(true);
                                boardData.boardSquares[y-1][x-1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y-1][x-1].setPieceOccupied("king");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x-1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y-1][x-1].setOccupied(true);
                            boardData.boardSquares[y-1][x-1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y-1][x-1].setPieceOccupied("king");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x-1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y-1 >= 0 && x+1 <8){
                            if(boardData.boardSquares[y-1][x-1].isOccupied() && boardData.boardSquares[y-1][x+1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y-1][x+1].setOccupied(true);
                                boardData.boardSquares[y-1][x+1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y-1][x+1].setPieceOccupied("king");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x+1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y-1][x+1].setOccupied(true);
                            boardData.boardSquares[y-1][x+1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y-1][x+1].setPieceOccupied("king");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y-1,x+1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y+1 < 8 && x-1 >=0){
                            if(boardData.boardSquares[y+1][x-1].isOccupied() && boardData.boardSquares[y+1][x-1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y+1][x-1].setOccupied(true);
                                boardData.boardSquares[y+1][x-1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y+1][x-1].setPieceOccupied("king");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x-1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y+1][x-1].setOccupied(true);
                            boardData.boardSquares[y+1][x-1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y+1][x-1].setPieceOccupied("king");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x-1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                        if(y+1 < 8 && x+1 <8){
                            if(boardData.boardSquares[y+1][x+1].isOccupied() && boardData.boardSquares[y+1][x+1].getTeamOccupied() != currentTeam){
                                boardData.boardSquares[y][x].setOccupied(false);
                                boardData.boardSquares[y][x].setTeamOccupied("empty");
                                boardData.boardSquares[y][x].setPieceOccupied("empty");

                                boardData.boardSquares[y+1][x+1].setOccupied(true);
                                boardData.boardSquares[y+1][x+1].setTeamOccupied(currentTeam);
                                boardData.boardSquares[y+1][x+1].setPieceOccupied("king");
                                if(!checkIfChecked()){
                                    System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x+1);
                                    revertToPrevious();
                                    return false;
                                }
                                else{
                                    revertToPrevious();

                                    break;
                                }
                            }

                            boardData.boardSquares[y+1][x+1].setOccupied(true);
                            boardData.boardSquares[y+1][x+1].setTeamOccupied(currentTeam);
                            boardData.boardSquares[y+1][x+1].setPieceOccupied("king");

                            boardData.boardSquares[y][x].setOccupied(false);
                            boardData.boardSquares[y][x].setTeamOccupied("empty");
                            boardData.boardSquares[y][x].setPieceOccupied("empty");

                            if(!checkIfChecked()){
                                System.out.printf("move (%d, %d) to (%d,%d)\n", y,x,y+1,x+1);
                                revertToPrevious();
                                return false;
                            }
                            else{
                                revertToPrevious();
                            }
                        }
                    } */
                }
            }
        }

        return true;
    }

    private boolean isValidMove(int destRow, int destCol) {

        //this checks the dest.square if the occupant is from the same team
        if(boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied() != boardData.boardSquares[destRow][destCol].getTeamOccupied()){

            if((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "knight") {
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
            if((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "king") {
                    int xDistance = Math.abs(destCol - currentSelectedX);
                    int yDistance = Math.abs(destRow - currentSelectedY);
                    if(xDistance<=1&&yDistance<=1){
                        if(currentTeam == "white"){
                            wKingY = destRow;
                            wKingX = destCol;
                        }
                        else{
                            bKingY = destRow;
                            bKingX = destCol;
                        }
                        return true;
                    }
                }

                //White Pawns
            if((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "pawn" &&boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied() == "white"){
                    Square p = boardData.boardSquares[destRow][currentSelectedX];
                    if(p.isOccupied() == true && destCol == currentSelectedX && destRow - currentSelectedY == -1){ return false; }
                    int yDistance = Math.abs(destRow - currentSelectedY);

                    if(currentSelectedY == 6){ // Current position of the white pawns, 6 meaning the [number of tiles -1] xd
                        if(yDistance == 2) { // yDistance == 2 enables the pawns to move 2 squares forward from their original position
                            for (int i = 1; i < 2; i++) {
                                p = boardData.boardSquares[currentSelectedY - i][currentSelectedX];
                                if (p.isOccupied() == true) {
                                    return false;
                                }
                            }
                            if(destCol == currentSelectedX){
                                return true;
                            }
                        }
                    }
                    if(destRow - currentSelectedY == -1 && currentSelectedX == destCol && !boardData.boardSquares[destRow][destCol].isOccupied()){
                        return true;
                    }
                    if(destRow - currentSelectedY == -1 && currentSelectedX != destCol) {
                        Square a = boardData.boardSquares[destRow][destCol];
                        int xDistance = Math.abs(destCol - currentSelectedX);
                        if (a.isOccupied() == true && xDistance == 1) {
                            return true;
                        }
                    }
                }

                //Black Pawns
            if((boardData.boardSquares[currentSelectedY][currentSelectedX].getPieceOccupied()) == "pawn" && boardData.boardSquares[currentSelectedY][currentSelectedX].getTeamOccupied() == "black"){
                    Square p = boardData.boardSquares[destRow][currentSelectedX];
                    if(p.isOccupied() == true && destCol == currentSelectedX && destRow - currentSelectedY == -1){ return false; }
                    int yDistance = Math.abs(destRow - currentSelectedY);

                    if(currentSelectedY == 1){
                        if(yDistance == 2) { // yDistance == 2 enables the pawns to move 2 squares forward from their original position
                            for (int i = 1; i < 2; i++) {
                                p = boardData.boardSquares[currentSelectedY + i][currentSelectedX];
                                if (p.isOccupied() == true) {
                                    return false;
                                }
                            }
                            if(destCol == currentSelectedX){
                                return true;}
                        }
                    }
                    if(destRow - currentSelectedY == 1 && currentSelectedX == destCol  && !boardData.boardSquares[destRow][destCol].isOccupied()
                    ){
                        return true;
                    }
                    if(destRow - currentSelectedY == 1 && currentSelectedX != destCol) {
                        Square a = boardData.boardSquares[destRow][destCol];
                        int xDistance = Math.abs(destCol-currentSelectedX);
                        if (a.isOccupied() == true &&xDistance==1) {
                            return true;
                        }
                    }
                }
            }




       return false;


    }

    private void showSelected( int curRow, int curCol){
        squares[curRow][curCol].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
    }





}
