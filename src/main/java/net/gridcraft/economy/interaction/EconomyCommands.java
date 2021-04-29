package net.gridcraft.economy.interaction;

import net.gridcraft.economy.Economy;
import net.gridcraft.economy.EconomyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {

        // console can send commands
        if (!cmd.getName().equalsIgnoreCase("econ") && !cmd.getName().equalsIgnoreCase("economy")) {
            return true;
        }

        if (sender instanceof Player && !sender.isOp()) return true;

        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            sender.sendMessage("-- Economy Commands --");
            sender.sendMessage("/econ bal <player> :: shows a players balance");
            sender.sendMessage("/econ set <player> <amount> :: sets a players balance (can be negative)");
            sender.sendMessage("/econ add <player> <amount> :: add a value to a players balance (can be negative)");
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("bal")) {
                Player p = Bukkit.getPlayer(args[1]);
                if (p == null || !p.isOnline()) {
                    sender.sendMessage("Could not find player or they are offline");
                    return true;
                }
                sender.sendMessage("Player " + p.getName() + "'s balance: " +
                        Economy.getInstance().getManager().getPlayer(p.getUniqueId()).getBalance());
            }
        } else if (args.length == 3) {

            Player p = Bukkit.getPlayer(args[1]);
            if (p == null || !p.isOnline()) {
                sender.sendMessage("Could not find player or they are offline");
                return true;
            }
            int amount;

            if (args[0].equalsIgnoreCase("set")) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException thrown) {
                    sender.sendMessage("Non-numerical amount entered");
                    return true;
                }

                EconomyPlayer economyPlayer = Economy.getInstance().getManager().getPlayer(p.getUniqueId());
                economyPlayer.setBalance(amount);
            } else if (args[0].equalsIgnoreCase("add")) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException thrown) {
                    sender.sendMessage("Non-numerical amount entered");
                    return true;
                }

                EconomyPlayer economyPlayer = Economy.getInstance().getManager().getPlayer(p.getUniqueId());
                economyPlayer.add(amount);
            }
        } else {
            sender.sendMessage("Invalid command, see /econ help");
        }

        return true;
    }
}
