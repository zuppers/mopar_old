package org.mopar.game.markup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Created by eve on 1/11/2015
 */
@RunWith(JUnit4.class)
public final class MarkupReaderTest {

    @Test
    public void parseIntegerTest() {
        fail();
    }

    @Test
    public void parseDoubleTest() {
        fail();
    }

    @Test
    public void parseStringTest() {
        fail();
    }

    @Test
    public void parseBooleanTest() {
        fail();
    }

    @Test
    public void parseTupleTest() {
        fail();
    }

    @Test
    public void a() throws IOException {
        MarkupReader parser = new MarkupReader();
        parser.attach(new MarkupReaderHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onNestingUpdate(int diff) {
                System.out.println("diff: " + diff);
            }

            @Override
            public void onEnd() {

            }

            @Override
            public void parsedIdentifier(String value) {
                System.out.println("ident: " + value);
            }

            @Override
            public void parsedString(String value) {
                System.out.println("str: " + value);
            }

            @Override
            public void parsedInteger(int value) {
                System.out.println("int: " + value);
            }

            @Override
            public void parsedDouble(double value) {
                System.out.println("double: " + value);
            }

            @Override
            public void parsedBoolean(boolean value) {
                System.out.println("bool: " + value);
            }

            @Override
            public void parsedStartOfTuple() {
                System.out.println("tuple start");
            }

            @Override
            public void parsedEndOfTuple() {
                System.out.println("tuple end ");
            }
        });
        parser.parse(new StringReader("hi.\n\t\tbye."));
    }
}
