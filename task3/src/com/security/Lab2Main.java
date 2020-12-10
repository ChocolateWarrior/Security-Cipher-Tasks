package com.security;

import com.security.lcg.command.LcgCrackerCommand;
import com.security.mt1.command.MtCrackerCommand;
import com.security.mt2.command.Mt2CrackerCommand;
import com.security.util.ExampleCommand;

public class Lab2Main {

    public static void main(String[] args) throws Exception {
        System.out.println(">>>>> LCG cracker");
        final ExampleCommand lcgCrackerCommand = new LcgCrackerCommand();
        lcgCrackerCommand.execute();

        System.out.println(">>>>> MT cracker");
        final ExampleCommand mtCrackerCommand = new MtCrackerCommand();
        mtCrackerCommand.execute();

        System.out.println(">>>>> MT2 cracker");
        final ExampleCommand mt2CrackerCommand = new Mt2CrackerCommand();
        mt2CrackerCommand.execute();
    }
}