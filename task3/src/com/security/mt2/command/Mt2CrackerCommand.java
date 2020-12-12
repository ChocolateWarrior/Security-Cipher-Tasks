package com.security.mt2.command;

import com.security.utils.Bet;
import com.security.utils.Client;
import com.security.utils.ExampleCommand;
import org.apache.commons.math3.random.MersenneTwister;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.security.utils.UntemperingUtils.unTemp;

public class Mt2CrackerCommand implements ExampleCommand {
    private static final String PLAYER_ID = "PlayerHardMT";
    private static final String MODE = "BetterMt";
    public static final int STATES_AMOUNT = 624;
    public static final int INTEGER_ONE = 1;
    public static final int INTEGER_32 = 32;
    public static final String MT = "mt";

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
                    .getDeclaredField(MT);
            state.setAccessible(true);

            state.set(serverGenerator, serverState);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private int[] getState() throws Exception {
        final int[] serverState = new int[STATES_AMOUNT];

        for (int i = 0; i < STATES_AMOUNT; i++) {
            final Bet bet = Client.createBet(MODE, PLAYER_ID, INTEGER_ONE, INTEGER_ONE).get();
            serverState[i] = unTemp((int) bet.getRealNumber());
        }
        return serverState;
    }


}