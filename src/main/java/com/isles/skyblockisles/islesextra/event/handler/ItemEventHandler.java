package com.isles.skyblockisles.islesextra.event.handler;

import com.isles.skyblockisles.islesextra.bossrush.general.LowAmmoWarning;
import com.isles.skyblockisles.islesextra.client.CustomText;
import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import java.util.Optional;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.util.ActionResult;

public final class ItemEventHandler implements EventHandler {

  @Override
  public void register() {
    ItemTooltipCallback.EVENT.register(((stack, context, type, lines) -> {
      var nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
      if (nbtComponent == null) {
        return;
      }
      var nbt = nbtComponent.copyNbt();

      // TODO; figure out wtf this does again
      for (int i = 0; nbt.contains(IslesExtraClient.MOD_ID + ".lore." + i); i++) {
        Optional<String> newline = nbt.getString(IslesExtraClient.MOD_ID + ".lore." + i);
        newline.ifPresent(s -> lines.add(new CustomText(s).getValue()));
      }
    }));

    UseItemCallback.EVENT.register(((player, world, hand) -> {
      LowAmmoWarning.init();
      return ActionResult.PASS;
    }));
  }
}
