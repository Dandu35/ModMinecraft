package es.mariaanasanz.ut7.mods.base;

import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public interface ILivingDamageEvent {

    public void onLivingDamage(LivingDamageEvent event);

    public void onLivingDeath(LivingDeathEvent event);

}
