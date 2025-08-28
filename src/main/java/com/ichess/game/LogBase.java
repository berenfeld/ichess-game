//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple logging base class for the game package
 */
public class LogBase {
    private final String CLASSNAME;

    public LogBase(String className) {
        CLASSNAME = className;
    }

    public void debug(Level level, String message) {
        Logger logger = Logger.getLogger(CLASSNAME);
        logger.log(level, message);
    }

    public void debug(String message) {
        debug(Utils.DEBUG, message);
    }

    public void info(String message) {
        debug(Utils.INFO, message);
    }

    public void warning(String message) {
        debug(Utils.WARNING, message);
    }

    public void error(String message) {
        debug(Utils.ERROR, message);
    }
}
