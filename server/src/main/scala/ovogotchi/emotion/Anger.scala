package ovogotchi.emotion

import akka.actor.FSM
import ovogotchi.emotion.EmotionEngineV2._

trait Anger {
  this: FSM[State, StateData] =>

  def personality: Personality

  when(Angry) {
    case Event(Tick, StateData(c, e)) =>
      if (c.wellbeing > 0) {
        val newWellbeing = c.wellbeing - personality.temper
        goto(Angry) using StateData(c.copy(wellbeing = newWellbeing), e)
      } else {
        stay
      }
  }

}
