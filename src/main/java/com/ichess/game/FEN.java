//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game;

import com.ichess.game.piece.*;
import com.ichess.game.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ichess.game.Log.Log;
/**
 * Used to import and export games from the FEN notation. The FEN specification
 * is used to record a status of a game, so it can be continued at a later time.
 *
 * @see <a
 *      href="http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN
 *      notation specification.</a>
 * @author Ran Berenfeld
 * @version 1.0
 */
public class FEN {

	public static final String FEN_COLOR_BLACK = "b";
	public static final String FEN_COLOR_WHITE = "w";

	/**
	 * FEN value of an empty board
	 */
	public static final String FEN_EMPTY_POS = "8/8/8/8/8/8/8/8";

	public static final String FEN_EMPTY_POS_BLACK = "8/8/8/8/8/8/8/8 b";
	public static final String FEN_EMPTY_POS_WHITE = "8/8/8/8/8/8/8/8 w";
	/**
	 * FEN value of initial position.
	 */
	public static final String FEN_INITIAL_POS = Common.STD_INIT_FEN;
    public static final String FEN_INITIAL_POS_NO_CASTLE = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
	public static final String FEN_GRASSHOPER_POS = "rnbqkbnr/gggggggg/pppppppp/8/8/PPPPPPPP/GGGGGGGG/RNBQKBNR w KQkq - 0 1";
	public static final String FEN_MINICAPA_POS = "rabqkbcr/pppppppp/8/8/8/8/PPPPPPPP/RABQKBCR w KQkq - 0 1";
    public static final String FEN_CRAZYHOUSE_POS = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR[-] w KQkq - 0 1";

    // if true will encode last row captured pieces in crazyhouse (XFEN)
    // by default encode in Droppable pieces in [] like winboard format for crazyhouse / bughouse
    public static boolean ENCODE_CAPTURED_AS_XFEN = false;

	/**
	 * Returns the FEN position of the given Game. The FEN position is the first
	 * token of the FEN string, containing only the pieces position.
	 *
	 * @param game
	 *            the game whose position is returned
	 * @return The FEN position of the given Game. null on error.
	 */
	public static String getFENPosition(Game game) {
		if (game == null) {
			Log.warning("game == null");
			return null;
		}

		StringBuilder result = new StringBuilder();

		for (int row = 8; row > 0; row--) {
			int emptyCount = 0;
			for (int col = 1; col < 9; col++) {
				Piece piece = game.getPieceAt(row, col);
				if (piece == null) {
					emptyCount++;
					continue;
				}
				if (emptyCount > 0) {
					result.append(emptyCount);
					emptyCount = 0;
				}
				String pieceStr = Notation.sPieceToCharEng.get(piece.getType());
				if (piece.isBlack()) {
					pieceStr = pieceStr.toLowerCase();
				}
				result.append(pieceStr);
                if ((game.isCrazyOrBugHouse()) && (piece.isPromoted()))
                {
                    result.append('~');
                }
			}
			if (emptyCount > 0) {
				result.append(emptyCount);
			}
			if (row != 1) {
				result.append('/');
			}
		}
        if (game.isCrazyOrBugHouse())
        {
            if (ENCODE_CAPTURED_AS_XFEN)
            {
                // add crazyhouse captured pieces
                if (game.isCrazyHouse())
                {
                    if (game.hasCapturedPieces()) {
                        result.append('/');
                        for (Piece piece : game.getCapturedPieces(Common.COLOR_WHITE)) {
                            String pieceChar = Notation.sPieceToCharEng.get(piece.getType());
                            result.append(pieceChar.toUpperCase());
                        }
                        for (Piece piece : game.getCapturedPieces(Common.COLOR_BLACK)) {
                            String pieceChar = Notation.sPieceToCharEng.get(piece.getType());
                            result.append(pieceChar.toLowerCase());
                        }
                    }
                }
            }
            else {
                // add crazyhouse / bughouse holding
                result.append('[');
                boolean hasDroppablePieces = false;
                for (Piece piece : game.getDroppablePieces(Common.COLOR_WHITE)) {
                    String pieceChar = Notation.sPieceToCharEng.get(piece.getTypeWhenDropping());
                    result.append(pieceChar);
                    hasDroppablePieces = true;
                }
                for (Piece piece : game.getDroppablePieces(Common.COLOR_BLACK)) {
                    String pieceChar = Notation.sPieceToCharEng.get(piece.getTypeWhenDropping());
                    result.append(pieceChar.toLowerCase());
                    hasDroppablePieces = true;
                }
                if (!hasDroppablePieces) {
                    result.append("-");
                }
                result.append("]");
            }
        }
		return result.toString();
	}

