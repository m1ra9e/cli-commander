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

final class MainTest {

    @ParameterizedTest(name = "[{0}] : {3}")
    @CsvSource(delimiter = ';', value = {
            // testName.....|.args...........................|.description
            "display        ; -d car,truck,motorcycle,car    ; Valid display operation",
            "display_unique ; --display-unique car,truck,car ; Valid display-unique operation",
            "empty_arg      ; ' '                            ; Valid empti arguments operation",
            "help           ; --help                         ; Valid help operation",
    })
    void validArgumentsTest(String testName, String args, String description)
            throws Exception {
        assertDoesNotThrow(() -> Main.executeApplication(args.split(" ")));
    }

    @ParameterizedTest(name = "[{0}] : {3}")
    @CsvSource(delimiter = ';', value = {
            // testName...........|.args....................|.erroMsg.........................................................................................|.description
            "more_than_one_params ; -d truck,car -u car     ; Incorrect parameters count                                                                      ; Throws an error if the number of parameters is greater than one",
            "unknown_operation    ; --unknown-operation car ; passed main parameter '--unknown-operation' but no main parameter was defined in your arg class ; Throws an error if unsupported operation",
    })
    void incorrectArgumentsTest(String testName, String args, String erroMsg,
            String description) throws Exception {
        try {
            Main.executeApplication(args.split(" "));
            fail("Expected java.lang.Exception to be thrown, but nothing was thrown.");
        } catch (Exception e) {
            String actual = e.getMessage().strip();
            assertTrue(actual.endsWith(erroMsg), """
                    Expected and actual error message does not match:
                    expected: %s
                    actual: %s
                    """.formatted(erroMsg, actual));
        }
    }
}
