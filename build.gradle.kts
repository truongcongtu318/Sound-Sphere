// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript { repositories {
    mavenCentral()
}}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
//    alias(libs.plugins.google.gms.google.services) apply false
//    id("com.google.gms.google-services") version "4.4.1" apply false
}