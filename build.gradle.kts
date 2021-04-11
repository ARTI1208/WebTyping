import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform") version "1.4.32"
    application
}

group = "me.arti1208"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

kotlin {

    val ktorVersion = "1.5.3"

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
        withJava()
    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-websockets:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {

                val reactVersion = "17.0.2-pre.153-kotlin-1.4.32"

                implementation("org.jetbrains:kotlin-react:$reactVersion")
                implementation("org.jetbrains:kotlin-react-dom:$reactVersion")
                implementation("org.jetbrains:kotlin-styled:5.2.3-pre.153-kotlin-1.4.32")

                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")

                implementation(npm("react-mde", "11.1.0"))
                implementation(npm("react-markdown", "5.0.3"))
                implementation(npm("react-showdown", "2.3.0"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

application {
    mainClassName = "ServerKt"
}

tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack") {
    outputFileName = "js.js"
}

tasks.getByName<Jar>("jvmJar") {
    dependsOn(tasks.getByName("jsBrowserProductionWebpack"))
    val jsBrowserProductionWebpack = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")
    from(File(jsBrowserProductionWebpack.destinationDirectory, jsBrowserProductionWebpack.outputFileName))
}

tasks.getByName<JavaExec>("run") {
    dependsOn(tasks.getByName<Jar>("jvmJar"))
    classpath(tasks.getByName<Jar>("jvmJar"))
}