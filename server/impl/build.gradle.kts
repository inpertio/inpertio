plugins {
    id("inpertio.kotlin-common-conventions")
    id("org.springframework.boot") version "2.4.0"
}

apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
}