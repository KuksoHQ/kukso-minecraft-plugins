plugins {
    id("com.kukso.minecraft.library")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("commons-io:commons-io:2.15.1")
    implementation("de.tr7zw:item-nbt-api:2.15.1")
    implementation("org.lushplugins:ChatColorHandler:4.0.0")
}
