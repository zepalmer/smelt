grammar SmeltANTLR;

// LEXER CUSTOMIZATION ////////////////////////////////////////////////////////

@lexer::header {
    import java.util.LinkedList;
    import java.util.Queue;
    import java.util.Stack;
}

@lexer::members {
    /** The stack of indentation levels in the parser.  If a new line occurs, its indentation is compared with the
     *  top of this stack to determine whether to emit indents or dedents. */
    private Stack<Integer> indentationStack = new Stack<>();
    /** Holds the indentation level for the current line. */
    private int currentIndentation = 0;
    /** Determines whether we are at the start of a line (for indentation purposes). */
    private boolean atLineStart = true;
    
    private Queue<Token> tokens = new LinkedList<>();
    
    @Override
    public Token nextToken() {
        while (tokens.isEmpty()) {
            Token token = super.nextToken();
            switch (token.getType()) {
                case NEWLINE:
                    // Process indentation.
                    if (!this.atLineStart) {
                        this.currentIndentation = 0;
                        this.atLineStart = true;
                        this.tokens.offer(makeToken(SmeltANTLRParser.EOL));
                    } else {
                        // We're already at the start of a line, so an EOL would be wrong.
                    }
                    break;
                case EOF:
                    // At the end of the file, we need to emit all of the dedent tokens for the current indentation
                    // levels.
                    this.currentIndentation = 0;
                    matchCurrentIndentation();
                    this.tokens.offer(token);
                    break;
                case WHITESPACE:
                    if (this.atLineStart) {
                        // This whitespace appears at the start of a line.  Increase current indentation appropriately.
                        this.currentIndentation += token.getText().length();
                    } else {
                        // This whitespace is within a line and therefore unimportant.
                    }
                    break;
                default:
                    // If this is the first token on a line, we have to process the current indentation levels by
                    // emitting the appropriate indent and dedent tokens.
                    if (this.atLineStart) {
                        matchCurrentIndentation();
                        this.atLineStart = false;
                    } 
                    tokens.offer(token);
                    break;
            }
        }
        return tokens.poll();
    }
    
    private int getContextIndent() {
        return this.indentationStack.isEmpty() ? 0 : this.indentationStack.peek();
    }
    
    private Token makeToken(final int type) {
        return _factory.create(_tokenFactorySourcePair, type, "",
                Token.DEFAULT_CHANNEL,
                -1, -1,
                _tokenStartLine, _tokenStartCharPositionInLine);
    }
    
    private void matchCurrentIndentation() {
        if (this.currentIndentation > getContextIndent()) {
            // The current indentation level is larger than we've seen.  Start a new indentation level at this point
            // and emit an indent token for it.
            this.indentationStack.push(this.currentIndentation);
            this.tokens.offer(makeToken(SmeltANTLRParser.INDENT));
        } else if (this.currentIndentation < getContextIndent()) {
            // The current indentation level is smaller than we've seen.  This means that at least one indentation
            // level just ended.  We must make sure that we've retreated to a valid indentation level -- that is,
            // if the indents were at 4, 8, 12, and 16, the next line can't be at 6.  Otherwise, though, we just emit
            // one dedent token for each level implicitly closed.
            do {
                this.indentationStack.pop();
                this.tokens.offer(makeToken(SmeltANTLRParser.DEDENT));
            } while (this.currentIndentation < getContextIndent());
            if (this.currentIndentation > getContextIndent()) {
                // This is the case where we've retreated to an indentation level that doesn't exist.  Fail.
                throw new IndentationException();
            }
        }
    }
    
    private class IndentationException extends RecognitionException {
        public IndentationException() {
            super(SmeltANTLRLexer.this, SmeltANTLRLexer.this._input, null);
        }
    }
}

// PARSER CUSTOMIZATION ///////////////////////////////////////////////////////

@parser::header {
}

// TOKEN DECLARATIONS /////////////////////////////////////////////////////////

tokens {
    INDENT,
    DEDENT,
    EOL
}

// PARSER RULES ///////////////////////////////////////////////////////////////

document
:
    declaration* EOF
;

declaration
:
    message
    | list
;

list
:
    IDENTIFIER
    ( ',' IDENTIFIER )*
    ','?
;

message
:
    messageHeader (end messageBody?)?
;

messageHeader
:
    IDENTIFIER
    (
        ':' declarationArgument
        (
            ',' declarationArgument
        )* ','?
    )?
;

declarationArgument
:
    namedArgument
    | positionalArgument
;

namedArgument
:
    IDENTIFIER '=' IDENTIFIER+
;

positionalArgument
:
    IDENTIFIER+
;

messageBody
:
    INDENT declaration* DEDENT
;

end
:
    EOL
    | EOF
;

// LEXER RULES ////////////////////////////////////////////////////////////////

NEWLINE
:
    '\n'
;

WHITESPACE
:
    ' '+
;

IDENTIFIER
:
    IDENTIFIER_START IDENTIFIER_CONT*
;

EQUALS
:
    '='
;

COLON
:
    ':'
;

COMMA
:
    ','
;

fragment
IDENTIFIER_START
:
    .
    {Character.isAlphabetic(_input.LA(-1))}?

;

fragment
IDENTIFIER_CONT
:
    .
    {Character.isDigit(_input.LA(-1)) || Character.isAlphabetic(_input.LA(-1))}?

;
