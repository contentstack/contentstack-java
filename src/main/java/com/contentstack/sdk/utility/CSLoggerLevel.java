package com.contentstack.sdk.utility;

import org.apache.log4j.Level;

/**
 * MIT License
 *
 * Copyright (c) 2012 - 2019 Contentstack
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */



@SuppressWarnings("serial")
public class CSLoggerLevel extends Level {

    /**
     * Value of CSLoggerLevel level. This value is lesser than DEBUG_INT and higher
     * than TRACE_INT}
     */
    public static final int CONTENTSTACK_INT = DEBUG_INT - 10;

    /**
     * Level representing my log level
     */
    public static final Level CONTENTSTACK = new CSLoggerLevel(CONTENTSTACK_INT, "CONTENTSTACK", 10);

    /**
     * Constructor
     */
    protected CSLoggerLevel(int arg0, String arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    /**
     * Checks whether logArgument is "CONTENTSTACK" level. If yes then returns
     * CONTENTSTACK}, else calls CSLoggerLevel#toLevel(String, Level) passing
     * it Level#DEBUG as the defaultLevel.
     */
    public static Level toLevel(String logArgument) {
        if (logArgument != null && logArgument.toUpperCase().equals("CONTENTSTACK")) {
            return CONTENTSTACK;
        }
        return (Level) toLevel(logArgument, Level.DEBUG);
    }

    /**
     * Checks whether val is CSLoggerLevel#CONTENTSTACK_INT. If yes then
     * returns CSLoggerLevel#CONTENTSTACK, else calls
     * CSLoggerLevel#toLevel(int, Level) passing it Level#DEBUG as the
     * defaultLevel
     *
     */
    public static Level toLevel(int val) {
        if (val == CONTENTSTACK_INT) {
            return CONTENTSTACK;
        }
        return (Level) toLevel(val, Level.DEBUG);
    }


    /**
     * Checks whether val is CSLoggerLevel#CONTENTSTACK_INT. If yes
     * then returns CSLoggerLevel#CONTENTSTACK, else calls Level#toLevel(int, org.apache.log4j.Level)
     *
     */
    public static Level toLevel(int val, Level defaultLevel) {
        if (val == CONTENTSTACK_INT) {
            return CONTENTSTACK;
        }
        return Level.toLevel(val, defaultLevel);
    }

    /**
     * Checks whether logArgument is "CONTENTSTACK" level. If yes then returns
     * CSLoggerLevel#CONTENTSTACK, else calls
     * Level#toLevel(java.lang.String, org.apache.log4j.Level)
     *
     */
    public static Level toLevel(String logArgument, Level defaultLevel) {
        if (logArgument != null && logArgument.toUpperCase().equals("CONTENTSTACK")) {
            return CONTENTSTACK;
        }
        return Level.toLevel(logArgument, defaultLevel);
    }
}