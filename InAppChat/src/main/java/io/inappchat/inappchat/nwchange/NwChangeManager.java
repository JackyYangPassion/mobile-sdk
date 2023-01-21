package io.inappchat.inappchat.nwchange;

public interface NwChangeManager {

  void enable();

  void disable();

  void setNwChangeListener(NwChangeListener listener);
}
