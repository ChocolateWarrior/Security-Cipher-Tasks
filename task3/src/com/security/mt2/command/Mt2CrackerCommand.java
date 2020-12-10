package com.security.mt2.command;

import com.security.util.ExampleCommand;
import com.security.utils.Bet;
import com.security.utils.Client;
import org.apache.commons.math3.random.MersenneTwister;

import java.lang.reflect.Field;
import java.util.Optional;

public class Mt2CrackerCommand implements ExampleCommand {
    private static final String PLAYER_ID = "PlayerHardMT";
    private static final String MODE = "BetterMt";

    @Override
    public void execute() throws Exception {
        Client.createAcc(PLAYER_ID);

        final int[] serverState = getState();

        final MersenneTwister serverGenerator = new MersenneTwister();

        setState(serverState, serverGenerator);

        final long tryValue1 = Integer.toUnsignedLong(serverGenerator.nextInt());
        final long tryValue2 = Integer.toUnsignedLong(serverGenerator.nextInt());

        System.out.println("try value1: " + tryValue1);
        System.out.println("try value2: " + tryValue2);

        final Optional<Bet> tryWinBet1 = Client.createBet(MODE, PLAYER_ID, 100, tryValue1);
        final Optional<Bet> tryWinBet2 = Client.createBet(MODE, PLAYER_ID, 1000, tryValue2);

        final Bet winBet1 = tryWinBet1.orElseThrow(NumberFormatException::new);
        final Bet winBet2 = tryWinBet2.orElseThrow(NumberFormatException::new);

        System.out.println(winBet1.toString());
        System.out.println(winBet2.toString());
    }

    private void setState(final int[] serverState, final MersenneTwister serverGenerator) {
        try {
            final Field state = serverGenerator.getClass()
                    .getDeclaredField("mt");
            state.setAccessible(true);

            state.set(serverGenerator, serverState);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private int[] getState() throws Exception {
        final int[] serverState = new int[624];

        for (int i = 0; i < 624; i++) {
            final Bet bet = Client.createBet(MODE, PLAYER_ID, 1, 1).get();
            serverState[i] = unTemp((int) bet.getRealNumber());
        }
        return serverState;
    }

    //    tempering
    //    y ^=  y >>> 11;
    //    y ^= (y <<   7) & 0x9d2c5680;
    //    y ^= (y <<  15) & 0xefc60000;
    //    y ^=  y >>> 18;
    private int unTemp(int x) {
        x = unshiftRight(x, 18);
        x = unshiftLeft(x, 15, 0xefc60000);
        x = unshiftLeft(x, 7, 0x9d2c5680);
        x = unshiftRight(x, 11);
        return x;
    }

    private int unshiftRight(int x, int shift) {
        int res = x;
        for (int i = 0; i < 32; i++) {
            res = x ^ res >>> shift;
        }
        return res;
    }

    private int unshiftLeft(int x, int shift, int mask) {
        int res = x;
        for (int i = 0; i < 32; i++) {
            res = x ^ (res << shift & mask);
        }
        return res;
    }
}