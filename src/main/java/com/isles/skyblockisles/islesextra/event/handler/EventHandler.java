package com.isles.skyblockisles.islesextra.event.handler;

import java.util.List;

public interface EventHandler {
  void register();

  static void registerAll() {
    List<EventHandler> handlers = List.of(
      new ClientEventHandler(),
      new ConnectionStateEventHandler(),
      new IslesEventHandler(),
      new ItemEventHandler(),
      new IslesHudHandler()
    );

    handlers.forEach(EventHandler::register);
  }
}
