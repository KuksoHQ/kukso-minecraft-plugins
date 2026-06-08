plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("minecraftBase") {
            id = "com.kukso.minecraft.base"
            implementationClass = "com.kukso.gradle.MinecraftBasePlugin"
        }
        register("minecraftLibrary") {
            id = "com.kukso.minecraft.library"
            implementationClass = "com.kukso.gradle.MinecraftLibraryPlugin"
        }
        register("minecraftPlugin") {
            id = "com.kukso.minecraft.plugin"
            implementationClass = "com.kukso.gradle.MinecraftPluginPlugin"
        }
    }
}
