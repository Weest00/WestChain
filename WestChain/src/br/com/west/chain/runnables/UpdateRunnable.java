package br.com.west.chain.runnables;

import br.com.west.chain.Main;
import br.com.west.chain.models.PlayerChain;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UpdateRunnable implements Runnable {
    @Override
    public void run() {
        Main.getInstance().getSqLiteManager().updateTop("kills", Main.getInstance().getPlayerChainManager().getTopKills());
        Main.getInstance().getSqLiteManager().updateTop("deaths", Main.getInstance().getPlayerChainManager().getTopDeaths());

        int count = 1;
        for (PlayerChain playerChain : Main.getInstance().getPlayerChainManager().getPlayerChain().values()) {
            double kdr = (double) playerChain.getKills() / (playerChain.getDeaths() == 0 ? 1 : playerChain.getDeaths());
            Main.getInstance().getPlayerChainManager().getTopKdr().put(playerChain.getPlayer(), kdr);
            count++;

            if (count>=11) break;
        }

        Stream<Map.Entry<String, Double>> ordenedStream = Main.getInstance().getPlayerChainManager().getTopKdr().entrySet().stream()
                .sorted((x, y) -> y.getValue().compareTo(x.getValue()));

        Main.getInstance().getPlayerChainManager().ordenedTopKdr = ordenedStream.collect(Collectors.toList());


    }
}
