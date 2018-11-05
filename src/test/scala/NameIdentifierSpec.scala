import org.scalatest.{Matchers, WordSpec}

class NameIdentifierrSpec extends WordSpec with Matchers {
	"NameIdentifier.name" should {
		"return the name we passed in the construct" in {
			val identifier = new NameIdentifier("anyname")
			identifier.name should be("anyname")
		}
	}
}