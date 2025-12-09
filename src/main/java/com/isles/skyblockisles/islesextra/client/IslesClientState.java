package com.isles.skyblockisles.islesextra.client;

public class IslesClientState {

  private static boolean onIsles = false;
  private static String location = "";
  private static String instanceId = "";

  public static void leftIsles() {
    onIsles = false;
  }

  public static void joinedIsles() {
    onIsles = true;
  }

  public static boolean isOnIsles() {
    return onIsles;
  }

  public static String getLocation() {
    return location;
  }

  public static String getInstanceId() {
    return instanceId;
  }

  public static void updateLocationData(String newLocation, String newInstanceId) {
    location = newLocation;
    instanceId = newInstanceId;
  }
}
