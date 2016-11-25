package ovogotchi.input

sealed trait InputEvent
case class PipelineFailure(name: String, step: String) extends InputEvent
case class ProductionDeployment(name: String) extends InputEvent
case class OpenPullRequests(prs: Seq[PullRequest]) extends InputEvent

case class PullRequest(name: String, ageHours: Int)
