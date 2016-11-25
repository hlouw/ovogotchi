package ovogotchi.emotion

import akka.actor.FSM
import ovogotchi.emotion.EmotionEngineV2._

trait Illness {
  this: FSM[State, CharacterState] =>

  def personality: Personality

  when(Ill) {
    case Event(input: InputEvent, d: CharacterState) if fixesIll(input) =>
      goto(Undecided)

    case Event(Tick, d: CharacterState) =>
      if (d.wellbeing > 0) {
        val newWellbeing = d.wellbeing - personality.temper
        goto(Ill) using d.copy(wellbeing = newWellbeing)
      } else {
        stay
      }
  }

  private def fixesIll(input: InputEvent): Boolean = {
    true
  }
}
