package com.security.utils;

import java.time.LocalDateTime;

public class Account {
    private String id;
    private long money;
    private LocalDateTime deletionTime;

    public Account() {

    }

    public Account(final String id, final int money, final LocalDateTime deletionTime) {
        this.id = id;
        this.money = money;
        this.deletionTime = deletionTime;
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final long getMoney() {
        return money;
    }

    public final void setMoney(long money) {
        this.money = money;
    }

    public final LocalDateTime getDeletionTime() {
        return deletionTime;
    }

    public final void setDeletionTime(final LocalDateTime deletionTime) {
        this.deletionTime = deletionTime;
    }

    @Override
    public final String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", money=" + money +
                ", deletionTime=" + deletionTime +
                '}';
    }
}
