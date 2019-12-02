package futures.crawler

import futures.crawler.Downloader.{MapUrls, SetUrls}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Spider extends App {

  override def main(args: Array[String]): Unit = {
    scan(Set(Baseurl.url), limitDeepLevel = 5)
    Thread.sleep(160000)
  }

  def scan(urlsToExplore: SetUrls = Set(), limitDeepLevel: Int, exploredDeepLevel: Int = 0, mapProcessed: MapUrls = Map(), exploredUrls: SetUrls = Set()): Unit = {
    val futureUrls: Future[MapUrls] = Downloader.parseBunchUrls(urlsToExplore)

    futureUrls foreach { (m: MapUrls) =>
      val newExploredUrls: SetUrls = exploredUrls ++ urlsToExplore
      val parsedUrls: SetUrls = m.values.toSet.flatten
      val newUnexploredUrls: SetUrls = parsedUrls diff newExploredUrls
      val newMapProcessed = mapProcessed ++ m

      if(exploredDeepLevel != limitDeepLevel) {
        showMap(exploredDeepLevel, m)
        scan(newUnexploredUrls, limitDeepLevel, exploredDeepLevel + 1, newMapProcessed, newExploredUrls)
      } else {
        println("Done.")
      }
    }
  }

  private def showMap(exploredDeepLevel: Int, m: MapUrls) = {
    println("LEVEL: " + exploredDeepLevel)
    println("==============")
    for ((url, listUrls) <- m) {
      println("LEVEL: " + exploredDeepLevel + " => " + url)
      for (parsed <- listUrls) {
        println("\t\t" + parsed)
      }
    }
  }
}
