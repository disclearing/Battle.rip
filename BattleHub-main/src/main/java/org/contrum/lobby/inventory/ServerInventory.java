package org.contrum.lobby.inventory;

import fr.mrmicky.fastinv.FastInv;

/**
 * @author UKry
 * Created: 18/11/2022
 * Project BattleHub
 **/

public abstract class ServerInventory extends FastInv {
    public ServerInventory(String title, int size) {
        super(size, title);
    }

    public abstract void update();
}