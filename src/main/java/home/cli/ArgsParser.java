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
package home.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import home.utils.AppInfo;

public final class ArgsParser {

    private static final Logger LOG = LoggerFactory.getLogger(ArgsParser.class);

    public static Options parse(String[] inputDataArray) {
        var options = new Options();

        JCommander jCommander = JCommander.newBuilder()
                .programName(AppInfo.getNameAndVersion())
                .addObject(options)
                .build();

        String optionsDescriptions = generateOptionsDescriptions(jCommander);
        setAdditionalValues(options, inputDataArray, optionsDescriptions);

        try {
            jCommander.parse(inputDataArray);
        } catch (ParameterException e) {
            LOG.error(e.getMessage());
            throw e;
        }

        return options;
    }

    private static String generateOptionsDescriptions(JCommander jCommander) {
        var sb = new StringBuilder();
        jCommander.usage(sb);
        return sb.toString();
    }

    private static void setAdditionalValues(Options options, String[] inputDataArray, String optionsDescriptions) {
        if (inputDataArray.length == 0) {
            options.setHelp(true);
        }

        options.setOptionsDescriptions(optionsDescriptions);
        options.setVersionInfo(AppInfo.getVersion());
    }

    private ArgsParser() {
    }
}
