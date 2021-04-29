package net.gridcraft.economy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class EconomyManager {

    // No duplicates allowed
    private final Set<EconomyPlayer> players;

    public EconomyManager() {
        this.players = new HashSet<>();
    }

    public boolean register(EconomyPlayer player) {
        boolean exists = this.players.add(player);
        if (!exists) player.flagForSave();
        return exists;
    }

    public boolean register(UUID uuid) {
        return this.register(uuid, 0);
    }

    public boolean register(UUID uuid, int balance) {
        return this.register(new EconomyPlayer(uuid, balance));
    }

    public void unRegister(EconomyPlayer economyPlayer) {
        this.players.remove(economyPlayer);
    }

    /**
     * Gets a player based on their UUID if they are present in this management instance's list.
     * If one isn't found, one is made, marked for save, and returned.
     *
     * @param uuid The UUID of the player to find or make
     * @return The found player or a newly made one
     */
    public EconomyPlayer getPlayer(UUID uuid) {
        EconomyPlayer found = null;
        for (EconomyPlayer player : this.players) {
            if (player.getUuid().equals(uuid)) {
                found = player;
            }
        }

        if (found == null) {
            found = new EconomyPlayer(uuid, 0);
            found.flagForSave();
        }

        return found;
    }

    /**
     * Returns a set of all the players that should be saved. (ie.) the ones flagged for save in their object state
     *
     * @return A set of players that should be saved to the the DB
     */
    public Set<EconomyPlayer> getPlayersToSave() {
        return this.players.stream().filter(EconomyPlayer::shouldSave).collect(Collectors.toSet());
    }
}
