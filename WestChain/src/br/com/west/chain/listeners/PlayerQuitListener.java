package br.com.west.chain.listeners;

import br.com.west.chain.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {


    @EventHandler
    void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (Main.getInstance().getPlayerChainManager().getOnChain().contains(playerName)) {
            Main.getInstance().getPlayerChainManager().getOnChain().remove(playerName);
            Main.getInstance().clearPlayer(player);
            }

    }
}
