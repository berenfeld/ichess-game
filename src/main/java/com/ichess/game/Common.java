//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game;

import static com.ichess.game.Log.Log;

/**
 * Common constants and methods for the chess game package
 * Extracted from IChessCommon for independence
 */
public class Common {

    // Color constants
    public static final int COLOR_START = 0;
    public static final int COLOR_WHITE = 1;
    public static final int COLOR_BLACK = 2;
    public static final int COLOR_ILLEGAL = 3;
    public static final int COLOR_NUM = 4;

    // Game kind constants
    public static final int GAME_KIND_REGULAR = 1;
    public static final int GAME_KIND_FISCHER = 2;
    public static final int GAME_KIND_SUICIDE = 3;
    public static final int GAME_KIND_GRASSHOPER = 4;
    public static final int GAME_KIND_MINICAPA = 5;
    public static final int GAME_KIND_FREEPLAY = 6;
    public static final int GAME_KIND_CRAZY_HOUSE = 7;
    public static final int GAME_KIND_SEVEN_BOOM = 8;
    public static final int GAME_KIND_MAX_VALUE_FOR_MIXED_TOURNMENTS = 8;
    public static final int GAME_KIND_BUG_HOUSE = 23;
    public static final int GAME_KIND_NUM = 24;

    // Game rules constants
    public static final int GAME_RULES_REGULAR = 1;
    public static final int GAME_RULES_FISCHER = 2;
    public static final int GAME_RULES_SUICIDE = 3;
    public static final int GAME_RULES_FREEPLAY = 4;
    public static final int GAME_RULES_CRAZY_HOUSE = 5;

    // Piece type constants
    public static final int PIECE_TYPE_START = 0;
    public static final int PIECE_TYPE_PAWN = 1;
    public static final int PIECE_TYPE_KNIGHT = 2;
    public static final int PIECE_TYPE_BISHOP = 3;
    public static final int PIECE_TYPE_ROOK = 4;
    public static final int PIECE_TYPE_QUEEN = 5;
    public static final int PIECE_TYPE_KING = 6;
    public static final int PIECE_TYPE_ILLEGAL = 7;
    public static final int PIECE_TYPE_NUM = 8;
    public static final int PIECE_TYPE_GRASSHOPER = 9;
    public static final int PIECE_TYPE_ARCHBISHOP = 10;
    public static final int PIECE_TYPE_CHANCELLOR = 11;
    public static final int PIECE_TYPE_DROP_ANY = 12;

    // Return code constants
    public static final int RC_OK = 0;
    public static final int RC_GENERAL_FAILURE = 9999;

    public static final int CASTLE_START = 0;
    public static final int CASTLE_KING = 1;
    public static final int CASTLE_QUEEN = 2;
    public static final int CASTLE_NUM = 3;

    public static final String STD_INIT_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    // Text arrays
    public static final String[] GAME_KIND_TEXT = { "מעורב", "רגיל", "פישר 960", "אנטי שח", "שח חרגולים", "מיני קפבלנקה",
            "חופשי", "שח השתלות", "7-בום", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "שח שוודי" };

    public static final String[] GAME_RULES_TEXT = { "", "רגיל", "פישר 960", "אנטי שח", "חופשי", "שח השתלות"};

    public static final String[] PIECE_TYPE_TEXT = { "", "חייל", "פרש", "רץ", "צריח", "מלכה", "מלך", "לא חוקי", "", "", "ארכיבישוף", "קנצלר", "טיפת כלי" };

    // Site name
    public static final String ICHESS_SITENAME = "אתר השחמט הישראלי - ichess";

    /**
     * Get color as string
     */
    public static String GetColor(int color) {
        switch (color) {
        case COLOR_WHITE:
            return "white";
        case COLOR_BLACK:
            return "black";
        case COLOR_ILLEGAL:
            return "random";
        default:
            return "unknown";
        }
    }

    /**
     * Get color as Hebrew string
     */
    public static String GetColorHeb(int color) {
        switch (color) {
        case COLOR_WHITE:
            return "לבן";
        case COLOR_BLACK:
            return "שחור";
        case COLOR_ILLEGAL:
            return "אקראי";
        default:
            return "לא ידוע";
        }
    }

    /**
     * Check if color is black
     */
    public static boolean isBlack(int color) {
        return color == COLOR_BLACK;
    }

    /**
     * Check if color is white or black
     */
    public static boolean isBlackOrWhite(int color) {
        return isWhite(color) || isBlack(color);
    }

    /**
     * Check if color is white
     */
    public static boolean isWhite(int color) {
        return color == COLOR_WHITE;
    }

    /**
     * Get the opposite color
     */
    public static int OtherColor(int col) {
        switch (col) {
        case COLOR_WHITE:
            return COLOR_BLACK;
        case COLOR_BLACK:
            return COLOR_WHITE;
        }
        Log.warning("illegal color " + col);
        return col;
    }
}
