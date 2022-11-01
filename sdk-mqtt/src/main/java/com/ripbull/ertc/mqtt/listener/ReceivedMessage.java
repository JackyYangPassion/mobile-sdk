package com.ripbull.ertc.mqtt.listener;

import java.util.Date;

/** Created by DK on 16/03/19. */
public interface ReceivedMessage {

  String getTopic();

  String getMessage();

  String tile();

  String body();

  Date getTimestamp();
}
