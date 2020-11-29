package com.security;

import com.security.base64.Base64Decoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.security.base64.Base64ExampleCommand;
import com.security.subtask1.command.DecipherCaesarXorExampleCommand;
import com.security.util.ExampleCommand;

public class Main {

    public static void main(String[] args) {
        System.out.println(">>>>> Base64");
        ExampleCommand base64ExampleCommand = new Base64ExampleCommand();
        base64ExampleCommand.execute();
        System.out.println(">>>>> Subtask1");
        ExampleCommand decipherCaesarCommand = new DecipherCaesarXorExampleCommand();
        decipherCaesarCommand.execute();
    }
}
