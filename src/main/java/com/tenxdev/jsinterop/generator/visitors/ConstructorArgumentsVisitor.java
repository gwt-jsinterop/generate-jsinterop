package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.MethodArgument;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

public class ConstructorArgumentsVisitor extends WebIDLBaseVisitor<List<MethodArgument>> {

    @Override
    public List<MethodArgument> visitExtendedAttributeInner(WebIDLParser.ExtendedAttributeInnerContext ctx) {
        WebIDLParser.ExtendedAttributeInnerContext extendedAttributeInnerContext = ctx;
        List<MethodArgument> arguments = new ArrayList<>();
        List<String> tokens = new ArrayList<>();
        while (extendedAttributeInnerContext != null) {
            if (extendedAttributeInnerContext.otherOrComma() != null) {
                String token = extendedAttributeInnerContext.otherOrComma().getText();
                if (",".equals(token)) {
                    createArgument(tokens, arguments);
                    tokens.clear();
                } else {
                    tokens.add(token);
                }
                extendedAttributeInnerContext = extendedAttributeInnerContext.extendedAttributeInner(0);
            } else if (extendedAttributeInnerContext.extendedAttributeInner(0) != null) {
                String type = "";
                WebIDLParser.ExtendedAttributeInnerContext subContext = extendedAttributeInnerContext.extendedAttributeInner(0);
                while (subContext != null && subContext.otherOrComma() != null) {
                    String token = subContext.otherOrComma().getText();
                    type += "or".equals(token) ? "|" : token;
                    subContext = subContext.extendedAttributeInner(0);
                }
                tokens.add(type);
                extendedAttributeInnerContext = extendedAttributeInnerContext.extendedAttributeInner(1);
            } else {
                break;
            }
        }
        createArgument(tokens, arguments);
        return arguments;
    }

    private void createArgument(List<String> tokens, List<MethodArgument> arguments) {
        //This is ugky, but not as ugly as the parse tree generated by the grammar for extended attributes
        if (!tokens.isEmpty()) {
            boolean optional = false;
            if (tokens.remove("optional")) {
                optional = true;
            }
            if (tokens.remove("?")) {
                optional = true;
            }
            for (String token : tokens) {
                if ("optional".equals(token) || "?".equals(token)) {
                    optional = true;
                    continue;
                }
            }
            String defaultValue=null;
            int equals = tokens.indexOf("=");
            if (equals!=-1){
                defaultValue=tokens.get(equals+1);
                tokens.remove(equals+1);
                tokens.remove(equals);
            }

            String name = tokens.get(tokens.size() - 1);
            String type = "";
            for (int i = 0; i < tokens.size() - 1; ++i) {
                type += tokens.get(i);
            }
            if (type.indexOf('|') != -1) {
                String[] types = type.split("\\|");
                arguments.add(new MethodArgument(name, types, false, optional, defaultValue));
            } else {
                arguments.add(new MethodArgument(name, type, false, optional, defaultValue));
            }
        }
    }
}

