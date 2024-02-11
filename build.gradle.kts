plugins {
	`java-library`
	id("org.teavm") version "0.9.2"
}

repositories {
    mavenCentral() 
}

dependencies {
	teavm(teavm.libs.interop)
}
