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
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Cool Shop"
include(":app")
include(":core:coolshop-api")
include(":features:coolshop-main")
include(":core:data")
include(":features:coolshop-details")
include(":Mapper")
include(":core:State")
include(":core:database")
include(":features:coolshop-cart")
include(":features:coolshop-user")
include(":features:coolshop-reviews")
include(":core:utils")
include(":utils")
