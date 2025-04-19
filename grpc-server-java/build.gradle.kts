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
    api(libs.grpcStub)
    api(libs.grpcProtobuf)
    api(libs.logback)
    api(libs.mariaDB)
    api(libs.mysqlConnector)
    api(libs.hikari)
    api(projects.stubJava)
}

tasks {
    shadowJar {
        from(sourceSets.main.get().output)

        manifest {
            attributes(
                "Main-Class" to "dev.xpepelok.wishlist.ServerApplication",
                "Encoding" to "UTF-8"
            )
        }

        includeEmptyDirs = false
        mergeServiceFiles()
    }
}