package com.chess.chesspieces;

import javax.swing.*;

public class ChessPiece {
    public static class Knight{
        public String iconDestinationBlack = "src/com/chess/images/black_pieces/Knight.png";
        public String iconDestinationWhite = "src/com/chess/images/white_pieces/Knight.png";
        public String chessPieceName = "knight";
    }

    public class Bishop{
        public String iconDestinationBlack = "src/com/chess/images/black_pieces/Bishop.png";
        public String iconDestinationWhite = "src/com/chess/images/white_pieces/Bishop.png";
        public String chessPieceName = "bishop";
    }
    public static class Rook{
        public String iconDestinationBlack = "src/com/chess/images/black_pieces/Rook.png";
        public String iconDestinationWhite = "src/com/chess/images/white_pieces/Rook.png";
        public String chessPieceName = "rook";
    }
    public class King{
        public ImageIcon icon = new ImageIcon("src/com/chess/images/black_pieces/King.png");
        String chessPieceName = "king";
    }
    public class Queen{
        public ImageIcon icon = new ImageIcon("src/com/chess/images/black_pieces/Queen.png");
        String chessPieceName = "queen";
    }
    public class Pawn{
        public ImageIcon icon = new ImageIcon("src/com/chess/images/black_pieces/Pawn.png");
        String chessPieceName = "pawn";
    }
}
