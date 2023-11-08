package br.com.west.chain.listeners;

import br.com.west.chain.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandListener implements Listener {

    @EventHandler
    void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (Main.getInstance().getPlayerChainManager().getOnChain().contains(playerName) && !event.getMessage().equalsIgnoreCase("/chain")) {
            player.sendMessage("§cVocê não pode digitar nenhum comando enquanto estiver na arena chain, digite /chain para sair.");
            player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
            event.setCancelled(true);
        }

    }

}
