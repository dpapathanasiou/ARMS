buildscript {
    repositories {
        gradleScriptKotlin()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

repositories {
    mavenCentral()
}

group = "org.papathanasiou.denis.ARMS"
version = "0.0.1-SNAPSHOT"

plugins {
    application
}

apply {
    plugin("kotlin")
}

application {
    mainClassName = "org.papathanasiou.denis.ARMS.AnotherRESTfulMongoService"
}

repositories {
    gradleScriptKotlin()
}

dependencies {
    compile(kotlinModule("stdlib"))
    compile("org.glassfish.jersey.core:jersey-server:2.25.1")
    compile("org.glassfish.jersey.containers:jersey-container-netty-http:2.25.1")
}
