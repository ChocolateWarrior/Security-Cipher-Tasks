package com.security.lcg.command;

import com.security.utils.Bet;
import com.security.utils.Client;
import com.security.utils.ExampleCommand;

import java.math.BigInteger;
import java.util.Optional;

public class LcgCrackerCommand implements ExampleCommand {
    private static final String PLAYER_ID = "PlayerLCG";
    private static final String MODE = "Lcg";
    private static final long M = (long) Math.pow(2, 32);
    public static final int INTEGER_3 = 3;
    public static final int BET = 600;


    @Override
    public void execute() throws Exception {
        Client.createAcc(PLAYER_ID);

        final Optional<Bet> tryBet1 = Client.createBet(MODE, PLAYER_ID, INTEGER_3, INTEGER_3);
        final Optional<Bet> tryBet2 = Client.createBet(MODE, PLAYER_ID, INTEGER_3, INTEGER_3);
        final Optional<Bet> tryBet3 = Client.createBet(MODE, PLAYER_ID, INTEGER_3, INTEGER_3);

        final Bet bet1 = tryBet1.orElseThrow(NumberFormatException::new);
        final Bet bet2 = tryBet2.orElseThrow(NumberFormatException::new);
        final Bet bet3 = tryBet3.orElseThrow(NumberFormatException::new);

        System.out.println(bet1.toString());
        System.out.println(bet2.toString());
        System.out.println(bet3.toString());

        final Optional<Bet> tryWinBet = Client.createBet(MODE, PLAYER_ID, BET, calculateNextNumber(bet1.getRealNumber(), bet2.getRealNumber(), bet3.getRealNumber()));
        final Bet winBet = tryWinBet.orElseThrow(NumberFormatException::new);

        System.out.println(winBet.toString());
    }


    //  r2 = (a * r1 + c) % M
//  r3 = (a * r2 + c) % M
//  r3 - r2 = (a * (r2 - r1)) % M
//  (r3 - r2) % M= a * (r2 - r1)
//  a = ((r3 - r2) * (r2 - r1).modInverse(M)) % M
    private long calculateNextNumber(final long result1,
                                     final long result2,
                                     final long result3) {
        long a = ((result3 - result2) * (BigInteger.valueOf(result2 - result1).modInverse(BigInteger.valueOf(M))).longValue()) % M;
        long c = (result2 - result1 * a) % M;
        return (a * result3 + c) % M;
    }
}
