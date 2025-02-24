package pull.request.backup

import com.typesafe.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters._


class BackupConfig {

  private val conf: Config = ConfigFactory.load()

  private val url: String = conf.getString("pull-request.backup.url")
  private val path: String = conf.getString("pull-request.backup.path")
  val uri: String = url + path

  val token: String = conf.getString("pull-request.backup.token")
  val projects: List[String] = conf.getStringList("pull-request.backup.projects").asScala.toList

}
