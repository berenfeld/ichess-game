//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game.tests;

import com.ichess.game.*;
import com.ichess.game.piece.Piece;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ichess.game.Log.Log;
import org.junit.Test;

/**
* Unit tests for the chess game package
* Extracted from GameTestLib.java - contains only non-DB related tests
*/
public class GameTest {

    @Test
    public void testGameCreation() {
        Log.debug("testGameCreation");
        Game game = new Game();
        assertNotNull(game);
    }

    @Test
    public void testGameWithMoves() {
        Log.debug("testGameWithMoves");
        Game game = new Game();
        game.playMove("e2e4");
        game.playMove("e7e5");
        assertEquals(2, game.getCurrentMove());
    }

    @Test
    public void testGameNotation() {
        Log.debug("testGameNotation");
        Game game = new Game();
        game.playMove("e2e4");
        game.playMove("e7e5");
        String notation = game.getMoveListAlg();
        assertTrue(notation.contains("e4"));
        assertTrue(notation.contains("e5"));
    }

    @Test
    public void test3FoldRepeatitionChecksEnPassent() {
        Game game = new Game();
        game.setAttribute(Game.CHECK_REPEATITION_DRAW, true);
        assertTrue((Boolean) game.getAttribute(Game.CHECK_REPEATITION_DRAW));
        // f5 pawn here make EP option for white, so the opening position repeated 3 times is not a draw
        // (on the first time there was EP option)
        assertTrue(game.playMoveList("1.e4 a6 2.e5 f5 3.Nf3 Nc6 4.Ng1 Nb8 5.Nf3 Nc6 6.Ng1 Nb8"));
        assertFalse(game.isEnded());
        // and now we will draw it by getting back to the opening position using a different method
        assertTrue(game.playMoveList("Nc3 Nf6 Nb1 Ng8"));
        assertTrue(game.isEnded());
        // make sure all the moves were played
        assertEquals(16, game.getCurrentMove());
        assertEquals(Common.COLOR_ILLEGAL, game.getWinner());
    }

    @Test
    public void test3FoldRepeatitionChecksAllPositionIncludeEnPassentAndColor() {
        Game game = FEN.loadGame("8/8/7p/p6p/p6p/K6p/p6p/rkr4Q w - - 0 1");
        game.setAttribute(Game.CHECK_REPEATITION_DRAW, true);
        assertTrue((Boolean) game.getAttribute(Game.CHECK_REPEATITION_DRAW));
        assertNotNull(game);
        assertTrue(game.playMoveList("1. Qe4+ Rc2 2. Qe1+ Rc1 3. Qd2 Rc2 4. Qd1+ Rc1 5. Qd3+ Rc2 6. Qe4 h1=Q 7." +
        "Qxh1+ Rc1 8. Qe4+ Rc2 9. Qe1+ Rc1 10. Qd2 Rc2 11. Qd1+ Rc1 12. Qd3+ Rc2 13. Qe4 h2 " +
        "14. Qh1+ Rc1 15. Qe4+ Rc2 16. Qe1+ Rc1 17. Qd2 Rc2 18. Qd1+ Rc1 19. Qd3+ Rc2 20. Qe4 *"));
        assertFalse(game.isEnded());
    }

