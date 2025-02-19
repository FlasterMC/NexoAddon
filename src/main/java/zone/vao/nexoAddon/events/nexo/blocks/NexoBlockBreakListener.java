package zone.vao.nexoAddon.events.nexo.blocks;

import com.nexomc.nexo.api.events.custom_block.NexoBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.chorusblock.NexoChorusBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.nexoAddon.events.nexo.blocks.breaks.*;
import zone.vao.nexoAddon.utils.BlockUtil;

public class NexoBlockBreakListener implements Listener {

  @EventHandler
  public void onNoteBlockBreak(NexoNoteBlockBreakEvent event) {

    BlockUtil.startDecay(event.getBlock().getLocation());
    MiningToolsListener.onBreak(event);
    DropExperienceListener.onBreak(event);
    InfestedListener.onBreak(event);
    ShiftBlockListener.onShiftBlockBreak(event);
    BlockAuraListener.onBlockBreak(event);
  }

  @EventHandler
  public void onChorusBreak(NexoChorusBlockBreakEvent event) {
    MiningToolsListener.onBreak(event);
    BlockAuraListener.onBlockBreak(event);
  }

  @EventHandler
  public void onStringBreak(NexoStringBlockBreakEvent event) {
    BlockAuraListener.onBlockBreak(event);
  }
}
