package com.kyllian.islesextra.islesextra.client;

import com.kyllian.islesextra.islesextra.IslesExtra;
import com.kyllian.islesextra.islesextra.gui.CustomGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.List;

public abstract class ClientUtils {

    public static void sendMessage(String message) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(new LiteralText(message), false);
    }

    public static void sendMessage(Text message) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(message, false);
    }

    public static void showText(String text, int milliseconds) {
        assert MinecraftClient.getInstance().player != null;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ArmorStandEntity ent = new ArmorStandEntity(player.clientWorld, player.getX(), player.getY(), player.getZ());
        player.clientWorld.addEntity(99999, ent);
        ent.setCustomName(new CustomText(text, 168, 235, 52).getValue());
        ent.setCustomNameVisible(true);
        ent.setNoGravity(true);
        ent.setInvisible(true);
        new java.util.Timer().schedule(new java.util.TimerTask() { @Override public void run() { ent.remove(Entity.RemovalReason.DISCARDED); } },milliseconds);
    }

    public static void openScreen(Screen screen) {
        IslesExtra.nextScreen = screen;
    }

    public static void setTracker(Vec3d target) {
        assert MinecraftClient.getInstance().player != null;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        Point targetPoint = new Point(target.getX(), target.getZ());

        ArmorStandEntity ent = new ArmorStandEntity(player.clientWorld, player.getX(), player.getY(), player.getZ());
        player.clientWorld.addEntity(99999, ent);
        ent.setCustomName(new CustomText("Tracker", 168, 235, 52).getValue());
        ent.setCustomNameVisible(true);
        ent.setNoGravity(true);
        ent.setInvisible(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                int radius = 10;
                Vec3d pos = player.getPos();
                Point playerPoint = new Point(pos.getX(), pos.getZ());

                if (pos.distanceTo(target) <= 1) {
                    ClientUtils.sendMessage("§aDestination reached!");
                    ent.remove(Entity.RemovalReason.DISCARDED);
                    this.cancel();
                    return;
                }

                if (pos.distanceTo(target) <= radius) ent.setPosition(target.getX(), target.getY()-1, target.getZ());
                else if (pos.distanceTo(ent.getPos()) < 3 || pos.distanceTo(ent.getPos()) > 20 || pos.distanceTo(target) < ent.getPos().distanceTo(target)) {
                    List<Point> results = getCircleLineIntersectionPoint(playerPoint, targetPoint, playerPoint, radius);
                    Point point1 = results.get(0);
                    Point point2 = results.get(1);

                    if (distance(point1, playerPoint) < distance(point2, playerPoint)) {
                        ent.setPosition(point1.x, pos.getY() - 1, point1.y);
                    } else {
                        ent.setPosition(point2.x, pos.getY() - 1, point2.y);
                    }
                }
                ent.setCustomName(new LiteralText("§7Tracker - §f" + Math.round(Math.round(distance(playerPoint, targetPoint))) + "§7m"));
            }
        }, 0, 100);

    }

    // Pulled from StackOverflow
    public static List<Point> getCircleLineIntersectionPoint(Point pointA,
                                                             Point pointB, Point center, double radius) {
        double baX = pointB.x - pointA.x;
        double baY = pointB.y - pointA.y;
        double caX = center.x - pointA.x;
        double caY = center.y - pointA.y;

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Point p1 = new Point(pointA.x - baX * abScalingFactor1, pointA.y
                - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Point p2 = new Point(pointA.x - baX * abScalingFactor2, pointA.y
                - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    public static double distance(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.x-point2.x, 2) + Math.pow(point1.y-point2.y, 2));
    }

    static class Point {
        double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
        @Override
        public String toString() {
            return "Point [x=" + x + ", y=" + y + "]";
        }
    }

}
