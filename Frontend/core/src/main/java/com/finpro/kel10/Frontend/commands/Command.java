package com.finpro.kel10.Frontend.commands;

import com.finpro.kel10.Frontend.entities.Player;

public interface Command {
    void execute(Player player, float dt);
}
