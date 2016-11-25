package ovogotchi.emotion

import akka.actor.FSM
import ovogotchi.emotion.EmotionEngineV2._

trait Wellbeing {
  this: FSM[State, StateData] =>

  def personality: Personality

  when(WellbeingDriven) {
    case Event(RefreshState, StateData(charState, envState)) =>
      val newCharState = charState.copy(emotionalState = wellbeingState(charState.wellbeing), thought = None)
      goto(WellbeingDriven) using StateData(newCharState, envState)

    case Event(ProductionDeployment(_), StateData(c, e)) =>
      goto(WellbeingDriven) using StateData(c.copy(wellbeing = c.wellbeing + 5), e)

    case Event(Tick, StateData(c, e)) =>
      if (c.wellbeing < personality.restingWellnessLow) {
        val newWellbeing = c.wellbeing + personality.recovery
        goto(WellbeingDriven) using StateData(c.copy(wellbeing = newWellbeing), e)
      } else if (c.wellbeing > personality.restingWellnessHigh) {
        val newWellbeing = c.wellbeing - personality.recovery
        goto(WellbeingDriven) using StateData(c.copy(wellbeing = newWellbeing), e)
      } else {
        stay
      }
  }

  onTransition {
    case _ -> WellbeingDriven if nextStateData.charState.emotionalState != wellbeingState(nextStateData.charState.wellbeing) =>
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

}
