package com.saigut.test_parsec;

import org.jparsec.*;

public class Calculator2 {

    // ignore charactors
    static final Parser<Void> IGNORED = Parsers.or(
            Scanners.JAVA_LINE_COMMENT,
            Scanners.JAVA_BLOCK_COMMENT,
            Scanners.WHITESPACES).skipMany();



    // Use for tokenizer recognize operator charactors
//    private static final Terminals OPERATORS =
//            Terminals.operators("+", "-", "*", "/", "(", ")");
    private static final Terminals OPERATORS =
            Terminals.operators("(", ")", "lambda");


    static final Parser<String> FIELD_NAME_TOKENIZER = Terminals.Identifier.TOKENIZER.source();
    // tokenizer which can recognize number and operator charactor, other is
    // whitespace or bad charactor
    static final Parser<?> TOKENIZER =
            Parsers.or(OPERATORS.tokenizer(), FIELD_NAME_TOKENIZER, Terminals.DecimalLiteral.TOKENIZER);


    static final Parser<?> QUOTED_STRING_TOKENIZER = Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER.or(Terminals.StringLiteral.DOUBLE_QUOTE_TOKENIZER);
    static final Terminals TERMINALS = Terminals.caseSensitive(new String[] { "=", "(", ")", "[", "]", ",", "<>" }, new String[] { "OR", "AND", "NOT", "IN" });
    static final Parser<?> TOKENIZER2 = Parsers.or(TERMINALS.tokenizer(), QUOTED_STRING_TOKENIZER);




    // receive literal opeator names, and point to one of the real operator
    static Parser<?> term(String... names) {
        return OPERATORS.token(names);
    }
    // get the value of operator. o_O
    static <T> Parser<T> op(String name, T value) {
        return term(name).retn(value);
    }


    // receive literal number, point to real number
    static final Parser<Double> NUMBER =
            Terminals.DecimalLiteral.PARSER.map(Double::valueOf);


    static Parser<Double> calculator(Parser<Double> atom) {
        Parser.Reference<Double> ref = Parser.newReference();
        Parser<Double> unit = ref.lazy().between(term("("), term(")")).or(atom);
        Parser<Double> parser = new OperatorTable<Double>()
                .infixl(op("+", (l, r) -> l + r), 10)
//                .infixl(op("-", (l, r) -> l - r), 10)
//                .infixl(op("*", ((l, r) -> l * r)), 20)
//                .infixl(op("/", (l, r) -> l / r), 20)
//                .prefix(op("-", v -> -v), 30)
                .build(unit);
        ref.set(parser);
        return parser;
    }


    public static final Parser<Double> CALCULATOR =
            calculator(NUMBER).from(TOKENIZER, IGNORED);


    public static void print_token(String s) {

        Object result = Parsers.or(TOKENIZER, Scanners.WHITESPACES.cast()).many().parse(s);


        System.out.println("s: " + s);
        System.out.println("token: " + result.toString());
//                Parsers.or(TOKENIZER, TOKENIZER).many().parse(s).toString());
//        System.out.print("s: " + "foo='abc' AND bar<>'def' OR (biz IN ['a', 'b', 'c'] AND NOT baz = 'foo')");
//        System.out.print("token: " + TOKENIZER.parse("foo='abc' AND bar<>'def' OR (biz IN ['a', 'b', 'c'] AND NOT baz = 'foo')").toString());
    }

}
