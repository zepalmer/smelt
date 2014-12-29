package com.bahj.smelt.tests.model.syntax.datamodel.lexer;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.bahj.smelt.model.syntax.datamodel.lexer.LexerException;
import com.bahj.smelt.model.syntax.datamodel.lexer.SmeltLexer;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.ColonToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.DedentToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.EndOfFileToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.EndOfLineToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.IdentifierToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.IndentToken;
import com.bahj.smelt.model.syntax.datamodel.lexer.token.Token;

public class LexerTest {
    @Test
    public void lexerTest() throws IOException, LexerException {
        // @formatter:off
		String document =
		        "foo:\n" +
		        "    bar: Bar\n" +
		        "        baz: 1\n" +
		        "\n" +
		        "        baz: 2\n" +
		        "    qux\n";
		// @formatter:on
        StringReader reader = new StringReader(document);
        SmeltLexer lexer = new SmeltLexer("<static>", reader);
        Collection<Token> tokens = lexer.allTokens();
        List<Class<? extends Token>> expected = Arrays.asList(IdentifierToken.class, ColonToken.class,
                EndOfLineToken.class, IndentToken.class, IdentifierToken.class, ColonToken.class,
                IdentifierToken.class, EndOfLineToken.class, IndentToken.class, IdentifierToken.class,
                ColonToken.class, IdentifierToken.class, EndOfLineToken.class, IdentifierToken.class, ColonToken.class,
                IdentifierToken.class, EndOfLineToken.class, DedentToken.class, IdentifierToken.class,
                EndOfLineToken.class, DedentToken.class, EndOfFileToken.class);
        Iterator<Token> ittok = tokens.iterator();
        Iterator<Class<? extends Token>> ittyp = expected.iterator();
        while (ittok.hasNext() && ittyp.hasNext()) {
            Token tok = ittok.next();
            Class<? extends Token> typ = ittyp.next();
            try {
                typ.cast(tok);
            } catch (ClassCastException e) {
                Assert.fail("Expected token type " + typ.getName() + " but got " + tok.getClass().getName());
            }
        }
        Assert.assertTrue(ittok.hasNext() == ittyp.hasNext());
    }
}
