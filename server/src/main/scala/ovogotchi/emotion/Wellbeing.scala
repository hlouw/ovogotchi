package ovogotchi.emotion

import akka.actor.FSM
import ovogotchi.emotion.EmotionEngineV2._

trait Wellbeing {
  this: FSM[State, CharacterState] =>

  def personality: Personality

  when(WellbeingDriven) {
    case Event(RefreshState, d: CharacterState) =>
      goto(WellbeingDriven) using d.copy(emotionalState = wellbeingState(d.wellbeing))

    case Event(input: InputEvent, d: CharacterState) if makesIll(input) =>
      goto(Ill) using d.copy(emotionalState = EmotionalState.Ill)

    case Event(input: InputEvent, d: CharacterState) if makesAngry(input) =>
      goto(Angry) using d.copy(emotionalState = EmotionalState.Angry)

    case Event(Tick, d: CharacterState) =>
      if (d.wellbeing < 100) {
        val newWellbeing = d.wellbeing + personality.recovery
        goto(WellbeingDriven) using d.copy(wellbeing = newWellbeing)
      } else {
        stay
      }
  }

  onTransition {
    case _ -> WellbeingDriven if nextStateData.emotionalState != wellbeingState(nextStateData.wellbeing) =>
      self ! RefreshState
  }

  private def wellbeingState(wellbeing: Int): EmotionalState = {
    if (wellbeing >= Personality.HappyThreshold)
      EmotionalState.Happy
    else if (wellbeing < Personality.NeutralThreshold)
      EmotionalState.Sad
    else
      EmotionalState.Neutral
  }

  def makesAngry(input: InputEvent): Boolean = {
    true
  }

  def makesIll(input: InputEvent): Boolean = {
    true
  }

}
