//==============================================================================
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

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static com.ichess.game.Log.Log;
/**
 * @author Ran Berenfeld
 * @version 1.0
 */
public class Notation {

	// mapping of English characters to pieces
	public static HashMap<String, Integer> sCharEngToPiece = new HashMap<String, Integer>();

	// mapping of hebrew characters to pieces
	public static HashMap<String, Integer> sCharHebToPiece = new HashMap<String, Integer>();

	// map of numbers to rows, English
	public static final String[] sColNamesEng = { "", "a", "b", "c", "d", "e", "f", "g", "h" };

	// map of numbers to columns, hebrew
	public static final String[] sColNamesHeb = { "", "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח" };

	// mapping of pieces to English characters
	public static HashMap<Integer, String> sPieceToCharEng = new HashMap<Integer, String>();

	// mapping of pieces to English characters
	public static HashMap<Integer, String> sPieceToCharHeb = new HashMap<Integer, String>();

	// map of numbers to rows
	public static final String[] sRowNames = { "", "1", "2", "3", "4", "5", "6", "7", "8" };

	static {
		sCharEngToPiece.put("Q", Common.PIECE_TYPE_QUEEN);
		sCharEngToPiece.put("K", Common.PIECE_TYPE_KING);
		sCharEngToPiece.put("R", Common.PIECE_TYPE_ROOK);
		sCharEngToPiece.put("N", Common.PIECE_TYPE_KNIGHT);
		sCharEngToPiece.put("B", Common.PIECE_TYPE_BISHOP);
		sCharEngToPiece.put("P", Common.PIECE_TYPE_PAWN);
		sCharEngToPiece.put("G", Common.PIECE_TYPE_GRASSHOPER);
		sCharEngToPiece.put("A", Common.PIECE_TYPE_ARCHBISHOP);
		sCharEngToPiece.put("C", Common.PIECE_TYPE_CHANCELLOR);
        sCharEngToPiece.put("X", Common.PIECE_TYPE_DROP_ANY);
		sCharEngToPiece.put("", Common.PIECE_TYPE_PAWN);
	}

	static {
		sPieceToCharEng.put(Common.PIECE_TYPE_QUEEN, "Q");
		sPieceToCharEng.put(Common.PIECE_TYPE_KING, "K");
		sPieceToCharEng.put(Common.PIECE_TYPE_ROOK, "R");
		sPieceToCharEng.put(Common.PIECE_TYPE_KNIGHT, "N");
		sPieceToCharEng.put(Common.PIECE_TYPE_BISHOP, "B");
		sPieceToCharEng.put(Common.PIECE_TYPE_PAWN, "P");
		sPieceToCharEng.put(Common.PIECE_TYPE_GRASSHOPER, "G");
		sPieceToCharEng.put(Common.PIECE_TYPE_ARCHBISHOP, "A");
		sPieceToCharEng.put(Common.PIECE_TYPE_CHANCELLOR, "C");
        sPieceToCharEng.put(Common.PIECE_TYPE_DROP_ANY, "X");
	}

	static {
		sCharHebToPiece.put("מה", Common.PIECE_TYPE_QUEEN);
		sCharHebToPiece.put("מ", Common.PIECE_TYPE_KING);
		sCharHebToPiece.put("צ", Common.PIECE_TYPE_ROOK);
		sCharHebToPiece.put("פ", Common.PIECE_TYPE_KNIGHT);
		sCharHebToPiece.put("ר", Common.PIECE_TYPE_BISHOP);
		sCharHebToPiece.put("ח", Common.PIECE_TYPE_GRASSHOPER);
		sCharHebToPiece.put("א", Common.PIECE_TYPE_ARCHBISHOP);
		sCharHebToPiece.put("ק", Common.PIECE_TYPE_CHANCELLOR);
        sCharHebToPiece.put(" ", Common.PIECE_TYPE_DROP_ANY);
		sCharHebToPiece.put("", Common.PIECE_TYPE_PAWN);
	}

	static {
		sPieceToCharHeb.put(Common.PIECE_TYPE_QUEEN, "מה");
		sPieceToCharHeb.put(Common.PIECE_TYPE_KING, "מ");
		sPieceToCharHeb.put(Common.PIECE_TYPE_ROOK, "צ");
		sPieceToCharHeb.put(Common.PIECE_TYPE_KNIGHT, "פ");
		sPieceToCharHeb.put(Common.PIECE_TYPE_BISHOP, "ר");
		sPieceToCharHeb.put(Common.PIECE_TYPE_GRASSHOPER, "ח");
		sPieceToCharHeb.put(Common.PIECE_TYPE_ARCHBISHOP, "א");
		sPieceToCharHeb.put(Common.PIECE_TYPE_CHANCELLOR, "ק");
        sPieceToCharHeb.put(Common.PIECE_TYPE_DROP_ANY, "X");
		sPieceToCharHeb.put(Common.PIECE_TYPE_PAWN, "");
	}

