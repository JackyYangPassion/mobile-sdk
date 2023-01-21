package io.inappchat.inappchat.typing.handler;

import io.reactivex.Completable;

/** Created by DK on 05/04/19. */
public interface TypingHandler {

  enum State {
    ACTIVE,
    COMPOSING,
    PAUSED,
    INACTIVE,
    GONE
  }

  void typingOn(Thread thread);

  void typingOff(Thread thread);

  Completable setChatState(State state, Thread thread);
}
