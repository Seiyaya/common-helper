# 常用的代码片段

## common-quartz
+ 数据库配置定时任务，通过后台管理系统直接管理定时任务

## common-tools
### 常用注解
+ `RepeatSubmitLimit`: 防止重复提交的注解: 可以通过expression来配置需要作为锁定的key，会在第一个参数中通过ognl查找对应的key的组成部分
+ `CrawlAttribute`注解: 标注在bean上面主要是快速映射字段，想把一个对象的属性复制到另外一个属性上面
+ `AuthorityTableData`注解: 后端控制权限的列表
+ `@UpdateLogInfo`注解: 用来得到 旧值修改为新值的比较，类似禅道的bug状态流转  
+ `@DictFormat`注解: 用来返回json时直接根据对应的字典key转成value

### 常用工具类
+ `MybatisSqlHelper`: 主要用来快速得到xml片段参数化后的sql,方便更快的调试。
+ `RepeatSubmitInterceptor`: 防止重复提交的拦截器，目前只实现了针对userId的，后面可以直接根据传递的参数通过ognl表达式得到针对一个接口的防止重复提交

## common-tester
+ 主要是做一些源码的测试类，或者一些不明白、不太清楚的地方的Tester