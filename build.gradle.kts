import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.5.10"
  id("org.jetbrains.compose") version "0.4.0"
}

group = "net.cdr0"
version = "1.0"

repositories {
  jcenter()
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  maven("https://packages.confluent.io/maven/")
}

dependencies {
  testImplementation(kotlin("test"))
  implementation(compose.desktop.currentOs)

  // https://mvnrepository.com/artifact/org.json/json
  implementation("org.json:json:20180813")

  // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
  implementation("org.apache.kafka:kafka-clients:2.8.0")

  // https://mvnrepository.com/artifact/org.apache.kafka/kafka-streams
  implementation("org.apache.kafka:kafka-streams:2.8.0")

  // https://mvnrepository.com/artifact/org.apache.kafka/connect-runtime
  implementation("org.apache.kafka:connect-runtime:2.8.0")

  // https://mvnrepository.com/artifact/io.confluent/kafka-json-serializer
  implementation("io.confluent:kafka-json-serializer:6.2.0")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "11"
}

compose.desktop {
  application {
    mainClass = "MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "rabbit"
      packageVersion = "1.0.0"
    }
  }
}