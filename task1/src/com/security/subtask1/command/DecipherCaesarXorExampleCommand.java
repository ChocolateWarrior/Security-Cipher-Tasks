package com.security.subtask1.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Write a piece of software to attack a single-byte XOR cipher which is the same as Caesar but with xor op.
//Yx`7cen7v7ergrvc~yp:|rn7OXE7t~g.re97R9p97~c7d.xb{s7cv|r7v7dce~yp75.r{{x7`xe{s57vys;7p~ary7c.r7|rn7~d75|rn5;7oxe7c.r7q~edc7{rccre75.57`~c.75|5;7c.ry7oxe75r57`~c.75r5;7c.ry75{57`~c.75n5;7vys7c.ry7oxe7yroc7t.ve75{57`~c.75|57vpv~y;7c.ry75x57`~c.75r57vys7dx7xy97Nxb7zvn7bdr7vy7~ysro7xq7tx~yt~srytr;7_vzz~yp7s~dcvytr;7\vd~d|~7rovz~yvc~xy;7dcvc~dc~tv{7crdcd7xe7`.vcrare7zrc.xs7nxb7qrr{7`xb{s7d.x`7c.r7urdc7erdb{c9

public class DecipherCaesarXorExampleCommand implements ExampleCommand{

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String CIPHERED = "Yx`7cen7v7ergrvc~yp:|rn7OXE7t~g.re97R9p97~c7d.xb{s7cv|r7v7dce~yp75.r{{x7`xe{s57vys;7p~ary7c.r7|rn7~d75|rn5;7oxe7c.r7q~edc7{rccre75.57`~c.75|5;7c.ry7oxe75r57`~c.75r5;7c.ry75{57`~c.75n5;7vys7c.ry7oxe7yroc7t.ve75{57`~c.75|57vpv~y;7c.ry75x57`~c.75r57vys7dx7xy97Nxb7zvn7bdr7vy7~ysro7xq7tx~yt~srytr;7_vzz~yp7s~dcvytr;7\\vd~d|~7rovz~yvc~xy;7dcvc~dc~tv{7crdcd7xe7`.vcrare7zrc.xs7nxb7qrr{7`xb{s7d.x`7c.r7urdc7erdb{c9";

    @Override
    public void execute() {
        decipherXorCaesar(CIPHERED);
    }

    private void decipherXorCaesar(String ciphered) {
        String deciphered = "";
        List<Character> charList = getChars(ciphered);

        List<String> variants = new ArrayList<>();

        for (byte xorCount = Byte.MIN_VALUE; xorCount < Byte.MAX_VALUE; xorCount++) {
            variants.add(xorEach(charList, xorCount));
        }

        printVariants(variants);
        System.out.println(deciphered);
    }

    private List<Character> getChars(String ciphered) {
        return Arrays.stream(ciphered.split(""))
                .map(x -> x.charAt(0))
                .collect(Collectors.toList());
    }

    //влоб
    private String xorEach(List<Character> ciphered, byte xorCount){
        StringBuilder res = new StringBuilder();
        for(Character c : ciphered) {
            res.append((char) (c ^ xorCount));
        }
        return res.toString();
    }

    private void printVariants(List<String> variants) {
        variants.forEach(x -> {
            System.out.println();
            System.out.println(x);
            System.out.println();
        });
    }
}