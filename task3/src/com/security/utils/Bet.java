package com.security.utils;

public class Bet {
    private String message;
    private Account account;
    private long realNumber;

    public Bet() {

    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(final String message) {
        this.message = message;
    }

    public final Account getAccount() {
        return account;
    }

    public final void setAccount(final Account account) {
        this.account = account;
    }

    public final long getRealNumber() {
        return realNumber;
    }

    public final void setRealNumber(final long realNumber) {
        this.realNumber = realNumber;
    }

    @Override
    public final String toString() {
        return "Bet{" +
                "message='" + message + '\'' +
                ", account=" + account +
                ", realNumber=" + realNumber +
                '}';
    }
}
