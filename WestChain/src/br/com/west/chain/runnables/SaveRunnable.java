package br.com.west.chain.runnables;

import br.com.west.chain.Main;

public class SaveRunnable implements Runnable{
    @Override
    public void run() {
        Main.getInstance().getSqLiteManager().save();
        Main.getInstance().getPlayerChainManager().getActivePlayers().clear();
    }
}
