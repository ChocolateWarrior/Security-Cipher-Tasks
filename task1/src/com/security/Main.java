package com.security;

import com.security.base64.Base64ExampleCommand;
import com.security.subtask1.command.DecipherCaesarXorExampleCommand;
import com.security.subtask2.command.Xor3ExampleCommand;
import com.security.subtask3.command.SubstitutionExampleCommand;
import com.security.subtask4.command.PolyAlphabeticExampleCommand;
import com.security.util.ExampleCommand;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println(">>>>> Base64");
        ExampleCommand base64ExampleCommand = new Base64ExampleCommand();
//        base64ExampleCommand.execute();

        System.out.println(">>>>> Subtask1");
        ExampleCommand decipherCaesarCommand = new DecipherCaesarXorExampleCommand();
//        decipherCaesarCommand.execute();

        System.out.println(">>>>> Subtask2");
        ExampleCommand xor3Command = new Xor3ExampleCommand();
//        xor3Command.execute();

        System.out.println(">>>>> Subtask3");
        ExampleCommand substitutionCommand = new SubstitutionExampleCommand();
//        substitutionCommand.execute();

        System.out.println(">>>>> Subtask4");
        ExampleCommand poly = new PolyAlphabeticExampleCommand();
        poly.execute();

//        PolyAlphabeticAlgorithmProcess p = new PolyAlphabeticAlgorithmProcess();
//        List<List<Character>> cipherList = new ArrayList<>();
//        List<Character> list1 = Arrays.stream("DEFGHIJKLMNOPQRSTUVWXYZABC".split("")).map(x -> x.charAt(0)).collect(Collectors.toList());
//        List<Character> list2 = Arrays.stream("GHIJKLMNOPQRSTUVWXYZABCDEF".split("")).map(x -> x.charAt(0)).collect(Collectors.toList());
//        cipherList.add(list1);
//        cipherList.add(list2);
//        System.out.println(p.decodeBySubstitutionGrouped("PGUQNXDS", cipherList));

    }
//        private String decodeBySubstitutionGrouped(String cipher, List<List<Character>> groupedKeys)
}