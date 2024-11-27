package es.mariaanasanz.ut7.mods.base;

import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public interface IUseItemEvent {

    public void onUseItem(LivingEntityUseItemEvent event);

}
