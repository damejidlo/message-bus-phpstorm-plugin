# message-bus-phpstorm-plugin

Abandoned
Use fork at https://github.com/ondrejbouda/message-bus-phpstorm-plugin.

## instalace do stormy

Do stormy se importuje zip build, např. `message-bus-phpstorm-plugin-1.0-SNAPSHOT.zip` z https://github.com/damejidlo/message-bus-phpstorm-plugin/releases

## build

* Gradle projects: Tasks -> intellij -> buildPlugin
* viz build/distributions/

## debug

Ve službě `MessageHandlersMarkerProvider` použít připravený logger:
```
LOG.info("test");
```

Najít v logu `idea.log` položky `INFO - #MessageHandlersMarkerProvider`.
