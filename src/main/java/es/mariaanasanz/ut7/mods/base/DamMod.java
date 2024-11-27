package es.mariaanasanz.ut7.mods.base;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;

public abstract class DamMod {

    public static final String MOD_ID = "examplemod";

    public static final Logger LOGGER = LogUtils.getLogger();

    public DamMod() {
        // Nos "registramos" en el bus de eventos de minecraft para que capture nuestros eventos
        MinecraftForge.EVENT_BUS.register(this);
    }

    public abstract String autor();
}
