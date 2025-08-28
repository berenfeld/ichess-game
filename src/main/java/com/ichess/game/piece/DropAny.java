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

/**
 * @author Ran Berenfeld
 * @version 1.0
 */
public class DropAny extends Piece {

	public DropAny(int color) {
		super(Common.PIECE_TYPE_DROP_ANY, color);
	}

	@Override
	public boolean canMoveTo(int x, int y, Game position) {
		return false;
	}

	@Override
	public void doCalcReachability(Game pos) {
		return;
	}

	@Override
	public String toString() {
		return _color == Common.COLOR_WHITE ? "X" : "x";
	}

}
