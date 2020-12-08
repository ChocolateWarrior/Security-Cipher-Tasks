package com.security;

import com.security.base64.Base64ExampleCommand;
import com.security.lcg.command.LcgCrackerCommand;
import com.security.mt1.command.MtCrackerCommand;
import com.security.util.ExampleCommand;

public class Lab2Main {

    public static void main(String[] args) throws Exception {
//        System.out.println(">>>>> LCG cracker");
//        final ExampleCommand lcgCrackerCommand = new LcgCrackerCommand();
//        lcgCrackerCommand.execute();

        System.out.println(">>>>> MT cracker");
        final ExampleCommand mtCrackerCommand = new MtCrackerCommand();
        mtCrackerCommand.execute();
    }
}