	/**
	 * Returns the FEN representation of the given Game.
	 *
	 * @param game
	 *            the game whose FEN representation is returned
	 * @return The FEN representation of the given Game. null on error.
	 */
	public static String getFENString(Game game) {
		if (game == null) {
			Log.warning("game == null");
			return null;
		}

        String fenString = game.getCurrentFENString();
        if (! Utils.isEmptyString(fenString)) {
            return fenString;
        }

		StringBuilder fen = new StringBuilder();

		fen.append(getFENPosition(game));
		fen.append(" ");

		switch (game.getCurrentColor()) {
		case Common.COLOR_WHITE:
			fen.append(FEN_COLOR_WHITE);
			break;
		case Common.COLOR_BLACK:
			fen.append(FEN_COLOR_BLACK);
			break;
		default:
			Log.warning("illegal game color");
			return null;
		}

		// append castling availability
		fen.append(" ");

		boolean[][] _castling = new boolean[Common.COLOR_NUM][Common.CASTLE_NUM];

		for (int color = Common.COLOR_START; color < Common.COLOR_NUM; color++) {
			for (int cstl = Common.CASTLE_START; cstl < Common.CASTLE_NUM; cstl++) {
				if (Common.isBlackOrWhite(color)) {
					_castling[color][cstl] = game.getCastlingAvailability(color, cstl);
				}
			}
		}

        StringBuilder castlingToken = new StringBuilder();
		boolean canCastle = false;
		char right = 'k';
		char left = 'q';
		if (game.isFischer()) {
			right = (char) ( (int)'a' + (Integer)game.getAttribute(Game.RIGHT_ROOK_LOCATION) - 1 );
			left = (char) ( (int)'a' + (Integer)game.getAttribute(Game.LEFT_ROOK_LOCATION) - 1 );
		}
		if (_castling[Common.COLOR_WHITE][Common.CASTLE_KING]) {
            castlingToken.append(Character.toUpperCase(right) );
			canCastle = true;
		}
		if (_castling[Common.COLOR_WHITE][Common.CASTLE_QUEEN]) {
            castlingToken.append(Character.toUpperCase(left) );
			canCastle = true;
		}
		if (_castling[Common.COLOR_BLACK][Common.CASTLE_KING]) {
            castlingToken.append( right );
			canCastle = true;
		}
		if (_castling[Common.COLOR_BLACK][Common.CASTLE_QUEEN]) {
            castlingToken.append(left);
			canCastle = true;
		}
		if (!canCastle) {
            castlingToken.append("-");
		}
        // sort the string to always have the same FEN representation
        char[] castlingTokenChar = castlingToken.toString().toCharArray();
        Arrays.sort(castlingTokenChar);

        fen.append(new String(castlingTokenChar));

		// append ep move

		fen.append(" ");

		Pawn epPawn = game.getEpPawn();
		if (epPawn != null) {
			boolean ep_capture_pawn_found = false;
			// also check if opponent pawn can make an en passent capture
			int epY = epPawn.getY();
			int epX = epPawn.getX();
			if (epY > 1) {
				Piece capturePawn = game.getPieceAt(epX, epY - 1);
				if (capturePawn != null) {
					if (capturePawn.isPawn()) {
						if (capturePawn.isColor(game.getCurrentColor())) {
							// found
							ep_capture_pawn_found = true;
						}
					}
				}
			}
			if (epY < 8) {
				Piece capturePawn = game.getPieceAt(epX, epY + 1);
				if (capturePawn != null) {
					if (capturePawn.isPawn()) {
						if (capturePawn.isColor(game.getCurrentColor())) {
							// found
							ep_capture_pawn_found = true;
						}
					}
				}
			}
			if (ep_capture_pawn_found) {
				if (epPawn.isWhite()) {
					fen.append(Notation.getSquareEng(epPawn.getX() - 1, epPawn.getY()));
				} else {
					fen.append(Notation.getSquareEng(epPawn.getX() + 1, epPawn.getY()));
				}
			} else {
				fen.append("-");
			}
		} else {
			fen.append("-");
		}

		// append half moves

		fen.append(" ");

		// half moves
		MoveInfo currentInfo = game.getCurrentMoveInfo();
		if (currentInfo != null) {
			fen.append(currentInfo.getDraw50MovesCount());
		} else {
			fen.append("0");
		}

		// append full moves number
		fen.append(" ");

        fen.append(game.getMoveNumber());

		return fen.toString();
	}

