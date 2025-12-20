package com.finpro.kel10.Frontend.commands;

import com.finpro.kel10.Frontend.GameManager;
import com.finpro.kel10.Frontend.entities.Player;

public class RestartCommand implements  Command{
    @Override
    public void execute(Player player, float delta){
        player.reset();
        GameManager.getInstance().resetScore();
    }
}
