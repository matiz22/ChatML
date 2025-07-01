plugins {
    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
}

allprojects {
    group = "pl.matiz22.chatml"
    version = "0.0.2"
}
