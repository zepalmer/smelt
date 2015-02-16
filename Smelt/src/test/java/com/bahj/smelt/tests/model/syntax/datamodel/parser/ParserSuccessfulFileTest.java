package com.bahj.smelt.tests.model.syntax.datamodel.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParserSuccessfulFileTest {
    private File file;

    public ParserSuccessfulFileTest(File file) {
        super();
        this.file = file;
    }

    @Test
    public void parserTest() throws IOException {
        ParserFileTester tester = new ParserFileTester(file);
        List<String> errorMessages = tester.test();
        if (errorMessages.size() > 0) {
            fail("Errors occurred while parsing " + this.file + ": " + errorMessages);
        }
    }

    @Parameters(name="successParse: {0}")
    public static Collection<Object[]> findTestFiles() {
        File root = new File(String.join(File.separator, "src","test","resources","success"));
        List<Object[]> args = new ArrayList<>();
        for (File f : root.listFiles()) {
            if (f.getName().endsWith(".smeltm")) {
                args.add(new Object[] { f });
            }
        }
        return args;
    }
}
