package com.kyllian.skyblockisles.islesextra;

import com.kyllian.skyblockisles.islesextra.annotation.OnIsles;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesJoin;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesLeave;
import com.kyllian.skyblockisles.islesextra.client.ClientData;
import com.kyllian.skyblockisles.islesextra.client.CustomText;
import com.kyllian.skyblockisles.islesextra.client.IslesExtraClient;
import com.kyllian.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.kyllian.skyblockisles.islesextra.entity.IslesEntities;
import com.kyllian.skyblockisles.islesextra.entity.custom.queen_bee.QueenBeeEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class IslesExtra implements ModInitializer {

    public static Screen nextScreen = null;

    public final static String MOD_ID = "islesextra";

    public static DiscordHandler discord;

    @Override
    public void onInitialize() {

        discord = new DiscordHandler();

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles") || ip.contains("localhost")) {
                invokeIslesJoinMethods();
                IslesExtraClient.setOnIsles(true);
            }
        }));

        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles") || ip.contains("localhost")) {
                invokeIslesLeaveMethods();
                IslesExtraClient.setOnIsles(false);
            }
        }));

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (nextScreen!=null) client.setScreen(nextScreen);
            nextScreen = null;
            if (client.player == null) return;
            ClientData.updatePickedUpItems();
        }));
        ItemTooltipCallback.EVENT.register(((stack, context, lines) -> {
            NbtCompound nbt = stack.getNbt();
            if (nbt != null) {
                int i = 0;
                while (nbt.contains(IslesExtra.MOD_ID + ".lore." + i)) {
                    lines.add(new CustomText(nbt.getString(IslesExtra.MOD_ID + ".lore." + i)).getValue());
                    i++;
                }
            }
        }));
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(IslesEntities.QUEEN_BEE, QueenBeeEntity.createMobAttributes());
    }

    private void invokeIslesJoinMethods() {
        invokeIslesMethods("JOIN");
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
                        .setScanners(Scanners.ConstructorsAnnotated, Scanners.MethodsAnnotated)
        );
        Set<Method> methods = reflections.getMethodsAnnotatedWith(OnIslesJoin.class);
        try {
            for (Method method : methods) method.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException e) { throw new RuntimeException(e); }
    }

    private void invokeIslesLeaveMethods() {
        invokeIslesMethods("LEAVE");
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
                        .setScanners(Scanners.ConstructorsAnnotated, Scanners.MethodsAnnotated)
        );
        Set<Method> methods = reflections.getMethodsAnnotatedWith(OnIslesLeave.class);
        try {
            for (Method method : methods) method.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException e) { throw new RuntimeException(e); }
    }

    private void invokeIslesMethods(String action) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
                        .setScanners(Scanners.ConstructorsAnnotated, Scanners.MethodsAnnotated)
        );
        Set<Method> methods = reflections.getMethodsAnnotatedWith(OnIsles.class);
        try {
            for (Method method : methods) {
                if (method.getAnnotation(OnIsles.class).action().equalsIgnoreCase(action) || method.getAnnotation(OnIsles.class).action().equalsIgnoreCase("ALL"))
                    method.invoke(null);
            }
        } catch (InvocationTargetException | IllegalAccessException e) { throw new RuntimeException(e); }
    }
}
