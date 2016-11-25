package ovogotchi.emotion

case class Personality(
                        temper: Int,
                        recovery: Int,
                        restingWellnessLow: Int,
                        restingWellnessHigh: Int
                      )

object Personality {

  val HappyThreshold = 75
  val SadThreshold = 25
}