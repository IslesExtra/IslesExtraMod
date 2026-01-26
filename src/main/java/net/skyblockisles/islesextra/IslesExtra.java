package net.skyblockisles.islesextra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

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
  public final static String ISLES_ID = "isles";

  private final static Logger LOGGER = LogManager.getLogger();

  @Override
  public void onInitializeClient() {
    init();
  }

  private static void init() {
    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()).setScanners(Scanners.MethodsAnnotated));
    Set<Method> methods = reflections.getMethodsAnnotatedWith(Init.class);

    for (Method m : methods) {
      try { m.invoke(null); }
      catch(IllegalAccessException | InvocationTargetException e) {
        LOGGER.warn("Method could not be invoked: " + e);
      }
    }
  }
}
