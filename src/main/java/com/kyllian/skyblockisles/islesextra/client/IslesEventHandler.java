package com.kyllian.skyblockisles.islesextra.client;

import com.kyllian.skyblockisles.islesextra.annotation.OnIsles;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesJoin;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesLeave;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesSwitch;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class IslesEventHandler {

    public static void init() {

        loadMethods();

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles")) {
                if (IslesExtraClient.isOnIsles()) invokeIslesSwitchMethods();
                else invokeIslesJoinMethods();
            }
        }));

        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            String ip = handler.getConnection().getAddress().toString();
            if (ip.contains("isles")) invokeIslesLeaveMethods();
        }));
    }

    public static Reflections reflections;

    public static void loadMethods() {
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
                        .setScanners(Scanners.MethodsAnnotated)
                        .filterInputsBy(new FilterBuilder().includePackage("com.kyllian.skyblockisles.islesextra"))
        );
    }

    public static void invokeIslesJoinMethods() {
        invokeIslesMethods("JOIN");
        Set<Method> methods = reflections.getMethodsAnnotatedWith(OnIslesJoin.class);
        try {
            for (Method method : methods) method.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException e) { throw new RuntimeException(e); }
    }

    public static void invokeIslesLeaveMethods() {
        invokeIslesMethods("LEAVE");
        Set<Method> methods = reflections.getMethodsAnnotatedWith(OnIslesLeave.class);
        try {
            for (Method method : methods) method.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException e) { throw new RuntimeException(e); }
    }

    public static void invokeIslesSwitchMethods() {
        invokeIslesMethods("SWITCH");
        Set<Method> methods = reflections.getMethodsAnnotatedWith(OnIslesSwitch.class);
        try {
            for (Method method : methods) method.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException e) { throw new RuntimeException(e); }
    }

    private static void invokeIslesMethods(String action) {
        Set<Method> methods = reflections.getMethodsAnnotatedWith(OnIsles.class);
        try {
            for (Method method : methods) {
                if (method.getAnnotation(OnIsles.class).value().equalsIgnoreCase(action))
                    method.invoke(null);
                else if (method.getAnnotation(OnIsles.class).value().equalsIgnoreCase("ALL") && method.getParameters().length>0 && method.getParameters()[0].getType().equals(String.class))
                    method.invoke(null, action);
            }
        } catch (InvocationTargetException | IllegalAccessException e) { throw new RuntimeException(e); }
    }

}
