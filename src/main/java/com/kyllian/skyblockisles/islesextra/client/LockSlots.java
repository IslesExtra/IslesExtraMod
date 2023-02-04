package com.kyllian.skyblockisles.islesextra.client;

import com.kyllian.skyblockisles.islesextra.annotation.OnIslesJoin;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesLeave;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class LockSlots {

    private static MinecraftClient client;
    public static KeyBinding lockKey;

    public static void init() {
        client = MinecraftClient.getInstance();
        lockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.skyblockisles.lock",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.skyblockisles.islesextra"
        ));
    }

    public static void lockSlot(Slot slot) {
        if (!(slot.inventory instanceof PlayerInventory)) return;
        if (LOCKED_SLOTS.contains(slot.getIndex())) LOCKED_SLOTS.remove((Integer) slot.getIndex());
        else LOCKED_SLOTS.add(slot.getIndex());
        System.out.println(LOCKED_SLOTS);
    }

    @OnIslesJoin
    public static void loadLocks() {
        System.out.println("Loading locked slots");
        if (bytes==null || bytes.length==0) return;
        BitSet set = BitSet.valueOf(bytes);
        boolean[] bools = new boolean[bytes.length * 8];
        for (int i = set.nextSetBit(0); i != -1; i = set.nextSetBit(i+1))
            bools[i] = true;
        System.out.println("Boolean array: " + Arrays.toString(bools));
        for (int i = 0; i<42; i++) {
            if (bools[i]) LOCKED_SLOTS.add(i);
        }
    }

    private static byte[] bytes;

    @OnIslesLeave
    public static void saveLocks() {
        System.out.println("Saving locked slots");
        BitSet bits = new BitSet(42);
        for (int i = 0; i < 42; i++) {
            bits.set(i, LOCKED_SLOTS.contains(i));
        }
        bytes = bits.toByteArray();
        System.out.println("Byte array: " + Arrays.toString(bytes));
    }

    private final static List<Integer> LOCKED_SLOTS = new ArrayList<>();

    public static boolean isLocked(int slotID) {
        return LOCKED_SLOTS.contains(slotID);
    }

}
