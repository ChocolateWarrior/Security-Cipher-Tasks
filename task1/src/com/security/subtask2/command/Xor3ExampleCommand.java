package com.security.subtask2.command;

import com.security.util.ExampleCommand;


//Now try a repeating-key XOR cipher. E.g. it should take a string "hello world" and, given the key is "key",
//xor the first letter "h" with "k", then xor "e" with "e", then "l" with "y", and then xor next char "l" with "k" again,
//then "o" with "e" and so on. You may use an index of coincidence, Hamming distance, Kasiski examination, statistical 
//tests or whatever method you feel would show the best result.

public class Xor3ExampleCommand implements ExampleCommand {
    
    @Override
    public void execute() {
    }
}
