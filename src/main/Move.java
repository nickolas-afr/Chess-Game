package main;

import pieces.Piece;

public class Move {

    int prevCol;
    int prevRow;
    int newCol;
    int newRow;

    Piece piece;
    Piece take;

    public Move(Board board, Piece piece, int newCol, int newRow){
        this.prevCol = piece.col;
        this.prevRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;
        this.take = board.getPiece(newCol, newRow);
    }

}