	// mapping of pieces to unicode figures (white)
	public static HashMap<Integer, String> sPieceToUnicodeFiguresWhite = new HashMap<Integer, String>();

	// mapping of pieces to unicode figures (black)
	public static HashMap<Integer, String> sPieceToUnicodeFiguresBlack = new HashMap<Integer, String>();

	static {
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_KING, "\u2654");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_QUEEN, "\u2655");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_ROOK, "\u2656");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_BISHOP, "\u2657");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_KNIGHT, "\u2658");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_PAWN, "\u2659");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_GRASSHOPER, "\u2645");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_ARCHBISHOP, "\u2647");
		sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_CHANCELLOR, "\u2646");
        sPieceToUnicodeFiguresWhite.put(Common.PIECE_TYPE_DROP_ANY, "X");
	}

	static {
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_KING, "\u265A");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_QUEEN, "\u265B");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_ROOK, "\u265C");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_BISHOP, "\u265D");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_KNIGHT, "\u265E");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_PAWN, "\u265F");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_GRASSHOPER, "\u2648");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_ARCHBISHOP, "\u2650");
		sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_CHANCELLOR, "\u2649");
        sPieceToUnicodeFiguresBlack.put(Common.PIECE_TYPE_DROP_ANY, " ");
	}

    /**
     * try to find the piece type from the given string. look in all supported languages. english first
     * @param str
     * @return
     */
    static int getPieceType(String str)
    {
        Integer pieceType = sCharEngToPiece.get(str);
        return pieceType == null ? Common.PIECE_TYPE_ILLEGAL : pieceType;
    }

	public static Move getMove(Game game, String str) {
		Utils.AssertNull(game);
		Utils.AssertNull(str);

		int fromX, fromY, toX, toY;

		// check if move is in long algebraic format
		if (str.length() >= 4) {
			fromX = str.charAt(1) - 48;
			fromY = str.charAt(0) - 96;
			toX = str.charAt(3) - 48;
			toY = str.charAt(2) - 96;

            Log.debug("Found move numeric " + fromX + "," + fromY + "-" + toX + "," + toY);
			if ((Utils.isBetween(fromX, 1, 8)) && (Utils.isBetween(fromY, 1, 8)) && (Utils.isBetween(toX, 1, 8))
				&& (Utils.isBetween(toY, 1, 8))) {
				// move with numeric format. if length = 4, add a space
				Integer additionalPieceType = Common.PIECE_TYPE_ILLEGAL;
				if (str.length() == 5) {
					String promotionStr = String.valueOf(str.charAt(4)).toUpperCase();
                    additionalPieceType = sCharEngToPiece.get(promotionStr);
                    if (additionalPieceType == null)
                    {
                        additionalPieceType = Common.PIECE_TYPE_ILLEGAL;
                    }
					Log.debug("promotion string is '" + promotionStr + "' promotion piece is " + additionalPieceType);
				}

                if (game.isCrazyOrBugHouse() && (fromX == toX) && (fromY == toY))
                {
                    // handle drop move
                    int sourcePieceType = additionalPieceType;
                    if (! game.isCrazyOrBugHouse())
                    {
                        Log.warning("a drop move in non crazyhouse/bughouse game");
                        return null;
                    }
                    String destString = str.substring(str.indexOf('@') + 1, str.length());
                    if (destString.length() < 2) {
                        Log.warning("could not parse move " + str);
                        return null;
                    }

                    Log.debug("a drop move '" + str + "' of " + sourcePieceType + " to " + toX + "," + toY);
                    // first look for a valid drop move (if it's there its all ok)
                    Move validDropMove = game.getValidMove(toX, toY, toX, toY, sourcePieceType);
                    if (validDropMove != null)
                    {
                        // we got the drop move
                        return validDropMove;
                    }
                    Move move = game.getValidMove(toX, toY, toX, toY, Common.PIECE_TYPE_DROP_ANY);
                    if (move == null)
                    {
                        // drop move not found.
                        Log.warning("can't find drop move " + str);
                        return null;
                    }
                    // drop move found. now check if piece can be dropped
                    if ((sourcePieceType == Common.PIECE_TYPE_PAWN) && ( (toX == 1) || (toX == 8)))
                    {
                        Log.warning("can't find drop move " + str + " : pawn can't be dropped on 1st or 8th line");
                        return null;
                    }
                    List<Piece> droppable = game.getDroppablePieces(game.getCurrentColor());
                    Piece droppablePiece = Game.findPieceToDrop(droppable, sourcePieceType);
                    if ( droppablePiece != null )
                    {
                        game.getCurrentMoveInfo().addValidMove(droppablePiece, toX, toY, true);
                    }
                    // ok. if all is ok, we now have a valid drop move
                    validDropMove = game.getValidMove(toX, toY, toX, toY, sourcePieceType);
                    if (validDropMove == null)
                    {
                        Log.warning("can't find drop move " + str);
                    }
                    return validDropMove;
                }
				return game.getValidMove(fromX, fromY, toX, toY, additionalPieceType);
			}
		}

		int color = game.getCurrentColor();

		// PGN notation notation
		int sourcePieceType;

		int sourceColumn = 0;
		int sourceRow = 0;
		int destColumn = 0;
		int destRow = 0;

		int promotionPiece = Common.PIECE_TYPE_ILLEGAL;

		// remove unwanted tokens in moves (like #,+,!,?,ep)
		str = str.replaceAll("[\\?!\\+#\\.\\$]+", "");
		str = str.replaceAll("ep", "");

		// handle promotion =
		int eqLoc = str.indexOf('=');
		if (eqLoc != -1) {
			if (str.length() != eqLoc + 2) {
				Log.warning("bad move string " + str);
				return null;
			}
			String promotionPieceChar = String.valueOf(str.charAt(eqLoc + 1));
			Integer pPiece = sCharEngToPiece.get(promotionPieceChar);
			if (pPiece == null) {
				Log.warning("illegal promotion piece");
				return null;
			}
			promotionPiece = pPiece;

			str = str.substring(0, str.length() - 2);
		}
		String moveUpper = str.toUpperCase();

		// special case 0-0, 0-0-0
		if ("O-O".equals(moveUpper)) {
			// small castle
			if (color == Common.COLOR_WHITE) {
				fromX = 1;
				fromY = (Integer)game.getAttribute(Game.KING_LOCATION);
				toX = 1;
				toY = 7;
                if ( ! game.isFischer() )
                {
                    return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
                }
                else
                {
                    // in fischer 960 use another option - point the king on the rook
                    return game.getValidMove(fromX, fromY, toX, (Integer)game.getAttribute(Game.RIGHT_ROOK_LOCATION), promotionPiece);
                }
			} else {
				fromX = 8;
				fromY = (Integer)game.getAttribute(Game.KING_LOCATION);
				toX = 8;
				toY = 7;
                if ( ! game.isFischer() )
                {
                    return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
                }
                else
                {
                    // in fischer 960 use another option - to point the king on the rook
                    return game.getValidMove(fromX, fromY, toX, (Integer)game.getAttribute(Game.RIGHT_ROOK_LOCATION), promotionPiece);
                }
			}
		}
		if ("O-O-O".equals(moveUpper)) {
            Log.debug("checking castling move " + moveUpper);
			// long castle
			if (color == Common.COLOR_WHITE) {
				fromX = 1;
				fromY = (Integer)game.getAttribute(Game.KING_LOCATION);
				toX = 1;
				toY = 3;
                if ( ! game.isFischer() )
                {
				    return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
                }
                else
                {
                    // in fischer 960 use another option - to point the king on the rook
                    return game.getValidMove(fromX, fromY, toX, (Integer)game.getAttribute(Game.LEFT_ROOK_LOCATION), promotionPiece);
                }
			} else {
				fromX = 8;
				fromY = (Integer)game.getAttribute(Game.KING_LOCATION);
				toX = 8;
				toY = 3;
                if ( ! game.isFischer() )
                {
				    return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
                }
                else
                {
                    // in fischer 960 use another option - to point the king on the rook
                    return game.getValidMove(fromX, fromY, toX, (Integer)game.getAttribute(Game.LEFT_ROOK_LOCATION), promotionPiece);
                }
			}
		}

		if (str.length() < 2) {
			Log.warning("illegal move '" + str + "'");
			return null;
		}
		char pieceChar = str.charAt(0);

		Integer pPiece = sCharEngToPiece.get(String.valueOf(pieceChar));
		sourcePieceType = (pPiece == null ? Common.PIECE_TYPE_PAWN : pPiece);

		if (sourcePieceType == Common.PIECE_TYPE_PAWN) {
			// first letter is source column (unless drop move)
            if (!str.contains("@"))
            {
                sourceColumn = pieceChar - 96;
                if (!((sourceColumn >= 1) && (sourceColumn <= 8))) {
                    Log.warning("bad source column");
                    return null;
                }
            }
		}

		MoveInfo currentInfo = game.getCurrentMoveInfo();

		if (!str.contains("x")) {
            if (!str.contains("@"))
            {
                // a move. not a capture nor a drop
                if (sourcePieceType == Common.PIECE_TYPE_PAWN) {
                    // a pawn advance move . second char is destination row
                    destColumn = sourceColumn;
                    destRow = str.charAt(1) - 48;
                    if (!((destRow >= 1) && (destRow <= 8))) {
                        Log.warning("bad dest row");
                        return null;
                    }

                    // find a pawn on the source column that can move to destination row
                    if (color == Common.COLOR_WHITE) {
                        // find the pawn on dest row - 1
                        sourceRow = destRow - 1;
                        if (sourceRow == 0) {
                            Log.warning("illegal pawn move " + str);
                            return null;
                        }
                        Piece whitePawn = game.getPieceAt(sourceRow, sourceColumn);

                        if (whitePawn == null) {
                            // if dest row is 4, source column can also be 2
                            if (destRow == 4) {
                                sourceRow = destRow - 2;
                                whitePawn = game.getPieceAt(sourceRow, sourceColumn);
                            }
                            if (whitePawn == null) {
                                Log.warning("bad piece moving");
                                return null;
                            }
                        }
                        // verify its a white pawn
                        if (!whitePawn.isWhite()) {
                            Log.warning("bad piece moving");
                            return null;
                        }
                        if (!whitePawn.isPawn()) {
                            Log.warning("bad piece moving");
                            return null;
                        }
                        // move the white pawn
                        fromX = sourceRow;
                        fromY = sourceColumn;
                        toX = destRow;
                        toY = destColumn;
                        return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
                    } else {
                        // the same for pawn advance, black
                        // find the pawn on dest row + 1
                        sourceRow = destRow + 1;
                        if (sourceRow == 9) {
                            Log.warning("illegal pawn move " + str);
                            return null;
                        }
                        Piece blackPawn = game.getPieceAt(sourceRow, sourceColumn);

                        if (blackPawn == null) {
                            // if dest row is 5, source column can also be 7
                            if (destRow == 5) {
                                sourceRow = destRow + 2;
                                blackPawn = game.getPieceAt(sourceRow, sourceColumn);
                            }
                            if (blackPawn == null) {
                                Log.warning("bad piece moving");
                                return null;
                            }
                        }
                        // verify its a black pawn
                        if (!blackPawn.isPawn()) {
                            Log.warning("bad piece moving");
                            return null;
                        }
                        if (!blackPawn.isBlack()) {
                            Log.warning("bad piece moving");
                            return null;
                        }

                        // move the white pawn
                        fromX = sourceRow;
                        fromY = sourceColumn;
                        toX = destRow;
                        toY = destColumn;
                        return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
                    }
                } else {

                    // a piece is moving. 2 letters in the end is the destination
                    destRow = str.charAt(str.length() - 1) - 48;
                    destColumn = str.charAt(str.length() - 2) - 96;

                    // if there are more letters they are source helpers
                    for (int index = 1; index < str.length() - 2; index++) {
                        char sourceHelp = str.charAt(index);
                        if (Character.isDigit(sourceHelp)) {
                            sourceRow = sourceHelp - 48;
                        }
                        if ((Character.isLetter(sourceHelp)) && (Character.isLowerCase(sourceHelp))) {
                            sourceColumn = sourceHelp - 96;
                        }
                    }
                    // find a candidate piece that can move
                    Piece srcPiece = null;
                    List<Piece> srcPieces = game.findPieces(sourcePieceType, color);

                    for (Piece piece : srcPieces) {
                        if ((sourceRow != 0) && (piece.getX() != sourceRow)) {
                            continue;
                        }
                        if ((sourceColumn != 0) && (piece.getY() != sourceColumn)) {
                            continue;
                        }
                        if (!(currentInfo.isMoveValid(piece.getX(), piece.getY(), destRow, destColumn))) {
                            continue;
                        }
                        srcPiece = piece;
                        break;
                    }

                    if (srcPiece == null) {
                        Log.warning("could not parse move " + str);
                        return null;
                    }

                    //Log.debug("parsing regular algebric move of " + Notation.getPieceCharacter(srcPiece.getType()) + " str '" + str +
                            //"' from " + srcPiece.getX() + "," + srcPiece.getY() + " to " + destRow + "," + destColumn);

                    fromX = srcPiece.getX();
                    fromY = srcPiece.getY();
                    toX = destRow;
                    toY = destColumn;
                    return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
                }
            }
            else
            {
                // a drop move destination must be a square
                if (! game.isCrazyOrBugHouse())
                {
                    Log.warning("a drop move in non crazyhouse/bughouse game");
                    return null;
                }
                String destString = str.substring(str.indexOf('@') + 1, str.length());
                if (destString.length() < 2) {
                    Log.warning("could not parse move " + str);
                    return null;
                }
                for (int i = 0; i < destString.length(); i++) {
                    char destInfo = destString.charAt(i);
                    if (Character.isDigit(destInfo)) {
                        destRow = destInfo - 48;
                    }
                    if ((Character.isLetter(destInfo)) && (Character.isLowerCase(destInfo))) {
                        destColumn = destInfo - 96;
                    }
                }
                Log.debug("a drop move '" + str + "' of " + sourcePieceType + " to " + destRow + "," + destColumn);
                toX = destRow;
                toY = destColumn;
                // first look for a valid drop move (if it's there its all ok)
                Move validDropMove = game.getValidMove(toX, toY, toX, toY, sourcePieceType);
                if (validDropMove != null)
                {
                    // we got the drop move
                    return validDropMove;
                }
                Move move = game.getValidMove(toX, toY, toX, toY, Common.PIECE_TYPE_DROP_ANY);
                if (move == null)
                {
                    // drop move not found.
                    Log.warning("can't find drop move " + str);
                    return null;
                }
                // drop move found. now check if piece can be dropped
                if ((sourcePieceType == Common.PIECE_TYPE_PAWN) && ( (toX == 1) || (toX == 8)))
                {
                    Log.warning("can't find drop move " + str + " : pawn can't be dropped on 1st or 8th line");
                    return null;
                }
                List<Piece> droppable = game.getDroppablePieces(game.getCurrentColor());
                Piece droppablePiece = Game.findPieceToDrop(droppable, sourcePieceType);
                if ( droppablePiece != null )
                {
                    game.getCurrentMoveInfo().addValidMove(droppablePiece, toX, toY, true);
                }
                // ok. if all is ok, we now have a valid drop move
                validDropMove = game.getValidMove(toX, toY, toX, toY, sourcePieceType);
                if (validDropMove == null)
                {
                    Log.warning("can't find drop move " + str);
                }
                return validDropMove;
            }
		} else {
			// a capture move. destination can be a piece or a pawn
			// and anyway must contain a valid square
			String destString = str.substring(str.indexOf('x') + 1, str.length());
			String sourceString = str.substring(0, str.indexOf('x'));

			if (destString.length() < 2) {
				Log.warning("could not parse move " + str);
				return null;
			}
			// pieceChar = String.valueOf(destString.charAt(0));

			// try to get row/column from source/dest string
			for (int i = 0; i < sourceString.length(); i++) {
				char sourceInfo = sourceString.charAt(i);
				if (Character.isDigit(sourceInfo)) {
					sourceRow = sourceInfo - 48;
				}
				if ((Character.isLetter(sourceInfo)) && (Character.isLowerCase(sourceInfo))) {
					sourceColumn = sourceInfo - 96;
				}
			}
			for (int i = 0; i < destString.length(); i++) {
				char destInfo = destString.charAt(i);
				if (Character.isDigit(destInfo)) {
					destRow = destInfo - 48;
				}
				if ((Character.isLetter(destInfo)) && (Character.isLowerCase(destInfo))) {
					destColumn = destInfo - 96;
				}
			}

			Piece srcPiece = null;
			List<Piece> srcPieces = game.findPieces(sourcePieceType, color);
			for (Piece piece : srcPieces) {
				if ((sourceRow != 0) && (piece.getX() != sourceRow)) {
					continue;
				}
				if ((sourceColumn != 0) && (piece.getY() != sourceColumn)) {
					continue;
				}
				if (!currentInfo.isMoveValid(piece.getX(), piece.getY(), destRow, destColumn)) {
					continue;
				}
				srcPiece = piece;
				break;
			}
			if (srcPiece == null) {
				Log.warning("can't find moving piece type " + sourcePieceType + " at " + sourceRow + "," + sourceColumn + " for move '" + str + "'");
				return null;
			}

			fromX = srcPiece.getX();
			fromY = srcPiece.getY();
			toX = destRow;
			toY = destColumn;
			return game.getValidMove(fromX, fromY, toX, toY, promotionPiece);
		}
	}

	/*
	 * given a game and the PGN move string, returns a new move with the given
	 * string
	 */

	public static int getNames(Move move) {
		Utils.AssertNull(move);
		Game game = move.getGame();
		Utils.AssertNull(game);

		int fromX = move.getFromX();
		int fromY = move.getFromY();
		int toX = move.getToX();
		int toY = move.getToY();
		boolean FischerCastle = false;

		String name_num = "";

        if (move.isDropMove())
        {
            name_num += (char) (toY + 96);
            name_num += (char) (toX + 48);
        }
        else
        {
            name_num += (char) (fromY + 96);
            name_num += (char) (fromX + 48);
        }

		name_num += (char) (toY + 96);
		name_num += (char) (toX + 48);

		if (move.getAdditionalPieceTypeInfo() != Common.PIECE_TYPE_ILLEGAL) {
			String promotionPieceName = sPieceToCharEng.get(move.getAdditionalPieceTypeInfo()).toLowerCase();
			Log.debug("move promotion piece is " + move.getAdditionalPieceTypeInfo() + " piece name " + promotionPieceName);
			name_num += promotionPieceName;
		} else {
			name_num += " ";
		}

		move.setNameNum(name_num);

		MoveInfo info = game.getCurrentMoveInfo();
		Utils.AssertNull(info);

		if (Utils.isEmptyString(move.getNameAlg())) {

            String name_alg = "";
            String name_alg_heb = "";
            String name_fig = "";
            String name_fig_heb = "";

			Piece piece = move.getMovedPiece();
			Utils.AssertNull(piece);

            if (move.isDropMove())
            {
                // special names for drop move
                name_alg += sPieceToCharEng.get(piece.getType());
                name_alg_heb += sPieceToCharHeb.get(piece.getType());
                if (piece.isWhite())
                {
                    name_fig += sPieceToUnicodeFiguresWhite.get(piece.getType());
                    name_fig_heb += sPieceToUnicodeFiguresWhite.get(piece.getType());
                }
                else
                {
                    name_fig += sPieceToUnicodeFiguresBlack.get(piece.getType());
                    name_fig_heb += sPieceToUnicodeFiguresBlack.get(piece.getType());
                }

                name_alg += "@";
                name_alg_heb += "@";
                name_fig += "@";
                name_fig_heb += "@";

                name_alg += sColNamesEng[toY];
                name_alg += sRowNames[toX];
                name_alg_heb += sColNamesHeb[toY];
                name_alg_heb += sRowNames[toX];
                name_fig += sColNamesEng[toY];
                name_fig += sRowNames[toX];
                name_fig_heb += sColNamesHeb[toY];
                name_fig_heb += sRowNames[toX];
            }
            else
            {
                Piece destPiece = move.getCapturedPiece();
                String captureEng = "";
                String captureHeb = "";
                if (destPiece != null) {
                    captureEng = "x";
                    captureHeb = ":";
                    if (destPiece.isRook() && piece.isKing() && destPiece.isColor(piece.getColor())) {
                        FischerCastle = true;
                    }
                }

                if (piece.isPawn()) {

                    name_alg += sColNamesEng[fromY];
                    name_alg_heb += sColNamesHeb[fromY];

                    if (piece.isWhite())
                    {
                        name_fig += sPieceToUnicodeFiguresWhite.get(piece.getType());
                        name_fig_heb += sPieceToUnicodeFiguresWhite.get(piece.getType());
                    }
                    else
                    {
                        name_fig += sPieceToUnicodeFiguresBlack.get(piece.getType());
                        name_fig_heb += sPieceToUnicodeFiguresBlack.get(piece.getType());
                    }

                    name_fig += sColNamesEng[fromY];
                    name_fig_heb += sColNamesHeb[fromY];

                    // pawn move, not capture
                    if (destPiece == null) {
                        name_alg += sRowNames[toX];
                        name_alg_heb += sRowNames[toX];
                        name_fig += sRowNames[toX];
                        name_fig_heb += sRowNames[toX];
                    } else {
                        // pawn capture
                        name_alg += captureEng;
                        name_alg_heb += captureHeb;
                        name_fig += captureEng;
                        name_fig_heb += captureHeb;
                        name_alg += sColNamesEng[toY];
                        name_alg_heb += sColNamesHeb[toY];
                        name_fig += sColNamesEng[toY];
                        name_fig_heb += sColNamesHeb[toY];
                        name_alg += sRowNames[toX];
                        name_alg_heb += sRowNames[toX];
                        name_fig += sRowNames[toX];
                        name_fig_heb += sRowNames[toX];
                    }
                    // handle promotion
                    if ((toX == 8) || (toX == 1)) {
                        Log.debug("move promotion piece is " + move.getAdditionalPieceTypeInfo());
                        name_alg += '=' + sPieceToCharEng.get(move.getAdditionalPieceTypeInfo());
                        name_alg_heb += '=' + sPieceToCharHeb.get(move.getAdditionalPieceTypeInfo());
                        if (piece.isWhite())
                        {
                            name_fig += '=' + sPieceToUnicodeFiguresWhite.get(move.getAdditionalPieceTypeInfo());
                            name_fig_heb += '=' + sPieceToUnicodeFiguresWhite.get(move.getAdditionalPieceTypeInfo());
                        }
                        else
                        {
                            name_fig += '=' + sPieceToUnicodeFiguresBlack.get(move.getAdditionalPieceTypeInfo());
                            name_fig_heb += '=' + sPieceToUnicodeFiguresBlack.get(move.getAdditionalPieceTypeInfo());
                        }
                    }
                    if (game.isSevenBoom() && (toX == 7 || toX == 2)) {
                        Log.debug("move promotion piece is " + move.getAdditionalPieceTypeInfo());
                        if (move.getAdditionalPieceTypeInfo() != Common.PIECE_TYPE_PAWN) { // promotion
                            name_alg += '=' + sPieceToCharEng.get(move.getAdditionalPieceTypeInfo());
                            name_alg_heb += '=' + sPieceToCharHeb.get(move.getAdditionalPieceTypeInfo());
                            if (piece.isWhite())
                            {
                                name_fig += '=' + sPieceToUnicodeFiguresWhite.get(move.getAdditionalPieceTypeInfo());
                                name_fig_heb += '=' + sPieceToUnicodeFiguresWhite.get(move.getAdditionalPieceTypeInfo());
                            }
                            else
                            {
                                name_fig += '=' + sPieceToUnicodeFiguresBlack.get(move.getAdditionalPieceTypeInfo());
                                name_fig_heb += '=' + sPieceToUnicodeFiguresBlack.get(move.getAdditionalPieceTypeInfo());
                            }
                        }
                    }
                } else {
                    // piece move
                    name_alg += sPieceToCharEng.get(piece.getType());
                    name_alg_heb += sPieceToCharHeb.get(piece.getType());
                    if (piece.isWhite())
                    {
                        name_fig += sPieceToUnicodeFiguresWhite.get(piece.getType());
                        name_fig_heb += sPieceToUnicodeFiguresWhite.get(piece.getType());
                    }
                    else
                    {
                        name_fig += sPieceToUnicodeFiguresBlack.get(piece.getType());
                        name_fig_heb += sPieceToUnicodeFiguresBlack.get(piece.getType());
                    }

                    // verify if helpers needed
                    String h1 = "";
                    String h1heb = "";
                    String h2 = "";
                    String h2heb = "";

                    Log.debug("name_alg is '" + name_alg + "'");

                    List<Piece> others = game.findPieces(piece.getType(), piece.getColor());

                    Log.debug("others size " + others.size());

                    for (Piece other : others) {
                        if ( ( other.getX() != piece.getX() ) || ( other.getY() != piece.getY() ) )
                        {
                            Log.debug("found helper piece " + other.getType() + " color " + other.getColor());
                            if (info.isMoveValid(other.getX(), other.getY(), toX, toY)) {
                                if (other.getY() != fromY) {
                                    // add column helper
                                    if (Utils.isEmptyString(h1)) {
                                        h1 += sColNamesEng[fromY];
                                        h1heb += sColNamesHeb[fromY];

                                    }
                                } else {
                                    // add row helper
                                    if (Utils.isEmptyString(h2)) {
                                        h2 += sRowNames[fromX];
                                        h2heb += sRowNames[fromX];
                                    }
                                }
                            }
                        }
                    }

                    name_alg += (h1 + h2);
                    name_alg_heb += (h1heb + h2heb);
                    name_fig += (h1 + h2);
                    name_fig_heb += (h1heb + h2heb);

                    name_alg += captureEng;
                    name_alg_heb += captureHeb;
                    name_fig += captureEng;
                    name_fig_heb += captureHeb;
                    name_alg += sColNamesEng[toY];
                    name_alg_heb += sColNamesHeb[toY];
                    name_fig += sColNamesEng[toY];
                    name_fig_heb += sColNamesHeb[toY];
                    name_alg += sRowNames[toX];
                    name_alg_heb += sRowNames[toX];
                    name_fig += sRowNames[toX];
                    name_fig_heb += sRowNames[toX];
                    Log.debug("name_alg is '" + name_alg + "'");

                    // check for castle
                    if (piece.isKing()) {
                        int dist = move.getFromY() - move.getToY();
                        if ( dist > 1 || (FischerCastle && dist == 1) ) {
                            name_alg = "O-O-O";
                            name_alg_heb = "O-O-O";
                            name_fig = "O-O-O";
                            name_fig_heb = "O-O-O";
                        }
                        if (dist < -1 || (FischerCastle && dist == -1) ) {
                            name_alg = "O-O";
                            name_alg_heb = "O-O";
                            name_fig = "O-O";
                            name_fig_heb = "O-O";
                        }
                    }
                }
            }

            Log.debug("move names are '" + name_alg + "' , '" + name_alg_heb + "' , '" + name_fig + " , " + name_fig_heb);
			move.setNameAlg(name_alg);
			move.setNameAlgHeb(name_alg_heb);
			move.setNameFig(name_fig);
			move.setNameFigHeb(name_fig_heb);
		}

		// suffix is null and this is the actual move played, then we can
		// compute the suffix
		if (Utils.isEmptyString(move.getNameAlgSuffix())) {
			MoveInfo nextInfo = game.getMoveInfo(move.getMoveNumber() + 1);
			if (nextInfo != null) {
				if (move.getNameNum().equals(game.getMove(move.getMoveNumber()).getNameNum())) {
					if (nextInfo.isCheckMate()) {
						move.setNameAlgSuffix("#");
						Log.debug("renaming name of move to " + move.getNameAlg());
					} else if (nextInfo.isFloatCheck()) {
						move.setNameAlgSuffix("++");
						Log.debug("renaming name of move to " + move.getNameAlg());
					} else if (nextInfo.isCheck()) {
						move.setNameAlgSuffix("+");
						Log.debug("renaming name of move to " + move.getNameAlg());
					}
				}
			}
		}
		return Common.RC_OK;
	}

	/*
	 * calculates the algebraic, hebrew algebraic and numeric names of a move
	 */

	public static String getSquareEng(int x, int y) {
		return sColNamesEng[y] + sRowNames[x];
	}

	public static boolean playMoveList(Game game, String movelist) {
		return playMoveList(game, movelist, 0);
	}

	public static boolean playMoveList(Game game, String movelist, int toMove) {


		Utils.AssertNull(game);

		Log.debug("playing move list " + movelist);

		if (movelist == null) {
			;
			return false;
		}

		// play the moves on an empty game
		// remove all kinds of spaces, convert with " "
		movelist = movelist.replaceAll("[\\s+]", " ");

		movelist = movelist.replace(".", ". ");
		movelist = movelist.replace("{", " { ");
		movelist = movelist.replace("}", " } ");

		StringTokenizer st = new StringTokenizer(movelist, " ");
		while (st.hasMoreTokens()) {

			if ((game.getCurrentMove() > toMove) && (toMove != 0)) {
				// no need to play move moves
				break;
			}
			String tok = st.nextToken();

			tok = tok.trim();

			if ("{".equals(tok)) {
				String comment = "";
				tok = st.nextToken();
				do {
					if (!st.hasMoreTokens()) {
						Log.warning("end of pgn in the middle of comment");
						return false;
					}
					comment += tok + " ";
					tok = st.nextToken();
				} while (!("}".equals(tok)));

				// add comment for current move
				Move move = game.getLastMove();
				if (move != null) {
					move.setComment(comment);
				}

				continue;
			}

			if (game.isEnded()) {
				Log.info("game ended. ignoring the rest of the move list");
				break;
			}

			// remove unwanted tokens in moves (like #,+,!,?,ep)
			tok = tok.replaceAll("[\\?!\\+#\\.\\$]+", "");
			tok = tok.replaceAll("ep", "");

			if (Utils.isEmptyString(tok))
			{
				continue;
			}

			// filter out tokens that does not begin with a letter
			if (!Character.isLetter(tok.charAt(0))) {
				continue;
			}

			if (Common.RC_OK != game.playMove(tok)) {
				Log.warning("invalid move token '" + tok + "'");
				;
				return false;
			}
		}
		;
		return true;
	}

	public static Game playMoveList(String movelist) {
		Game game = new Game();
		if (!playMoveList(game, movelist)) {
			return null;
		}
		return game;
	}

}
