package com.bahj.smelt.model.syntax.datamodel.lexer;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * The lexer for the Smelt file format.
 * 
 * @author Zachary Palmer
 */
public class SmeltLexer {
	private PushbackReader reader;
	private SourceLocation location;

	private Stack<Integer> indentationLevels;

	/**
	 * Used to keep track of queued tokens. A single lexer operation may
	 * generate multiple tokens, so the spares are kept here until they are
	 * needed.
	 */
	private Queue<Token> tokenQueue;
	private boolean emittedLastToken;

	private int lastChar;
	private SourceLocation lastLocation;
	private boolean lastValid;

	public SmeltLexer(String filename, Reader reader) {
		super();
		this.reader = new PushbackReader(reader);
		this.location = new SourceLocation(filename);
		this.indentationLevels = new Stack<>();
		this.tokenQueue = new LinkedList<>();
		this.emittedLastToken = false;

		this.lastValid = false;
	}

	/**
	 * Retrieves the next character for this lexer.
	 * 
	 * @return The next character.
	 */
	private int nextChar() throws LexerException {
		int c;
		try {
			c = this.reader.read();
		} catch (IOException e) {
			throw new LexerException(this.location, e);
		}
		this.lastChar = c;
		this.lastLocation = this.location;
		this.lastValid = true;
		if (c == '\n') {
			this.location = this.location.nextLine();
		} else {
			this.location = this.location.nextColumn();
		}
		return c;
	}

	/**
	 * Pushes a single character back onto the buffer.
	 */
	private void pushback() throws LexerException {
		if (!this.lastValid) {
			throw new IllegalStateException(
					"Cannot push back when no valid character exists!");
		} else {
			this.lastValid = false;
			if (this.lastChar != -1) {
				try {
					this.reader.unread(this.lastChar);
				} catch (IOException ioe) {
					throw new IllegalStateException(
							"I/O exception on pushback of character", ioe);
				}
				this.location = this.lastLocation;
			}
		}
	}

	/**
	 * Generates a lexer exception in the current location.
	 */
	private void lexfail(String errorMessage) throws LexerException {
		throw new LexerException(this.location, errorMessage);
	}

	/**
	 * Reads characters until the specified predicate fails. The failed
	 * character is pushed back.
	 * 
	 * @param pred
	 *            The predicate.
	 * @return The string containing the characters which were read.
	 */
	private String readWhile(CharacterPredicate pred) throws LexerException {
		StringBuilder sb = new StringBuilder();
		int c;
		while ((c = nextChar()) != -1 && pred.test(c)) {
			sb.append((char) c);
		}
		pushback();
		return sb.toString();
	}

	/**
	 * Generates the next token found in the reader or <code>null</code> if no
	 * such token exists.
	 * 
	 * @return The next token.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws LexerException
	 *             If no legal token can be lexed.
	 */
	public Token nextToken() throws LexerException {
		while (tokenQueue.isEmpty() && findToken()) {
			// The findToken() above changes state, so this loop needs no body.
		}
		return tokenQueue.poll();
	}

	/**
	 * Lexes all remaining tokens.
	 * 
	 * @return The remaining tokens.
	 */
	public Collection<Token> allTokens() throws LexerException {
		List<Token> tokens = new ArrayList<>();
		Token t;
		while ((t = nextToken()) != null) {
			tokens.add(t);
		}
		return tokens;
	}

