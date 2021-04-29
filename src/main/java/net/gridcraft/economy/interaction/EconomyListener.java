package net.gridcraft.economy.interaction;

import net.gridcraft.economy.Economy;
import net.gridcraft.economy.EconomyPlayer;
import net.gridcraft.economy.database.EconomyDAO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EconomyListener implements Listener {

    // this should be run first so lowest
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Economy instance = Economy.getInstance();
        instance.getManager().register(EconomyDAO.loadPlayer(instance.getInfo(), event.getPlayer().getUniqueId()));
    }

    // this should be run first so lowest
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent event) {
        Economy instance = Economy.getInstance();
        EconomyPlayer player = instance.getManager().getPlayer(event.getPlayer().getUniqueId());
        EconomyDAO.savePlayer(instance.getInfo(), player);
        instance.getManager().unRegister(player);
    }
}
