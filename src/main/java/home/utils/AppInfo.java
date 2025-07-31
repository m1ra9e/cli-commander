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

import java.util.function.BiFunction;

public enum AppInfo {

    INSTANCE;

    private static final String DEFAULT_APP_NAME = "=VEHICLE_ACCOUNTING_CLI=";
    private static final String DEFAULT_APP_VERSION = "UNKNOWN";

    private static final String NAME_AND_VERSION_TEMPLATE = "%s v%s";

    private final String nameAndVersion;

    private AppInfo() {
        BiFunction<String, String, String> getSafeVal = (val, def) -> val != null ? val : def;
        Package pkg = getClass().getPackage();

        String name = getSafeVal.apply(pkg.getImplementationTitle(), DEFAULT_APP_NAME);
        String version = getSafeVal.apply(pkg.getImplementationVersion(), DEFAULT_APP_VERSION);

        nameAndVersion = NAME_AND_VERSION_TEMPLATE.formatted(name, version);
    }

    public static String getNameAndVersion() {
        return INSTANCE.nameAndVersion;
    }
}
