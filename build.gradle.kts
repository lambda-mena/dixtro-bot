plugins {
	java
	id("org.springframework.boot") version "3.3.6"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "com.vaatu.bots"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven("https://jitpack.io")
	maven("https://maven.lavalink.dev/releases")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("net.dv8tion:JDA:5.0.0-beta.24")
	implementation("dev.arbjerg:lavaplayer:2.2.2")
	implementation("dev.lavalink.youtube:v2:1.10.2")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
