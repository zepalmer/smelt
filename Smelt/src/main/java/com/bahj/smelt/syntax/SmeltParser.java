package com.bahj.smelt.syntax;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import com.bahj.smelt.syntax.antlr.SmeltANTLRLexer;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.DeclarationArgumentContext;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.DeclarationBodyContext;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.DeclarationContext;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.DeclarationHeaderContext;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.DocumentContext;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.NamedArgumentContext;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.PositionalArgumentContext;
import com.bahj.smelt.syntax.antlr.SmeltANTLRParser.StringContext;
import com.bahj.smelt.syntax.ast.ArgumentNode;
import com.bahj.smelt.syntax.ast.DeclarationHeaderNode;
import com.bahj.smelt.syntax.ast.DeclarationNode;
import com.bahj.smelt.syntax.ast.DocumentNode;
import com.bahj.smelt.syntax.ast.NamedArgumentNode;
import com.bahj.smelt.syntax.ast.PositionalArgumentNode;
import com.bahj.smelt.syntax.ast.impl.DeclarationHeaderNodeImpl;
import com.bahj.smelt.syntax.ast.impl.DeclarationNodeImpl;
import com.bahj.smelt.syntax.ast.impl.DocumentNodeImpl;
import com.bahj.smelt.syntax.ast.impl.NamedArgumentNodeImpl;
import com.bahj.smelt.syntax.ast.impl.PositionalArgumentNodeImpl;

public class SmeltParser {
    private String resourceName;

    public SmeltParser(String resourceName) {
        this.resourceName = resourceName;
    }

    public DocumentNode parse(String string) throws SmeltParseFailureException, IOException {
        return parse(new ByteArrayInputStream(string.getBytes()));
    }

    public DocumentNode parse(InputStream inputStream) throws SmeltParseFailureException, IOException {
        SmeltANTLRLexer lexer = new SmeltANTLRLexer(new ANTLRInputStream(inputStream));
        SmeltANTLRParser parser = new SmeltANTLRParser(new CommonTokenStream(lexer));
        final List<SmeltParseFailure> failures = new ArrayList<>();
        parser.removeErrorListeners();
        parser.addErrorListener(new ANTLRErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                    int charPositionInLine, String msg, RecognitionException e) {
                failures.add(new SmeltParseFailure(new SourceLocation(resourceName, line, charPositionInLine), msg));
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
        DocumentContext documentContext = parser.document();
        inputStream.close();
        if (failures.size() > 0) {
            throw new SmeltParseFailureException(failures);
        } else {
            return astTransform(documentContext);
        }
    }

    private DocumentNode astTransform(DocumentContext documentContext) {

        List<DeclarationNode> declarations = new ArrayList<>();
        for (DeclarationContext declarationContext : documentContext.declaration()) {
            declarations.add(astTransform(declarationContext));
        }
        return new DocumentNodeImpl(locationOf(documentContext), declarations);
    }

    private DeclarationNode astTransform(DeclarationContext declarationContext) {
        DeclarationHeaderNode header = astTransform(declarationContext.declarationHeader());
        DeclarationBodyContext bodyContext = declarationContext.declarationBody();
        List<DeclarationNode> children = new ArrayList<>();
        if (bodyContext != null) {
            for (DeclarationContext declContext : bodyContext.declaration()) {
                children.add(astTransform(declContext));
            }
        }
        return new DeclarationNodeImpl(locationOf(declarationContext), header, children);
    }

    private DeclarationHeaderNode astTransform(DeclarationHeaderContext messageHeaderContext) {
        String name = messageHeaderContext.IDENTIFIER().getText();
        List<PositionalArgumentNode> posArgs = new ArrayList<>();
        Map<String, NamedArgumentNode> namedArgs = new HashMap<>();
        for (DeclarationArgumentContext declarationArgumentContext : messageHeaderContext.declarationArgument()) {
            ArgumentNode argumentNode = astTransform(declarationArgumentContext);
            if (argumentNode instanceof PositionalArgumentNode) {
                posArgs.add((PositionalArgumentNode) argumentNode);
            } else if (argumentNode instanceof NamedArgumentNode) {
                NamedArgumentNode namedArgumentNode = (NamedArgumentNode) argumentNode;
                namedArgs.put(namedArgumentNode.getName(), namedArgumentNode);
            } else {
                throw new IllegalStateException("Unrecognized argument node type.");
            }
        }
        return new DeclarationHeaderNodeImpl(locationOf(messageHeaderContext), name, posArgs, namedArgs);
    }

    private ArgumentNode astTransform(DeclarationArgumentContext declarationArgumentContext) {
        if (declarationArgumentContext.namedArgument() != null) {
            return astTransform(declarationArgumentContext.namedArgument());
        } else if (declarationArgumentContext.positionalArgument() != null) {
            return astTransform(declarationArgumentContext.positionalArgument());
        } else {
            throw new IllegalStateException("Unrecognized argument context type.");
        }
    }

    private NamedArgumentNode astTransform(NamedArgumentContext namedArgumentContext) {
        String name = namedArgumentContext.IDENTIFIER().getText();
        List<String> args = namedArgumentContext.string().stream().map(SmeltParser::processString)
                .collect(Collectors.toList());
        return new NamedArgumentNodeImpl(locationOf(namedArgumentContext), name, args);
    }

    private PositionalArgumentNode astTransform(PositionalArgumentContext positionalArgumentContext) {
        List<String> args = positionalArgumentContext.string().stream().map(SmeltParser::processString)
                .collect(Collectors.toList());
        return new PositionalArgumentNodeImpl(locationOf(positionalArgumentContext), args);
    }

    private SourceLocation locationOf(ParserRuleContext context) {
        return new SourceLocation(resourceName, context.getStart().getLine(), context.getStart()
                .getCharPositionInLine());
    }

    private static String processString(StringContext context) {
        if (context.STRING() == null) {
            return context.IDENTIFIER().getText();
        } else {
            String raw = context.STRING().getText();
            // This is a quoted string. We need to remove the quotes and process the escapes.
            StringBuilder result = new StringBuilder();
            raw = raw.substring(1, raw.length() - 1); // remove quotes
            for (int i = 0; i < raw.length(); i++) {
                switch (raw.charAt(i)) {
                    case '\\':
                        i++;
                        switch (raw.charAt(i)) {
                            case '\\':
                                result.append('\\');
                                break;
                            case '"':
                                result.append('"');
                                break;
                            default:
                                throw new IllegalStateException("Unrecognized escape code!");
                        }
                        break;
                    default:
                        result.append(raw.charAt(i));
                }
            }
            return result.toString();
        }
    }
}
