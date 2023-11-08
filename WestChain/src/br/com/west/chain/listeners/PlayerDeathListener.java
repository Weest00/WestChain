package br.com.west.chain.listeners;

import br.com.west.chain.Main;
import br.com.west.chain.managers.PlayerChainManager;
import br.com.west.chain.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {


    @EventHandler
    void onDeath(PlayerDeathEvent event) {
        Player playerKiller = event.getEntity().getPlayer().getKiller();
        Player playerDeath = event.getEntity().getPlayer();
        String playerKillerName = playerKiller.getName();
        String playerDeathName = playerDeath.getName();
        PlayerChainManager playerChain = Main.getInstance().getPlayerChainManager();


        if (playerChain.getOnChain().contains(playerDeathName) && playerChain.getOnChain().contains(playerDeathName)) {
            playerChain.addPlayerKills(playerKillerName, 1);
            playerChain.addPlayerDeaths(playerDeathName, 1);
            playerChain.getOnChain().remove(playerDeathName);

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§eO jogador §f" + playerKillerName + "§e acabou de matar");
            Bukkit.broadcastMessage("§eo infeliz do §f" + playerDeathName + "§e na arena chain.");
            Bukkit.broadcastMessage("");


            playerKiller.sendMessage("§eVocê matou o jogador §f" + playerDeathName + "§e na arena chain, sua vida foi regenerada §f+ §6✪§f" + Main.getInstance().format(Config.KILL_PRICE) + "§e no seu armazém.");
            playerDeath.sendMessage("§eVocê morreu para o jogador §f" + playerKillerName + "§e na arena chain.");
            playerKiller.setHealth(20);
            playerKiller.playSound(playerKiller.getLocation(), Sound.SUCCESSFUL_HIT,1F,1F);
            playerChain.addPlayerCoins(playerKillerName, Config.KILL_PRICE);

        }
    }


}

