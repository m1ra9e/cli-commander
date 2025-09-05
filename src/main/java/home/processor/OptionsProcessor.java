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
package home.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import home.cli.Options;
import home.operation.DisplayOperation;
import home.operation.DisplayUniqueOperation;
import home.operation.DisplayStringOperation;
import home.operation.IOperation;
import home.operation.InteractiveOperation;

public final class OptionsProcessor {

    private enum OperationType {
        DISPLAY, DISPLAY_UNIQUE, HELP, INTERACTIVE_MODE, VERSION;
    }

    public static void process(Options options) {
        Entry<OperationType, Object> operationAndValue = checkAndGetOperationData(options);
        OperationType operationType = operationAndValue.getKey();
        Object value = operationAndValue.getValue();

        IOperation operation = switch (operationType) {
            case DISPLAY ->          new DisplayOperation();
            case DISPLAY_UNIQUE ->   new DisplayUniqueOperation();
            case INTERACTIVE_MODE -> new InteractiveOperation();
            case HELP, VERSION ->    new DisplayStringOperation();
        };

        operation.run(value);
    }

    private static Entry<OperationType, Object> checkAndGetOperationData(Options options) {
        var operationAndValueMap = new HashMap<OperationType, Object>();

        putToMapIfExists(OperationType.DISPLAY, options.getDataForDisplay(),
                operationAndValueMap, () -> options.getDataForDisplay() != null);
        putToMapIfExists(OperationType.DISPLAY_UNIQUE, options.getDataForDisplayUnique(),
                operationAndValueMap, () -> options.getDataForDisplayUnique() != null);
        putToMapIfExists(OperationType.HELP, options.getOptionsDescriptions(),
                operationAndValueMap, () -> options.isHelp());
        putToMapIfExists(OperationType.INTERACTIVE_MODE, options.isInteractiveMode(),
                operationAndValueMap, () -> options.isInteractiveMode());
        putToMapIfExists(OperationType.VERSION, options.getVersionInfo(),
                operationAndValueMap, () -> options.isVersion());

        checkOperations(operationAndValueMap.keySet());

        return operationAndValueMap.entrySet().iterator().next();
    }

    private static void putToMapIfExists(OperationType operationType, Object value,
            Map<OperationType, Object> operationAndValue, Supplier<Boolean> hasOption) {
        if (hasOption.get()) {
            operationAndValue.put(operationType, value);
        }
    }

    private static void checkOperations(Set<OperationType> operationTypes) {
        if (operationTypes.size() != 1) {
            throw new IllegalArgumentException("Incorrect parameters count");
        }
    }

    private OptionsProcessor() {
    }
}
