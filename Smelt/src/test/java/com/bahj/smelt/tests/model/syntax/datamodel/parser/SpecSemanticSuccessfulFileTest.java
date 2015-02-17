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

import com.bahj.smelt.SmeltApplicationModel;
import com.bahj.smelt.configuration.ApplicationModelCreationException;
import com.bahj.smelt.configuration.Configuration;
import com.bahj.smelt.syntax.ast.DocumentNode;
import com.bahj.smelt.util.FileUtils;

@RunWith(Parameterized.class)
public class SpecSemanticSuccessfulFileTest {
    private File file;

    public SpecSemanticSuccessfulFileTest(File file) {
        super();
        this.file = file;
    }

    @Test
    public void parserTest() throws IOException, ApplicationModelCreationException, ClassNotFoundException, ClassCastException {
        ParserFileTester tester = new ParserFileTester(file);
        List<String> errorMessages = tester.test();
        if (errorMessages.size() > 0) {
            fail("Errors occurred while parsing " + this.file + ": " + errorMessages);
        }
        // Now test the resulting document using a default configuration.
        DocumentNode documentNode = tester.getDocumentNode();
        SmeltApplicationModel applicationModel = new SmeltApplicationModel(Configuration.createDefaultConfiguration());
        applicationModel.loadApplicationSpecification(documentNode);
    }

    @Parameters(name="successParse: {0}")
    public static Collection<Object[]> findTestFiles() {
        File root = new File(String.join(File.separator, "src","test","resources","semantic-success-default"));
        List<Object[]> args = new ArrayList<>();
        for (File f : root.listFiles()) {
            if (f.getName().endsWith(FileUtils.SMELT_SPEC_EXTENSION)) {
                args.add(new Object[] { f });
            }
        }
        return args;
    }
}
