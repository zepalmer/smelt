package com.bahj.smelt.model.syntax.datamodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.DeclarationArgumentContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.DeclarationContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.DocumentContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.ListContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.MessageBodyContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.MessageContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.MessageHeaderContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.NamedArgumentContext;
import com.bahj.smelt.model.syntax.datamodel.SmeltANTLRParser.PositionalArgumentContext;
import com.bahj.smelt.model.syntax.datamodel.ast.ArgumentNode;
import com.bahj.smelt.model.syntax.datamodel.ast.DeclarationNode;
import com.bahj.smelt.model.syntax.datamodel.ast.DocumentNode;
import com.bahj.smelt.model.syntax.datamodel.ast.ListNode;
import com.bahj.smelt.model.syntax.datamodel.ast.MessageHeaderNode;
import com.bahj.smelt.model.syntax.datamodel.ast.MessageNode;
import com.bahj.smelt.model.syntax.datamodel.ast.NamedArgumentNode;
import com.bahj.smelt.model.syntax.datamodel.ast.PositionalArgumentNode;

public class SmeltParser {
    public SmeltParser() {

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
                failures.add(new SmeltParseFailure(line, charPositionInLine, msg));
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
        return new DocumentNode(declarations);
    }

    private DeclarationNode astTransform(DeclarationContext declarationContext) {
        if (declarationContext.list() != null) {
            return astTransform(declarationContext.list());
        } else if (declarationContext.message() != null) {
            return astTransform(declarationContext.message());
        } else {
            throw new IllegalStateException("Unrecognized declaration context type.");
        }
    }

    private ListNode astTransform(ListContext listContext) {
        List<String> values = new ArrayList<>();
        for (TerminalNode node : listContext.IDENTIFIER()) {
            values.add(node.getText());
        }
        return new ListNode(values);
    }

    private MessageNode astTransform(MessageContext messageContext) {
        MessageHeaderNode header = astTransform(messageContext.messageHeader());
        MessageBodyContext bodyContext = messageContext.messageBody();
        List<DeclarationNode> children = new ArrayList<>();
        if (bodyContext != null) {
            for (DeclarationContext declContext : bodyContext.declaration()) {
                children.add(astTransform(declContext));
            }
        }
        return new MessageNode(header, children);
    }

    private MessageHeaderNode astTransform(MessageHeaderContext messageHeaderContext) {
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
        return new MessageHeaderNode(name, posArgs, namedArgs);
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
        Iterator<TerminalNode> it = namedArgumentContext.IDENTIFIER().iterator();
        String name = it.next().getText();
        List<String> args = new ArrayList<>();
        while (it.hasNext()) {
            args.add(it.next().getText());
        }
        return new NamedArgumentNode(name, args);
    }

    private PositionalArgumentNode astTransform(PositionalArgumentContext positionalArgumentContext) {
        List<String> args = new ArrayList<>();
        for (TerminalNode node : positionalArgumentContext.IDENTIFIER()) {
            args.add(node.getText());
        }
        return new PositionalArgumentNode(args);
    }
}
