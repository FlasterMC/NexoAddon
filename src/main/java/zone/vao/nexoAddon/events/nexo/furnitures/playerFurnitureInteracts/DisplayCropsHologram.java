package zone.vao.nexoAddon.events.nexo.furnitures.playerFurnitureInteracts;

import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.utils.HologramUtil;

import static zone.vao.nexoAddon.events.nexo.furnitures.playerFurnitureInteracts.Fertilize.EVOLUTION_KEY;

public class DisplayCropsHologram {

  public static void onInteract(NexoFurnitureInteractEvent event){

    if(!NexoAddon.getInstance().getGlobalConfig().getBoolean("furniture_evolution_status", true))
      return;
    FurnitureMechanic furniture = NexoFurniture.furnitureMechanic(event.getBaseEntity());

    if(furniture == null || furniture.getEvolution() == null) return;

    double progress = (double) event.getBaseEntity().getPersistentDataContainer().get(EVOLUTION_KEY, PersistentDataType.INTEGER)
        / NexoFurniture.furnitureMechanic(event.getBaseEntity()).getEvolution().getDelay();
    HologramUtil.displayProgressBar(event.getBaseEntity(), Math.max(0.0, Math.min(1.0, progress)), event.getPlayer());
  }
}
