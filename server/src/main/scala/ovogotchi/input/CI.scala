package ovogotchi.input

import ovogotchi.input.Effect.BecomeAngry

object CI {

  object Effects {
    val BuildFailed = Seq(BecomeAngry)
  }

}
