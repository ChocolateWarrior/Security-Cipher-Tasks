package com.security.subtask4.command;

import com.security.subtask4.process.PolyAlphabeticAlgorithmProcess;
import com.security.util.ExampleCommand;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.security.util.Constants.THREAD_POOL_SIZE;

// ADD THE ABILITY TO DECIPHER ANY KIND OF POLY ALPHABETIC SUBSTITUTION CIPHERS THE ONE USED IN THE CIPHERTEXTS
// HERE HAS TWENTY SIX INDEPENDENT RANDOMLY CHOSEN MONOALPHABETIC SUBSTITUTION PATTERNS FOR EACH LETTER FROM ENGLISH
// ALPHABET IT IS CLEAR THAT YOU CAN NO LONGER RELY ON THE SAME SIMPLE ROUTINE OF GUESSING THE KEY BY EXHAUSTING SEARCH
// WHICH YOU PROBABLY USED TO DECIPHER THE PREVIOUS PARAGRAPH BILL THE INDEX OF COINCIDENCE STILL WORK. AS A SUGGESTION
// YOU CAN TRY TO DIVIDE THE MESSAGE IN PARTS BY THE NUMBER OF CHARACTERS IN A KEY AND APPLY FREQUENCY ANALYSIS TO EACH
// OF THEM. CAN YOU FIND A WAY TO USE CIPHER ORDER FREQUENCY STATISTICS WITH THIS TYPE OF CIPHER THE NEXT MAGICAL WORD
// WILL TAKE TO THE NEXT LAB. ENJOY BITLY SLASH THREE FOUR CAPITAL D N CAPITAL BJ CAPITAL B

public class PolyAlphabeticExampleCommand implements ExampleCommand {

    @Override
    public void execute() throws InterruptedException {
        System.out.println(decipher());
    }

    private String decipher() throws InterruptedException {
        StringBuffer deciphered = new StringBuffer();

        List<PolyAlphabeticAlgorithmProcess> processes = createThreads();

        processes.forEach(Thread::start);
        Thread.sleep(6000);
        processes.forEach(x -> deciphered.append("\nkey: ")
                .append(x.getKeys())
                .append("\nresult: ")
                .append(x.getResult()));

        return deciphered.toString();
    }

    private List<PolyAlphabeticAlgorithmProcess> createThreads() {
        return IntStream.range(0, THREAD_POOL_SIZE)
                .mapToObj(x -> new PolyAlphabeticAlgorithmProcess())
                .collect(Collectors.toList());
    }



}
