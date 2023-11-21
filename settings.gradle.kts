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
    }

    versionCatalogs {
        create("ktor") {
            from(files("./gradle/ktor.versions.toml"))
        }
    }
}