	/**
	 * Tries to find tokens in the reader. By the end of this operation, some
	 * tokens may have been added to the token queue.
	 * 
	 * @return <code>true</code> if there may be more tokens in the future;
	 *         <code>false</code> if there definitely won't be.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws LexerException
	 *             If no legal token can be lexed.
	 */
	private boolean findToken() throws LexerException {
		SourceLocation start = this.location;
		int c = nextChar();
		if (c == -1) {
			if (!this.emittedLastToken) {
				// We've reached the end of the reader. Pop all the dedents.
				while (!this.indentationLevels.isEmpty()) {
					this.indentationLevels.pop();
					this.tokenQueue.offer(new Token(this.location, "",
							Token.Type.DEDENT));
				}
				// Emit the last token.
				this.tokenQueue.offer(new Token(this.location, "",
						Token.Type.EOF));
				this.emittedLastToken = true;
			}
			// Now indicate that we're no longer going to be producing tokens.
			return false;
		} else if (c == '\n') {
			// A newline has appeared. We need to try to read characters to see
			// if we get whitespace. If we do, we need to deal with the
			// indentation stack.
			start = this.location;
			String spaceString = readWhile((int ch) -> Character
					.isWhitespace(ch) && ch != '\n');
			final int spaces = spaceString.length();
			// If the next character is a newline, then we just saw a blank line
			// with space on it. Ignore it.
			c = nextChar();
			pushback();
			if (c == '\n') {
				return true;
			}
			// Because it wasn't just a blank line, we need to insert an EOL
			// token.
			this.tokenQueue.offer(new Token(start, "\n", Token.Type.EOL));
			// If we didn't see any space, then we just consumed the newline.
			// We've added that token, so we're finished in that case.
			if (spaces == 0) {
				return true;
			}
			// Otherwise, we saw some number of spaces followed by a blank line.
			// If this is larger than the current indentation level, then we
			// emit an indentation token. If it is less, we may emit numerous
			// dedentation tokens. Otherwise, we emit nothing.
			int currentIndentation = this.indentationLevels.isEmpty() ? 0
					: this.indentationLevels.peek();
			if (spaces > currentIndentation) {
				// Then this is deeper. Emit an indentation and record the
				// level.
				this.indentationLevels.push(spaces);
				this.tokenQueue.offer(new Token(start, spaceString,
						Token.Type.INDENT));
			} else if (spaces < currentIndentation) {
				// Then this is hopefully shallower. But if we dedent to a
				// level we've never seen before, that's an error.
				while (!this.indentationLevels.isEmpty()
						&& this.indentationLevels.peek() > spaces) {
					this.tokenQueue.offer(new Token(start, spaceString,
							Token.Type.DEDENT));
					this.indentationLevels.pop();
				}
				currentIndentation = this.indentationLevels.isEmpty() ? 0
						: this.indentationLevels.peek();
				if (currentIndentation < spaces) {
					// Then we dedented to a level we've never encountered
					// before, e.g.
					// .foo:
					// . bar:
					// . baz
					// . problem
					// So we scream.
					lexfail("Invalid indentation");
				}
			} else {
				// Then this is the same indentation level as before and nothing
				// happens.
			}
			return true;
		} else if (Character.isWhitespace(c)) {
			// Then we've found some whitespace. Ignore it.
			readWhile((int ch) -> Character.isWhitespace(ch) && ch != '\n');
			return true;
		} else if (c == ':') {
			this.tokenQueue.offer(new Token(start, ":", Token.Type.COLON));
			return true;
		} else if (c == ',') {
			this.tokenQueue.offer(new Token(start, ",", Token.Type.COMMA));
			return true;
		} else if (c == '=') {
			this.tokenQueue.offer(new Token(start, "=", Token.Type.EQUAL));
			return true;
		} else if (Character.isAlphabetic(c) || Character.isDigit(c)) {
			// Then we have found a text term. Read until we can't find more
			// text.
			pushback(); // put c back in the reader
			String text = readWhile((int ch) -> (Character.isAlphabetic(ch)
					|| Character.isDigit(ch) || Character.isWhitespace(ch))
					&& ch != '\n');
			this.tokenQueue.offer(new Token(start, text, Token.Type.TEXT));
			return true;
		} else {
			lexfail("Unrecognized character: " + c);
			return true; // this can't be reached but satisfies flow analysis
		}
	}

	private static interface CharacterPredicate {
		public boolean test(int c);
	}
}