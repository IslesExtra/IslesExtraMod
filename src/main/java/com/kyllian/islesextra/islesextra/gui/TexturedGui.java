package com.kyllian.islesextra.islesextra.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyllian.islesextra.islesextra.IslesExtra;
import com.kyllian.islesextra.islesextra.client.ClientUtils;
import com.kyllian.islesextra.islesextra.client.CustomText;
import com.kyllian.islesextra.islesextra.gui.elements.ButtonElement;
import com.kyllian.islesextra.islesextra.gui.elements.TextElement;
import com.kyllian.islesextra.islesextra.gui.elements.TextureElement;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class TexturedGui extends Screen {

    Identifier gui;
    String textureName;

    public TexturedGui(String textureName) {
        super(Text.literal("png"));
        this.textureName = textureName;
    }

    boolean isMouseDown = false;
    HashMap<int[], String> buttonActions = new HashMap<>();

    private GuiData data;
    @Override
    public void init() {
        System.out.println("GUI INIT");
        ScreenMouseEvents.beforeMouseRelease(this).register(((screen, mouseX, mouseY, button) -> {
            isMouseDown = false;
        }));

        // LOAD STUFF
        JsonObject root;
        InputStream is = IslesExtra.class.getClassLoader().getResourceAsStream("assets/islesextra/textures/gui/" + textureName + ".json");
        JsonParser jsonParser = new JsonParser();
        assert is != null;
        root = (JsonObject) jsonParser.parse(
                new InputStreamReader(is, StandardCharsets.UTF_8));

        if (root == null) return;
        JsonObject vars = root.getAsJsonObject("variables");

        int width = vars.getAsJsonArray("gui_size").get(0).getAsInt();
        int height = vars.getAsJsonArray("gui_size").get(1).getAsInt();

        int textureWidth = vars.getAsJsonArray("texture_size").get(0).getAsInt();
        int textureHeight = vars.getAsJsonArray("texture_size").get(1).getAsInt();

        int defaultOffsetX = vars.getAsJsonArray("default_offset").get(0).getAsInt();
        int defaultOffsetY = vars.getAsJsonArray("default_offset").get(1).getAsInt();

        assert client != null;
        data = new GuiData(width, height, defaultOffsetX, defaultOffsetY, textureWidth, textureHeight, new Identifier("islesextra:textures/gui/" + textureName + ".png"));

        JsonArray components = root.getAsJsonArray("components");

        for (JsonElement c : components) {
            JsonObject component = c.getAsJsonObject();
            if (component.get("type").getAsString().equalsIgnoreCase("texture")) {
                int offsetX = component.getAsJsonArray("position_offset").get(0).getAsInt();
                int offsetY = component.getAsJsonArray("position_offset").get(1).getAsInt();

                int textureX = component.getAsJsonArray("texture_position").get(0).getAsInt();
                int textureY = component.getAsJsonArray("texture_position").get(1).getAsInt();

                int sizeX = component.getAsJsonArray("texture_size").get(0).getAsInt();
                int sizeY = component.getAsJsonArray("texture_size").get(1).getAsInt();

                Identifier texture = component.has("texture") ? new Identifier("islesextra:textures/gui/" + component.get("texture").getAsString() + ".png") : null;

                data.addTexture(new TextureElement(textureX, textureY, sizeX, sizeY, offsetX, offsetY, texture));
            }
            else if (component.get("type").getAsString().equalsIgnoreCase("button")) {
                int sizeX = component.getAsJsonArray("texture_size").get(0).getAsInt();
                int sizeY = component.getAsJsonArray("texture_size").get(1).getAsInt();

                int x = component.getAsJsonArray("position_offset").get(0).getAsInt();
                int y = component.getAsJsonArray("position_offset").get(1).getAsInt();

                String hook = component.get("action").getAsString();
                int[] buttonValues = new int[] { x, y, sizeX, sizeY };
                if (!buttonActions.containsKey(buttonValues)) buttonActions.put(buttonValues, hook);

                int clickedX = component.getAsJsonArray("clicked").get(0).getAsInt();
                int clickedY = component.getAsJsonArray("clicked").get(1).getAsInt();

                int hoverX = component.getAsJsonArray("hovered").get(0).getAsInt();
                int hoverY = component.getAsJsonArray("hovered").get(1).getAsInt();

                int normalX = component.getAsJsonArray("default").get(0).getAsInt();
                int normalY = component.getAsJsonArray("default").get(1).getAsInt();

                Identifier texture = component.has("texture") ? new Identifier("islesextra:textures/gui/" + component.get("texture").getAsString() + ".png") : null;

                data.addButton(new ButtonElement(normalX, normalY, hoverX, hoverY, clickedX, clickedY, x, y, sizeX, sizeY, texture));
            }
            else if (component.get("type").getAsString().equalsIgnoreCase("text")) {
                String value = component.get("value").getAsString();
                int offsetX = component.getAsJsonArray("position_offset").get(0).getAsInt();
                int offsetY = component.getAsJsonArray("position_offset").get(1).getAsInt();

                int r = component.getAsJsonArray("color").get(0).getAsInt();
                int g = component.getAsJsonArray("color").get(1).getAsInt();
                int b = component.getAsJsonArray("color").get(2).getAsInt();

                data.addText(new TextElement(value, offsetX, offsetY, new Color(r, g, b).getRGB()));
            }
            else {
                ClientUtils.sendMessage("§cUnknown component type!");
            }
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        return true;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        super.charTyped(chr, modifiers);

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        isMouseDown = true;

        for (int[] buttonValues : buttonActions.keySet()) {
            int x = buttonValues[0], y = buttonValues[1], sizeX = buttonValues[2], sizeY = buttonValues[3];
            if (mouseX < x + sizeX && mouseX > x
                    && mouseY < y + sizeY && mouseY > y) {
                try {
                    this.getClass().getMethod(buttonActions.get(buttonValues)).invoke(this);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    ClientUtils.sendMessage("§cButton method does not yet exist.");
                }
                return true;
            }
        }
        return true;
    }

    public void addPokemon() {
        ClientUtils.sendMessage(new CustomText("&#777777Pokemon!").getValue());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        data.renderElements(matrices, mouseX, mouseY, isMouseDown);

    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}
