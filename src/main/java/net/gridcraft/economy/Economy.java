package net.gridcraft.economy;

import net.gridcraft.economy.database.DatabaseInfo;
import net.gridcraft.economy.database.EconomyDAO;
import net.gridcraft.economy.interaction.EconomyCommands;
import net.gridcraft.economy.interaction.EconomyListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Economy extends JavaPlugin {

    private static Economy instance;
    public static Economy getInstance() {
        return instance;
    }

    private DatabaseInfo info;
    private EconomyManager manager;

    @Override
    public void onEnable() {
        instance = this;
        // usually init this in a config
        info = new DatabaseInfo("root", "password", "localhost", "3306", "economy");
        manager = new EconomyManager();

        getServer().getPluginManager().registerEvents(new EconomyListener(), instance);
        instance.getCommand("econ").setExecutor(new EconomyCommands());
    }

    @Override
    public void onDisable() {
        EconomyDAO.savePlayers(this.info, manager.getPlayersToSave());
    }

    public EconomyManager getManager() {
        return manager;
    }

    public DatabaseInfo getInfo() {
        return info;
    }
}
