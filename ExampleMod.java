package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.DamMod;
import es.mariaanasanz.ut7.mods.base.ILivingDamageEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import java.util.Random;
@Mod(DamMod.MOD_ID)
public class ExampleMod extends DamMod implements  ILivingDamageEvent {

    public final int MAX_COFRE = 27;

    public ExampleMod() {
        super();
    }

    @Override
    public String autor() {
        return "David Andueza Ferro";
    }

    @Override
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        System.out.println("evento LivingDamageEvent invocado " + event.getEntity().getClass() + " provocado por " + event.getSource().getEntity());
    }

    @Override
    @SubscribeEvent
    // Este método se ejecuta cuando un ente (como un jugador o un monstruo) muere en el juego
    public void onLivingDeath(LivingDeathEvent event) {
        // Verificamos que la entidad que causó la muerte no sea nula
        if (event.getSource().getEntity() != null) {
            Level mundo = event.getEntity().getLevel();// Obtenemos el mundo en el que se juega
            Player jugador = (Player) event.getSource().getEntity();// Obtenemos el jugador que causó la muerte
            int levelJugador = jugador.experienceLevel;// Obtenemos el nivel de experiencia del jugador
            long horaDia = mundo.getDayTime();// Obtenemos el momento en el que se causó la muerte
            Vec3 coordenadasDead = event.getEntity().position(); // Obtenemos las coordenadas en las que se causó la muerte
            BlockPos posicionBloque = new BlockPos(coordenadasDead.x, coordenadasDead.y, coordenadasDead.z);// Creamos una posición de bloque a partir de las coordenadas de la muerte

            // Si el nivel del jugador es mayor que el máximo permitido para el cofre, lo establecemos al máximo
            if (levelJugador > MAX_COFRE) {
                levelJugador = MAX_COFRE;
            }

            // Si la entidad muerta es un esqueleto y es de día, creamos un cofre con un mapa del tesoro
            if (event.getEntity() instanceof Skeleton && horaDia < 12000) {
                BlockState chestState = Blocks.CHEST.defaultBlockState();// Creamos un estado de bloque para un cofre
                BlockPos chestPos = posicionBloque;// Establecemos la posición del cofre en las mismas coordenadas que la muerte
                BlockPos dirtPos = posicionBloque.above();// Establecemos la posición del bloque de tierra encima del cofre

                // Si el bloque encima del cofre está vacío, creamos el cofre y agregamos los objetos
                if (mundo.getBlockState(dirtPos).isAir()) {
                    // Creamos el cofre
                    mundo.setBlock(chestPos, chestState, 2);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    BlockEntity chest = mundo.getBlockEntity(chestPos);// Obtenemos la entidad de bloque del cofre recién creado

                    // Creamos un segundo cofre en una ubicación aleatoria cerca del primero
                    Random random = new Random();
                    int randomNumber = random.nextInt(50) + 1;
                    randomNumber += 1000;
                    randomNumber -= 950;
                    int chestX = chestPos.getX() + randomNumber;
                    int chestZ = chestPos.getZ() + randomNumber;
                    int chestY = chestPos.getY() - 1;

                    BlockPos chestPos2 = new BlockPos(chestX, chestY, chestZ);
                    mundo.setBlockAndUpdate(chestPos2, Blocks.CHEST.defaultBlockState());
                    BlockEntity blockEntity = mundo.getBlockEntity(chestPos2);
                    ChestBlockEntity chest1 = (ChestBlockEntity) blockEntity;

                    //Creamos el Libro donde estaran las coordenadas del cofre del tesoro.
                    ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
                    CompoundTag bookTag = new CompoundTag();
                    //Agregamos el autor del libro y el titulo
                    bookTag.putString("author", "David");
                    bookTag.putString("title", "Tesoro");
                    // Creamos una pagina en el que mostramos las coordenadas.
                    ListTag pages = new ListTag();
                    pages.add(StringTag.valueOf("El cofre se ha  generado:              " + "X:" + chestX +  "                  Y:" + chestY + "                  Z:" + chestZ));

                    bookTag.put("pages", pages);
                    book.setTag(bookTag);
                    ((ChestBlockEntity) chest).setItem(0, book);

                    ItemStack[] cofre = new ItemStack[MAX_COFRE];
                    for (int i = 0; i < MAX_COFRE ; i++) {
                            // Para las  posiciones del cofre, agregamos objetos aleatorios con una probabilidad basada en el nivel del jugador
                            Random random1 = new Random();
                            int probabilidad = random1.nextInt(100) + 1;
                            if (probabilidad <= levelJugador){
                                ItemStack contenido = new ItemStack(calcularItem());
                                cofre[i] = contenido;
                                ((ChestBlockEntity) chest1).setItem(i, contenido);
                            }

                    }
                    chest = mundo.getBlockEntity(chestPos);

                }
            }
        }
    }

    //Este código define cuatro conjuntos de elementos de Minecraft, que se clasifican en función de su rareza:
    // arrayImposibles, arrayEspeciales, arrayRaros, arrayNormales y arrayComunes.
    // Luego, el código genera un número aleatorio entre 1 y 1,000,000 (inclusive) usando un objeto Random.
    // Según el valor del número aleatorio, el código devuelve un elemento de una de las cuatro matrices.
    public Item calcularItem(){

        Item[] arrayImposibles = {Items.ELYTRA, Items.TRIDENT};

        Item[] arrayEspeciales = {Items.DIAMOND_BLOCK, Items.EMERALD_BLOCK, Items.TOTEM_OF_UNDYING, Items.NETHER_STAR, Items.DIAMOND_HORSE_ARMOR, Items.DRAGON_EGG,
                Items.END_CRYSTAL, Items.ENCHANTED_GOLDEN_APPLE, Items.HEART_OF_THE_SEA, Items.BLUE_SHULKER_BOX};

        Item[] arrayRaros = {Items.DIAMOND, Items.EMERALD, Items.SADDLE, Items.DRAGON_BREATH, Items.IRON_HORSE_ARMOR, Items.GOLDEN_HORSE_ARMOR, Items.EMERALD,
                Items.IRON_BLOCK, Items.LAPIS_BLOCK, Items.GOLD_BLOCK, Items.TURTLE_HELMET, Items.GLOWSTONE, Items.ENDER_PEARL, Items.OBSIDIAN, Items.GHAST_TEAR,
                Items.BLAZE_ROD, Items.MAGMA_CREAM, Items.NETHER_WART, Items.PRISMARINE_SHARD, Items.PRISMARINE_CRYSTALS, Items.SEA_LANTERN, Items.SHULKER_SHELL,
                Items.IRON_NUGGET, Items.GOLD_NUGGET, Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_SHOVEL, Items.DIAMOND_SWORD, Items.DIAMOND_AXE,
                Items.DIAMOND_SHOVEL, Items.DIAMOND_PICKAXE, Items.EMERALD_BLOCK, Items.GOLD_BLOCK, Items.IRON_BLOCK, Items.REDSTONE_BLOCK, Items.SLIME_BLOCK,
                Items.HONEY_BLOCK, Items.BREWING_STAND, Items.CAULDRON, Items.HOPPER, Items.PISTON, Items.DISPENSER, Items.DROPPER, Items.TRIPWIRE_HOOK, Items.OAK_BOAT,
                Items.CROSSBOW, Items.SHIELD, Items.CAMPFIRE, Items.LEAD, Items.TNT, Items.TNT_MINECART};

        Item[] arrayNormales = {Items.REDSTONE, Items.LAPIS_LAZULI, Items.IRON_PICKAXE, Items.GOLDEN_APPLE, Items.EXPERIENCE_BOTTLE, Items.FISHING_ROD,
                Items.BOW, Items.IRON_INGOT, Items.GOLD_INGOT, Items.BEACON, Items.SHIELD, Items.NAUTILUS_SHELL};

        Item[] arrayComunes = {Items.COOKED_BEEF, Items.PUMPKIN_PIE, Items.ARROW, Items.COAL, Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS,
                Items.MELON_SEEDS, Items.COCOA_BEANS, Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.CAKE, Items.STONE, Items.DIRT, Items.COBBLESTONE,
                Items.WARPED_FENCE, Items.OAK_LOG, Items.BIRCH_LOG, Items.SPRUCE_LOG, Items.JUNGLE_LOG, Items.ACACIA_LOG, Items.DARK_OAK_LOG, Items.SAND,
                Items.GRAVEL, Items.COAL, Items.IRON_INGOT, Items.GOLD_INGOT, Items.STICK, Items.STRING, Items.FEATHER, Items.LEATHER, Items.BEEF, Items.PORKCHOP,
                Items.CHICKEN, Items.EGG, Items.SUGAR, Items.WHEAT, Items.PAPER, Items.BOOK, Items.GLASS, Items.BRICK, Items.CLAY_BALL, Items.BONE, Items.GUNPOWDER};
        Random random = new Random();
        int ruleta = random.nextInt(1000) + 1;
        if (ruleta == 1) {
            return arrayImposibles[random.nextInt(arrayImposibles.length)];
        } else if (ruleta <= 11) {
            return arrayEspeciales[random.nextInt(arrayEspeciales.length)];
        } else if (ruleta <= 200) {
            return arrayRaros[random.nextInt(arrayRaros.length)];
        } else if (ruleta <= 500) {
            return arrayNormales[random.nextInt(arrayNormales.length)];
        } else {
            return arrayComunes[random.nextInt(arrayComunes.length)];
        }
    }

}
