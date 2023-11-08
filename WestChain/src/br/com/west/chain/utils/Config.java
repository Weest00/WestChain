package br.com.west.chain.utils;

import br.com.west.chain.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static double KILL_PRICE;

    public static void loadVariables() {
        KILL_PRICE = getConfig().getDouble("opcoes.kill_price");
    }

    public static FileConfiguration getConfig() {
        return Main.getInstance().getConfig();
    }



}

