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

import java.sql.SQLException;

import home.handler.DisplayHandler;
import home.handler.DisplayUniqueHandler;
import home.handler.IHandler;
import home.handler.ScannerHandler;

final class ArgsProcessor {

    static void execute(String[] args) throws SQLException {
        checkArgs(args);

        String operationName = args[0];
        Operation operation = Operation.getOperation(operationName);
        verifyOperation(operation, operationName);

        String values = args.length != 1 ? args[1] : null;

        IHandler handler = switch (operation) {
            case DISPLAY -> new DisplayHandler();
            case DISPLAY_UNIQUE -> new DisplayUniqueHandler();
            case SCANNER -> new ScannerHandler();
        };
        handler.run(values);
    }

    private static void checkArgs(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Enter arguments");
        } else if (args.length > 2) {
            throw new IllegalArgumentException("Incorrect arguments count");
        }
    }

    private static void verifyOperation(Operation operation, String operationName) {
        if (operation == null) {
            throw new IllegalArgumentException("Unsupported operation: " + operationName);
        }
    }

    private ArgsProcessor() {
    }
}
