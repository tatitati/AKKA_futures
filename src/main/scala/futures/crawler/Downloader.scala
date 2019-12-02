package futures.crawler

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import collection.JavaConverters._
import scala.collection.mutable

object Downloader {
  type Url = String
  type SetUrls = Set[String]
  type MapUrls = Map[Url, SetUrls]

  def parseBunchUrls(urls: SetUrls): Future[MapUrls] = {
      val whatever: Set[Future[SetUrls]] = urls.map{ url => parseParallel(url)}
      val futs: Future[Set[SetUrls]] = Future.sequence(whatever)

      futs.map{f =>
        (urls zip f).toMap
      }
  }

  def parseParallel(url: Url): Future[SetUrls] = {
    Future{
      //println(Thread.currentThread.getName() + " | with url: " + url)
      parseLinks(getHtml(url))
        .filter(!_.endsWith("pdf"))
        .filter(!_.endsWith("xml"))
        .filter(!_.endsWith("/"))
    }
  }

  def getHtml(url: Url): Document = {
    Jsoup.connect(url).get
  }

  def parseLinks(doc: Document): SetUrls = {
    val body: Element = doc.body()
    val links: mutable.Buffer[Element] = body.select("a").asScala

    val whatever: mutable.Buffer[String] = for {
      link <- links
      if link.attr("href").startsWith("/")
    } yield Baseurl.url + link.attr("href")

    whatever.toSet
  }
}
