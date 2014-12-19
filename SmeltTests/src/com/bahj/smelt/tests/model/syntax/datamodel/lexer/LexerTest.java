package com.bahj.smelt.tests.model.syntax.datamodel.lexer;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.bahj.smelt.model.syntax.datamodel.lexer.SmeltLexer;
import com.bahj.smelt.model.syntax.datamodel.lexer.LexerException;
import com.bahj.smelt.model.syntax.datamodel.lexer.Token;

public class LexerTest {
	@Test
	public void lexerTest() throws IOException, LexerException {
		String document = "foo:\n" + "    bar: Bar\n" + "        baz: 1\n\n"
				+ "        baz: 2\n" + "    qux\n";
		StringReader reader = new StringReader(document);
		SmeltLexer lexer = new SmeltLexer("<static>", reader);
		Collection<Token> tokens = lexer.allTokens();
		List<Token.Type> expected = Arrays.asList(Token.Type.TEXT,
				Token.Type.COLON, Token.Type.EOL, Token.Type.INDENT,
				Token.Type.TEXT, Token.Type.COLON, Token.Type.TEXT,
				Token.Type.EOL, Token.Type.INDENT, Token.Type.TEXT,
				Token.Type.COLON, Token.Type.TEXT, Token.Type.EOL,
				Token.Type.TEXT, Token.Type.COLON, Token.Type.TEXT,
				Token.Type.EOL, Token.Type.DEDENT, Token.Type.TEXT,
				Token.Type.EOL, Token.Type.DEDENT);
		Iterator<Token> ittok = tokens.iterator();
		Iterator<Token.Type> ittyp = expected.iterator();
		while (ittok.hasNext() && ittyp.hasNext())
		{
			Token tok = ittok.next();
			Token.Type typ = ittyp.next();
			Assert.assertEquals(tok.getType(), typ);
		}
		Assert.assertTrue(ittok.hasNext() == ittyp.hasNext());
	}
}
