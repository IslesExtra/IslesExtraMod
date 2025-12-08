package com.isles.skyblockisles.islesextra.client.commands;

import com.google.gson.JsonObject;
import com.isles.skyblockisles.islesextra.client.ClientCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class TestCommand extends ClientCommand {

    // Simply a command to trigger certain code that requires conditions within the game to be met (for example ClientPlayerEntity != null)

    @Override
    public String getCommandName() {
        return "test";
    }

    public static void main(String[] args) {
        try {
            URL url = new URL("https://24a9cdc8-dd75-4e92-8cbc-3353a9c4ec76.lumine.io/player/profile/Joshinn");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JsonObject object = (JsonObject) com.google.gson.JsonParser.parseString(content.toString());
            JsonObject metadata = object.getAsJsonObject("metadata");
            String islesDataString = metadata.get("isles").getAsString();

            JsonObject islesData = (JsonObject) com.google.gson.JsonParser.parseString(islesDataString);
            JsonObject gameProfileSlots = islesData.getAsJsonObject("gameProfileSlots");
            JsonObject firstSlot = gameProfileSlots.getAsJsonObject("1");
            String encodedInventoryString = firstSlot.get("inventory").getAsString();
            System.out.println(encodedInventoryString);
            System.out.println("Result: " + new String(decode(encodedInventoryString)));

            decodeInventory(decode(encodedInventoryString));


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

  public static byte[] decode(String src) {
        return Base64.getDecoder().decode(src);
    }

    public static ItemStack[] decodeInventory(byte[] in) throws IOException, ClassNotFoundException {
        ObjectInputStream s = new ObjectInputStream(new ByteArrayInputStream(in));
        int length = s.readInt();
        for (int i = 0; i<length; i++) {
            Object o = s.readObject();
        }

        return null;
    }

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(this.getCommandName()).executes(this));
    }

}
