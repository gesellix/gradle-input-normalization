import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
  groovy
  `java-library`
}

repositories {
  jcenter()
}

dependencies {
  implementation("org.codehaus.groovy:groovy-all:2.5.9")
  testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
}

normalization {
  runtimeClasspath {
    ignore("**/build-info.properties")
  }
}

tasks {
  val buildInfoTask = create("buildInfo") {
    val buildInfoFile = file("${buildDir}/resources/main/META-INF/build-info.properties")
    outputs.file(buildInfoFile)
    outputs.upToDateWhen { false }
    doFirst {
      val timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
      mkdir(buildInfoFile.parent)
      buildInfoFile.createNewFile()
      buildInfoFile.writeText("version=${timestamp}")
    }
    doLast {
      logger.lifecycle("build-info: ${buildInfoFile.readText()}")
    }
  }
  listOf(processResources, compileJava, compileGroovy).forEach { it.get().dependsOn(buildInfoTask) }
}