    private void assertGameEquals(Game game1, Game game2) {
        assertEquals(game1.getGameKind(), game2.getGameKind());
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                Piece piece1 = game1.getPieceAt(x, y);
                Piece piece2 = game2.getPieceAt(x, y);
                if (piece1 == null) {
                    assertNull(piece2);
                    continue;
                }
                assertEquals(piece1.getType(), piece2.getType());
                assertEquals(piece1.getColor(), piece2.getColor());
                if (game1.isCrazyOrBugHouse()) {
                    assertEquals(piece1.isPromoted(), piece2.isPromoted());
                }
            }
        }
        assertEquals(game1.getCurrentColor(), game2.getCurrentColor());
    }

    private void verifyPGN(String pgn) {
        Game game = PGN.loadGame(pgn);
        assertNotNull("failed to parse PGN '" + pgn + "'", game);

        boolean bpgn = pgn.contains("1A.");

        if (!bpgn) {
            // verify moves count
            Pattern moveNumberPattern = Pattern.compile("\\s+(\\d+)\\.");
            Matcher m = moveNumberPattern.matcher(pgn);
            int moveNumber = 0;
            while (m.find()) {
                moveNumber = Utils.parseInt(m.group(1));
            }
            // verify game number of moves
            if (game.getCurrentColor() == Common.COLOR_WHITE) {
                assertEquals((moveNumber * 2), game.getCurrentMove());
            } else {
                assertEquals((moveNumber * 2) - 1, game.getCurrentMove());
            }
        }

        String gamePGN = PGN.getPGNString(game);
        Log.debug("Got PGN  : '" + gamePGN + "'");
        // tests PGNs are the same.
        // remove [] tags
        pgn = pgn.replaceAll("\\[.*\\]\n", "");
        gamePGN = gamePGN.replaceAll("\\[.*\\]\n", "");
        // remove comments
        pgn = pgn.replaceAll("\\{.*\\}", "");
        gamePGN = gamePGN.replaceAll("\\{.*\\}", "");
        // unify spaces
        pgn = pgn.replaceAll("\\s+", " ");
        gamePGN = gamePGN.replaceAll("\\s+", " ");
        // remove non ascii (UTF8 controls) control characters
        pgn = pgn.replaceAll("\\P{Cc}", "");
        gamePGN = gamePGN.replaceAll("\\P{Cc}", "");

        assertEquals(pgn, gamePGN);

        String fen = FEN.getFENString(game);
        Log.debug("FEN String '" + fen + "'");
        Game fromFEN = FEN.loadGame(fen, true, game.getGameKind());
        // TODO add equals to Game object
        // assertEquals(fromFEN, game);
        if (game.isCrazyHouse()) {
            // XFEN encoder captured, not dropped, and lose the type when dropping...
            if (FEN.ENCODE_CAPTURED_AS_XFEN) {
                // XFEN does not encode "was promoted"
                for (Piece piece : game.getCapturedPieces(Common.COLOR_WHITE)) {
                    piece.clearPromoted();
                }
                for (Piece piece : game.getCapturedPieces(Common.COLOR_BLACK)) {
                    piece.clearPromoted();
                }
                assertEquals(game.getCapturedPieces(Common.COLOR_WHITE), fromFEN.getCapturedPieces(Common.COLOR_WHITE));
                assertEquals(game.getCapturedPieces(Common.COLOR_BLACK), fromFEN.getCapturedPieces(Common.COLOR_BLACK));
            } else {
                assertEquals(game.getActualDroppablePieceTypes(Common.COLOR_WHITE), fromFEN.getActualDroppablePieceTypes(Common.COLOR_WHITE));
                assertEquals(game.getActualDroppablePieceTypes(Common.COLOR_BLACK), fromFEN.getActualDroppablePieceTypes(Common.COLOR_BLACK));

                List<Integer> droppableTypes1 = new ArrayList<Integer>();
                List<Integer> droppableTypes2 = new ArrayList<Integer>();
                for (Piece piece : fromFEN.getDroppablePieces(Common.COLOR_WHITE)) {
                    droppableTypes1.add(piece.getTypeWhenDropping());
                }
                for (Piece piece : game.getDroppablePieces(Common.COLOR_WHITE)) {
                    droppableTypes2.add(piece.getTypeWhenDropping());
                }
                assertEquals(droppableTypes1, droppableTypes2);
                droppableTypes1.clear();
                droppableTypes2.clear();
                for (Piece piece : fromFEN.getDroppablePieces(Common.COLOR_BLACK)) {
                    droppableTypes1.add(piece.getTypeWhenDropping());
                }
                for (Piece piece : game.getDroppablePieces(Common.COLOR_BLACK)) {
                    droppableTypes2.add(piece.getTypeWhenDropping());
                }
                assertEquals(droppableTypes1, droppableTypes2);
            }
        } else if (game.isBugHouse()) {
            Game otherGame = game.getOtherGame();
            if (otherGame != null) {
                String otherFEN = FEN.getFENString(otherGame);
                Log.debug("other FEN String '" + otherFEN + "'");
                Game fromOtherFEN = FEN.loadGame(otherFEN, true, otherGame.getGameKind());
                assertEquals(fromOtherFEN, otherGame);
            }
        } else {
            assertTrue(fromFEN.getCapturedPieces(Common.COLOR_WHITE).isEmpty());
            assertTrue(fromFEN.getCapturedPieces(Common.COLOR_BLACK).isEmpty());
            assertEquals(FEN.getFENString(fromFEN), fen);
        }

        Log.debug("taking back all moves");

        Game otherGame = game.getOtherGame();
        if ((!game.isBugHouse()) || (otherGame == null)) {
            game.takebackAllMoves();
            assertTrue(game.getCapturedPiecesWhite().isEmpty());
            assertTrue(game.getCapturedPiecesBlack().isEmpty());
            if (game.isCrazyHouse()) {
                assertTrue(game.getDroppablePieces(Common.COLOR_WHITE).isEmpty());
                assertTrue(game.getDroppablePieces(Common.COLOR_BLACK).isEmpty());
            }
        } else {
            // take back all moves in both games - carefully
            while (game.canTakeback() || otherGame.canTakeback()) {
                if (game.canTakeback()) {
                    game.takeback();
                }
                if (otherGame.canTakeback()) {
                    otherGame.takeback();
                }
            }

            Log.debug("done taking back bughouse moves");
            assertTrue(game.getCapturedPiecesWhite().isEmpty());
            assertTrue(game.getCapturedPiecesBlack().isEmpty());
            assertTrue(game.getDroppablePieces(Common.COLOR_WHITE).isEmpty());
            assertTrue(game.getDroppablePieces(Common.COLOR_BLACK).isEmpty());
            assertTrue(otherGame.getCapturedPiecesWhite().isEmpty());
            assertTrue(otherGame.getCapturedPiecesBlack().isEmpty());
            assertTrue(otherGame.getDroppablePieces(Common.COLOR_WHITE).isEmpty());
            assertTrue(otherGame.getDroppablePieces(Common.COLOR_BLACK).isEmpty());
        }
    }

    @Test
    public void testSinglePGN() {
        String pgn =
        "[Event \"Leipzig olm prel\"]\n" +
        "[Site \"Leipzig\"]\n" +
        "[Date \"1960.??.??\"]\n" +
        "[Round \"2\"]\n" +
        "[White \"Fuchs, Reinhart\"]\n" +
        "[Black \"Veizaj\"]\n" +
        "[Result \"1-0\"]\n" +
        "[WhiteElo \"\"]\n" +
        "[BlackElo \"\"]\n" +
        "[ECO \"A42\"]\n" +
        "\n" +
        "1.d4 g6 2.c4 Bg7 3.Nc3 d6 4.e4 Nc6 5.Be3 Nf6 6.Nf3 Bg4 7.Be2 e5 8.d5 Bxf3\n" +
        "9.Bxf3 Nd4 10.Bxd4 exd4 11.Qxd4 O-O 12.Qd2 Nd7 13.O-O Qh4 14.Rae1 a6 15.Bd1 b5\n" +
        "16.cxb5 axb5 17.b4 Rfb8 18.Bb3 Ne5 19.f4 Nc4 20.Qd3 Qf6 21.Nxb5 Rxb5 22.Qxc4 Qd4+\n" +
        "23.Qxd4 Bxd4+ 24.Kh1 f6 25.Rc1 Rxb4 26.Rxc7 Re8 27.Re1 Bf2 28.Re2 Rbxe4 29.Rxe4 Rxe4\n" +
        "30.g3 Re2 31.Rc2 Re1+ 32.Kg2 Bc5 33.Kf3 Re3+ 34.Kg2 Kf7 35.Bc4 Ra3 36.Re2 Re3\n" +
        "37.Rxe3 Bxe3 38.Kf3 Bc5 39.a4 Bg1 40.Bd3 Bc5 41.a5 h6 42.a6 g5 43.Kg4 Be3\n" +
        "44.Kf5 gxf4 45.gxf4 h5 46.h4  1-0‬";
        verifyPGN(pgn);
    }

    @Test
    public void test_canCastleEvenIfPawnThreadOnKingWayBug() {
        String fen = "r1b1k2r/ppp1Pppp/8/3q4/2pP4/2Qn1N2/PP2pPPP/R5KR b kq - 0 1";
        Game game = FEN.loadGame(fen, true);
        assertNotNull(game);
        assertEquals(game.getCurrentColor(), Common.COLOR_BLACK);
        assertEquals(Common.RC_GENERAL_FAILURE, game.playMove("0-0"));
        assertEquals(game.getCurrentColor(), Common.COLOR_BLACK);
    }

    @Test
    public void testStangeCheckNotationInViewboard() {
        Game game = FEN.loadGame("1R1K4/k1n5/1p6/1P6/8/8/8/8 w - - 0 1");
        game.playMoveList("1. Ra8+ Nxa8 2. Kc8 Nc7 3. Kxc7 Ka8 4. Kxb6 Kb8 5. Ka6 Ka8 6. b6 Kb8");
        assertEquals(game.getMoveListAlg(), "Ra8+ Nxa8 Kc8 Nc7 Kxc7 Ka8 Kxb6 Kb8 Ka6 Ka8 b6 Kb8");
    }

    @Test
    public void testCrazyHouseBug_canMoveIntoCheck() {
        Game game = new Game(Common.GAME_KIND_CRAZY_HOUSE);
        game.playMoveList("e2e4  b8c6  d2d4  d7d5  e4e5  c8f5  f1b5  e7e6  b5c6  b7c6  b1c3  b4b4b c1d2  b4c3  d2c3  f5c2  d1c2  b4b4n c3b4  f8b4  c3c3b e4e4b d3d3b e4d3  c2d3  e4e4b d3g3  b4c3  b2c3  b2b2p a1d1  d2d2b e1d2  b2b1q d1b1  e4b1  g3g7  c2c2r d2d1  d8e7  c5c5b e7c5  d4c5  h5h5b f3f3p h5f3  g2f3  c2c1  d1c1  d2d2p c1d2  g8f6  g7h8  e8e7  h8f6  e7d7  e7e7q d7c8  a6a6b");
        List<Move> nextMoves = game.getValidNextMoves();
        assertEquals(nextMoves.size(), 1);
        assertEquals(nextMoves.get(0).getNameAlg(), "Kb8");
    }

    @Test
    public void test_Fischer960Bug_CastelingIntoMate() {
        Game game = FEN.loadGame("nbrqbkrn/pppppppp/8/8/8/8/PPPPPPPP/NBRQBKRN w CGcg - 0 1", true, Common.GAME_KIND_FISCHER );
        assertNotNull(game);
        assertTrue(game.playMoveList("c4 d5 Bxh7"));
        assertNotSame(Common.RC_OK, game.playMove("0-0"));
        assertNotSame(Common.RC_OK, game.playMove("O-O"));
    }

    @Test
    public void testCrayHouseFEN_EditboardBug() {
        String fen = "rk1B4/pbp3pp/2p1R3/2Bp4/3P4/2PB1N2/P1PK1PPP/q4R2[NPNPQRNq] w";
        Game game = FEN.loadGame(fen, true, 0);
        assertNotNull(game);
        String fromGame = FEN.getFENString(game);
        assertEquals(fromGame, "rk1B4/pbp3pp/2p1R3/2Bp4/3P4/2PB1N2/P1PK1PPP/q4R2[NPNPQRNq] w - - 0 1");
    }

    @Test
    public void testCrayHouseFEN_EditboardBug2() {
        String fen = "rk1B4/pbp3pp/2p1R3/2Bp4/3P4/2PB1N2/P1PK1PPP/q4R2/Qnpnpqrn w";
        Game game = FEN.loadGame(fen, true, 0);
        assertNotNull(game);
        String fromGame = FEN.getFENString(game);
        assertEquals(fromGame, "rk1B4/pbp3pp/2p1R3/2Bp4/3P4/2PB1N2/P1PK1PPP/q4R2[NPNPQRNq] w - - 0 1");
    }

    @Test
    public void testBugHouse() {
        Game game1 = new Game(Common.GAME_KIND_BUG_HOUSE);
        Game game2 = new Game(Common.GAME_KIND_BUG_HOUSE);

        game1.setOtherGame(game2);
        game2.setOtherGame(game1);

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());

        assertTrue(game1.playMoveList("e4 d5 exd5 Qxd5"));
        assertTrue(game2.playMoveList("e4 e5 P@d4 P@d5"));

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());

        game2.takebackAllMoves();
        game1.takebackAllMoves();

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());

        assertTrue(game2.playMoveList("e4 d5 exd5 Qxd5"));
        assertTrue(game1.playMoveList("e4 e5 P@d4 P@d5"));

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());

        game1.takebackAllMoves();
        game2.takebackAllMoves();

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());
    }

    @Test
    public void testBugHouse2() {
        Game game1 = new Game(Common.GAME_KIND_BUG_HOUSE);
        Game game2 = new Game(Common.GAME_KIND_BUG_HOUSE);

        game1.setOtherGame(game2);
        game2.setOtherGame(game1);

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());

        assertTrue(game1.playMoveList("e4 d5 exd5"));
        assertTrue(game2.playMoveList("e4 P@d5 exd5"));
        assertTrue(game1.playMoveList("P@e4"));

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());

        game1.takeback();
        game2.takebackAllMoves();
        game1.takebackAllMoves();

        assertFalse(game1.hasCapturedPieces());
        assertFalse(game2.hasCapturedPieces());
    }

    @Test
    public void test_CrazyHouse_CustomFEN_PGN() {
        String pgn = "[Date \"2014.09.26\"]\n" +
        "[Result \"*\"]\n" +
        "[Variant \"CrazyHouse\"]\n" +
        "[SetUp \"1\"]\n" +
        "[FEN \"rn1q3r/ppp1kPpp/3bP3/3n4/8/8/PPPP1PPP/RNB1KB1R[PNBRQpnbrq] b - - 0 11\"]\n" +
        "\n" +
        "1... Nc6 Q@g8 Rxg8 4. fxg8=Q Qxg8 5. Nc3 5. P@e4 N@g4 6. Q@f3 *";
        Game game = PGN.loadGame(pgn);
        assertNotNull(game);
    }

    @Test
    public void test_CrazyHouse_CheckMate_CantDropPawnOnFirstLevel() {
        Game game = new Game(Common.GAME_KIND_CRAZY_HOUSE);
        String movelist = "1. e4 b6 2. Nf3 Bb7 3. Nc3 e6 4. Bc4 Bb4 5. O-O Bxc3 6. bxc3 Bxe4 7. Ba3 Bxf3 8. Qxf3 d5 9. Bxd5 exd5 10. Rfe1+ P@e7 11. B@b7 B@e4 12. Rxe4 dxe4 13. Qxe4 R@e6 14. Qxe6 fxe6 15. R@c8 Qxc8 16. Bxc8 Nd7 17. Bxd7+ Kf8 18. Q@f3+ Nf6 19. Qxa8+ R@e8 20. Bxe8 N@e2+ 21. Kh1 Nxe8 22. Qf3+ N@f4 23. N@d7+ Kf7 24. Ne5+ Kg8 25. P@f7+ Kf8 26. fxe8=Q+ Kxe8 27. R@a8+ Q@b8 28. Rxb8+ B@c8";
        assertTrue(game.playMoveList(movelist));
        assertTrue(game.playMoveList("29. Rxc8+"));
        assertTrue(game.isCheck());
        assertTrue(game.isCheckMate());
        assertTrue(game.isEnded());
    }

    @Test
    public void test_CrazyHouse_BugWithDroppingPromotedPawn() {
        String fen = "rn1q3r/ppp1kPpp/3bP3/3n4/8/8/PPPP1PPP/RNB1KB1R[PNBRQQpnbrqq] w - - 0 11";
        Game game = FEN.loadGame(fen, 0);
        assertNotNull(game);
        assertTrue(game.isCrazyHouse());
        game.playMoveList("g8g8q h8g8  f7g8q d8g8  b1c3  e4e4p g4g4n f3f3p");
        assertEquals(game.getMoveListAlg(), "Q@g8 Rxg8 fxg8=Q Qxg8 Nc3 P@e4 N@g4 P@f3");
    }

    @Test
    public void test_CrazyHouse_PGN_Bug() {
        String pgn = "[Site \"אתר השחמט הישראלי ichess\"]\n" +
        "[Date \"2014.09.27\"]\n" +
        "[Result \"*\"]\n" +
        "[Variant \"Crazyhouse\"]\n" +
        "[SetUp \"1\"]\n" +
        "[FEN \"rnbq~kbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQ~KBNR w KQkq - 0 1\"]\n" +
        "\n" +
        "*";
        Game game = PGN.loadGame(pgn);
        assertNotNull(game);
        assertTrue(game.isCrazyHouse());
        Piece queen = game.findPiece(Common.PIECE_TYPE_QUEEN, Common.COLOR_WHITE);
        assertTrue(queen.isPromoted());
    }
}
