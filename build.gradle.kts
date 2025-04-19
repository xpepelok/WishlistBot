import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import io.freefair.gradle.plugins.lombok.LombokPlugin

plugins {
    java
    `java-library`
    alias(libs.plugins.lombok)
    alias(libs.plugins.shadowJar)
}

allprojects {
    group = "dev.xpepelok.wishlist"

    repositories {
        mavenCentral()
    }

    apply<JavaPlugin>()
    apply<LombokPlugin>()
    apply<JavaLibraryPlugin>()
    apply<ShadowPlugin>()

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks {
        shadowJar {
            archiveClassifier.set("")
        }
    }
}