package com.saigut.test_parsec;


import org.jparsec.*;
import org.jparsec.pattern.CharPredicates;
import org.jparsec.pattern.Patterns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Main {

    enum Tag {
        WHITE_SPACE
    }

    public static void main(String[] args) {
	// write your code here
//        System.out.print(Calculator.evaluate("(1 +2)*10"));
//        Calculator2.print_token("(a 1 2)");

//        Terminals operators = Terminals.operators(","); // only one operator supported so far
//        Parser<?> integerTokenizer = Terminals.IntegerLiteral.TOKENIZER;
//        Parser<String> integerSyntacticParser = Terminals.IntegerLiteral.PARSER;
//        Parser<?> ignored = Parsers.or(Scanners.JAVA_BLOCK_COMMENT, Scanners.WHITESPACES);
//        Parser<?> tokenizer = Parsers.or(operators.tokenizer(), integerTokenizer); // tokenizes the operators and integer
//        Parser<List<String>> integers = integerSyntacticParser.sepBy(operators.token(","))
//                .from(tokenizer, ignored.skipMany());
//        System.out.println(integers.parse("1, /*this is comment*/2, 3"));



        final Parser<Tokens.Fragment> WHITESPACES =
                Patterns.many1(CharPredicates.IS_WHITESPACE).toScanner("whitespaces").map((a) ->
                {
                    if (null != a) {
//                        System.out.println("wwww a: " + a.toString());
//                        System.out.println("wwww type: " + a.getClass().getName());
                        return Tokens.fragment(a.toString(), Tag.WHITE_SPACE);
                    } else {
//                        System.out.println("wwww a: null");
                        return Tokens.fragment("null", Tag.WHITE_SPACE);
                    }

                });


        Terminals operators = Terminals.operators("(", ")", "[", "]");
        Parser<?> integerTokenizer = Terminals.IntegerLiteral.TOKENIZER;
        Parser<String> integerSyntacticParser = Terminals.IntegerLiteral.PARSER;
        Parser<?> ignored = Parsers.or(Scanners.JAVA_BLOCK_COMMENT, WHITESPACES);
        Parser<?> tokenizer = Parsers.or(operators.tokenizer(), integerTokenizer); // tokenizes the operators and integer
        Parser<?> no_pass2 = Parsers.or(tokenizer, ignored)
                .map((a) ->
                {
                    if (null != a) {
                    System.out.println("a: " + a.toString());
                    System.out.println("type: " + ((Tokens.Fragment)a).tag().toString());
                    } else {
                        System.out.println("a: null");
                    }
                    return a;}).many();
//        Parser<?> parser = new OperatorTable()
//                .build(no_pass2);
//        Parser<?> parser2 = parser.from(tokenizer, ignored.skipMany());
//        Parser<List<String>> integers = integerSyntacticParser.
//                .from(tokenizer, ignored.skipMany());
//        System.out.println(integers.parse("1 2 3"));
        System.out.println(no_pass2.parse("(13 2/* dfdgdg */ 3 4 5)").toString());

//        Parser<String> scanner = Scanners.IDENTIFIER;
////        Parser<?> scanners = Parsers.or(scanner, Scanners.WHITESPACES).many().token();
//        Parser<Void> white = Scanners.WHITESPACES;
//        Parser<List<List<String>>> scanners = Parsers.or(scanner).many().until(white);
//        System.out.println(scanners.parse("foo ddd             ").toString());

    }
}
