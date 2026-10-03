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
package home.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class ConfigUtils {

    static List<?> castToList(Object obj, String tagName) {
        if (obj instanceof List<?> list) {
            return list;
        }

        throw new IllegalArgumentException("Error while parse values of [%s]"
                .formatted(tagName));
    }

    static Map<?, ?> castToMap(Object obj, String tagName) {
        if (obj instanceof Map<?, ?> map) {
            return map;
        }

        throw new IllegalArgumentException("Error while parse value of [%s]"
                .formatted(tagName));
    }

    static Map<String, String> convertWildMapToStringMap(Map<?, ?> wildMap) {
        var strMap = new HashMap<String, String>();
        for (Entry<?, ?> entry : wildMap.entrySet()) {
            String key = entry.getKey().toString();
            Object val = entry.getValue();
            if (val == null) {
                throw new IllegalArgumentException("The '%s' is null".formatted(key));
            }
            strMap.put(key, val.toString());
        }

        return strMap;
    }

    static boolean endsWithIgnoreCase(String str, String suffix) {
        int suffixLength = suffix.length();
        return str.regionMatches(true, str.length() - suffixLength, suffix, 0, suffixLength);
    }

    private ConfigUtils() {
    }
}
