plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    mavenCentral()
    google()
}

//Resolve the used operating system
var currentOS = org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem()
var platform = ""
if (currentOS.isMacOsX) {
    platform = "mac"
} else if (currentOS.isLinux) {
    platform = "linux"
} else if (currentOS.isWindows) {
    platform = "win"
}

val javaFXVersion = "18"
val appClassName = "org.example.App"
val appModuleName = "org.example"

val compiler = javaToolchains.compilerFor {
    languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_18.majorVersion))
}

dependencies {
    implementation("org.openjfx:javafx-base:${javaFXVersion}:${platform}")
    implementation("org.openjfx:javafx-controls:${javaFXVersion}:${platform}")
    implementation("org.openjfx:javafx-graphics:${javaFXVersion}:${platform}")
    implementation("org.openjfx:javafx-fxml:${javaFXVersion}:${platform}")
//
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")

    implementation("com.fazecast:jSerialComm:2.9.2")

    implementation("org.controlsfx:controlsfx:11.1.1")
}

application {
    // Define the main class for the application.
    mainModule.set("org.example")
    mainClass.set(appClassName)
//    if (platform.equals("mac")) {
//        applicationDefaultJvmArgs = listOf("-Dsun.java2d.metal=true")
//    }
}

java {
    modularity.inferModulePath.set(true)
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

tasks {
    task<Copy>("copyDependencies") {
        from(configurations.runtimeClasspath)
        into("$buildDir/modules")
    }

    task<Exec>("package") {
        dependsOn(listOf("build", "copyDependencies"))
        val jdkHome = compiler.get().metadata.installationPath.asFile.absolutePath
        commandLine("${jdkHome}/bin/jpackage")
        if (platform == "win") {
            args(
                    listOf(
                            "--name", "PedalBox",
                            "--module-path", "$buildDir/modules" + File.pathSeparator + "$buildDir/libs",
                            "--dest", "$buildDir/installer",
                            "--module", "${appModuleName}/${appClassName}",
                            "--type", "msi", //type installer // {"app-image", "exe", "msi", "rpm", "deb", "pkg", "dmg"}
                            "--vendor", "PedalBox",
                            "--install-dir", "PedalBox",
                            "--icon", "src/main/resources/org/example/assets/pedal.ico",
                            "--win-menu-group", "PedalBox",
                            "--win-dir-chooser",
                            "--win-shortcut"
                    )
            )
        } else {
            args(listOf(
                    "-n", "PedalBox",
                    "-p", "$buildDir/modules" + File.pathSeparator + "$buildDir/libs",
                    "-d", "$buildDir/installer",
                    "-m", "${appModuleName}/${appClassName}"))
        }
    }

}
