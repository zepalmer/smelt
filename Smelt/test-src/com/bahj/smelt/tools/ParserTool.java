package com.bahj.smelt.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bahj.smelt.model.syntax.datamodel.SmeltParseFailureException;
import com.bahj.smelt.model.syntax.datamodel.SmeltParser;
import com.bahj.smelt.model.syntax.datamodel.ast.DocumentNode;

public class ParserTool {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null && line.length() > 0) {
            sb.append(line).append('\n');
        }
        SmeltParser parser = new SmeltParser();
        try {
            DocumentNode node = parser.parse(sb.toString());
            System.out.println(node.getTreeDescription());
        } catch (SmeltParseFailureException e) {
            e.printStackTrace();
        }
    }
}
