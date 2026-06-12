pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.codemc.io/repository/maven-public/")
        maven("https://repo.extendedclip.com/releases/")
        maven("https://repo.lushplugins.org/releases/")
    }
}

rootProject.name = "kukso-minecraft"

include(":lib", ":dialogs", ":dialogs-exp-config-addon", ":worlds", ":items")
project(":lib").projectDir = file("modules/lib")
project(":dialogs").projectDir = file("modules/dialogs")
project(":dialogs-exp-config-addon").projectDir = file("modules/dialogs-exp-config-addon")
project(":worlds").projectDir = file("modules/worlds")
project(":items").projectDir = file("modules/items")
