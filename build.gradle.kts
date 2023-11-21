plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.serialization)
}

group = "dev.stashy.lokiwriter"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.kotlin.serialization.json)
    implementation(libs.tinylog.api)
    implementation(libs.tinylog.impl)

    implementation(ktor.client.engine)
    implementation(ktor.client.resources)
    implementation(ktor.client.contentNegotiation)
    implementation(ktor.serialization.json)
}

tasks.test {
    useJUnitPlatform()
}

publishing.publications {
    create<MavenPublication>("maven") {
        groupId = rootProject.group.toString()
        artifactId = rootProject.name
        version = rootProject.version.toString()
        from(components["java"])
    }
}
