package ovogotchi.slackbot

import ovogotchi.emotion.EmotionEngine.State

object Responders {

  val responses: Map[QuestionAsked, (State => String)] = Map(
    How -> { (state: State) =>
      s"I'm ${state.emotionalState}"
    },
    Why -> { (state: State) =>
      "Hentie broke the build"
    },
    What -> { (state: State) =>
      "Get Laurence a beer"
    }
  )

}
