plugins {
    id("org.springframework.boot") version "3.1.3" apply false
    id("io.spring.dependency-management") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    kotlin("jvm") version "1.9.10" apply false
    kotlin("plugin.spring") version "1.9.10" apply false
    kotlin("plugin.jpa") version "1.9.10" apply false
    java
}

val detektVersion by extra("1.23.1")
val springCloudVersion by extra("2022.0.3")
val netflixEurekaVersion by extra("4.0.3")

group = "com.example"
version = "2023"

repositories {
    mavenCentral()
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        dependencies {
            imports {
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
            }
        }
    }

    repositories {
        mavenCentral()
    }


    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

detekt {
    autoCorrect = true
}

project(":config") {
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
        implementation("org.springframework.cloud:spring-cloud-config-server")
    }
}


project(":discovery") {
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    }
}

project(":accounts") {
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        implementation("org.springframework.data:spring-data-commons")
        implementation("org.hsqldb:hsqldb")

        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
        implementation("org.springframework.cloud:spring-cloud-starter-config")
    }
}

project(":web") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
        implementation("org.springframework.cloud:spring-cloud-starter-config")
    }
}