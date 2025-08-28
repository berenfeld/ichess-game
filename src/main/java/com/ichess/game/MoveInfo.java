//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game;

import com.ichess.game.Common;
import com.ichess.game.piece.Piece;
import com.ichess.game.Utils;

import java.util.ArrayList;
import java.util.List;
import static com.ichess.game.Log.*;

/**
 * This class contains additional information about the state of the game when
 * the move was played. A move info is obtained from a Game using getMoveInfo,
 * and is valid as long as the move was not taken back. This move info can also
 * be obtained from Move.getMoveInfo()
 *
 * @author Ran Berenfeld
 * @version 1.0
 */
public class MoveInfo {

	private Game _game;

	private boolean check = false;
	private boolean doubleCheck = false;
	private boolean checkMate = false;
	private int draw50MovesCount; // for 50 moves, count moves with no pawn or
	private String fenString;
    private String fenPosition;

    public String getFenStringForDrawTesting() {
        if ( fenStringForDrawTesting != null ) {
            return fenStringForDrawTesting;
        }
        fenStringForDrawTesting = fenString;
        // remove trailing move number
        fenStringForDrawTesting = fenStringForDrawTesting.substring(0, fenStringForDrawTesting.lastIndexOf(' '));
        // remove trailing 50 moves count
        fenStringForDrawTesting = fenStringForDrawTesting.substring(0, fenStringForDrawTesting.lastIndexOf(' '));
        return fenStringForDrawTesting;
    }

    private String fenStringForDrawTesting = null;
	private boolean hasEnoughMaterial[] = { true, true, true, true };
	private Move move;
	private Piece movedPiece[] = { null, null };
	private boolean staleMate = false;
	private List<Move> validNextMoves = new ArrayList<Move>();
    private int ecoId = -1; // cached eco id. -1 means was not calculated

	public MoveInfo(Game game) {
		_game = game;
	}

    public int getEcoId() {
        return ecoId;
    }

    public void setEcoId(int ecoid)
    {
        this.ecoId = ecoid;
    }

    int addValidMove(Piece piece, int toX, int toY ) {
        return addValidMove(piece, toX, toY, false);
    }

	/*
	 * add valid move of a piece to a target square
	 */

	int addValidMove(Piece piece, int toX, int toY, boolean drop ) {
		Utils.AssertNull(piece);
		boolean promotion = piece.isPawn() && ((toX == 1) || (toX == 8));
        if (_game.isSevenBoom() && piece.isPawn()) {
            if (toX == 2 || toX == 7) {
                promotion = true;
            }
        }
		if (promotion) {
			if (_game.isSuicide())
			{
				addValidMove(piece, toX, toY, Common.PIECE_TYPE_KING);
			}

			if (_game.isGrassHopper())
			{
				addValidMove(piece, toX, toY, Common.PIECE_TYPE_GRASSHOPER);
			}

			if (_game.isMiniCapa())
			{
				addValidMove(piece, toX, toY, Common.PIECE_TYPE_ARCHBISHOP);
				addValidMove(piece, toX, toY, Common.PIECE_TYPE_CHANCELLOR);
			}

            if (_game.isSevenBoom()) {
                if (toX == 1 || toX == 8) {
			        addValidMove(piece, toX, toY, Common.PIECE_TYPE_QUEEN);
                }
                else {
                    addValidMove(piece, toX, toY, Common.PIECE_TYPE_PAWN);  // stays as a pawn
                }
            }
            else {
                addValidMove(piece, toX, toY, Common.PIECE_TYPE_QUEEN);
            }
			addValidMove(piece, toX, toY, Common.PIECE_TYPE_ROOK);
			addValidMove(piece, toX, toY, Common.PIECE_TYPE_KNIGHT);
			return addValidMove(piece, toX, toY, Common.PIECE_TYPE_BISHOP);
		}
        if (drop)
        {
            return addValidMove( piece, toX, toY, Common.PIECE_TYPE_ILLEGAL, true );
        }

		return addValidMove(piece, toX, toY, Common.PIECE_TYPE_ILLEGAL, false);

	}

    int addValidMove(Piece piece, int toX, int toY, int promotionPiece) {
        return addValidMove(piece, toX, toY, promotionPiece, false);
    }

