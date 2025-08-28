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
import com.ichess.game.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.ichess.game.Log.*;

/**
 * @author Ran Berenfeld
 * @version 1.0
 */
public class PGN {

	private static final SimpleDateFormat PGN_DATE_FORMATTER = new SimpleDateFormat("yyyy.MM.dd");

	public static final String PGN_VARIANT_FISCHER = "Fischerandom";
	public static final String PGN_VARIANT_SUICIDE = "Suicide";
	public static final String PGN_VARIANT_GRASSHOPER = "Grasshoper";
	public static final String PGN_VARIANT_MINICAPA = "MiniCapa";
	public static final String PGN_VARIANT_FREEPLAY = "Free";
    public static final String PGN_VARIANT_CRAZY_HOUSE = "CrazyHouse";
    public static final String PGN_VARIANT_BUG_HOUSE = "BugHouse";
    public static final String PGN_VARIANT_SEVEN_BOOM = "SevenBoom";

	public static final String STR_BLACK = "Black";
    public static final String STR_BLACK_A = "BlackA";
    public static final String STR_BLACK_B = "BlackB";
	public static final String STR_BLACK_ELO = "BlackElo";
	public static final String STR_BLACK_NA = "BlackNA";
	public static final String STR_BLACK_TYPE = "BlackType";
	public static final String STR_DATE = "Date";
	public static final String STR_ECO = "ECO";
	public static final String STR_VARIANT = "Variant";
	public static final String STR_EVENT = "Event";
	public static final String STR_FEN = "FEN";
	public static final String STR_RESULT = "Result";
	public static final String STR_RESULT_BLACK = "0-1";
	public static final String STR_RESULT_DRAW = "1/2-1/2";
	public static final String STR_RESULT_ONGOING = "*";
	public static final String STR_RESULT_WHITE = "1-0";
	public static final String STR_ROUND = "Round";
	public static final String STR_SETUP = "SetUp";
	public static final String STR_SITE = "Site";
	public static final String STR_TIME_CONTROL = "TimeControl";
	public static final String STR_TIME_CONTROL_UNLIMIT = "-";
	public static final String STR_TYPE_HUMAN = "human";
	public static final String STR_TYPE_PROGRAM = "program";
	public static final String STR_WHITE = "White";
    public static final String STR_WHITE_A = "WhiteA";
    public static final String STR_WHITE_B = "WhiteB";
	public static final String STR_WHITE_ELO = "WhiteElo";
	public static final String STR_WHITE_NA = "WhiteNA";
	public static final String STR_WHITE_TYPE = "WhiteType";

	// game end strings

	// white wins
	public static final String STR_BLACK_RESIGN = "שחור נכנע";
	public static final String STR_BLACK_TIMEOUT = "שחור עבר את הזמן";
	public static final String STR_BLACK_MATE = "מט לשחור";
	public static final String STR_BLACK_DISCONNECT = "שחור התנתק";
	public static final String STR_WHITE_WIN = "נצחון ללבן";

	// draw
	public static final String STR_DRAW = "הוסכם תיקו";
	public static final String STR_NO_MATERIAL = "אין מספיק חומר לשני הצדדים";
	public static final String STR_REPETITION = "חזרה על מסעים";
	public static final String STR_50_MOVES = "50 מסעים ללא הכאת כלי או מסע רגלי";
	public static final String STR_WHITE_ABORT = "לבן ביטל את המשחק";
	public static final String STR_BLACK_ABORT = "שחור ביטל את המשחק";
	public static final String STR_STALEMATE = "פט";

	// black wins
	public static final String STR_WHITE_RESIGN = "לבן נכנע";
	public static final String STR_WHITE_TIMEOUT = "לבן עבר את הזמן";
	public static final String STR_WHITE_MATE = "מט ללבן";
	public static final String STR_WHITE_DISCONNECT = "לבן התנתק";
	public static final String STR_BLACK_WIN = "נצחון לשחור";

	public static final String[] _pgn_attrbutes = { STR_EVENT, STR_SITE, STR_DATE, STR_ROUND, STR_WHITE, STR_BLACK, STR_RESULT,
		STR_SETUP, STR_FEN, STR_WHITE_ELO, STR_BLACK_ELO, STR_WHITE_NA, STR_BLACK_NA, STR_WHITE_TYPE, STR_BLACK_TYPE,
		STR_TIME_CONTROL, STR_ECO, STR_VARIANT, STR_WHITE_A, STR_WHITE_B, STR_BLACK_A, STR_BLACK_B };

