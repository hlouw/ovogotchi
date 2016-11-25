package ovogotchi.emotion

sealed trait InputEvent
case class PipelineFailure(name: String) extends InputEvent
case class PipelineSuccess(name: String) extends InputEvent
case class ProductionDeployment(name: String) extends InputEvent
case class OpenPullRequests(prs: Seq[PullRequest]) extends InputEvent
case class EnvironmentStatus(name: String, status: String) extends InputEvent

case class PullRequest(name: String, ageHours: Int)
