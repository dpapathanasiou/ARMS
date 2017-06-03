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
    from("versions.gradle")
}

application {
    mainClassName = "org.papathanasiou.denis.ARMS.AnotherRESTfulMongoService"
}

repositories {
    gradleScriptKotlin()
}

dependencies {
    compile(kotlinModule("stdlib"))
    compile("org.glassfish.jersey.core:jersey-server:"+ext.get("jersey"))
    compile("org.glassfish.jersey.containers:jersey-container-netty-http:"+ext.get("jersey"))
}
