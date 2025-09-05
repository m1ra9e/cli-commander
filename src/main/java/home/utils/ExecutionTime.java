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
package home.utils;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExecutionTime {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionTime.class);

    public static void measure(Consumer<String[]> method, String[] args) {
        long startTimeNs = System.nanoTime();
        method.accept(args);
        long endTimeNs = System.nanoTime();
        long executionTimeNs = endTimeNs - startTimeNs;

        long executionTimeMs = executionTimeNs / 1_000_000;
        if (executionTimeMs != 0) {
            logExecutionTime(executionTimeMs, "ms");
            return;
        }

        logExecutionTime(executionTimeNs, "ns");
    }

    private static void logExecutionTime(long executionTime, String unitOfMeasurement) {
        LOG.info("\nExecution time, {}: {}", unitOfMeasurement, separateThousands(executionTime));
    }

    private static String separateThousands(long number) {
        return "%,d".formatted(number).replace(',', ' ');
    }

    private ExecutionTime() {
    }
}
