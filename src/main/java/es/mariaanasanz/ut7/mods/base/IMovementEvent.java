package es.mariaanasanz.ut7.mods.base;

import net.minecraftforge.client.event.MovementInputUpdateEvent;

public interface IMovementEvent {

    public void onPlayerWalk(MovementInputUpdateEvent event);

}
