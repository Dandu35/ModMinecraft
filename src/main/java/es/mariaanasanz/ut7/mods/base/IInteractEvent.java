package es.mariaanasanz.ut7.mods.base;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface IInteractEvent {

    public void onPlayerTouch(PlayerInteractEvent.RightClickBlock event);

}
