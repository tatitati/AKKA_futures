package actors.HierarchyActor

import org.scalatest.{FunSuite, Matchers, WordSpec}

class CreateChildActorRequestSpec extends FunSuite with Matchers {

	test("return the name we passed in the construct") {
		val request = new CreateChildActorRequest("anyname")
		request.name should be("anyname")
	}
}