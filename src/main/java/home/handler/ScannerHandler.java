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
package home.handler;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ScannerHandler implements IHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ScannerHandler.class);

    private static final String DELIMITER = ",";

    private static final String STOP = "STOP";
    private static final String ENTER_MESSAGE = "Enter value (to exit, enter %s) : "
            .formatted(STOP);

    private static final String USER_VALUES = "User values : %s";

    @Override
    public void run(String values) {
        try (var scanner = new Scanner(System.in)) {
            boolean isStopped = false;
            String valueFromUser = null;
            var sb = new StringBuilder();

            if (values != null) {
                Arrays.stream(values.split(DELIMITER))
                        .map(String::strip)
                        .forEach(v -> sb.append(v).append(DELIMITER));
            }

            while (!isStopped) {
                LOG.info(ENTER_MESSAGE);
                valueFromUser = scanner.next().strip();
                isStopped = STOP.equalsIgnoreCase(valueFromUser);
                if (!isStopped) {
                    sb.append(valueFromUser).append(DELIMITER);
                    LOG.info(USER_VALUES.formatted(sb.toString()));
                }
            }
        }

        LOG.info(getClass().getSimpleName() + " stopped.");
    }
}
