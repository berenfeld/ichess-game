//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//==============================================================================

package com.ichess.game.tests;

import com.ichess.game.Common;
import com.ichess.game.Game;
import com.ichess.game.PGN;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Annotated classic PGNs (comments in {}, numeric castling 0-0) must load for
 * editboard import and the classic-games poller.
 */
public class PGNAnnotatedTest {

    private static String readResource(String name) {
        InputStream in = PGNAnnotatedTest.class.getResourceAsStream("/pgn/" + name);
        assertNotNull("missing test resource pgn/" + name, in);
        try (Scanner s = new Scanner(in, StandardCharsets.UTF_8.name())) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    @Test
    public void loadImmortalGameWithBraceComments() {
        String pgn = readResource("immortal_anderssen_kieseritzky.pgn");
        assertTrue(pgn.contains("{"));
        assertTrue(pgn.contains("Immortal"));

        Game game = PGN.loadGame(pgn);
        assertNotNull("annotated Immortal Game PGN must parse", game);
        assertEquals("Anderssen", game.getWhiteName());
        assertEquals("Kieseritzky", game.getBlackName());
        assertEquals(45, game.getCurrentMove());
        assertEquals(Common.COLOR_WHITE, game.getWinner());

        List<String> comments = game.getComments();
        assertEquals(45, comments.size());
        int nonEmpty = 0;
        for (String c : comments) {
            if (c != null && !c.trim().isEmpty()) {
                nonEmpty++;
            }
        }
        assertTrue("expected brace comments attached to moves, got " + nonEmpty, nonEmpty >= 10);
        assertTrue(comments.get(0).contains("Immortal") || comments.get(0).contains("Anderssen"));
    }

    @Test
    public void loadNumericZeroCastling() {
        String pgn = readResource("castling_zeros.pgn");
        assertTrue(pgn.contains("0-0"));

        Game game = PGN.loadGame(pgn);
        assertNotNull("PGN with 0-0 castling must parse (was: עמדה לא חוקית)", game);
        assertTrue(game.getCurrentMove() >= 14);
        String alg = game.getMoveListAlg();
        assertTrue("expected kingside castling in movelist: " + alg, alg.contains("O-O"));
    }

    @Test
    public void normalizeCastlingToken() {
        assertEquals("O-O", PGN.normalizeCastlingToken("0-0"));
        assertEquals("O-O-O", PGN.normalizeCastlingToken("0-0-0"));
        assertEquals("O-O+", PGN.normalizeCastlingToken("0-0+"));
        assertEquals("O-O-O!", PGN.normalizeCastlingToken("0-0-0!"));
        assertNull(PGN.normalizeCastlingToken("e4"));
        assertNull(PGN.normalizeCastlingToken("10."));
    }
}
