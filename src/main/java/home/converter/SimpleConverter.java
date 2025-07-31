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
package home.converter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import home.model.VehicleModel;
import home.model.VehicleType;

public final class SimpleConverter {

    // regex for similar expressions: n3-4Um-8b--E43r
    private static final Pattern VALUE_PATTERN = Pattern.compile("^[a-zA-Z0-9-]+$");

    private static final String OBJECTS_DELIMITER = ",";
    private static final String VALUES_DELIMITER = "_";

    private static final int TYPE_VALUE_IDX = 0;
    private static final int COLOR_VALUE_IDX = 1;
    private static final int NUMBER_VALUE_IDX = 2;

    /**
     * Converts simple string (in format "type_color_number,type_color_number,...")
     * to list of data objects
     *
     * @param textOfManyObjs simple string containing data about the objects in the
     *                       format "type_color_number,type_color_number,..."
     * @return list of data objects
     */
    public List<VehicleModel> convertStringToObjs(String textOfManyObjs) {
        return Arrays.stream(textOfManyObjs.split(OBJECTS_DELIMITER))
                .map(textOfOneObj -> convertStringToObj(textOfOneObj.strip()))
                .toList();
    }

    /**
     * Converts simple string (in format "type_color_number") to data object
     *
     * @param data simple string containing data about the object in the format
     *             "type_color_number"
     * @return data object
     */
    private VehicleModel convertStringToObj(String textOfOneObj) {
        String[] values = textOfOneObj.split(VALUES_DELIMITER);
        var vehicle = new VehicleModel();

        for (int value_idx = 0; value_idx < values.length; value_idx++) {
            String value = values[value_idx];
            checkParamValue(value);
            switch (value_idx) {
                case TYPE_VALUE_IDX ->   vehicle.setType(VehicleType.getVehicleType(value));
                case COLOR_VALUE_IDX ->  vehicle.setColor(value);
                case NUMBER_VALUE_IDX -> vehicle.setNumber(value);
                default -> throw new IllegalArgumentException(
                        "Unexpected %d value : %s".formatted(value_idx + 1, value));
            }
        }

        return vehicle;
    }

    private void checkParamValue(String value) {
        if (!VALUE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("The value has an invalid format : " + value);
        }
    }
}
