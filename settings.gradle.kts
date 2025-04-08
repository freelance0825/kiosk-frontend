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
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven("https://jitpack.io")
        mavenCentral()
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
