package net.skyblockisles.islesextra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.skyblockisles.islesextra.annotations.EveryTick;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.skyblockisles.islesextra.annotations.Init;

@Environment(EnvType.CLIENT)
public class IslesExtra implements ClientModInitializer {

  public final static String MOD_ID = "islesextra";

  private final static Logger LOGGER = LogManager.getLogger();

  @Override
  public void onInitializeClient() {
    init();
  }

  private static void init() {

    Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage("net.skyblockisles.islesextra"))
        .setScanners(Scanners.MethodsAnnotated));

    Set<Method> initMethods = reflections.getMethodsAnnotatedWith(Init.class);
    Set<Method> tickMethods = reflections.getMethodsAnnotatedWith(EveryTick.class);

    for (Method m : initMethods) {
      try { m.invoke(null); }
      catch(IllegalAccessException | InvocationTargetException e) {
        LOGGER.warn("Init method could not be invoked: {}", e.toString());
      }
    }

    ClientTickEvents.START_CLIENT_TICK.register(client -> {
      for (Method m : tickMethods) {
        try { m.invoke(null, client); }
        catch (IllegalAccessException | InvocationTargetException e) {
          LOGGER.warn("Tick method could not be invoked: {}", e.toString());
        }
      }
    });
  }
}
