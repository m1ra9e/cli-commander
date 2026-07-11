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

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.db.Dao;

public final class SelectOperation implements IOperation {

    private static final Logger LOG = LoggerFactory.getLogger(SelectOperation.class);

    @Override
    public void run(Object values) {
        String soughtValuesQuery = values.toString().strip();
        Set<String> soughtValues = (soughtValuesQuery.isBlank())
                ? Collections.emptySet()
                : Arrays.stream(soughtValuesQuery.split(","))
                        .map(String::strip)
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.toUnmodifiableSet());

        Dao.getInstance().select(soughtValues);
        LOG.info("Data successfully selected from database");
    }
}
