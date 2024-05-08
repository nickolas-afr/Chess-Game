package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {

    public int squareSize = 70;
    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantSquare = -1;




    public Board(){
        this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    public Piece getPiece(int col, int row){
        for(Piece piece : pieceList){
            if(piece.row == row && piece.col == col)
                return piece;
        }

        return null;
    }

    public void makeMove(Move move){

        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }
        else if(move.piece.name.equals("King")) {
            moveKing(move);
        }
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;

            move.piece.x = move.newCol * squareSize;
            move.piece.y = move.newRow * squareSize;

            move.piece.isFirstMove = false;

            take(move.take);
    }

    private void moveKing(Move move){

        if(Math.abs(move.piece.col - move.newCol) == 2){
            Piece rook;
            if(move.piece.col < move.newCol){
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else{
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.x = rook.col * squareSize;
        }

    }

    private void movePawn(Move move) {

        //en passant
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if(getSquareNum(move.newCol, move.newRow) == enPassantSquare){
            move.take = getPiece(move.newCol, move.newRow + colorIndex);
        }

        if(Math.abs(move.piece.row - move.newRow) == 2){
            enPassantSquare = getSquareNum(move.newCol, move.newRow + colorIndex);
        }
        else{
            enPassantSquare = -1;
        }

        //promotions
        colorIndex = move.piece.isWhite ? 0 : 7;
        if(move.newRow == colorIndex){
            promotePawn(move);
        }
    }

    private void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        take(move.piece);
    }

    public void take(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move){

        if(sameTeam(move.piece, move.take)){
            return false;
        }

        if(!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }

        if(move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }

        if(checkScanner.isKingChecked(move)){
            return false;
        }

        return true;
    }

    public boolean sameTeam(Piece p1, Piece p2){
        if(p1 == null || p2 == null){
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public int getSquareNum(int col, int row){
        return row * rows + col;
    }

    Piece findKing(boolean isWhite){
        for(Piece piece : pieceList){
            if(isWhite == piece.isWhite && piece.name.equals("King")){
                return piece;
            }
        }
        return null;
    }

    public void addPieces(){
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));

        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));

        pieceList.add(new Pawn(this, 0, 1, false));
        pieceList.add(new Pawn(this, 1, 1, false));
        pieceList.add(new Pawn(this, 2, 1, false));
        pieceList.add(new Pawn(this, 3, 1, false));
        pieceList.add(new Pawn(this, 4, 1, false));
        pieceList.add(new Pawn(this, 5, 1, false));
        pieceList.add(new Pawn(this, 6, 1, false));
        pieceList.add(new Pawn(this, 7, 1, false));

        pieceList.add(new Pawn(this, 0, 6, true));
        pieceList.add(new Pawn(this, 1, 6, true));
        pieceList.add(new Pawn(this, 2, 6, true));
        pieceList.add(new Pawn(this, 3, 6, true));
        pieceList.add(new Pawn(this, 4, 6, true));
        pieceList.add(new Pawn(this, 5, 6, true));
        pieceList.add(new Pawn(this, 6, 6, true));
        pieceList.add(new Pawn(this, 7, 6, true));

    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        // show board
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                g2d.setColor((c+r) % 2 == 0 ? new Color(252, 235, 151, 255) : new Color(60, 10, 7, 255));
                g2d.fillRect(c * squareSize, r * squareSize, squareSize, squareSize);
            }
        }

        // show highlights
        if(selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {

                        g2d.setColor(new Color(0, 255, 0, 110));
                        g2d.fillRect(c * squareSize, r * squareSize, squareSize, squareSize);

                    }
                }
            }
        }

        // show pieces
        for(Piece piece : pieceList){
            piece.paint(g2d);
        }

    }

}
