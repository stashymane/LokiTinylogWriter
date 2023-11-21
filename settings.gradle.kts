rootProject.name = "LokiTinylogWriter"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    versionCatalogs {
        create("ktor") {
            from(files("./gradle/ktor.versions.toml"))
        }
    }
}
