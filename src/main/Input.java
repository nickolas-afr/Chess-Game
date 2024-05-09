package main;

import pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input extends MouseAdapter {

    Board board;
    public Input(Board board){
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int col = e.getX() / board.squareSize;
        int row = e.getY() / board.squareSize;

        Piece pieceXY = board.getPiece(col, row);
        if(pieceXY != null){
            board.selectedPiece = pieceXY;
        }

    }
    @Override
    public void mouseDragged(MouseEvent e) {

        if(board.selectedPiece != null){
            board.selectedPiece.x = e.getX() - board.squareSize / 2;
            board.selectedPiece.y = e.getY() - board.squareSize / 2;

            board.repaint();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int col = e.getX() / board.squareSize;
        int row = e.getY() / board.squareSize;

        if(board.selectedPiece != null){

            Move move = new Move(board, board.selectedPiece, col, row);

            if(board.isValidMove(move)){
                board.makeMove(move);
            } else{
                board.selectedPiece.x = board.selectedPiece.col * board.squareSize;
                board.selectedPiece.y = board.selectedPiece.row * board.squareSize;
            }
        }

        board.selectedPiece = null;
        board.repaint();

    }

}
