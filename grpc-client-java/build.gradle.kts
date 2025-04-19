plugins {
    alias(libs.plugins.springBootPlugin)
    alias(libs.plugins.springDependencyManagment)
}

dependencies {
    api(libs.springBootStarter)
    api(libs.springContext)

    annotationProcessor(libs.springConfigurationProcessor)
    compileOnly(libs.springConfigurationProcessor)

    api(libs.grpcNettyShaded)
    api(libs.grpcStub)
    api(libs.grpcProtobuf)
    api(projects.stubJava)
}