plugins {
    id("com.kukso.minecraft.plugin")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly(project(":lib"))
    implementation("de.tr7zw:item-nbt-api:2.15.1")
}
