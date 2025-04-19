plugins {
    alias(libs.plugins.springBootPlugin)
    alias(libs.plugins.springDependencyManagment)
}

version = "1.0-SNAPSHOT"

dependencies {
    api(libs.springBootStarter)
    api(libs.springContext)

    annotationProcessor(libs.springConfigurationProcessor)
    compileOnly(libs.springConfigurationProcessor)
    compileOnly(libs.jetBrainsAnnotations)

    api(libs.grpcNettyShaded)
    api(libs.telegramApi)
    api(libs.slf4j)
    api(libs.logback)
    api(libs.reflections)
    api(projects.grpcClientJava)
}

tasks {
    shadowJar {
        from(sourceSets.main.get().output)

        manifest {
            attributes(
                "Main-Class" to "dev.xpepelok.wishlist.TelegramApplication",
                "Encoding" to "UTF-8"
            )
        }
    }
}