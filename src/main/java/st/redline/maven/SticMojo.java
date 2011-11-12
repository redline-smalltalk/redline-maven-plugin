/*
 * Copyright (c) 2011 Robert Roland
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package st.redline.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import st.redline.Stic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Goal which touches a timestamp file.
 *
 * @goal stic
 * @phase verify
 */
public class SticMojo extends AbstractMojo {
    /**
     * Location of the source.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File sourcePath;
    /**
     * Classname of the source file.
     * 
     * @parameter expression="st.redline.HelloWorld"
     * @required
     */
    private String sourceClass;
    /**
     * Location of the Smalltalk runtime.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File runtimePath;

    public void execute() throws MojoExecutionException {        
        if(!runtimePath.exists()) {
            throw new MojoExecutionException("Unable to locate the Smalltalk runtime library at " + runtimePath);
        }
        if(!sourcePath.exists()) {
            throw new MojoExecutionException("Unable to locate the Smalltalk sources at " + sourcePath);
        }
        
        List<String> sticArguments = new LinkedList<String>();

        sticArguments.add("-r");
        sticArguments.add(runtimePath.getAbsolutePath());
        sticArguments.add("-s");
        sticArguments.add(sourcePath.getAbsolutePath());
        sticArguments.add(sourceClass);

        getLog().debug("Parameters = " + sticArguments);
        
        getLog().info("Invoking Redline...");

        try {
            Stic.invokeWith(sticArguments.toArray(new String[0]));
        } catch(Exception ex) {
            throw new MojoExecutionException("Unable to invoke class", ex);
        }
    }
}
