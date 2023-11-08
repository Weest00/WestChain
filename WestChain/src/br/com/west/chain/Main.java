package br.com.west.chain;

import br.com.west.chain.commands.ChainCommand;
import br.com.west.chain.database.SQLite;
import br.com.west.chain.database.SQLiteManager;
import br.com.west.chain.managers.PlayerChainManager;
import br.com.west.chain.runnables.SaveRunnable;
import br.com.west.chain.runnables.UpdateRunnable;
import br.com.west.chain.utils.Config;
import br.com.west.chain.utils.ItemBuilder;
import br.com.west.chain.utils.RegisterListeners;
import br.com.west.chain.utils.Vault;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    private static Main instance;
    private SQLite sqLite;
    private SQLiteManager sqLiteManager;
    private PlayerChainManager playerChainManager;

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        sqLite = new SQLite();
        sqLiteManager = new SQLiteManager();
        playerChainManager = new PlayerChainManager();
        RegisterListeners register = new RegisterListeners();
        register.setupListeners();
        Config.loadVariables();
        loadDatabase();
        loadTasks();
        getCommand("chain").setExecutor(new ChainCommand());


        if (!Vault.hasVault() || !Vault.hasEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§cPlugin de economia ou Vault não foram encontrados, desligando o plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void onDisable() {
        getSqLiteManager().save();
    }

    private void loadDatabase() {
        getSqLiteManager().create();
        getSqLiteManager().load();
    }

    private void loadTasks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new SaveRunnable(), 20 * 60 * 20, 20 * 60 * 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new UpdateRunnable(), 20, 20 * 60 * 15);
    }

    public void setEntradaChain(Player player) {
        getConfig().set("entrada", serializeLocation(player.getLocation()));
        saveConfig();
    }

    public void teleportChain(Player player) {
        player.teleport(deserializeLocation(getConfig().getString("entrada")));
    }

    public void setSaidaChain(Player player) {
        getConfig().set("saida", serializeLocation(player.getLocation()));
        saveConfig();
    }

    public void teleportSpawn(Player player) {
        player.teleport(deserializeLocation(getConfig().getString("saida")));
    }

    public void clearPlayer(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
    }

    private String serializeLocation(Location l) {
        return l.getWorld().getName() + ',' +
                l.getX() + ',' +
                l.getY() + ',' +
                l.getZ() + ',' +
                l.getYaw() + ',' +
                l.getPitch();
    }
    private Location deserializeLocation(String s) {
        String[] location = s.split(",");
        return new Location(
                Bukkit.getWorld(location[0]),
                Double.parseDouble(location[1]),
                Double.parseDouble(location[2]),
                Double.parseDouble(location[3]),
                Float.parseFloat(location[4]),
                Float.parseFloat(location[5]));
    }

    public void setKitChain(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.STONE_SWORD).enchantment(Enchantment.DURABILITY, 3).name("§aEspada Chain").lore("§7Faça uma baguncinha com esta espada!").build());
        player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE).amount(7).name("§aMaça Dourada").lore("§7Regenere sua vida!").build());
        player.getInventory().addItem(new ItemBuilder(Material.FISHING_ROD).enchantment(Enchantment.DURABILITY, 3).name("§aVara da Chain").lore("§7Irrite os jogadores!").build());
        player.getInventory().addItem(new ItemBuilder(Material.BOW).enchantment(Enchantment.DURABILITY, 3).name("§aArco da Chain").enchantment(Enchantment.DURABILITY, 3).lore("§7Mostre que você é bom de mira!").build());
        player.getInventory().setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).enchantment(Enchantment.DURABILITY, 3).name("§aPeitoral Chain").build());
        player.getInventory().setHelmet(new ItemBuilder(Material.CHAINMAIL_HELMET).enchantment(Enchantment.DURABILITY, 3).name("§aCapacete Chain").build());
        player.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).enchantment(Enchantment.DURABILITY, 3).name("§aBotas Chain").build());
        player.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).enchantment(Enchantment.DURABILITY, 3).name("§aCalça Chain").build());
        player.getInventory().addItem(new ItemBuilder(Material.ARROW).amount(32).name("§aFlechas").lore("§7Sua munição!").build());
    }
    public String format(Double value) {
        String[] suffix = {
                "K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N",
                "D", "UN", "DD",
                "TR", "QT", "QN", "SD", "SSD", "OD", "ND",
                "VG", "UVG", "DVG", "TVG", "QVG", "QVN", "SEV", "SPV", "OVG",
                "NVG",
                "TG"};
        int size = (value.intValue() != 0) ? (int) Math.log10(value.doubleValue()) : 0;
        if (size >= 3)
            while (size % 3 != 0)
                size--;
        double notation = Math.pow(10.0D, size);
        String result = (size >= 3) ? (
                String.valueOf(Math.round(value.doubleValue() / notation * 100.0D) / 100.0D) + suffix[size / 3 - 1]) : (
                new StringBuilder(String.valueOf(value.doubleValue()))).toString();
        return result;
    }

    public SQLite getSqLite() {
        return sqLite;
    }

    public SQLiteManager getSqLiteManager() {
        return sqLiteManager;
    }

    public PlayerChainManager getPlayerChainManager() {
        return playerChainManager;
    }

    public static Main getInstance() {
        return instance;
    }
}
