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

import com.beust.jcommander.Parameter;

public final class Parameters {

    @Parameter(names = { "-d", "--display" }, description = "Displaying input data")
    private String dataForDisplay;

    @Parameter(names = { "-u", "--display-unique" }, description = "Displaying unique input data")
    private String dataForDisplayUnique;

    @Parameter(names = { "-i", "--interactive" }, description = "Activate interactive mode")
    private boolean isInteractiveMode;

    @Parameter(names = { "-h", "--help" }, description = "Parameters information")
    private boolean isHelp;

    private String paramsInfo;

    public String getDataForDisplay() {
        return dataForDisplay;
    }

    public String getDataForDisplayUnique() {
        return dataForDisplayUnique;
    }

    public boolean isInteractiveMode() {
        return isInteractiveMode;
    }

    public boolean isHelp() {
        return isHelp;
    }

    public void setHelp(boolean isHelp) {
        this.isHelp = isHelp;
    }

    public String getParamsInfo() {
        return paramsInfo;
    }

    public void setParamsInfo(String paramsInfo) {
        this.paramsInfo = paramsInfo;
    }
}
