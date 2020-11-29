package com.security;

import com.security.subtask1.command.DecipherCaesarXorExampleCommand;
import com.security.subtask1.command.ExampleCommand;

public class Main {

    public static void main(String[] args) {
        System.out.println("hello");
        ExampleCommand decipherCaesarCommand = new DecipherCaesarXorExampleCommand();
        decipherCaesarCommand.execute();
    }
}
