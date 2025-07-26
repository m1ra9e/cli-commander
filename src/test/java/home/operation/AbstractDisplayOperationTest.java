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
package home.operation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

abstract sealed class AbstractDisplayOperationTest permits DisplayOperationTest, DisplayUniqueOperationTest {

    @Test
    void runTest() {
        Object textOfManyObjs = "car_red_c234ek,"
                + "TRUCK_white_t534kh,"
                + "TRUCK_wHiTe_t534kh,"
                + "moTORcycle_white-black_p879ds,"
                + "cAr_red_c234ek";

        assertDoesNotThrow(() -> getOperation().run(textOfManyObjs));
    }

    @Test
    void runWithWrongObjTypeTest() {
        Object textOfManyObjs = "car_red_c234ek,"
                + "truck_white_t534kh,"
                + "bicycle_blue_y542po";

        assertThrows(IllegalArgumentException.class,
                () -> getOperation().run(textOfManyObjs));
    }

    @Test
    void runWithUnexpectedValueCountTest() {
        Object textOfManyObjs = "car_red_c234ek_some-other-param";

        assertThrows(IllegalArgumentException.class,
                () -> getOperation().run(textOfManyObjs));
    }

    protected abstract IOperation getOperation();
}
