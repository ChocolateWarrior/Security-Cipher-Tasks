package com.security;

import com.security.base64.Base64ExampleCommand;
import com.security.subtask1.command.DecipherCaesarXorExampleCommand;
import com.security.subtask2.command.Xor3ExampleCommand;
import com.security.util.ExampleCommand;

public class Main {

    public static void main(String[] args) throws Exception{
//        System.out.println(">>>>> Base64");
//        ExampleCommand base64ExampleCommand = new Base64ExampleCommand();
//        base64ExampleCommand.execute();
//
//        System.out.println(">>>>> Subtask1");
//        ExampleCommand decipherCaesarCommand = new DecipherCaesarXorExampleCommand();
//        decipherCaesarCommand.execute();

        System.out.println(">>>>> Subtask2");
        ExampleCommand xor3Command = new Xor3ExampleCommand();
        xor3Command.execute();
    }
}