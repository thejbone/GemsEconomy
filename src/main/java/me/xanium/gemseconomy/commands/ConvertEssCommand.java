package me.xanium.gemseconomy.commands;

import com.earth2me.essentials.Essentials;
import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.account.Account;
import me.xanium.gemseconomy.currency.Currency;
import me.xanium.gemseconomy.file.F;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.atomic.AtomicInteger;

public class ConvertEssCommand implements CommandExecutor {

    private final GemsEconomy plugin = GemsEconomy.getInstance();
    private Essentials ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String v21315, String[] args) {

        if (!sender.hasPermission("gemseconomy.command.convertess")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }
        Currency curr = null;
        AtomicInteger totalProcess = new AtomicInteger();

        if (args.length == 0) {
            try {
                curr = plugin.getCurrencyManager().getDefaultCurrency();
            } catch (Exception e){
                sender.sendMessage(e.getMessage());
            }
            sender.sendMessage("Currency: " + curr.getPlural());
            Currency finalCurr = curr;
            ess.getUserMap().getAllUniqueUsers().forEach((a) -> {
                if(plugin.getAccountManager().getAccount(a) != null){
                   Account acc = plugin.getAccountManager().getAccount(a);
                   acc.setBalance(finalCurr, ess.getUser(a).getMoney().doubleValue());
                } else {
                    Account acc = new Account(a, ess.getUser(a).getName());
                    acc.setBalance(finalCurr, ess.getUser(a).getMoney().doubleValue());
                    plugin.getAccountManager().add(acc);
                }
                sender.sendMessage("Adding " + ess.getUser(a).getName() + ": " + ess.getUser(a).getMoney() + " " + finalCurr.getPlural());
                totalProcess.getAndIncrement();
            });
            sender.sendMessage("Finished " + totalProcess + " of users");
        } else {
            sender.sendMessage(F.getUnknownSubCommand());
        }
        return true;
    }
}
