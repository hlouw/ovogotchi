package ovogotchi.emotion

import akka.actor.FSM
import ovogotchi.emotion.EmotionEngineV2._

trait Anger {
  this: FSM[State, CharacterState] =>

  def personality: Personality

  when(Angry) {
    case Event(input: InputEvent, d: CharacterState) if fixesAngry(input) =>
      goto(Undecided)

    case Event(Tick, d: CharacterState) =>
      if (d.wellbeing > 0) {
        val newWellbeing = d.wellbeing - personality.temper
        goto(Angry) using d.copy(wellbeing = newWellbeing)
      } else {
        stay
      }
  }

  def fixesAngry(input: InputEvent): Boolean = {
    true
  }

}
