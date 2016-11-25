package ovogotchi.emotion

import akka.actor.FSM
import ovogotchi.emotion.EmotionEngineV2._

trait Loneliness {
  this: FSM[State, StateData] =>

  def personality: Personality

  when(Lonely) {
    case Event(Tick, StateData(c, e)) =>
      if (c.wellbeing > 0) {
        val newWellbeing = c.wellbeing - personality.temper
        goto(Lonely) using StateData(c.copy(wellbeing = newWellbeing), e)
      } else {
        stay
      }
  }
}