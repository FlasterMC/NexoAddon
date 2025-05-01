package zone.vao.nexoAddon.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.populators.orePopulator.CustomOrePopulator;

import java.util.ArrayList;

public class WorldLoadListener implements Listener {

  @EventHandler
  public void onWorldLoad(WorldInitEvent event) {

    applyPopulator(event);
  }

  @EventHandler
  public void onWorldLoad(WorldLoadEvent event) {

    applyPopulator(event);
  }

  private void applyPopulator(WorldEvent event) {
    NexoAddon.getInstance().foliaLib.getScheduler().runLater(() -> {
      NexoAddon.getInstance().getOrePopulator().getOres().forEach(ore -> {
        if(ore.getNexoFurniture() != null) return;
        for (String worldName : ore.getWorldNames()) {
          if(!worldName.equals(event.getWorld().getName())) return;
          CustomOrePopulator customOrePopulator = new CustomOrePopulator(NexoAddon.getInstance().getOrePopulator());
          if(!NexoAddon.getInstance().getWorldPopulators().containsKey(worldName)) {
            NexoAddon.getInstance().getWorldPopulators().put(worldName, new ArrayList<>());
          }
          NexoAddon.getInstance().addPopulatorToWorld(event.getWorld(), customOrePopulator);
          NexoAddon.getInstance().getWorldPopulators().get(event.getWorld().getName()).add(customOrePopulator);
          NexoAddon.getInstance().logPopulatorAdded("BlockPopulator", ore.getId(), event.getWorld());
        }
      });
    }, 20L*5);
  }

  @EventHandler
  public void onWorldLoad(WorldUnloadEvent event) {

    NexoAddon.getInstance().removePopulators(event.getWorld());

  }
}
