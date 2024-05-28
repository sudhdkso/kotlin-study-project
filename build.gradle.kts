import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"

	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
	kotlin("kapt") version "1.9.21"
	idea

}

group = "com.study"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	annotationProcessor("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(group = "org.mockito")
	}

	testImplementation("io.mockk:mockk:1.13.5")
	testImplementation("io.kotest:kotest-runner-junit5:5.4.2")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("com.querydsl:querydsl-apt:5.0.0:jakarta")


	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("org.springframework.boot:spring-boot-configuration-processor")
}

//JPA를 위한 설정
//특정 어노테이션들을 열어주는?
allOpen{
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperClass")
	annotation("jakarta.persistence.Embeddable")
}

//이 안에 있는 어노테이션이 붙은 클래스의 매개변수가 없는 생성자를 자동으로 추가해주는 역할
noArg{
	annotation("jakarta.persistence.Entity")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

idea {
	module {
		val kaptMain = file("build/generated/source/kapt/main")
		sourceDirs.add(kaptMain)
		generatedSourceDirs.add(kaptMain)
	}
}
