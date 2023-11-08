package br.com.west.chain.commands;

import br.com.west.chain.Main;
import br.com.west.chain.models.PlayerChain;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        if (!(sender instanceof Player)) return true;


        Player player = (Player) sender;
        String playerName = player.getName();


        if (args.length == 0) {
            if (!Main.getInstance().getPlayerChainManager().getPlayerChain().containsKey(playerName)) {
                PlayerChain playerChain = new PlayerChain(playerName, 0, 0, 0);
                Main.getInstance().getPlayerChainManager().getPlayerChain().put(playerName, playerChain);

            }

            player.openInventory(Main.getInstance().getPlayerChainManager().getPlayerChain().get(playerName).openChainInventory());
            return false;

        }

        if (!player.hasPermission("westchain.admin")) {
            player.sendMessage("§eVocê não possui permissão, use apenas (/chain).");
            player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
            return true;
        }

        if (args[0].equalsIgnoreCase("setentrada")) {
            Main.getInstance().setEntradaChain(player);
            player.sendMessage("§eA entrada da arena chain foi setada com sucesso, certifique-se que a saida §etambém foi setada.");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            return true;

        }

        if (args[0].equalsIgnoreCase("setsaida")) {
            Main.getInstance().setSaidaChain(player);
            player.sendMessage("§eA saida da arena chain foi setada com sucesso, certifique-se que a entrada também foi setada.");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            return true;
        }

        player.sendMessage("§eErro, use apenas /chain (§fsetentrada,setsaida§e), para setar as localizações.");
        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
        return false;
    }
}