    public static Pattern PGN_CLOCK_PATTERN = Pattern.compile("(.*)%clk ([0-9:]*)(.*)");

	public static String formatDate(Date date) {
		Utils.AssertNull(date);
		return PGN_DATE_FORMATTER.format(date);
	}

	/**
	 * Returns PGN move prefix of the last move in the given game for example if
	 * the game move number is 1 : ("1." for white, "1..." for black) if not
	 * found returns null.
	 *
	 * @param game
	 * @return PGN move prefix
	 */
	public static String getMovePrefix(Game game) {
		Utils.AssertNull(game);
		int moveNumber = game.getMoveNumber();
		if ((game.getCurrentColor() == Common.COLOR_WHITE) && (moveNumber > 1)) {
			moveNumber--;
		}
		return getMovePrefix(moveNumber, Common.OtherColor(game.getCurrentColor()));
	}

	/**
	 * Returns PGN move prefix with the given move number for example if move
	 * number is 1 : ("1." for white, "1..." for black) if not found returns
	 * null.
	 *
	 * @param moveNumber
	 * @param color
	 * @return PGN move prefix
	 */
	public static String getMovePrefix(int moveNumber, int color) {
		if (Common.isWhite(color)) {
			return moveNumber + ".";
		}
		if (Common.isBlack(color)) {
			return moveNumber + "...";
		}
		return null;
	}

    public static String getPGNString(Game game) {
        return getPGNString(game, false);
    }

	public static String getPGNString(Game game, boolean hebrew) {
		Utils.AssertNull(game);

		StringBuilder pgn = new StringBuilder();

        // in hebrew, dont put tags
        if (!hebrew)
        {
            for (String pgn_key : _pgn_attrbutes) {
                String value = (String) game.getAttribute(pgn_key);
                if (Utils.isEmptyString(value)) {
                    continue;
                }
                pgn.append('[').append(pgn_key).append(" \"").append(value).append("\"]\n");
            }
        }

		String initialPosition = (String) game.getAttribute(Game.INITIAL_POSITION_FEN);
		if (initialPosition != null) {
			if (!FEN.FEN_INITIAL_POS.equals(initialPosition)) {
				pgn.append('[').append(STR_SETUP).append(" \"1\"]\n[").append(STR_FEN).append(" \"").append(initialPosition).append(
					"\"]\n");
			}
		}

		pgn.append("\n");

		int index = 0;
		int moveNumber = 1;
		while (index < game.getCurrentMove())
		{
			Move move = game.getMove(index);
			if ((index == 0) || (move.getColor() == Common.COLOR_WHITE))
			{
				pgn.append(getMovePrefix(moveNumber, move.getColor())).append(" ");
			}

            if (hebrew)
            {
			    pgn.append(move.getNameAlgHeb()).append(" ");
            }
            else
            {
                pgn.append(move.getNameAlg()).append(" ");
            }

			String comment = move.getComment();
			if (! Utils.isEmptyString(comment))
			{
				// replace all {,} with (,)
				comment = comment.replaceAll("\\{", "\\(");
				comment = comment.replaceAll("\\}", "\\)");

				pgn.append(Utils.LRO).append("{ ")
					.append(Utils.encodeInRLE(comment))
					.append(" } ").append(Utils.PDF);
			}
			index ++;
			if (move.getColor() == Common.COLOR_BLACK)
			{
				moveNumber ++;
			}
		}
		pgn.append(getResultString(game.getWinner())).append('\n');

		return pgn.toString();
	}

	public static String getResultString(int winner) {
		switch (winner) {
		case Common.COLOR_WHITE:
			return STR_RESULT_WHITE;
		case Common.COLOR_BLACK:
			return STR_RESULT_BLACK;
		case Common.COLOR_ILLEGAL:
			return STR_RESULT_DRAW;
		}
		return STR_RESULT_ONGOING;
	}

