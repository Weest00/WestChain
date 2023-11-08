package br.com.west.chain.listeners;

import br.com.west.chain.Main;
import br.com.west.chain.models.PlayerChain;
import br.com.west.chain.utils.ActionBar;
import br.com.west.chain.utils.Vault;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {


    @EventHandler
    void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String playerName = player.getName();
        Inventory inventory = event.getClickedInventory();
        PlayerChain playerChain = Main.getInstance().getPlayerChainManager().getPlayerChain().get(playerName);
        int slot = event.getSlot();

        if (inventory == null || event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;


        if (inventory.getTitle().equalsIgnoreCase("§7Chain")) {
            event.setCancelled(true);

            switch (slot) {
                case 11:
                    player.openInventory(playerChain.openTopKillsInv());
                    break;

                case 12:
                    if (Main.getInstance().getPlayerChainManager().getOnChain().contains(playerName)) {
                        ActionBar.send(player, "§cVocê não pode acessar o menu de recompensas dentro da arena chain.");
                        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
                        player.closeInventory();
                    } else {
                        player.openInventory(playerChain.openShopInv());
                    }

                    break;

                case 14:
                    if (playerChain.getCoins() <= 0) {
                        ActionBar.send(player, "§cVocê não possui ChainCoins para converter no momento.");
                        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);

                    } else {
                        ActionBar.send(player, "§aVoce acabou de receber §2$§f" + Main.getInstance().format(playerChain.getCoins()) + "§a de coins convertidos.");
                        Vault.getEconomy().depositPlayer(player, playerChain.getCoins());
                        Main.getInstance().getPlayerChainManager().withdrawPlayerCoins(playerName, playerChain.getCoins());
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);

                    }
                    player.closeInventory();
                    break;


            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eEntrar na Arena")) {
                if (Main.getInstance().getConfig().getString("entrada") == null) {
                    ActionBar.send(player, "§cA arena chain ainda não foi definida, contate um administrador.");
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
                    player.closeInventory();
                    return;
                }

                if (Main.getInstance().getConfig().getString("saida") == null) {
                    ActionBar.send(player, "§cA arena chain ainda não foi definida, contate um administrador.");
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
                    player.closeInventory();
                    return;

                }

                if (!isInventoryEmpty(player)) {
                    ActionBar.send(player, "§cEsvazie seu inventário para entrar na arena chain.");
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
                    player.closeInventory();
                    return;

                }

                if (!hasArmor(player)) {
                    ActionBar.send(player, "§cEsvazie seu inventário para entrar na arena chain.");
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
                    player.closeInventory();
                    return;
                }

                ActionBar.send(player, "§aVocê foi teleportado até a arena chain, boa sorte!");
                Main.getInstance().teleportChain(player);
                Main.getInstance().setKitChain(player);
                Main.getInstance().getPlayerChainManager().getOnChain().add(playerName);
                player.sendMessage("§eVocê entrou na arena §fchain §ecom sucesso, boa sorte!");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                player.closeInventory();
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cSair da Chain")) {
                ActionBar.send(player, "§aVocê saiu da arena chain!");
                Main.getInstance().getPlayerChainManager().getOnChain().remove(playerName);
                Main.getInstance().teleportSpawn(player);
                Main.getInstance().clearPlayer(player);
                player.sendMessage("§eVocê saiu da arena §fchain §ecom sucesso.");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                player.closeInventory();


            }
        }

        if (inventory.getTitle().equalsIgnoreCase("§7Chain - TOP Abates")) {
            event.setCancelled(true);

            switch (slot) {
                case 40:
                    player.openInventory(playerChain.openChainInventory());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                    break;

                case 41:
                    player.openInventory(playerChain.openTopDeathsInv());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                    break;
            }
        }

        if (inventory.getTitle().equalsIgnoreCase("§7Chain - TOP Mortes")) {
            event.setCancelled(true);

            switch (slot) {
                case 40:
                    player.openInventory(playerChain.openChainInventory());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                    break;

                case 41:
                    player.openInventory(playerChain.openTopKdrInv());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                    break;
            }
        }

        if (inventory.getTitle().equalsIgnoreCase("§7Chain - TOP KDR")) {
            event.setCancelled(true);

            switch (slot) {
                case 40:
                    player.openInventory(playerChain.openChainInventory());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                    break;

                case 41:
                    player.openInventory(playerChain.openTopKillsInv());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                    break;
            }
        }


        if (inventory.getTitle().equalsIgnoreCase("§7Chain - Loja")) {
            event.setCancelled(true);

            for (String key : Main.getInstance().getConfig().getConfigurationSection("loja").getKeys(false)) {

                if (slot == Main.getInstance().getConfig().getInt("loja." + key + ".slot")) {
                    double price = Main.getInstance().getConfig().getDouble("loja." + key + ".price");
                    String command = Main.getInstance().getConfig().getString("loja." + key + ".command").replace("{player}", playerName);


                    if (playerChain.getCoins() < price) {
                        double totalPrice = price - playerChain.getCoins();
                        ActionBar.send(player, "§cVocê precisa de mais §6✪§f" + Main.getInstance().format(totalPrice) + "§c ChainCoins para comprar este item.");
                        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
                    } else {
                        ActionBar.send(player, "§aVocê comprou um item na loja da chain por §6✪" + Main.getInstance().format(price) + "§a.");
                        Main.getInstance().getPlayerChainManager().withdrawPlayerCoins(playerName, price);
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                        Bukkit.getConsoleSender().getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    }

                    player.closeInventory();

                }
            }

            if (slot == 40) {
                player.openInventory(playerChain.openChainInventory());
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            }

        }
    }


    private boolean isInventoryEmpty(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            {
                if (item != null)
                    return false;
            }
        }
        return true;

    }

    private boolean hasArmor(Player player) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            {
                if (item.getType() != Material.AIR)
                    return false;
            }
        }
        return true;
    }

}
