package ovogotchi.emotion

import io.circe.{Encoder, Json}

sealed trait EmotionalState

object EmotionalState {
  case object Happy extends EmotionalState
  case object Neutral extends EmotionalState
  case object Sad extends EmotionalState
  case object Angry extends EmotionalState
  case object Ill extends EmotionalState
  case object Hungry extends EmotionalState
  case object Lonely extends EmotionalState
  case object Stressed extends EmotionalState

  implicit val encoder: Encoder[EmotionalState] = Encoder.instance(s => Json.fromString(s.toString))
}
