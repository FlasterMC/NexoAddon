package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.chorusblock.NexoChorusBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.breaks.DropExperienceListener;
import zone.vao.nexoAddon.events.nexo.blocks.breaks.InfestedListener;
import zone.vao.nexoAddon.events.nexo.blocks.breaks.MiningToolsListener;
import zone.vao.nexoAddon.events.nexo.blocks.breaks.ShiftBlockListener;
import zone.vao.nexoAddon.utils.BlockUtil;

public class NexoBlockBreakListener implements Listener {

  @EventHandler
  public void onNoteBlockBreak(NexoNoteBlockBreakEvent event) {

    BlockUtil.startDecay(event.getBlock().getLocation());
    MiningToolsListener.onBreak(event);
    DropExperienceListener.onBreak(event);
    InfestedListener.onBreak(event);
    ShiftBlockListener.onShiftBlockBreak(event);
  }

  @EventHandler
  public void onChorusBreak(NexoChorusBlockBreakEvent event) {

    MiningToolsListener.onBreak(event);
  }
}
