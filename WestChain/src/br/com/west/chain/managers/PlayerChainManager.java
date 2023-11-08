package br.com.west.chain.managers;

import br.com.west.chain.models.PlayerChain;
import java.util.*;

public class PlayerChainManager {


    private final HashMap<String, PlayerChain> playerChain = new HashMap<>();
    private final HashSet<String> loadedPlayers = new HashSet<>();
    private final HashSet<String> activePlayers = new HashSet<>();
    private final HashSet<String> onChain = new HashSet<>();
    private final List<String> topKills = new ArrayList<>();
    private final List<String> topDeaths = new ArrayList<>();
    private final HashMap<String,Double> topKdr = new HashMap<>();
    public List<Map.Entry<String, Double>> ordenedTopKdr;

    public HashMap<String, PlayerChain> getPlayerChain() {
        return playerChain;
    }

    public HashSet<String> getOnChain() {
        return onChain;
    }


    public List<String> getTopKills() {
        return topKills;
    }

    public List<String> getTopDeaths() {
        return topDeaths;
    }


    public HashMap<String, Double> getTopKdr() {
        return topKdr;
    }

    public HashSet<String> getLoadedPlayers() {
        return loadedPlayers;
    }

    public HashSet<String> getActivePlayers() { return activePlayers; }

    public int getPlayerKills(String playerName) {
        return getPlayerChain().get(playerName).getKills();
    }

    public int getPlayerDeaths(String playerName) {
        return getPlayerChain().get(playerName).getDeaths();
    }

    public double getPlayerCoins(String playerName) {
        return getPlayerChain().get(playerName).getCoins();
    }

    public void addPlayerKills(String playerName, int kills) {
        getPlayerChain().get(playerName).setKills(getPlayerKills(playerName) + kills);
        getActivePlayers().add(playerName);
    }

    public void addPlayerDeaths(String playerName, int deaths) {
        getPlayerChain().get(playerName).setDeaths(getPlayerDeaths(playerName) + deaths);
        getActivePlayers().add(playerName);
    }

    public void addPlayerCoins(String playerName, double coins) {
        getPlayerChain().get(playerName).setCoins(getPlayerCoins(playerName) + coins);
        getActivePlayers().add(playerName);
    }

    public void withdrawPlayerCoins(String playerName, double coins){
        getPlayerChain().get(playerName).setCoins(getPlayerCoins(playerName) - coins);
        getActivePlayers().add(playerName);
    }
}
