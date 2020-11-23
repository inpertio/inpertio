rootProject.name = "inpertio"

include(
        ":server:impl",
        ":server:example",
        ":client:jvm:common",
        ":client:jvm:java:impl",
        ":client:jvm:java:example",
        ":client:jvm:kotlin:impl",
        ":client:jvm:kotlin:example",
        ":client:jvm:cucumber:impl",
        ":client:jvm:cucumber:example"
)