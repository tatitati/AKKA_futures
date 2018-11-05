import org.scalatest.{Matchers, WordSpec}

class CreateChildActorRequestSpec extends WordSpec with Matchers {
	"NameIdentifier.name" should {
		"return the name we passed in the construct" in {
			val request = new CreateChildActorRequest("anyname")
			request.name should be("anyname")
		}
	}
}