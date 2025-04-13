pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases")
        maven("https://maven.google.com")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven("https://jitpack.io")
        mavenCentral()

        maven("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases")
        maven("https://maven.google.com")
    }
}

rootProject.name = "Health Kiosk"

val modules = listOf(
    ":app",
    ":core",
    ":feature:auth",
    ":feature:tests",
    ":feature:telemedicine",

    // Base Bluetooth
//    ":ppbasekit",
//    ":ppbluetoothkit",
//    ":ppcalculatekit",
)

modules.forEach { include(it) }
