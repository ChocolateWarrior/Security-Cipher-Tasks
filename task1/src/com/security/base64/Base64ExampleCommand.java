package com.security.base64;

import com.security.util.ExampleCommand;

import static com.security.util.CiphersUtils.readFromFile;

public class Base64ExampleCommand implements ExampleCommand {
    public final void execute() {
        final Base64Decoder dec = new Base64Decoder();
        final String data = readFromFile("task1/src/resources/initialText.txt");

        final String decoded1 = dec.decode(data);
        final String decoded2 = dec.decode(decoded1);
        System.out.println(decoded2);
    }

    ;
}
