plugins {
    id("inpertio.kotlin-common-conventions")
    id("org.springframework.boot") version "2.4.0"
}

apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":client:jvm:common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.9.0.202009080501-r")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.cucumber:cucumber-java:${Version.CUCUMBER}")
    testImplementation("io.cucumber:cucumber-junit:${Version.CUCUMBER}")
    testImplementation("io.cucumber:cucumber-spring:${Version.CUCUMBER}")
}