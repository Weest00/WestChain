package br.com.west.chain.database;

import br.com.west.chain.Main;
import br.com.west.chain.models.PlayerChain;
import org.bukkit.Bukkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class SQLiteManager {

    public void create() {
        try {
            PreparedStatement createStatement = connection().prepareStatement("CREATE TABLE IF NOT EXISTS west_chain (player TEXT,kills INTEGER,deaths INTEGER,coins DOUBLE)");
            createStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Main.getInstance().getSqLite().disconnect();
        }
    }

    public void load() {
        try {
            PreparedStatement stm = connection().prepareStatement("SELECT * FROM west_chain");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String player = rs.getString("player");
                int kills = rs.getInt("kills");
                int deaths = rs.getInt("deaths");
                double coins = rs.getDouble("coins");
                PlayerChain playerChain = new PlayerChain(player, kills, deaths, coins);
                Main.getInstance().getPlayerChainManager().getPlayerChain().put(player, playerChain);
                Main.getInstance().getPlayerChainManager().getLoadedPlayers().add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Main.getInstance().getSqLite().disconnect();
        }
    }

    public void save() {
        int count = 0;
        try (PreparedStatement insertStatement = connection().prepareStatement("INSERT INTO west_chain (player,kills,deaths,coins) VALUES (?,?,?,?)")) {
            try (PreparedStatement updateStatement = connection().prepareStatement("UPDATE west_chain SET kills =?,deaths =?,coins =? WHERE player =?")) {
                for (PlayerChain playerChain : Main.getInstance().getPlayerChainManager().getPlayerChain().values()) {
                    if (!Main.getInstance().getPlayerChainManager().getActivePlayers().contains(playerChain.getPlayer())) {
                        continue;
                    }
                    if (Main.getInstance().getPlayerChainManager().getLoadedPlayers().contains(playerChain.getPlayer())) {
                        updateStatement.setInt(1, playerChain.getKills());
                        updateStatement.setInt(2, playerChain.getDeaths());
                        updateStatement.setDouble(3, playerChain.getCoins());
                        updateStatement.setString(4, playerChain.getPlayer());
                        updateStatement.executeUpdate();
                    } else {
                        insertStatement.setString(1, playerChain.getPlayer());
                        insertStatement.setInt(2, playerChain.getKills());
                        insertStatement.setInt(3, playerChain.getDeaths());
                        insertStatement.setDouble(4, playerChain.getCoins());
                        insertStatement.executeUpdate();
                    }
                    count++;
                }

                Bukkit.getLogger().info("[SQLITE] Foram atualizados " + count + " jogadores na database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Main.getInstance().getSqLite().disconnect();
        }
    }
    public void updateTop(String colum, List<String> list) {
        if (!list.isEmpty())
            list.clear();
        try {
            PreparedStatement updateTopStatement = connection().prepareStatement("SELECT * FROM west_chain ORDER BY " + colum + " DESC LIMIT 10");
            ResultSet rs = updateTopStatement.executeQuery();
            while (rs.next()) {
                String player = rs.getString("player");
                list.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Main.getInstance().getSqLite().disconnect();
        }
    }
    public Connection connection() {
        return Main.getInstance().getSqLite().getConnection();
    }
}
