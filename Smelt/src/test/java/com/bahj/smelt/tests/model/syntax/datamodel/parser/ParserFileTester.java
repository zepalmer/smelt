package com.bahj.smelt.tests.model.syntax.datamodel.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import com.bahj.smelt.syntax.antlr.SmeltANTLRLexer;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser;

public class ParserFileTester {
    private File file;

    public ParserFileTester(File file) {
        this.file = file;
    }

    public List<String> test() throws IOException {
        FileInputStream fis = new FileInputStream(this.file);
        SmeltANTLRLexer lexer = new SmeltANTLRLexer(new ANTLRInputStream(fis));
        SmeltANTLRParser parser = new SmeltANTLRParser(new CommonTokenStream(lexer));
        final List<String> errorMessages = new ArrayList<>();
        parser.removeErrorListeners();
        parser.addErrorListener(new ANTLRErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                    int charPositionInLine, String msg, RecognitionException e) {
                errorMessages.add(msg);
            }

            @Override
            public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                    int prediction, ATNConfigSet configs) {

            }

            @Override
            public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                    BitSet conflictingAlts, ATNConfigSet configs) {

            }

            @Override
            public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
                    BitSet ambigAlts, ATNConfigSet configs) {

            }
        });
        parser.document();
        fis.close();
        return errorMessages;
    }

}
