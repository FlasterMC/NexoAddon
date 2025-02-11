package zone.vao.nexoAddon.events.nexo.blocks.places;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import org.bukkit.inventory.ItemStack;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Mechanics;

import static zone.vao.nexoAddon.utils.BlockUtil.startShiftBlock;

public class ShiftBlockListener {
  public static void onShiftBlockPlace(final NexoNoteBlockPlaceEvent event) {

    Mechanics mechanics = NexoAddon.getInstance().getMechanics().get(event.getMechanic().getItemID());
    if(mechanics == null || mechanics.getShiftBlock() == null || !mechanics.getShiftBlock().onPlace()) return;

    CustomBlockMechanic customBlockMechanic = NexoBlocks.customBlockMechanic(mechanics.getShiftBlock().replaceTo());
    if(customBlockMechanic == null) return;
    ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
    if (
        (!mechanics.getShiftBlock().materials().isEmpty() && (mechanics.getShiftBlock().materials().contains(itemStack.getType())))
            && (!mechanics.getShiftBlock().nexoIds().isEmpty() && (!mechanics.getShiftBlock().nexoIds().contains(NexoItems.idFromItem(itemStack))))
    ) {
      return;
    }

    startShiftBlock(event.getBlock().getLocation(), customBlockMechanic, event.getMechanic(), mechanics.getShiftBlock().time());
  }
}
