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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class ArgsProcessorTest {

    @ParameterizedTest(name = "[{0}] : {3}")
    @CsvSource(delimiter = ';', value = {
            // testName.....|.args...........................|.description
            "display        ; -d car,truck,motorcycle,car    ; Valid display operation",
            "display_unique ; --display-unique car,truck,car ; Valid display-unique operation",
    })
    void validArgumentsTest(String testName, String args, String description)
            throws Exception {
        assertDoesNotThrow(() -> ArgsProcessor.execute(args.split(" ")));
    }

    @ParameterizedTest(name = "[{0}] : {3}")
    @CsvSource(delimiter = ';', value = {
            // testName.........|.args....................|.erroMsg....................................|.description
            "empty_arg          ; ' '                     ; Enter arguments                            ; Throws an error if the arguments are empty",
            "more_than_two_args ; -d truck -s car         ; Incorrect arguments count                  ; Throws an error if the number of arguments is greater than two",
            "unknown_operation  ; --unknown-operation car ; Unsupported operation: --unknown-operation ; Throws an error if unsupported operation",
    })
    void incorrectArgumentsTest(String testName, String args, String erroMsg,
            String description) throws Exception {
        try {
            ArgsProcessor.execute(args.split(" "));
            fail("Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.");
        } catch (IllegalArgumentException e) {
            String actual = e.getMessage().strip();
            assertTrue(actual.endsWith(erroMsg), """
                    Expected and actual error message does not match:
                    expected: %s
                    actual: %s
                    """.formatted(erroMsg, actual));
        }
    }
}
