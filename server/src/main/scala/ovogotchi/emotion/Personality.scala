package ovogotchi.emotion

case class Personality(
                        temper: Int,
                        recovery: Int,
                        restingWellnessLow: Int,
                        restingWellnessHigh: Int
                      )

object Personality {

  val HappyThreshold = 66
  val NeutralThreshold = 33
}