	public static String removeCastling(String fen){
		String[] tokens = fen.split("\\s+");
		if (tokens.length > 2) {
			String castle = tokens[2];
			int let = Character.toLowerCase( ((char)castle.getBytes()[0]) );
			if ( (let - 'a' >= 0 && let - 'a' <= 7) || let == 'k' || let == 'q') {
				tokens[2] = "-";
				fen = Utils.join(tokens, " ");
			}
		}
		return fen;
	}

	/**
	 * Loads the given FEN string into the given game. All the game moves are
	 * taken back. The FEN must be a valid chess position that a game can start
	 * from.
	 *
	 * @param game
	 *            - A non null game instance to load the FEN to.
	 * @param fen
	 *            - The FEN string to load.
	 * @return true on success. otherwise false.
	 */
	public static boolean loadGame(Game game, String fen) {
		return loadGame(game, fen, true, 0);
	}

	/**
	 * Loads the given FEN string into the given game. All the game moves are
	 * taken back. If startGame is false, then the FEN may be an invalid game
	 * position.
	 *
	 * @param game
	 *            - A non null game instance to load the FEN to.
	 * @param fen
	 *            - The FEN string to load.
	 * @param startGame
	 *            - If true, FEN position is validated as a valid chess
	 *            position.
	 * @return true on success. otherwise false.
	 */
	public static boolean loadGame(Game game, String fen, boolean startGame, int gkind) {
		Utils.AssertNull(fen);
		Utils.AssertNull(game);

		// try to guess game kind and game rules if needed
        int grules = 0;
        if (gkind == 0)
        {
            int whiteKings = game.findPieces(Common.PIECE_TYPE_KING, Common.COLOR_WHITE).size();
            int blackKings = game.findPieces(Common.PIECE_TYPE_KING, Common.COLOR_BLACK).size();
            if ((whiteKings != 1) || (blackKings != 1))
            {
                // missing/too many kings.this can be either suicide or freeplay. default is freeplay
                gkind = Common.GAME_KIND_FREEPLAY;
                grules = Common.GAME_RULES_FREEPLAY;
            }
            else if (! game.findPieces(Common.PIECE_TYPE_GRASSHOPER, 0).isEmpty()) {
                gkind = Common.GAME_KIND_GRASSHOPER;
                grules = Common.GAME_RULES_REGULAR;
            }
            else if (! game.findPieces(Common.PIECE_TYPE_ARCHBISHOP, 0).isEmpty() ||
                    ! game.findPieces(Common.PIECE_TYPE_CHANCELLOR, 0).isEmpty() ) {
                gkind = Common.GAME_KIND_MINICAPA;
                grules = Common.GAME_RULES_REGULAR;
            }
        }
        else
        {
            // gkind is given. so we can deduct game rules
            switch (gkind)
            {
                case Common.GAME_KIND_FISCHER:
                    grules = Common.GAME_RULES_FISCHER;
                    break;
                case Common.GAME_KIND_SUICIDE:
                    grules = Common.GAME_RULES_SUICIDE;
                    break;
                case Common.GAME_KIND_CRAZY_HOUSE:
                case Common.GAME_KIND_BUG_HOUSE:
                    grules = Common.GAME_RULES_CRAZY_HOUSE;
                    break;
                case Common.GAME_KIND_FREEPLAY:
                    grules = Common.GAME_RULES_FREEPLAY;
                    break;
                case Common.GAME_KIND_REGULAR:
                case Common.GAME_KIND_GRASSHOPER:
                case Common.GAME_KIND_MINICAPA:
                case Common.GAME_KIND_SEVEN_BOOM:
                    grules = Common.GAME_RULES_REGULAR;
                    break;
            }
        }

        Log.debug("loading fen '" + fen + "' start " + startGame + " gkind " + Common.GAME_KIND_TEXT[gkind] + " grules " +
                Common.GAME_RULES_TEXT[grules]);

		game.takebackAllMoves();
		game.clearBoard();

		fen = fen.trim();
		String[] tokens = fen.split("\\s+");
		ArrayList<String> toks = new ArrayList<String>();
		for (String tok : tokens) {
			if (!Utils.isEmptyString(tok)) {
				toks.add(tok);
			}
		}
		if (toks.size() < 1) {
			Log.warning("illegal FEN '" + fen + "'");
			return false;
		}

		// initialize game rules based on game kind
		game.setGameKind(gkind);
		game.setGameRules(grules);

		String position = toks.get(0);

		String currentColor = FEN.FEN_COLOR_WHITE;
		if (toks.size() > 1) {
			currentColor = toks.get(1);
		}

		String castling = "KQkq";
		if (toks.size() > 2) {
			castling = toks.get(2);
		}

		String epMoveStr = "-";
		if (toks.size() > 3) {
			epMoveStr = toks.get(3);
		}

		int draw50MovesRuleCount = 0;
		if (toks.size() > 4) {
			draw50MovesRuleCount = Utils.parseInt(toks.get(4));
		}

		String moveNumberStr = "1";
		if (toks.size() > 5) {
			moveNumberStr = toks.get(5);
		}

		// set the pieces
		String rows[] = position.split("[/\\[\\]]");
		if (rows.length < 8) {
			Log.info("illegal FEN " + fen + " length:" + rows.length + " position:" + position);
			return false;
		}
		Piece white_King = null;
		Piece black_King = null;
		int currentRow = 8;
		for (String row : rows) {
			if (currentRow == 0) {
				if ((gkind == Common.GAME_KIND_CRAZY_HOUSE) || (gkind == Common.GAME_KIND_BUG_HOUSE) || (gkind == 0))
                {
                    if (gkind == 0)
                    {
                        gkind = Common.GAME_KIND_CRAZY_HOUSE;
                        grules = Common.GAME_RULES_CRAZY_HOUSE;
                        game.setGameKind(gkind);
                        game.setGameRules(grules);
                    }
                    // this is a list of droppable pieces for crazy house or bug house
                    // XFEN mode is encoding captured pieces as "line 9"
                    boolean xfen = (position.indexOf('[') == -1);
                    List<Piece> capturedOrDroppableWhite = new ArrayList<Piece>();
                    List<Piece> capturedOrDroppableBlack = new ArrayList<Piece>();
                    Piece newPiece;
                    for (byte pieceB : row.getBytes()) {
                        char pieceCh = (char) pieceB;
                        if (pieceCh == '-')
                        {
                            break;
                        }
                        int color = Common.COLOR_WHITE;
                        if (Character.isLowerCase(pieceCh)) {
                            color = Common.COLOR_BLACK;
                        }
                        if (game.isCrazyHouse() && (!xfen))
                        {
                            color = Common.OtherColor(color);
                        }
                        pieceCh = Character.toUpperCase(pieceCh);
                        Integer type = Notation.sCharEngToPiece.get(String.valueOf(pieceCh));
                        if ((type == null) || (type == Common.PIECE_TYPE_ILLEGAL)) {
                            Log.warning("illegal FEN '" + fen + "' bad piece '" + pieceCh + "'");
                            return false;
                        }
                        newPiece = Piece.create(type, color);
                        if (newPiece.isWhite())
                        {
                            capturedOrDroppableWhite.add(newPiece);
                        }
                        else
                        {
                            capturedOrDroppableBlack.add(newPiece);
                        }
                    }
                    if (xfen)
                    {
                        game.setCapturedPieces(Common.COLOR_WHITE, capturedOrDroppableWhite);
                        game.setCapturedPieces(Common.COLOR_BLACK, capturedOrDroppableBlack);
                    }
                    else {
                        if (game.isCrazyHouse())
                        {
                            game.setDroppablePieces(Common.COLOR_BLACK, capturedOrDroppableWhite);
                            game.setDroppablePieces(Common.COLOR_WHITE, capturedOrDroppableBlack);
                        }
                        else
                        {
                            game.setDroppablePieces(Common.COLOR_WHITE, capturedOrDroppableWhite);
                            game.setDroppablePieces(Common.COLOR_BLACK, capturedOrDroppableBlack);
                        }
                    }
                    Log.debug("FEN set captured piece white " + capturedOrDroppableWhite + " black " + capturedOrDroppableBlack);
                    break;
                }
                Log.warning("bad extra row in FEN gkind " + Common.GAME_KIND_TEXT[gkind]);
				return false;
			}
			int column = 1;
            Piece newPiece = null;
            for (byte pieceB : row.getBytes()) {
				char pieceCh = (char) pieceB;

                if (pieceCh == '~')
                {
                    // last piece is a promoted pawn
                    newPiece.setPromoted();
                    continue;
                }

				if (Character.isDigit(pieceCh)) {
					column += (pieceCh - 48);
					continue;
				}

				if (column > 8) {
					Log.warning("illegal FEN '" + fen + "' bad row '" + row + "'");
					return false;
				}

				int color = Common.COLOR_WHITE;
				if (Character.isLowerCase(pieceCh)) {
					color = Common.COLOR_BLACK;
				}
				pieceCh = Character.toUpperCase(pieceCh);

				Integer type = Notation.sCharEngToPiece.get(String.valueOf(pieceCh));
				if ((type == null) || (type == Common.PIECE_TYPE_ILLEGAL)) {
					Log.warning("illegal FEN '" + fen + "' bad piece '" + pieceCh + "'");
					return false;
				}
				newPiece = Piece.create(type, color);
				game.setPieceAt(currentRow, column, newPiece);
				if (newPiece.isKing()) {
					if (color == Common.COLOR_WHITE) {
						if (white_King != null)
						{
							// 2 white kings - it can be free play or suicide or fail FEN loading
							if (grules == 0)
							{
								grules = Common.GAME_RULES_FREEPLAY;
								game.setGameRules(grules);
							}
							if ((grules != Common.GAME_RULES_FREEPLAY) && (grules != Common.GAME_RULES_SUICIDE))
							{
								Log.warning("game kind mismatch fen '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind]);
								return false;
							}
						}
						white_King = newPiece;
					}
					else {
						if (black_King != null)
						{
							// 2 black kings - it can be free play or suicide or fail FEN loading
							if (grules == 0)
							{
								grules = Common.GAME_RULES_FREEPLAY;
								game.setGameRules(grules);
							}
							if ((grules != Common.GAME_RULES_FREEPLAY) && (grules != Common.GAME_RULES_SUICIDE))
							{
								Log.warning("game kind mismatch fen '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind]);
								return false;
							}
						}
						black_King = newPiece;
					}
				}
				column++;
			}
			if (column != 9) {
				Log.warning("illegal FEN '" + fen + "' bad row '" + row + "'");
				return false;
			}
			currentRow--;
		}
		if (currentRow != 0) {
			Log.warning("illegal FEN '" + fen + "' bad number of rows");
			return false;
		}

		if ((white_King == null) || (black_King == null))
		{
			// missing kings - it can be free play or suicide or fail FEN loading. default free play
			if (grules == 0)
			{
				grules = Common.GAME_RULES_FREEPLAY;
				game.setGameRules(grules);
			}
			if ((grules != Common.GAME_RULES_FREEPLAY) && (grules != Common.GAME_RULES_SUICIDE))
			{
				Log.warning("game kind mismatch fen '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind] +
					" grules '" + Common.GAME_RULES_TEXT[grules]);
				return false;
			}
		}

        /*
		// verify game kind based on the pieces
		if (	(! game.findPieces(Common.PIECE_TYPE_ARCHBISHOP, 0).isEmpty()) ||
				(! game.findPieces(Common.PIECE_TYPE_CHANCELLOR, 0).isEmpty())	)
		{
			// mini capa ,custom or free play
			if ((gkind != 0) && (gkind != Common.GAME_KIND_MINICAPA) && (gkind != Common.GAME_KIND_FREEPLAY))
			{
				Log.warning("game kind mismatch fen '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind]);
				//return false;
				game.setGameKind(Common.GAME_KIND_MINICAPA);
			}
		}

		if (! game.findPieces(Common.PIECE_TYPE_GRASSHOPER, 0).isEmpty())
		{
			// grass hopper
			if ((gkind != 0) && (gkind != Common.GAME_KIND_GRASSHOPER))
			{
				Log.warning("game kind mismatch fen '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind]);
				//return false;
				game.setGameKind(Common.GAME_KIND_GRASSHOPER);
			}
		}
		*/

		if (FEN_COLOR_WHITE.equals(currentColor)) {
			game.setStartingColor(Common.COLOR_WHITE);
		} else if (FEN_COLOR_BLACK.equals(currentColor)) {
			game.setStartingColor(Common.COLOR_BLACK);
		} else {
			Log.warning("illegal FEN '" + fen + "' bad color " + currentColor);
			return false;
		}

		boolean canCastle[][] = new boolean[Common.COLOR_NUM][Common.CASTLE_NUM];

		// handle fischer style castling letters
		int kingLoc = 5;
		int LeftRook = 1;
		int RightRook = 8;
		if (!(castling.equals("-") || castling.contains("k") || castling.contains("K") || castling.contains("Q") || castling.contains("q")) ) {
			kingLoc = black_King.getY();
			LeftRook = 0; // if not defined in castling
			RightRook = 0;
			for (byte pieceB : castling.getBytes()) {
				char pieceCh = (char) pieceB;
				int p = pieceCh - 'a' + 1;
				if (p<0) { // white can castle
					p = pieceCh - 'A' + 1;
					kingLoc = white_King.getY();
				}
				if (p>kingLoc) {
					RightRook = p;
				}
				else {
					LeftRook = p;
				}
			}
		}
		Log.debug(LeftRook + "," + RightRook);
		if (LeftRook == 0) {
			LeftRook = kingLoc;
		}
		if (RightRook == 0) {
			RightRook = kingLoc;
		}
		if (LeftRook > 8 || RightRook > 8) {
			Log.warning("ERROR IN FEN CASTLING");
			castling = "-";
			LeftRook =  kingLoc;
			RightRook =  kingLoc;
		}
		game.setAttribute(Game.KING_LOCATION, kingLoc);
		game.setAttribute(Game.LEFT_ROOK_LOCATION, LeftRook);
		game.setAttribute(Game.RIGHT_ROOK_LOCATION, RightRook);
        Log.debug("Game rooks location right " + RightRook + " king " + kingLoc + " left " + LeftRook);

		Piece whiteKing = game.getPieceAt(1, kingLoc);
		if (whiteKing != null) {
			if (whiteKing.isKing() && whiteKing.isWhite()) {
				// white king in place. check rooks
				Piece rook1 = game.getPieceAt(1, RightRook);
				if (rook1 != null) {
					if (rook1.isRook() && rook1.isWhite()) {
						canCastle[Common.COLOR_WHITE][Common.CASTLE_KING] = true;
					}
				}
				Piece rook2 = game.getPieceAt(1, LeftRook);
				if (rook2 != null) {
					if (rook2.isRook() && rook2.isWhite()) {
						canCastle[Common.COLOR_WHITE][Common.CASTLE_QUEEN] = true;
					}
				}
			}
		}
		Piece blackKing = game.getPieceAt(8, kingLoc);
		if (blackKing != null) {
			if (blackKing.isKing() && blackKing.isBlack()) {
				// white king in place. check rooks
				Piece rook1 = game.getPieceAt(8, RightRook);
				if (rook1 != null) {
					if (rook1.isRook() && rook1.isBlack()) {
						canCastle[Common.COLOR_BLACK][Common.CASTLE_KING] = true;
					}
				}
				Piece rook2 = game.getPieceAt(8, LeftRook);
				if (rook2 != null) {
					if (rook2.isRook() && rook2.isBlack()) {
						canCastle[Common.COLOR_BLACK][Common.CASTLE_QUEEN] = true;
					}
				}
			}
		}
		game.setCastlingAvailability(Common.COLOR_WHITE, Common.CASTLE_KING, false);
		game.setCastlingAvailability(Common.COLOR_WHITE, Common.CASTLE_QUEEN, false);
		game.setCastlingAvailability(Common.COLOR_BLACK, Common.CASTLE_KING, false);
		game.setCastlingAvailability(Common.COLOR_BLACK, Common.CASTLE_QUEEN, false);
		for (byte castleB : castling.getBytes()) {
			char castleCh = (char) castleB;
			switch (castleCh) {
			case 'K':
				if (canCastle[Common.COLOR_WHITE][Common.CASTLE_KING]) {
					game.setCastlingAvailability(Common.COLOR_WHITE, Common.CASTLE_KING, true);
				}
				break;
			case 'Q':
				if (canCastle[Common.COLOR_WHITE][Common.CASTLE_QUEEN]) {
					game.setCastlingAvailability(Common.COLOR_WHITE, Common.CASTLE_QUEEN, true);
				}
				break;
			case 'k':
				if (canCastle[Common.COLOR_BLACK][Common.CASTLE_KING]) {
					game.setCastlingAvailability(Common.COLOR_BLACK, Common.CASTLE_KING, true);
				}
				break;
			case 'q':
				if (canCastle[Common.COLOR_BLACK][Common.CASTLE_QUEEN]) {
					game.setCastlingAvailability(Common.COLOR_BLACK, Common.CASTLE_QUEEN, true);
				}
				break;
			case '-':
				break;
			case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
				if (grules == 0)
				{
					grules = Common.GAME_RULES_FISCHER;
					game.setGameRules(grules);
				}
				if (grules != Common.GAME_RULES_FISCHER)
				{
					Log.warning("game kind mismatch fen '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind]);
					return false;
				}
				if ( ((int)castleCh) - 'a' + 1 > kingLoc ) {
					if (canCastle[Common.COLOR_BLACK][Common.CASTLE_KING]) {
						game.setCastlingAvailability(Common.COLOR_BLACK, Common.CASTLE_KING, true);
					}
				}
				else {
					if (canCastle[Common.COLOR_BLACK][Common.CASTLE_QUEEN]) {
						game.setCastlingAvailability(Common.COLOR_BLACK, Common.CASTLE_QUEEN, true);
					}
				}
				break;
			case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
				if (grules == 0)
				{
					grules = Common.GAME_RULES_FISCHER;
					game.setGameRules(grules);
				}
				if (grules != Common.GAME_RULES_FISCHER)
				{
					Log.warning("game kind mismatch fen '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind]);
					return false;
				}
				if ( ((int)castleCh) - 'A' + 1 > kingLoc ) {
					if (canCastle[Common.COLOR_WHITE][Common.CASTLE_KING]) {
						game.setCastlingAvailability(Common.COLOR_WHITE, Common.CASTLE_KING, true);
					}
				}
				else {
					if (canCastle[Common.COLOR_WHITE][Common.CASTLE_QUEEN]) {
						game.setCastlingAvailability(Common.COLOR_WHITE, Common.CASTLE_QUEEN, true);
					}
				}
				break;
			default:
				Log.warning("illegal FEN '" + fen + "' bad castling string " + castling);
				return false;
			}
		}

		if (grules == 0)
		{
			grules = Common.GAME_RULES_REGULAR;
			game.setGameRules(grules);
		}

		Log.debug("FEN '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind] + " grules " + Common.GAME_RULES_TEXT[grules]);
		if (gkind == 0)
		{
			// set game kind according to the resulted game rules
			switch (grules)
			{
				case Common.GAME_RULES_FISCHER:
					game.setGameKind(Common.GAME_KIND_FISCHER);
				break;
				case Common.GAME_RULES_SUICIDE:
					game.setGameKind(Common.GAME_KIND_SUICIDE);
				break;
				case Common.GAME_RULES_FREEPLAY:
					game.setGameKind(Common.GAME_KIND_FREEPLAY);
				break;
                case Common.GAME_RULES_CRAZY_HOUSE:
                    game.setGameKind(Common.GAME_KIND_CRAZY_HOUSE);
                break;
				default:
					game.setGameKind(Common.GAME_KIND_REGULAR);
				break;
			}
		}
        else
        {
            // game kind was already found
            game.setGameKind(gkind);
            game.setGameRules(grules);
        }
		Log.debug("FEN '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind] + " grules " + Common.GAME_RULES_TEXT[grules]);

		/*
		 * if FEN contains EP move, add it to the game
		 */
        game.setEpPawn(null);
		if (!("-".equals(epMoveStr))) {
			do {
				if (epMoveStr.length() != 2) {
					Log.warning("bad ep pawn move : " + epMoveStr);
					break;
				}
				int epX = epMoveStr.charAt(1) - 48;
				int epY = epMoveStr.charAt(0) - 96;
				if ((epY < 1) || (epY > 8) || (epX < 1) || (epX > 8)) {
					Log.warning("bad ep pawn move : " + epMoveStr);
					break;
				}

				int captureX;
				Piece epPawn;
				if ((game.getCurrentColor() == Common.COLOR_WHITE) && (epX == 6)) {
					// black EP pawn
					captureX = epX - 1;
					epPawn = game.getPieceAt(captureX, epY);
					if ((game.getPieceAt(epX, epY) != null) || (game.getPieceAt(epX + 1, epY) != null)) {
						Log.warning("bad ep pawn move : " + epMoveStr);
						break;
					}
				} else if ((game.getCurrentColor() == Common.COLOR_BLACK) && (epX == 3)) {
					// white EP pawn
					captureX = epX + 1;
					epPawn = game.getPieceAt(captureX, epY);
					if ((game.getPieceAt(epX, epY) != null) || (game.getPieceAt(epX - 1, epY) != null)) {
						Log.warning("bad ep pawn move : " + epMoveStr);
						break;
					}
				} else {
					Log.info("bad ep pawn move : " + epMoveStr);
					break;
				}
				if (epPawn == null) {
					Log.info("bad ep pawn move : " + epMoveStr);
					break;
				}
				if ((!epPawn.isPawn()) || (epPawn.getColor() == game.getCurrentColor())) {
					Log.info("bad ep pawn move : " + epMoveStr);
					break;
				}
				// look for capturing pawns
				Piece capturingPawn;
				if (epY > 1) {
					capturingPawn = game.getPieceAt(captureX, epY - 1);
					if (capturingPawn != null) {
						if ((capturingPawn.isPawn()) && (capturingPawn.isColor(game.getCurrentColor()))) {
							// found a capturing pawn !
							game.setEpPawn((Pawn) epPawn);
						}
					}
				}
				if (epY < 8) {
					capturingPawn = game.getPieceAt(captureX, epY + 1);
					if (capturingPawn != null) {
						if ((capturingPawn.isPawn()) && (capturingPawn.isColor(game.getCurrentColor()))) {
							// found a capturing pawn !
							game.setEpPawn((Pawn) epPawn);
						}
					}
				}
			} while (false);
		}

		int _moveNumber = Utils.parseInt(moveNumberStr);

		if (_moveNumber < 1) {
			_moveNumber = 1;
		}

		game.setMoveNumber(_moveNumber);
		game.setAttribute(Game.INITIAL_POSITION_FEN, fen);

        MoveInfo moveInfo;
        if (startGame) {
            moveInfo = game.getCurrentMoveInfo();
			if (null == moveInfo) {
				Log.warning("illegal FEN '" + fen + "' gkind " + Common.GAME_KIND_TEXT[gkind] + " game failed first analyse");
				return false;
			}
			moveInfo.setDraw50MovesCount(draw50MovesRuleCount);
		}
		return true;
	}

	/**
	 * Loads the given FEN string into a new game instance.
	 *
	 * @param fen
	 *            a FEN string.
	 * @return a new game instance.
	 */
	public static Game loadGame(String fen) {
		return loadGame(fen, Common.GAME_KIND_REGULAR);
	}

	public static Game loadGame(String fen, int gkind) {
		Game game = new Game(gkind);

		if (loadGame(game, fen, false, gkind)) {
			return game;
		}
		return null;
	}

	/**
	 * Loads the given FEN string into a new game instance. If startGame is
	 * false, then the FEN may be an invalid game position.
	 *
	 * @param fen
	 *            a FEN string.
	 * @param startGame
	 *            - If true, FEN position is validated as a valid chess
	 *            position.
	 * @return a new game instance.
	 */
	public static Game loadGame(String fen, boolean startGame) {
		Game game = new Game();
		if (loadGame(game, fen, startGame, 0)) {
			return game;
		}
		return null;
	}

	public static Game loadGame(String fen, boolean startGame, int gkind) {
		Game game = new Game(gkind);

		if (loadGame(game, fen, startGame, gkind)) {
			return game;
		}
		return null;
	}

	public static String create960FEN(){
		List <String> pos = new ArrayList <String>();
		pos.addAll( Arrays.asList("R", "K", "R" ) );
		int i = Utils.random(4);
		Log.debug(pos+ " I:"+i);
		pos.add(i, "Q");
		i = Utils.random(5);
		pos.add(i, "N");
		i = Utils.random(6);
		pos.add(i, "N");
		i = Utils.random(7);
		pos.add(i, "B"); // the right bishop
		int l = 0;
		if (((i+1) / 2) > 0)
		{
			l = Utils.random((i+1)/2);
		}
		pos.add(2*l + (i%2), "B"); // left bishop
		String POS = "";
		for (i=0;i<8;i++) {
			POS += pos.get(i);
		}
		List <String> letters = Arrays.asList("A","B","C","D","E","F","G","H");
		String castle = letters.get ( pos.indexOf("R") ) + letters.get(pos.lastIndexOf("R"));
		castle += castle.toLowerCase();
		return POS.toLowerCase() + "/pppppppp/8/8/8/8/PPPPPPPP/" + POS + " w " + castle + " - 0 1";
	}

    public static Piece type2piece(int type, int color) {
        switch(type) {
            case (Common.PIECE_TYPE_PAWN):
                return new Pawn(color);
            case (Common.PIECE_TYPE_KNIGHT):
                return new Knight(color);
            case (Common.PIECE_TYPE_BISHOP):
                return new Bishop(color);
            case (Common.PIECE_TYPE_ROOK):
                return new Rook(color);
            case (Common.PIECE_TYPE_QUEEN):
                return new Queen(color);
            case (Common.PIECE_TYPE_KING):
                return new King(color);
            case (Common.PIECE_TYPE_GRASSHOPER):
                return new Grasshoper(color);
            case (Common.PIECE_TYPE_ARCHBISHOP):
                return new Archbishop(color);
            case (Common.PIECE_TYPE_CHANCELLOR):
                return new Chancellor(color);
        }
        return null;
    }

    public static boolean containsSpecialPiece(String fen) {
        if (Utils.isEmptyString(fen)) {
            Log.warning("Error: empty fen:"+fen);
            return false;
        }
        String tfen = fen.substring(0, fen.indexOf(' '));
        return tfen.matches(".*[acgACG].*");
    }
}