	/*
	 * add a valid move
	 */
	int addValidMove(Piece piece, int toX, int toY, int promotionPiece, boolean drop) {

		Utils.AssertNull(piece);

        if (drop)
        {
            Move move = new Move(_game, toX, toY, toX, toY, piece.getTypeWhenDropping());
            move.setMoveNumber(_game.getCurrentMove() + 1);
            move.setMovedPiece(piece);
            if (! validNextMoves.contains(move)) {
                validNextMoves.add(move);
            }
            Log.debug("adding move " + _game.getCurrentMove() + " valid drop to " + toX + "," + toY + " piece " + piece.getTypeWhenDropping());
            return Common.RC_OK;
        }

		Move move = new Move(_game, piece.getX(), piece.getY(), toX, toY, promotionPiece);
		move.setMovedPiece(piece);
        move.setMoveNumber(_game.getCurrentMove() + 1);
		Piece captured = _game.getPieceAt(toX, toY);

		// check if EP capture move
		if (piece.isPawn()) {
			if ((piece.getY() != toY) && (captured == null)) {
				if (piece.isWhite()) {
					captured = _game.getPieceAt(toX - 1, toY);
				} else {
					captured = _game.getPieceAt(toX + 1, toY);
				}
			}
		}
		move.setCapturedPiece(captured);
        move.isCheck = piece.isCheck(toX, toY);

		Log.debug("adding valid move " + _game.getCurrentMove() + " from " + piece.getX() + "," + piece.getY() + " to " + toX + "," + toY +
            " piece " + piece.getType());

        if (! validNextMoves.contains(move)) {
            validNextMoves.add(move);
        }

		return Common.RC_OK;
	}

	/**
	 * Return the number of half-moves played from the last capture or pawn
	 * advance, after this move was played.
	 *
	 * @return The number of half-moves played from the last capture or pawn
	 *         advance.
	 */
	public int getDraw50MovesCount() {
		return draw50MovesCount;
	}

	/*
	 * Check if a move is valid
	 */

	/**
	 * Return a FEN representation of the Game right after this move was played.
	 *
	 * @return A FEN representation of the Game right after this move was
	 *         played.
	 */
	public String getFENString() {
		return fenString;
	}

    /**
     * Return a FEN representation of the Game board position right after this move was played.
     *
     * @return A FEN representation of the Game board position right after this move was
     *         played.
     */
    public String getFENPosition() {
        return fenPosition;
    }


	/*
	 * get a valid move given the coordinates
	 */

	public boolean[] getHasEnoughMaterial() {
		return hasEnoughMaterial;
	}

	/**
	 * Returns the corresponding move.
	 *
	 * @return The corresponding move.
	 */
	public Move getMove() {
		return move;
	}

	public Piece[] getMovedPiece() {
		return movedPiece;
	}

	Move getValidMove(int fromX, int fromY, int toX, int toY, int promotionPiece) {
		for (Move move : validNextMoves) {
			if ((move.getFromX() == fromX) && (move.getFromY() == fromY) && (move.getToX() == toX) && (move.getToY() == toY)
				&& (move.getAdditionalPieceTypeInfo() == promotionPiece)) {
				Log.debug("found valid move " + move.getNameNum());
				return move;
			}
		}
		return null;
	}

	/**
	 * Return all the valid moves that can be played from this move.
	 *
	 * @return All the valid moves that can be played from this move.
	 */
	public List<Move> getValidNextMoves() {
		return validNextMoves;
	}

	/**
	 * Returns true if the game is in check after this move. otherwise false.
	 *
	 * @return true if the game is in check after this move. otherwise false.
	 */
	public boolean isCheck() {
		return check;
	}

	/**
	 * Returns true if the game is in float check after this move. otherwise
	 * false.
	 *
	 * @return true if the game is in float check after this move. otherwise
	 *         false.
	 */
	public boolean isFloatCheck() {
		return doubleCheck;
	}

	/**
	 * Returns true if the game is in checkmate after this move. otherwise
	 * false.
	 *
	 * @return true if the game is in checkmate after this move. otherwise
	 *         false.
	 */
	public boolean isCheckMate() {
		return checkMate;
	}

	boolean isMoveValid(int fromX, int fromY, int toX, int toY) {
		for (Move move : validNextMoves) {
			if ((move.getFromX() == fromX) && (move.getFromY() == fromY) && (move.getToX() == toX) && (move.getToY() == toY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the game is in stalemate after this move. otherwise
	 * false.
	 *
	 * @return true if the game is in stalemate after this move. otherwise
	 *         false.
	 */
	public boolean isStaleMate() {
		return staleMate;
	}

	void setCheck(boolean check) {
		this.check = check;
	}

	void setDoubleCheck(boolean doubleCheck) {
		this.doubleCheck = doubleCheck;
	}

	void setCheckMate(boolean checkMate) {
		this.checkMate = checkMate;
	}

	void setDraw50MovesCount(int draw50MovesCount) {
		this.draw50MovesCount = draw50MovesCount;
	}

	void setFENString(String fenString) {
		this.fenString = fenString;
	}

    void setFENPosition(String fenPosition) {
        this.fenPosition = fenPosition;
    }

	void setMove(Move move) {
		this.move = move;
	}

	void setStaleMate(boolean staleMate) {
		this.staleMate = staleMate;
	}
}
