package com.bahj.smelt.tests.model.syntax.datamodel.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.bahj.flapjack.ParseException;
import com.bahj.smelt.model.syntax.datamodel.lexer.SmeltLexer;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.Token;
import com.bahj.smelt.model.syntax.datamodel.parser.SmeltParser;

@RunWith(Parameterized.class)
public class ParserTest {
    private File file;

    public ParserTest(File file) {
        super();
        this.file = file;
    }

    @Test
    public void parserTest() throws IOException {
        try {
            FileInputStream fis = new FileInputStream(this.file);
            SmeltLexer lexer = new SmeltLexer(file.getName(), new InputStreamReader(fis));
            SmeltParser parser = new SmeltParser();
            parser.parse(lexer.asIterator());
        } catch (ParseException e) {
            Token t = (Token)e.getLastToken();
            Assert.fail("Failure at span: " + t.getSpan());
        }
    }

    @Parameters
    public static Collection<Object[]> findTestFiles() {
        File root = new File("test-data" + File.separator + "success");
        List<Object[]> args = new ArrayList<>();
        for (File f : root.listFiles()) {
            if (f.getName().endsWith(".smeltm")) {
                args.add(new Object[] { f });
            }
        }
        return args;
    }
}
