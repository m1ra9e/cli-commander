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

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.converter.SimpleConverter;
import home.model.VehicleModel;

public final class InteractiveOperation implements IOperation {

    private static final Logger LOG = LoggerFactory.getLogger(InteractiveOperation.class);

    private static final String LS = System.lineSeparator();

    private static final String STOP = "STOP";
    private static final String ENTER_MESSAGE = "Enter value (to exit, enter %s) : "
            .formatted(STOP);

    private static final String USER_VALUES = "User values :" + LS + "%s";

    private final SimpleConverter simpleConverter = new SimpleConverter();

    @Override
    public void run(Object values) {
        try (var scanner = new Scanner(System.in)) {
            boolean isStopped = false;
            String valueFromUser = null;
            var sb = new StringBuilder();

            while (!isStopped) {
                LOG.info(ENTER_MESSAGE);
                valueFromUser = scanner.next().strip();
                isStopped = STOP.equalsIgnoreCase(valueFromUser);
                if (!isStopped) {
                    convertAndLog(valueFromUser, sb);
                }
            }
        }

        LOG.info(getClass().getSimpleName() + " stopped.");
    }

    private void convertAndLog(String textOfManyObjs, StringBuilder sb) {
        List<VehicleModel> vehicles = simpleConverter.convertStringToObjs(textOfManyObjs);
        vehicles.forEach(vehicle -> sb.append(vehicle).append(LS));
        LOG.info(USER_VALUES.formatted(sb.toString()));
    }
}
