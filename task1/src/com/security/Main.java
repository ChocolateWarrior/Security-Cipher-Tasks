package com.security;

import com.security.base64.Base64Decoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.security.subtask1.command.DecipherCaesarXorExampleCommand;
import com.security.subtask2.command.Xor3ExampleCommand;
import com.security.util.ExampleCommand;

public class Main {

    public static void main(String[] args) {
        System.out.println(">>>>> Base64");
        final Base64Decoder dec = new Base64Decoder();
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get("task1/src/resources/initialText.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String decoded1 = dec.decode(data);
        final String decoded2 = dec.decode(decoded1);
        System.out.println(decoded2);


        System.out.println(">>>>> Subtask1");
        ExampleCommand decipherCaesarCommand = new DecipherCaesarXorExampleCommand();
        decipherCaesarCommand.execute();

        System.out.println(">>>>> Subtask2");
        ExampleCommand xor3Command = new Xor3ExampleCommand();
        xor3Command.execute();
    }
}

///
