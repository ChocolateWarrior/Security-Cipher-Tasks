package com.security.mt1.command;

import com.security.utils.Bet;
import com.security.utils.Client;
import com.security.utils.ExampleCommand;
import org.apache.commons.math3.random.MersenneTwister;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.IntStream;

public class MtCrackerCommand implements ExampleCommand {
    private static final String PLAYER_ID = "PlayerEasyMT";
    private static final String MODE = "Mt";
    private static final int PERIOD = 5;
    public static final int INTEGER_3 = 3;
    public static final int BET_1 = 500;
    public static final int BET_2 = 600;

    @Override
    public void execute() throws Exception {
        Client.createAcc(PLAYER_ID);

        final Optional<Bet> tryBet1 = Client.createBet(MODE, PLAYER_ID, INTEGER_3, INTEGER_3);
        final long realNumber = tryBet1.orElseThrow(NumberFormatException::new).getRealNumber();
        final int time = (int) Instant.now().getEpochSecond();

        final MersenneTwister serverGenerator = IntStream.range(time - PERIOD, time + PERIOD)
                .mapToObj(MersenneTwister::new)
                .filter(generator -> Integer.toUnsignedLong(generator.nextInt()) == realNumber)
                .findFirst()
                .orElseThrow(NullPointerException::new);

        final Optional<Bet> tryWinBet1 = Client.createBet(MODE, PLAYER_ID, BET_1, serverGenerator.nextInt());
        final Optional<Bet> tryWinBet2 = Client.createBet(MODE, PLAYER_ID, BET_2, serverGenerator.nextInt());

        final Bet winBet1 = tryWinBet1.orElseThrow(NumberFormatException::new);
        final Bet winBet2 = tryWinBet2.orElseThrow(NumberFormatException::new);

        System.out.println(winBet1.toString());
        System.out.println(winBet2.toString());
    }
}