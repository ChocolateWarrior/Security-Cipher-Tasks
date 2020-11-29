package com.security.subtask1.command;

import java.util.Arrays;
import java.util.List;

public class ExampleCommand {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";


    public void decipherXorCeasar(String ciphered) {
        String deciphered = "";
        List<String> charList = Arrays.asList(ciphered.split(""));


        System.out.println(deciphered);
    }
}