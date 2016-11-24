package ovogotchi.engine

import ovogotchi.input.InputEvent

object EmotionEngine {

  def handleEvent(event: InputEvent): Unit = {
    // TODO update emotional state
    // TODO send update to frontend
    // TODO send Slack message if necessary
    println(s"My emotional state is changing! ${event.effects}")
  }

}
