package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.constants.MessageSender;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public class BowMixin {

  @Unique
  private final static int THRESHOLD = 128;

  @Inject(method = "onStoppedUsing", at = @At("TAIL"))
  public void lowAmmoWarning(ItemStack bowStack, World world, LivingEntity entity, int i,
      CallbackInfoReturnable<Boolean> cir) {
    if (!(entity instanceof PlayerEntity player)) {
      return;
    }

    var inventory = player.getInventory();
    int copperArrows = countItem(Items.SPECTRAL_ARROW, inventory);
    int honingStones = countItem(Items.NETHERITE_SCRAP, inventory);
    int magicRunes = countItem(Items.PHANTOM_MEMBRANE, inventory);

    if (IslesHudHandler.inBoss) {
      if (copperArrows < THRESHOLD) {
        MessageSender.sendTitle("§4Low Arrows");
      }
      if (honingStones < THRESHOLD) {
        MessageSender.sendTitle("§4Low Stones");
      }
      if (magicRunes < THRESHOLD) {
        MessageSender.sendTitle("§4Low Runes");
      }
    }
  }

  @Unique
  private int countItem(Item item, PlayerInventory inventory) {
    AtomicInteger count = new AtomicInteger();
    inventory.forEach(currentStack -> {
      if (currentStack != null && currentStack.isOf(item)) {
        count.addAndGet(currentStack.getCount());
      }
    });
    return count.get();
  }
}
