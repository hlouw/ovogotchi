package ovogotchi.output

import ovogotchi.emotion.EmotionalState

case class WebsocketPayload(state: EmotionalState, wellbeing: Int, thought: Option[String])
