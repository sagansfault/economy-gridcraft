package net.gridcraft.economy;

import java.util.UUID;

/**
 * Object instead of map from uuid to int to allow more data to be stored in future
 */
public class EconomyPlayer {

    private int balance;
    private final UUID uuid;
    // whether to save this player to the database. something has been modified
    private boolean save = false;

    public EconomyPlayer(UUID uuid, int balance) {
        this.balance = balance;
        this.uuid = uuid;
    }

    public EconomyPlayer(UUID uuid) {
        this(uuid, 0);
    }

    public int getBalance() {
        return balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        this.flagForSave();
    }

    public void add(int count) {
        this.setBalance(this.balance + count);
    }

    /**
     * Defines whether this player's data should be saved in the economy database. Flipped to true on balance changes.
     */
    public void flagForSave() {
        this.save = true;
    }

    public boolean shouldSave() {
        return save;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EconomyPlayer that = (EconomyPlayer) o;
        return balance == that.balance &&
                uuid.equals(that.uuid);
    }
}
