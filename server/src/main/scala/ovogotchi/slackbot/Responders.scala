package ovogotchi.slackbot

import ovogotchi.emotion.EmotionEngineV2.{EnvironmentState, StateData}
import ovogotchi.emotion.{EmotionalState, PullRequest}
import ovogotchi.emotion.EmotionalState.Angry

object Responders {

  val responses: Map[QuestionAsked, (StateData => String)] = Map(
    How -> { (state: StateData) =>
      state.charState.emotionalState match {
        case EmotionalState.Angry =>
          s"I'm clucking ${state.charState.emotionalState}"
        case EmotionalState.Happy =>
          s"I'm Eggcelent! "
        case EmotionalState.Neutral =>
          "I'm okay.."
        case EmotionalState.Lonely =>
          "I'm feeling a little egg-nored"
        case EmotionalState.Stressed =>
          "STRESSED!"
        case EmotionalState.Ill =>
          "I'm feeling a bit scrambled"
        case _ =>
          s"I'm $state"
      }
    },
    Why -> { (state: StateData) =>
      if(state.charState.emotionalState == EmotionalState.Happy){"The build was successful!"}
      else if(state.charState.emotionalState == EmotionalState.Neutral){"I'm just okay..."}
      else {
        val builds = state.envState.failedBuilds
        val prs = state.envState.prs
        val msg1 = if (builds.size == 1) "The following builds are broken : " + builds.mkString(", ") + "\n" else ""
        val msg2 = if (prs.nonEmpty) s"This pull request ${prs.head.name} made has been oustanding for ${prs.head.ageHours} hours \n" else "\n"

        msg1 + msg2
      }
    }
  )

  val alerts: Map[EmotionalState, (EnvironmentState => String)] = Map(
    Angry -> {
      (env: EnvironmentState) =>
        //val pullRequests = if(env.prs.length != 0){s"\nOutstanding pull requests: ${genPrMsg(env.prs)} \n"} else " "
        val failedBuilds = if(env.failedBuilds.nonEmpty){s"Failed builds: ${env.failedBuilds} \n"} else ""
        s"I'm clucking angry! $failedBuilds"
    }
  )

  private def genPrMsg(prs: Seq[PullRequest]): String = {
    prs.map{ pr =>
      s"A pull request by ${pr.name} has been outsanding ${pr.ageHours} hours"
    }.mkString(", \n")
  }

}
