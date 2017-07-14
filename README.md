## Spring Cloud Learning Demo

本Demo参考：https://github.com/dyc87112/SpringBoot-Learning。

## 本Demo说明

  计划集成 spring—boot、eureka、mybatis、pagehelper、redis、druid


## PageHelper
## Special Configurations
一般情况下，你不需要做任何配置。

Normally, you don't need to do any configuration.

如果需要配置，可以使用如下方式进行配置：

You can config PageHelper as the following:

application.properties:
```properties
pagehelper.propertyName=propertyValue
```
注意 pagehelper 配置，因为分页插件根据自己的扩展不同，支持的参数也不同，所以不能用固定的对象接收参数，所以这里使用的 `Map<String,String>`，因此参数名是什么这里就写什么，IDE 也不会有自动提示。

关于可配置的属性请参考 [如何使用分页插件](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md)。

