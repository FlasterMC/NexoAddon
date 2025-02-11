package zone.vao.nexoAddon.events.player.interacts;

import com.nexomc.nexo.api.NexoItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.mechanic.BottledExp;
import zone.vao.nexoAddon.utils.EventUtil;

public class BottledExpListener implements Listener {

  @EventHandler
  public static void onBottledExp(PlayerInteractEvent event) {
    Action action = event.getAction();
    if(event.getHand() != EquipmentSlot.HAND
        || action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
    ItemStack item = event.getItem();
    Player player = event.getPlayer();
    String itemId = NexoItems.idFromItem(player.getInventory().getItemInMainHand());
    if(NexoAddon.getInstance().getMechanics() == null
        || item == null
        || itemId == null || itemId.isEmpty()
        || NexoAddon.getInstance().getMechanics().get(itemId) == null
        || NexoAddon.getInstance().getMechanics().get(itemId).getBottledExp() == null
    ) return;

    BottledExp bottledExp = NexoAddon.getInstance().getMechanics().get(itemId).getBottledExp();

    int bottleCount = BottledExp.convertXpToBottles(player.getLevel(), player.getExp(), bottledExp.ratio());
    if (bottleCount > 0) {
      ItemStack bottlesStack = new ItemStack(Material.EXPERIENCE_BOTTLE,
          BottledExp.convertXpToBottles(player.getLevel(), player.getExp(), bottledExp.ratio()));
      player.getWorld().dropItem(player.getLocation(), bottlesStack);
      player.setLevel(0);
      player.setExp(0);

      if (bottledExp.cost() > 0 && item.getItemMeta() instanceof Damageable damageable)
        EventUtil.callEvent(new PlayerItemDamageEvent(player, item, bottledExp.cost(), damageable.getDamage()));
    }else{
      player.sendMessage(MiniMessage.miniMessage().deserialize(NexoAddon.getInstance().getGlobalConfig().getString("messages.bottledexp.not_enough_exp", "<red>Not enough experience to convert")));
    }
  }
}
