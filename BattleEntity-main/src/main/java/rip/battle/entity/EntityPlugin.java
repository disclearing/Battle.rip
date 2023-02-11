package rip.battle.entity;

import cc.stormworth.core.api.BattleAPI;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import com.jonahseguin.drink.command.DrinkCommandContainer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.commands.HologramCommand;
import rip.battle.entity.hologram.commands.sub.*;
import rip.battle.entity.hologram.runnable.HologramUpdateRunnable;
import rip.battle.entity.listeners.PlayerListener;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.commands.NPCCommand;
import rip.battle.entity.npc.commands.sub.*;
import rip.battle.entity.packet.PacketEntityUseAdapter;
import rip.battle.entity.taks.RainbowTask;
import rip.battle.entity.task.EntityVisibilityRunnable;
import rip.battle.entity.tick.TickableEntity;

@Getter
public class EntityPlugin extends JavaPlugin {

    private HologramManager hologramManager;
    private NPCManager npcManager;
    private BattleAPI battleAPI;

    @Override
    public void onEnable() {
        registerManagers();
        registerListeners();
        registerCommands();
        registerRunnables();
    }

    @Override
    public void onDisable() {
        hologramManager.saveHolograms();
        npcManager.saveNPCs();
    }

    private void registerManagers(){
        hologramManager = new HologramManager(this);
        npcManager = new NPCManager(this);
        battleAPI = BattleAPI.getBattleAPI(this);
    }

    private void registerRunnables(){
        getServer().getScheduler().runTaskTimerAsynchronously(this, new HologramUpdateRunnable(hologramManager), 0L, 1L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new RainbowTask(), 0L, 1L);
        getServer().getScheduler().runTaskTimer(this, new EntityVisibilityRunnable(), 0L, 20L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Entity entity : EntityManager.getEntities().values()) {

                if (entity instanceof TickableEntity){
                    TickableEntity livingEntity = (TickableEntity) entity;
                    livingEntity.tick();
                }

            }
        }, 0L, 1L);
    }

    private void registerCommands(){
        CommandService drink = Drink.get(this);

        DrinkCommandContainer hologramContainer = drink.register(new HologramCommand(), "hologram");

        hologramContainer.registerSub(new HologramCreateSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramDeleteSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramAddLineSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramAddItemLineSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramTeleportSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramMoveHereSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramSetLineSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramSetItemLineSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramListSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramRemoveLineSubCommand(hologramManager));
        hologramContainer.registerSub(new HologramEditSubCommand(this, hologramManager));


        DrinkCommandContainer npcContainer = drink.register(new NPCCommand(), "npc");
        npcContainer.registerSub(new NPCCreateSubCommand(npcManager));
        npcContainer.registerSub(new NPCDeleteSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetDisplaynameSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetSkinSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetArmorSubCommand(npcManager));
        npcContainer.registerSub(new NPCAddItemLineSubCommand(npcManager));
        npcContainer.registerSub(new NPCAddLineSubCommand(npcManager));
        npcContainer.registerSub(new NPCMoveHereSubCommand(npcManager));
        npcContainer.registerSub(new NPCTeleportSubCommand(npcManager));
        npcContainer.registerSub(new NPCRemoveLineSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetLineSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetItemInHandSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetHelmetSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetChestPlateSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetLeggingsSubCommand(npcManager));
        npcContainer.registerSub(new NPCSetBootsSubCommand(npcManager));
        npcContainer.registerSub(new NPCListCommand(npcManager));
        npcContainer.registerSub(new NPCEditSubCommand(this, npcManager));

        drink.registerCommands();
    }

    private void registerListeners(){
        battleAPI.registerAdapter(PacketPlayInUseEntity.class, new PacketEntityUseAdapter(this));

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
    }
}
