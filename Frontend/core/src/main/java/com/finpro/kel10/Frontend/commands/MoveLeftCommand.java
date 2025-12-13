package com.finpro.kel10.Frontend.commands;

import com.finpro.kel10.Frontend.entities.Player;

public class MoveLeftCommand implements Command{
    @Override
    public void execute(Player player, float dt){
        player.moveLeft(dt);
    }
}