	/**
	 * Loads the given game from a PGN string (takes back all moves, updates the
	 * information on the game)
	 *
	 * @param game
	 * @param pgnString
	 * @return
	 */
	public static int loadGame(Game game, String pgnString) {
		Utils.AssertNull(pgnString);
		Utils.AssertNull(game);

		Log.debug("loading PGN " + pgnString);

		String _pgnString = pgnString;

		// replace all white spaces with regular spaces
		_pgnString = _pgnString.replaceAll("\\s+", " ");

		// replace all control characters with spaces
		_pgnString = _pgnString.replaceAll("\\p{C}", " ");

		String temp = _pgnString;
		temp = temp.trim();

		boolean setup = false;
		String fen = "";

		int grules = Common.GAME_RULES_REGULAR;
		int gkind = Common.GAME_KIND_REGULAR;

        // parse PGN tags
        String key,value;
        int tagIndex;
        Pattern pgnTagsPattern = Pattern.compile("\\[\\s*([^\\s]*)\\s+\"([^\"]*)\"\\s*\\]");
        do {
            Matcher m = pgnTagsPattern.matcher(temp);
            if (!m.find())
            {
                break;
            }
            tagIndex = m.end();
            key = m.group(1);
            value = m.group(2);

            temp = temp.substring(tagIndex, temp.length());
            temp = temp.trim();

            if ((Utils.isEmptyString(key)) || (Utils.isEmptyString(value))) {
                continue;
            }
            for (String pgn_key : _pgn_attrbutes) {
                if (pgn_key.equalsIgnoreCase(key)) {
                    game.setAttribute(pgn_key, value);
                }
            }

            if (STR_SETUP.equalsIgnoreCase(key)) {
                setup = true;
            }
            if (STR_FEN.equalsIgnoreCase(key)) {
                fen = value;
                setup = true;
            }
            if (STR_VARIANT.equalsIgnoreCase(key))
            {
                if (PGN_VARIANT_FISCHER.equalsIgnoreCase(value))
                {
                    grules = Common.GAME_RULES_FISCHER;
                    gkind = Common.GAME_KIND_FISCHER;
                }
                else if (PGN_VARIANT_SUICIDE.equalsIgnoreCase(value))
                {
                    grules = Common.GAME_RULES_SUICIDE;
                    gkind = Common.GAME_KIND_SUICIDE;
                }
                else if (PGN_VARIANT_MINICAPA.equalsIgnoreCase(value))
                {
                    gkind = Common.GAME_KIND_MINICAPA;
                }
                else if (PGN_VARIANT_GRASSHOPER.equalsIgnoreCase(value))
                {
                    gkind = Common.GAME_KIND_GRASSHOPER;
                }
                else if (PGN_VARIANT_FREEPLAY.equalsIgnoreCase(value))
                {
                    grules = Common.GAME_RULES_FREEPLAY;
                    gkind = Common.GAME_KIND_FREEPLAY;
                }
                else if (PGN_VARIANT_CRAZY_HOUSE.equalsIgnoreCase(value))
                {
                    grules = Common.GAME_RULES_CRAZY_HOUSE;
                    gkind = Common.GAME_KIND_CRAZY_HOUSE;
                }
                else if (PGN_VARIANT_BUG_HOUSE.equalsIgnoreCase(value))
                {
                    grules = Common.GAME_RULES_CRAZY_HOUSE;
                    gkind = Common.GAME_KIND_BUG_HOUSE;
                }
                else if (PGN_VARIANT_SEVEN_BOOM.equalsIgnoreCase(value))
                {
                    grules = Common.GAME_RULES_REGULAR;
                    gkind = Common.GAME_KIND_SEVEN_BOOM;
                }

                Log.debug("setting game rules " + Common.GAME_RULES_TEXT[grules] + " game kind " + Common.GAME_KIND_TEXT[gkind] );
                game.setGameRules(grules);
                game.setGameKind(gkind);
            }

            // in any case, set the attribute for the game
            game.setAttribute(key, value);
        } while (true);

		temp = temp.trim();

		temp = temp.replaceAll("\\.", ". ");

		// replace all NAGs with the unicode display
		// (first 2 digits)
		temp = temp.replaceAll("\\$10", "=");
		temp = temp.replaceAll("\\$11", "=");
		temp = temp.replaceAll("\\$12", "=");
		temp = temp.replaceAll("\\$13", "\u221E");
		temp = temp.replaceAll("\\$14", "\u2A72");
		temp = temp.replaceAll("\\$15", "\u2A71");
		temp = temp.replaceAll("\\$16", "\u0177");
		temp = temp.replaceAll("\\$17", "\u2213");
		temp = temp.replaceAll("\\$18", "+-");
		temp = temp.replaceAll("\\$19", "-+");

		temp = temp.replaceAll("\\$1", "!");
		temp = temp.replaceAll("\\$2", "?");
		temp = temp.replaceAll("\\$3", "!!");
		temp = temp.replaceAll("\\$4", "??");
		temp = temp.replaceAll("\\$5", "!?");
		temp = temp.replaceAll("\\$6", "?!");


		// replace comments "{}[]()" with spaces to isloate comments
		List<String> openBrs = Arrays.asList( "{" , "(", "[" );
		List<String> closeBrs = Arrays.asList( "}" , ")", "]" );

		for (String openBr : openBrs) {
			temp = temp.replace(openBr, " " + openBr + " ");
		}
		for (String closeBr : closeBrs) {
			temp = temp.replace(closeBr, " " + closeBr + " ");
		}

		//Log.info("pgn movelist is " + temp);

		StringTokenizer st = new StringTokenizer(temp, " ");

		int winner = Common.COLOR_WHITE;
		boolean pgnEnded = false;

		if (setup) {
			if (!FEN.loadGame(game, fen, true, gkind)) {
				Log.warning("PGN failed to load fen : " + fen);
				Log.warning("failed to parse PGN : " + _pgnString);
				return Common.RC_GENERAL_FAILURE;
			}
            fen = FEN.getFENString(game);
			game.setAttribute(Game.INITIAL_POSITION_FEN, fen);
		} else {
			// no setup. start from initial position
			game.initialPosition();
		}

        // bpgn heuristic
        boolean bpgn = (pgnString.contains("1A."));
        Game currentGame = game;
        Game otherGame = null;
        if (game.isBugHouse() && bpgn)
        {
            otherGame = new Game(Common.GAME_KIND_BUG_HOUSE);
            game.setOtherGame(otherGame);
            otherGame.setOtherGame(game);
        }

        // set PGN names if exists
        if (! game.isBugHouse())
        {
            String white = (String)game.getAttribute(STR_WHITE);
            if (! Utils.isEmptyString(white))
            {
                game.setWhiteName(white);
            }
            String black = (String)game.getAttribute(STR_BLACK);
            if (! Utils.isEmptyString(black))
            {
                game.setBlackName(black);
            }
        }
        else
        {
            String whiteA = (String)game.getAttribute(STR_WHITE_A);
            if (! Utils.isEmptyString(whiteA))
            {
                game.setWhiteName(whiteA);
            }
            String blackA = (String)game.getAttribute(STR_BLACK_A);
            if (! Utils.isEmptyString(blackA))
            {
                game.setBlackName(blackA);
            }

            String whiteB = (String)game.getAttribute(STR_WHITE_B);
            if (! Utils.isEmptyString(whiteB))
            {
                otherGame.setWhiteName(whiteB);
            }
            String blackB = (String)game.getAttribute(STR_BLACK_B);
            if (! Utils.isEmptyString(blackB))
            {
                otherGame.setBlackName(blackB);
            }
        }

        /*
        if (! game.isBugHouse())
        {
            game.setWhiteName((String)game.getAttribute(STR_WHITE));
            game.setBlackName((String)game.getAttribute(STR_BLACK));
        }
        else
        {
            game.setWhiteName((String)game.getAttribute(STR_WHITE_A));
            game.setBlackName((String)game.getAttribute(STR_BLACK_A));
            otherGame.setWhiteName((String)game.getAttribute(STR_WHITE_B));
            otherGame.setBlackName((String)game.getAttribute(STR_BLACK_B));
        }
        */

		Stack<String> brackets = new Stack<String>();
		boolean inComment = false;
		String comment = "";

		while (st.hasMoreTokens()) {
			String tok = st.nextToken();

			tok = tok.trim();

			// filter out empty tokens
			if (Utils.isEmptyString(tok))
			{
				continue;
			}

			Log.debug("parse token " + tok);

			if (openBrs.contains(tok))
			{
				// start comment
				brackets.push(tok);
				inComment = true;
				comment = "";
				continue;
			}

			if (closeBrs.contains(tok))
			{
				String lastBr = brackets.pop();
				// validate correct bracket
				if ((closeBrs.indexOf(tok) != openBrs.indexOf(lastBr)))
				{
					Log.warning("brackets error. terminating");
					break;
				}
				if (brackets.isEmpty())
				{
					inComment = false;
					Move lastMove = game.getLastMove();
					if (lastMove != null)
					{
						if (! Utils.isEmptyString(comment))
						{
							comment = "( " + Utils.encodeInRLE(comment) + " )";
							lastMove.appendComment(comment);
						}
					}
				}
				continue;
			}

			if (inComment)
			{
				comment += " " + tok;
				continue;
			}

			if (game.isEnded()) {
				Log.debug("game ended. ignoring the rest of the move list");
				break;
			}

			// check special end of game markers (like 1-0, 1/2-1/2, *, etc)
			if (tok.contains(STR_RESULT_WHITE)) {
				winner = Common.COLOR_WHITE;
				pgnEnded = true;
				continue;
			}
			if (tok.contains(STR_RESULT_BLACK)) {
				winner = Common.COLOR_BLACK;
				pgnEnded = true;
				continue;
			}
			if (tok.contains(STR_RESULT_DRAW)) {
				winner = Common.COLOR_ILLEGAL;
				pgnEnded = true;
				continue;
			}
			if (tok.contains(STR_RESULT_ONGOING)) {
				winner = Common.COLOR_ILLEGAL;
				pgnEnded = false;
				continue;
			}

            if (Character.isDigit(tok.charAt(0)))
            {
                if (bpgn && game.isBugHouse())
                {
                    // in bughouse switch to current game
                    if ((tok.contains("A") || tok.contains("a")))
                    {
                        currentGame = game;
                    }
                    if ((tok.contains("B") || tok.contains("b")))
                    {
                        currentGame = otherGame;
                    }
                }
            }

            // filter out tokens that does not begin with a letter (move
            // numbers)
            if (! Character.isLetter(tok.charAt(0))) {
                continue;
            }

            // filter out tokens with dots (after move number)
            if (tok.contains("\\.")) {
                continue;
            }

			// remove unwanted tokens in moves (like #,+,!,?,ep)
			tok = tok.replaceAll("[\\?!\\+#\\.\\$]+", "");
			tok = tok.replaceAll("ep", "");

			Log.debug("handling token: " + tok);

			if (Common.RC_OK != currentGame.playMove(tok)) {
				Log.warning("invalid move token " + tok + " for " + Common.GetColor(currentGame.getCurrentColor()));
				Log.warning("failed to parse PGN : " + _pgnString);
				return Common.RC_GENERAL_FAILURE;
			}
		}

        Log.debug("game ended " + currentGame.isEnded() + " PGN ended " + pgnEnded + " end str " + currentGame.getEndString());
		if ((!currentGame.isEnded()) && (pgnEnded)) {
			switch (winner) {
                case Common.COLOR_WHITE:
                    currentGame.resign(Common.COLOR_BLACK);
                    break;
                case Common.COLOR_BLACK:
                    currentGame.resign(Common.COLOR_WHITE);
                    break;
                case Common.COLOR_ILLEGAL:
                    currentGame.drawMutual();
                    break;
			}
            if (bpgn && currentGame.isBugHouse())
            {
                currentGame.getOtherGame().otherGameEnded();
            }
		}

		return Common.RC_OK;
	}

	public static Game loadGame(String pgnString) {
		Game game = new Game();
		if (Common.RC_GENERAL_FAILURE == loadGame(game, pgnString)) {
			return null;
		}
		return game;
	}

	/**
	 * parses PGN style date (i.e 1962.??.??). if input is null or empty,
	 * returns parsing of current date
	 *
	 * @param dateStr
	 *            PGN style date to parse
	 * @return actual Date value
	 */
	public static Date parseDate(String dateStr) {

		if (Utils.isEmptyString(dateStr)) {
			return Utils.now();
		}

		// replace ?? with 01
		dateStr = dateStr.replaceAll("\\?+", "01");
		try {
			return PGN_DATE_FORMATTER.parse(dateStr);
		} catch (ParseException ex) {
			// no need to log
			// Utils.exception(ex);
		}
		return Utils.now();
	}

}
