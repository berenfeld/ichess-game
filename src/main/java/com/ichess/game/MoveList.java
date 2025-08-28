//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game;

import com.ichess.game.Utils;

import java.util.ArrayList;


/**
 * @author Ran Berenfeld
 * @version 1.0
 */
public class MoveList {

	private int _currentMove = 0;
	private String _listAlg;
	private String _listAlgHeb;
	private String _listFig;
	private String _listFigHeb;

	private String _listNum;
	private ArrayList<String> _movesAlg = new ArrayList<String>();
	private ArrayList<String> _movesAlgHeb = new ArrayList<String>();
	private ArrayList<String> _movesFig = new ArrayList<String>();
	private ArrayList<String> _movesFigHeb = new ArrayList<String>();
	private ArrayList<String> _movesNum = new ArrayList<String>();

	public MoveList() {
		_listAlg = "";
		_listAlgHeb = "";
		_listFig = "";
		_listFigHeb = "";
		_listNum = "";
	}

	public void addMove(Move move) {
		Utils.AssertNull(move);

		Game game = move.getGame();
		Utils.AssertNull(game);

		if (_currentMove != 0) {
			_listNum += " ";
			_listAlg += " ";
			_listAlgHeb += " ";
			_listFig += " ";
			_listFigHeb += " ";
		}

		_listNum += move.getNameNum();
		_listAlg += move.getNameAlg();
		_listAlgHeb += move.getNameAlgHeb();
		_listFig += move.getNameFig();
		_listFigHeb += move.getNameFigHeb();

		_movesNum.add(move.getNameNum());
		_movesAlg.add(move.getNameAlg());
		_movesAlgHeb.add(move.getNameAlgHeb());
		_movesFig.add(move.getNameFig());
		_movesFigHeb.add(move.getNameFigHeb());

		_currentMove++;
	}

	public String getListAlg() {
		return _listAlg;
	}

	public String getListAlg(int fromMove) {
		Utils.Assert(fromMove <= _currentMove);
		String result = "";
		for (int index = fromMove; index < _currentMove; index++) {
			result += _movesAlg.get(index);
			if (index != (_currentMove - 1)) {
				result += " ";
			}
		}
		return result;
	}

	public String getListAlgHeb() {
		return _listAlgHeb;
	}

	public String getListAlgHeb(int fromMove) {
		Utils.Assert(fromMove <= _currentMove);
		String result = "";
		for (int index = fromMove; index < _currentMove; index++) {
			result += _movesAlgHeb.get(index);
			if (index != (_currentMove - 1)) {
				result += " ";
			}
		}
		return result;
	}

	public String getListFig() {
		return _listFig;
	}

	public String getListFig(int fromMove) {
		Utils.Assert(fromMove <= _currentMove);
		String result = "";
		for (int index = fromMove; index < _currentMove; index++) {
			result += _movesFig.get(index);
			if (index != (_currentMove - 1)) {
				result += " ";
			}
		}
		return result;
	}

	public String getListFigHeb() {
		return _listFigHeb;
	}

	public String getListFigHeb(int fromMove) {
		Utils.Assert(fromMove <= _currentMove);
		String result = "";
		for (int index = fromMove; index < _currentMove; index++) {
			result += _movesFigHeb.get(index);
			if (index != (_currentMove - 1)) {
				result += " ";
			}
		}
		return result;
	}

	public String getListNum() {
		return _listNum;
	}

	public String getListNumFromMove(int fromMove) {
		Utils.Assert(fromMove <= _currentMove);
		String result = "";
		for (int index = fromMove; index < _currentMove; index++) {
			result += _movesNum.get(index);
			if (index != (_currentMove - 1)) {
				result += " ";
			}
		}
		return result;
	}

	public String getListNumToMove(int toMove) {
		Utils.Assert(toMove <= _currentMove);
		String result = "";
		for (int index = 0; index < toMove; index++) {
			result += _movesNum.get(index);
			if (index != (toMove - 1)) {
				result += " ";
			}
		}
		return result;
	}

	public void takeback() {

		if (_currentMove == 1) {
			_listAlg = "";
			_listAlgHeb = "";
			_listFig = "";
			_listFigHeb = "";
			_listNum = "";
		} else {
			_listNum = _listNum.substring(0, _listNum.length() - 6);

			int lastSpace = _listAlg.lastIndexOf(" ");
			_listAlg = _listAlg.substring(0, lastSpace);

			lastSpace = _listAlgHeb.lastIndexOf(" ");
			_listAlgHeb = _listAlgHeb.substring(0, lastSpace);

			lastSpace = _listFig.lastIndexOf(" ");
			_listFig = _listFig.substring(0, lastSpace);

			lastSpace = _listFigHeb.lastIndexOf(" ");
			_listFigHeb = _listFigHeb.substring(0, lastSpace);

		}

		_currentMove--;

		_movesAlg.remove(_currentMove);
		_movesAlgHeb.remove(_currentMove);
		_movesFig.remove(_currentMove);
		_movesFigHeb.remove(_currentMove);
		_movesNum.remove(_currentMove);
	}

	@Override
	public String toString() {
		return _listAlg;
	}

}
