//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game.piece;

import com.ichess.game.Common;
import com.ichess.game.Game;
import com.ichess.game.Utils;

import java.awt.*;
import java.util.Arrays;
import static com.ichess.game.Log.*;
/**
 * @author Ran Berenfeld
 * @version 1.0
 */
public abstract class Piece {

	public static Piece create(int type, int color) {
		Utils.AssertNull(type, "illegal null piece type");
		Piece newPiece = null;
		switch (type) {
		case Common.PIECE_TYPE_KING:
			newPiece = new King(color);
			break;
		case Common.PIECE_TYPE_QUEEN:
			newPiece = new Queen(color);
			break;
		case Common.PIECE_TYPE_ROOK:
			newPiece = new Rook(color);
			break;
		case Common.PIECE_TYPE_BISHOP:
			newPiece = new Bishop(color);
			break;
		case Common.PIECE_TYPE_KNIGHT:
			newPiece = new Knight(color);
			break;
		case Common.PIECE_TYPE_PAWN:
			newPiece = new Pawn(color);
			break;
		case Common.PIECE_TYPE_GRASSHOPER:
			newPiece = new Grasshoper(color);
			break;
		case Common.PIECE_TYPE_ARCHBISHOP:
			newPiece = new Archbishop(color);
			break;
		case Common.PIECE_TYPE_CHANCELLOR:
			newPiece = new Chancellor(color);
			break;
        case Common.PIECE_TYPE_DROP_ANY:
            newPiece = new DropAny(color);
            break;
		}
		return newPiece;
	}

	protected boolean _checkPin = false;
	protected int _color = Common.COLOR_ILLEGAL;
	protected boolean _moved = false;
	protected int _pieceType = Common.PIECE_TYPE_ILLEGAL;
	protected boolean _pinned;
	protected Piece _pinningPiece;
	protected boolean[] _reachable = new boolean[64];
	protected int _x = 0, _y = 0;
    protected boolean _promoted = false;
    protected boolean[] _checks = new boolean[64]; // which squares can give check
    protected boolean[] _threats = new boolean[64]; // which squares can cause a threat

	protected Piece(int pieceType, int color) {
		_pieceType = pieceType;
		_color = color;
	}

    public void setPromoted() { _promoted = true; };
    public void clearPromoted() { _promoted = false; };
    public boolean isPromoted() { return _promoted; }

    public boolean canBeDroppedAt(int x, int y) {
        return true;
    }

	public abstract boolean canMoveTo(int x, int y, Game position);

	public boolean canMoveTo(Point loc, Game position) {
		Utils.AssertNull(loc);
		Utils.AssertNull(position);
		return canMoveTo(loc.x, loc.y, position);
	}

	public void clearReachability() {
		Arrays.fill(_reachable, false);
        Arrays.fill(_threats, false);
        Arrays.fill(_checks, false);
	}

	public abstract void doCalcReachability(Game pos);

	public int getColor() {
		return _color;
	}

	public Piece getPinningPiece() {
		return _pinningPiece;
	}

	public int getType() {
		return _pieceType;
	}

    public int getTypeWhenDropping() {
        if (_promoted)
        {
            return Common.PIECE_TYPE_PAWN;
        }
        return _pieceType;
    }

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}

	@Override
	public int hashCode() {
		return (_pieceType * Common.PIECE_TYPE_NUM) + (_color * 2);
	}

	public boolean isBishop() {
		return _pieceType == Common.PIECE_TYPE_BISHOP;
	}

	public boolean isBlack() {
		return _color == Common.COLOR_BLACK;
	}

	public boolean isCheckPin() {
		return _checkPin;
	}

	public boolean isColor(int color) {
		return _color == color;
	}

	public boolean isKing() {
		return _pieceType == Common.PIECE_TYPE_KING;
	}

	public boolean isKnight() {
		return _pieceType == Common.PIECE_TYPE_KNIGHT;
	}

	public boolean isMoved() {
		return _moved;
	}

	public boolean isPawn() {
		return _pieceType == Common.PIECE_TYPE_PAWN;
	}

	public boolean isPinned() {
		return _pinned;
	}

	public boolean isQueen() {
		return _pieceType == Common.PIECE_TYPE_QUEEN;
	}

	public boolean isGrasshoper(){
		return _pieceType == Common.PIECE_TYPE_GRASSHOPER;
	}

	public boolean isArchbisop(){
		return _pieceType == Common.PIECE_TYPE_ARCHBISHOP;
	}

	public boolean isChancellor(){
		return _pieceType == Common.PIECE_TYPE_CHANCELLOR;
	}

	public boolean isReachable(int x, int y) {
		return _reachable[((x - 1) << 3) + (y - 1)];
	}

    public boolean isCheck(int x, int y) {
        return _checks[((x - 1) << 3) + (y - 1)];
    }

    public boolean isThreat(int x, int y) {
        return _threats[((x - 1) << 3) + (y - 1)];
    }

	public boolean isRook() {
		return _pieceType == Common.PIECE_TYPE_ROOK;
	}

	public boolean isWhite() {
		return _color == Common.COLOR_WHITE;
	}

	public void setCheckPin(boolean checkPin) {
		this._checkPin = checkPin;
	}

	public void setMoved(boolean moved) {
		this._moved = moved;
	}

	public void setPinned(boolean pinned) {
		this._pinned = pinned;
	}

	public void setPinningPiece(Piece pinningPiece) {
		this._pinningPiece = pinningPiece;
	}

	public void setReachable(int x, int y, boolean val) {
		_reachable[((x - 1) << 3) + (y - 1)] = val;
	}

    public void setReachable(int x, int y, boolean val, Game pos) {
        _reachable[((x - 1) << 3) + (y - 1)] = val;
        check(x,y,pos);  // not including discovered checks
    }

    public void setCheck(int x, int y, boolean val) {
        _checks[((x - 1) << 3) + (y - 1)] = val;
    }

    public void setThreaet(int x, int y, boolean val) {
        _threats[((x - 1) << 3) + (y - 1)] = val;
    }

	public void setX(int x) {
		this._x = x;
	}

	public void setY(int y) {
		this._y = y;
	}

    @Override
    public boolean equals(Object other)
    {
        Piece otherPiece = (Piece)other;
        return otherPiece != null && otherPiece._pieceType == _pieceType && otherPiece._color == _color && _promoted == otherPiece._promoted;
    }

    public boolean canMove(int fromX, int fromY, int toX, int toY, Game pos) {
        int X = _x;
        int Y = _y;
        _x = fromX;
        _y = fromY;
        boolean res = canMoveTo(toX, toY, pos);
        _x = X;
        _y = Y;
        return res;
    }

    public void check(int x, int y, Game pos) {
        King king = pos.getEnemyKing();
        Log.debug("checking " + x + "," + y + " king:" + king.getX() + "," + king.getY() + " for " + _pieceType + " in " + _x + "," + _y);
        if (canMove(x, y, king.getX(), king.getY(), pos)) {
            Log.debug("CHECK!");
            setCheck(x, y, true);
        }
        // TODO: set threats
    }
}
