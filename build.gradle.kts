plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.serialization)
}

group = "lt.kaskur"
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
