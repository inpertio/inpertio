## Table of Contents

* [1. General](#1-general)
* [2. Change events](#2-change-events)
* [3. Config provider factory](#3-config-provider-factory)
  * [3.1. Basic](#3-1-basic)
  * [3.2. Raw and public](#3-2-raw-and-public)
  * [3.3. Composite](#3-3-composite)
  * [3.4. Scattered](#3-4-scattered)

## 1. General

There are a couple of general ideas which are proved to be convenient for using config service client:
* fine grained configs - configs are represented as instances of corresponding config classes and every small domain has its own config, for example `DbConfig`, `RoutingConfig`, `FeatureXConfig` 
* configs are dynamic - instead of initializing configs once on application startup, they should be accessed using a wrapper interface with target config's property. Example:
  ```
  interface ConfigProvider<T> {
      val data: T // Read-only access
  }
  ```
  Business logic works with it like below:
  ```
  private val myFeatureConfigProvider: ConfigProvider<MyFeatureConfig>
  
  fun serve(request: Request) {
      if (!myFeatureConfigProvider.data.enabled) {
          return
      }
      // do process
  }
  ``` 
  That allows to make sure that business logic uses the latest known configs and is able to pick up an updated value in runtime without restarting the application.
* configs are refreshable - there should be an option to get the latest configs from config service, which automatically refreshes all previously created configs. If business logic accesses them via `ConfigProvider`, as explained above, new config values are immediately applied

## 2. Change events

Sometimes there are situations when config values are fixated, for example, we might define port to be used by our service in configs, and an application opens server socket on it on startup. If the port is changed in configs later on, the socket is already opened on a different port.

To handle this situation it's convenient to fire an event about underlying config change. When the application receives it and detects that the port is changed, it can start listening on the new port and close the previous one

The event should be fired only when there is a config value change, i.e. it shouldn't be fired on initial config creation.

The event should have a reference to either previous or current config objects.

## 3. Config provider factory

### 3.1. Basic

Generally, configs are a list of key/value pairs. So, the client library should allow creating config objects from them.

For example, suppose that we have the following configs:
```
servers:
  server1:
    address: address1
  server2:
    address: address2
```
Config class for that might be defined like this:
```
data class ServersConfig(val servers: Map<String, ServerConfig>)
data class ServerConfig(val address: String)
```
Client library should provide a convenient way to create such config objects from key/value pairs

### 3.2. Raw and public

Config class above closely reflects configs structure. That might be inconvenient to use in business logic. Example:

```
data class MyRawConfig(val host: String, val port: Int)
data class MyPublicConfig(val address: URL)
```

Client library should provide a way to create `ConfigProvider` for 'public config types' on top of `ConfingProvider` for 'raw config types':

```
factory.build(MyRawConfig::class) { raw ->
    return MyPublicConfig(URL("http://${raw.host}:${raw:port}"))
}
```

Note that if underlying 'raw config' is changed, then `ConfigProvider` for the 'public config' created via the factory, is automatically updated to reflect the latest changes.

### 3.3. Composite

There are situations when config class is based on the data derived from multiple underlying config classes. The factory should allow creating `ConfigProvider` based on multiple underlying config providers:

```
data class MyCompositeConfig(val filer: String)
data class UserConfig(val filter: String)
data class RegionConfig(val filter: String)

factory.build(
    MyCompositeConfig::class,
    userConfigProvider: ConfigProvider<UserConfig>,
    regionConfigProvider: ConfigProvider<RegionConfig>
) { source ->
    val userConfig = userConfigProvider.data
    val regionConfig = regionConfigProvider.data
    return MyCompositeConfig("${userConfig.filter} AND ${regionConfig.filter}")
}
```
Any change in underlying config provider should result in composite config provider to be automatically refreshed.

### 3.4. Scattered

Sometimes, especially during migration from old configs, there are situations when config keys from the same domain lay in different hierarchies. In that case the factory should allow building `ConfigProvider` with explicit mapping between config class properties and arget config keys
```
data class MyConfig(val prop1: String, val prop2: String)

factory.build(
    mapOf(MyConfig::prop1.name to "root1.level1.prop",
          MyConfig::prop2.name to "root1.level2.level3.prop"),
    MyConfig::class
)
```