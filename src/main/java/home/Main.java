/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2025 Lenar Shamsutdinov
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
 *******************************************************************************/
package home;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.cli.ArgsParser;
import home.cli.Options;
import home.utils.AppInfo;
import home.utils.ExecutionTime;

public final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            ExecutionTime.measure(Main::executeApplication, args);
            LOG.info("Application {} executed successfully.", AppInfo.getNameAndVersion());
        } catch (Exception e) {
            LOG.error("Application execution error", e);
        }
    }

    static void executeApplication(String[] args) {
        setUncaughtExceptionProcessing();

        Options options = ArgsParser.parse(args);
        OptionsProcessor.process(options);
    }

    private static void setUncaughtExceptionProcessing() {
        UncaughtExceptionHandler handler = (thread, throwable) -> {
            LOG.error("(!) Application execution error in %s".formatted(thread.getName()), throwable);
            System.exit(1);
        };

        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
}
