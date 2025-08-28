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
/**
 * @author Ran Berenfeld
 * @version 1.0
 */
public class Move {

	private Piece _capturedPiece = null;

	private int _color = Common.COLOR_ILLEGAL; // color of the moved piece
	private String _comment = "";
	private boolean _epCapture = false;
	private int _fromX, _fromY, _toX, _toY;
	private Game _game = null;
	private Piece _movedPiece = null;
	private MoveInfo _moveInfo; // move info after the move is played
	private int _moveNumber;
	private String _nameAlg = null; // move name in algebraic notation Nxf3
	private String _nameAlgHeb = null; // move name in algebraic hebrew notation
	private String _nameFig = null; // move name in figurine notation
	private String _nameFigHeb = null; // move name in figurine hebrew notation
	private String _nameAlgSuffix = null; // move name ending of algebraic name
	private String _nameNum = null; // move name in numeric notation g1f3
	private int _additionalPieceTypeInfo = Common.PIECE_TYPE_ILLEGAL;
	private long _timePlayed; // the time the move was played
	private int _move_time = 0; // move time (how much time it took to move) in milliseconds
    public boolean isCheck = false; //  check

	public Move(Game game, int fromX, int fromY, int toX, int toY, int additionalPieceTypeInfo) {
		_game = game;
		_fromX = fromX;
		_fromY = fromY;
		_toX = toX;
		_toY = toY;
		_additionalPieceTypeInfo = additionalPieceTypeInfo;
	}

	@Override
	public boolean equals(Object other) {
		if ((other == null) || (!(other instanceof Move)))
			return false;
		Move otherMove = (Move) other;
		return (otherMove._fromX == _fromX) && (otherMove._fromY == _fromY) && (otherMove._toX == _toX) && (otherMove._toY == _toY)
                && (otherMove._additionalPieceTypeInfo == _additionalPieceTypeInfo);
	}

    public boolean isCapture()
    {
        return _capturedPiece != null;
    }

	public Piece getCapturedPiece() {
		return _capturedPiece;
	}

	public int getColor() {
		return _color;
	}

	public String getComment() {
		return _comment;
	}

	public int getFromX() {
		return _fromX;
	}

	public int getFromY() {
		return _fromY;
	}

	public Game getGame() {
		return _game;
	}

	public Piece getMovedPiece() {
		return _movedPiece;
	}

	public MoveInfo getMoveInfo() {
		return _moveInfo;
	}

	public int getMoveNumber() {
		return _moveNumber;
	}

	public String getNameAlg() {
		return (_nameAlg != null ? _nameAlg : "") + (_nameAlgSuffix != null ? _nameAlgSuffix : "");
	}

	public String getNameAlgHeb() {
		return (_nameAlgHeb != null ? _nameAlgHeb : "") + (_nameAlgSuffix != null ? _nameAlgSuffix : "");
	}

	public String getNameFig() {
		return (_nameFig != null ? _nameFig : "") + (_nameAlgSuffix != null ? _nameAlgSuffix : "");
	}

	public String getNameFigHeb() {
		return (_nameFigHeb != null ? _nameFigHeb : "") + (_nameAlgSuffix != null ? _nameAlgSuffix : "");
	}

	public String getNameAlgSuffix() {
		return _nameAlgSuffix;
	}

	public String getNameNum() {
		return _nameNum;
	}

	public int getAdditionalPieceTypeInfo() {
		return _additionalPieceTypeInfo;
	}

    public boolean isDropMove() {
        return _fromX == _toX && _fromY == _toY;
    }

	public long getTimePlayed() {
		return _timePlayed;
	}

	public int getToX() {
		return _toX;
	}

	public int getToY() {
		return _toY;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _fromX;
		result = prime * result + _fromY;
		result = prime * result + _toX;
		result = prime * result + _toY;
		return result;
	}

	public boolean isEpCapture() {
		return _epCapture;
	}

	public void setCapturedPiece(Piece capturedPiece) {
		_capturedPiece = capturedPiece;
	}

	public void setComment(String comment) {
		this._comment = comment;
	}

	public void appendComment(String comment) {
		if (Utils.isEmptyString(_comment))
		{
			_comment = comment;
			return;
		}
		_comment += comment;
	}

	public void setEpCapture(boolean epCapture) {
		this._epCapture = epCapture;
	}

	public void setMovedPiece(Piece movedPiece) {
		_movedPiece = movedPiece;
		_color = _movedPiece.getColor();
	}

	public void setMoveInfo(MoveInfo moveInfo) {
		this._moveInfo = moveInfo;
	}

	public void setMoveNumber(int moveNumber) {
		this._moveNumber = moveNumber;
	}

	public void setName(String name) {
		this._nameAlg = name;
	}

	public void setNameAlg(String name_alg) {
		this._nameAlg = name_alg;
	}

	public void setNameAlgHeb(String name_alg_heb) {
		this._nameAlgHeb = name_alg_heb;
	}

	public void setNameFig(String name_fig) {
		this._nameFig = name_fig;
	}

	public void setNameFigHeb(String name_fig_heb) {
		this._nameFigHeb = name_fig_heb;
	}

	public void setNameAlgSuffix(String nameAlgSuffix) {
		_nameAlgSuffix = nameAlgSuffix;
	}

	public void setNameNum(String name_num) {
		this._nameNum = name_num;
	}

	public void setPromotionPiece(int promotionPiece) {
		_additionalPieceTypeInfo = promotionPiece;
	}

	public void setTimePlayed(long timePlayed) {
		this._timePlayed = timePlayed;
	}

	public void setMoveTime(int move_time) {
		_move_time = move_time;
	}

	public int getMoveTime() {
		return _move_time;
	}
}
