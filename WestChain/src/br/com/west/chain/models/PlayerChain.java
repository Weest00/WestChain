package br.com.west.chain.models;


import br.com.west.chain.Main;
import br.com.west.chain.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class PlayerChain {


    private final String player;
    private int kills;
    private int deaths;
    private double coins;


    public PlayerChain(String player, int kills, int deaths, double coins) {
        this.player = player;
        this.kills = kills;
        this.deaths = deaths;
        this.coins = coins;
    }

    public String getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public Inventory openChainInventory() {
        DecimalFormat number = new DecimalFormat("#,##0.00");
        double kdr = (double) kills / (deaths == 0 ? 1 : deaths);

        String haveCoins = coins == 0 ? "§7Você não possui nenhum saldo de chaincoins no armazém." : "§7Clique para converter §6✪" + Main.getInstance().format(coins) + "§7 chaincoins em saldo de coins.";
        String one = Main.getInstance().getPlayerChainManager().getOnChain().size() == 1 ? "§7 Atualmente possui §f" + Main.getInstance().getPlayerChainManager().getOnChain().size() + "§7 jogador na arena." : "§7 Atualmente possui §f" + Main.getInstance().getPlayerChainManager().getOnChain().size() + "§7 jogadores na arena.";
        String noOne = Main.getInstance().getPlayerChainManager().getOnChain().size() == 0 ? " §7Atualmente não possui nenhum jogador na arena." : one;

        Inventory inventory = Bukkit.createInventory(null, 27, "§7Chain");

        inventory.setItem(10, new ItemBuilder(Material.SKULL_ITEM).durability(3).owner(player).name("§eSuas informações").lore("§7Veja abaixo sua estatística na", "§7arena chain do servidor.", "", "§f Saldo de ChainCoins: §6✪" + Main.getInstance().format(coins), " §fAbates: §7" + kills, "§f Mortes: §7" + deaths, "§f Seu KDR: §7" + number.format(kdr)).build());

        inventory.setItem(11, new ItemBuilder(Material.ITEM_FRAME).name("§eTOP Jogadores").lore("§7Clique para visualizar o top jogadores.").build());

        inventory.setItem(12, new ItemBuilder(Material.WATCH).name("§eLoja Chain").lore("§7Clique para visualizar a loja.").build());


        if (coins <= 0) {
            inventory.setItem(14, new ItemBuilder(Material.WEB).name("§eConverter ChainCoins").lore(haveCoins).build());

        } else {
            inventory.setItem(14, new ItemBuilder(Material.STORAGE_MINECART).name("§eConverter ChainCoins").lore(haveCoins).build());
        }

        if (Main.getInstance().getPlayerChainManager().getOnChain().contains(player)) {
            inventory.setItem(15, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).name("§cSair da Chain").lore("", noOne, "", "§7Clique para sair da arena chain.").build());

        } else {

            inventory.setItem(15, new ItemBuilder(Material.FISHING_ROD).name("§eEntrar na Arena").lore("", noOne, "", "§eClique para entrar na arena chain.").build());
        }


        return inventory;

    }


    public Inventory openShopInv() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§7Chain - Loja");


        inventory.setItem(4, new ItemBuilder(Material.SKULL_ITEM).durability(3).owner(player).name("§eSuas informações").lore("§7Veja abaixo seu saldo de §fChainCoins§7, você", "§7pode gastar na loja ou transformar em coins.", "", "§f Saldo de ChainCoins: §6✪" + Main.getInstance().format(coins)).build());


        for (String key : Main.getInstance().getConfig().getConfigurationSection("loja").getKeys(false)) {
            int slot = Main.getInstance().getConfig().getInt("loja." + key + ".slot");
            List<String> lore = Main.getInstance().getConfig().getStringList("loja." + key + ".lore");
            String name = Main.getInstance().getConfig().getString("loja." + key + ".name").replace('&', '§');
            Material material = Material.getMaterial(Main.getInstance().getConfig().getString("loja." + key + ".material"));


            inventory.setItem(slot, new ItemBuilder(material).name(name).lore(lore).build());
        }

        inventory.setItem(40, new ItemBuilder(Material.ARROW).name("§eVoltar").lore("§7Clique para voltar a página anterior.").build());


        return inventory;


    }

    public Inventory openTopKillsInv() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§7Chain - TOP Abates");
        int[] slots = {
                9, 10, 11, 12, 13, 14, 15, 16, 21, 22,
                23};
        int count = 1;

        if (Main.getInstance().getPlayerChainManager().getTopKills().isEmpty()) {
            inventory.setItem(22, new ItemBuilder(Material.WEB).name("§cNenhum jogador disponível...").build());

        } else {
            for (String topKills : Main.getInstance().getPlayerChainManager().getTopKills()) {
                inventory.setItem(slots[count], new ItemBuilder(Material.SKULL_ITEM).durability(3).owner(topKills).name("§e" + count + "§eº: §7" + topKills).lore("§fSaldo de abates: §7" + Main.getInstance().getPlayerChainManager().getPlayerKills(topKills)).build());
                count++;

            }
        }
        inventory.setItem(40, new ItemBuilder(Material.ARROW).name("§eVoltar").lore("§7Clique para voltar a página anterior.").build());
        inventory.setItem(41, new ItemBuilder(Material.HOPPER).durability(1).name("§eFiltrar TOP").lore("§f▶ TOP Abates", "§7▶ TOP Mortes", "§7▶ TOP KDR", "", "§eClique para mudar de filtro.").build());
        return inventory;

    }

    public Inventory openTopDeathsInv() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§7Chain - TOP Mortes");
        int[] slots = {
                9, 10, 11, 12, 13, 14, 15, 16, 21, 22,
                23};
        int count = 1;

        if (Main.getInstance().getPlayerChainManager().getTopDeaths().isEmpty()) {
            inventory.setItem(22, new ItemBuilder(Material.WEB).name("§cNenhum jogador disponível...").build());

        } else {
            for (String topDeaths : Main.getInstance().getPlayerChainManager().getTopDeaths()) {
                inventory.setItem(slots[count], new ItemBuilder(Material.SKULL_ITEM).durability(3).owner(topDeaths).name("§e" + count + "§eº: §7" + topDeaths).lore("§fSaldo de mortes: §7" + Main.getInstance().getPlayerChainManager().getPlayerDeaths(topDeaths)).build());
                count++;

            }
        }
        inventory.setItem(40, new ItemBuilder(Material.ARROW).name("§eVoltar").lore("§7Clique para voltar a página anterior.").build());
        inventory.setItem(41, new ItemBuilder(Material.HOPPER).durability(1).name("§eFiltrar TOP").lore("§7▶ TOP Abates", "§f▶ TOP Mortes", "§7▶ TOP KDR", "", "§eClique para mudar de filtro.").build());
        return inventory;

    }

    public Inventory openTopKdrInv() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§7Chain - TOP KDR");
        DecimalFormat number = new DecimalFormat("#,##0.00");
        int[] slots = {
                9, 10, 11, 12, 13, 14, 15, 16, 21, 22,
                23};
        int count = 1;

        if (Main.getInstance().getPlayerChainManager().ordenedTopKdr.isEmpty()) {
            inventory.setItem(22, new ItemBuilder(Material.WEB).name("§cNenhum jogador disponível...").build());

        } else {
            for (Map.Entry<String, Double> entrada : Main.getInstance().getPlayerChainManager().ordenedTopKdr) {
                String player = entrada.getKey();
                Double kdr = entrada.getValue();
                inventory.setItem(slots[count], new ItemBuilder(Material.SKULL_ITEM).durability(3).owner(player).name("§e" + count + "§eº: §7" + player).lore("§fKDR atual: §7" + number.format(kdr)).build());
                count++;

            }
        }
        inventory.setItem(40, new ItemBuilder(Material.ARROW).name("§eVoltar").lore("§7Clique para voltar a página anterior.").build());
        inventory.setItem(41, new ItemBuilder(Material.HOPPER).durability(1).name("§eFiltrar TOP").lore("§7▶ TOP Abates", "§7▶ TOP Mortes", "§f▶ TOP KDR", "", "§eClique para mudar de filtro.").build());
        return inventory;

    }
}
