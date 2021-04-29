package net.gridcraft.economy.database;

import net.gridcraft.economy.EconomyPlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public class EconomyDAO {

    /**
     * Retrieves all the player data from the economy database and constructs it into EconomyPlayer objects which are
     * stored in a set and returned as a set. Connections, PreparedStatements, and ResultSets are all closed once
     * used/completed here.
     *
     * @param info The database info to be used to access the database
     * @return A set of the all the retrieved and constructed player economy data
     */
    public static Set<EconomyPlayer> loadPlayers(DatabaseInfo info) {
        Set<EconomyPlayer> players = new HashSet<>();

        String query = "SELECT * FROM player_data;";
        try (Connection conn = DriverManager.getConnection(info.url(), info.getUser(), info.getPassword());
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString(1));
                int balance = rs.getInt(2);
                players.add(new EconomyPlayer(uuid, balance));
            }
        } catch (SQLException thrown) {
            thrown.printStackTrace();
        }

        return players;
    }

    /**
     * Loads a player's data from the economy database and constructs it into an EconomyPlayer. Connections,
     * PreparedStatements, and ResultSets are all closed once used/completed here.
     *
     * @param info The database info to be used to access the database
     * @param uuid The uuid of the player to load or construct
     * @return The retrieved player or a newly constructed one
     */
    public static EconomyPlayer loadPlayer(DatabaseInfo info, UUID uuid) {

        String query = "SELECT * FROM player_data WHERE uuid=? LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(info.url(), info.getUser(), info.getPassword());
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, uuid.toString());

            try (ResultSet rs = ps.executeQuery()) {
                // a nice way to check if there were no results without having to backtrack if there were some
                if (!rs.next()) {
                    return new EconomyPlayer(uuid);
                } else {
                    // found data
                    int balance = rs.getInt(2);
                    return new EconomyPlayer(uuid, balance);
                }
            }
        } catch (SQLException thrown) {
            thrown.printStackTrace();
        }

        return new EconomyPlayer(uuid);
    }

    /**
     * Saves the players in the set provided to the economy database. No checks are done for if the player is flagged for save.
     * Connections, PreparedStatements, and ResultSets are all closed once used/completed here.
     *
     * @param info The database info to be used to access the database
     * @param players The players to save to the database
     */
    public static void savePlayers(DatabaseInfo info, Set<EconomyPlayer> players) {

        String initialQuery = "UPDATE player_data SET balance=? WHERE uuid=?;";
        String insertQuery = "INSERT INTO player_data (uuid, balance) VALUES (?, ?);";

        // usually batch transaction would be used but for the way I plan on executing and updating this, I wont use them
        // as they complicate parsing the results
        for (EconomyPlayer player : players) {

            try (Connection conn = DriverManager.getConnection(info.url(), info.getUser(), info.getPassword());
                 PreparedStatement checkPS = conn.prepareStatement(initialQuery)) {

                checkPS.setInt(1, player.getBalance());
                checkPS.setString(2, player.getUuid().toString());

                int updated = checkPS.executeUpdate();
                if (updated == 0) {
                    try (PreparedStatement insertPS = conn.prepareStatement(insertQuery)) {
                        insertPS.setString(1, player.getUuid().toString());
                        insertPS.setInt(2, player.getBalance());

                        insertPS.executeUpdate();
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Could not save player economy data for: " + player.getUuid() + ":");
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Saves a singular player to the economy database. No checks are done for if the player is flagged for save.
     * Connections, PreparedStatements, and ResultSets are all closed once used/completed here.
     *
     * @param info The database info to be used to access the database
     * @param player The player to save to the database
     */
    public static void savePlayer(DatabaseInfo info, EconomyPlayer player) {
        savePlayers(info, new HashSet<>(Collections.singleton(player)));
    }
}
