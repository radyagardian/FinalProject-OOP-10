package com.finpro.kel10.Frontend.observers;

import com.finpro.kel10.Frontend.entities.Player;

public interface Observer {
    void onNotify(Player player, String event);
}
