package com.bahj.smelt.tests.model.syntax.datamodel.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.bahj.smelt.syntax.SmeltParseFailure;
import com.bahj.smelt.syntax.SmeltParseFailureException;
import com.bahj.smelt.syntax.SmeltParser;
import com.bahj.smelt.syntax.ast.DocumentNode;

public class ParserFileTester {
    private File file;
    private DocumentNode documentNode;

    public ParserFileTester(File file) {
        this.file = file;
    }

    public List<String> test() throws IOException {
        SmeltParser parser = new SmeltParser(this.file.getName());
        try (FileInputStream fis = new FileInputStream(this.file)) {
            try {
                this.documentNode = parser.parse(fis);
            } catch (SmeltParseFailureException e) {
                return e.getFailures().stream().map(SmeltParseFailure::toString).collect(Collectors.toList());
            }            
        }
        return Collections.emptyList();
    }

    public DocumentNode getDocumentNode() {
        return documentNode;
    }
}
