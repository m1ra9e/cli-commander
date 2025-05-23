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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import home.cli.Parameters;
import home.operation.DisplayOperation;
import home.operation.DisplayUniqueOperation;
import home.operation.HelpOperation;
import home.operation.IOperation;
import home.operation.InteractiveOperation;

final class ParamsProcessor {

    private enum OperationType {
        DISPLAY, DISPLAY_UNIQUE, HELP, INTERACTIVE_MODE;
    }

    static void process(Parameters params) {
        Entry<OperationType, Object> operationAndValue = checkAndGetOperationData(params);
        OperationType operationType = operationAndValue.getKey();
        Object value = operationAndValue.getValue();

        IOperation operation = switch (operationType) {
            case DISPLAY -> new DisplayOperation();
            case DISPLAY_UNIQUE -> new DisplayUniqueOperation();
            case HELP -> new HelpOperation();
            case INTERACTIVE_MODE -> new InteractiveOperation();
        };

        operation.run(value);
    }

    private static Entry<OperationType, Object> checkAndGetOperationData(Parameters params) {
        var operationAndValue = new HashMap<OperationType, Object>();

        addIfExists(OperationType.DISPLAY, params.getDataForDisplay(),
                operationAndValue, () -> params.getDataForDisplay() != null);
        addIfExists(OperationType.DISPLAY_UNIQUE, params.getDataForDisplayUnique(),
                operationAndValue, () -> params.getDataForDisplayUnique() != null);
        addIfExists(OperationType.INTERACTIVE_MODE, params.isInteractiveMode(),
                operationAndValue, () -> params.isInteractiveMode());
        addIfExists(OperationType.HELP, params.getParamsInfo(),
                operationAndValue, () -> params.isHelp());

        checkOperationType(operationAndValue.keySet());

        return operationAndValue.entrySet().iterator().next();
    }

    private static void addIfExists(OperationType operationType, Object value,
            Map<OperationType, Object> operationAndValue, Supplier<Boolean> hasParam) {
        if (hasParam.get()) {
            operationAndValue.put(operationType, value);
        }
    }

    private static void checkOperationType(Set<OperationType> operationTypes) {
        if (operationTypes.size() != 1) {
            throw new IllegalArgumentException("Incorrect parameters count");
        }
    }

    private ParamsProcessor() {
    }
}
