# QiuSheng

[![CodeFactor](https://www.codefactor.io/repository/github/pocket-tech/qiusheng/badge/Dev-version)](https://www.codefactor.io/repository/github/pocket-tech/qiusheng/overview/Dev-version)

开源Malody V谱面服务器

[English](README_ENG.md) | 简体中文

### 简介
------------------
QiuSheng是一个简单的基于Spring boot + Mybatis所开发的遵循Malody Store API的Malody V谱面服务器。通过简单修改配置文件，玩家可以在自己的服务器上简单的搭建一个谱面服务器。目前QiuSheng已经实现了谱面的查询、上传、下载与服务器活动等主要功能，在1.1.0版本之后已添加了提供管理员权限的一些功能。将来将更新皮肤等相关功能以及针对服主的管理终端。

目前QiuSheng支持到202112版本的Malody Store API。

> ~~（P.S：QiuSheng这个名字来自我们一个关系不错的hxd的网络昵称，可以视作这个项目的精神图腾，尽管他本人没有参与开发^_^）~~

### 设定yml配置文件
------------------
由于该项目基于spring boot开发，所以服主可以根据自己的需求在配置文件中进行一些必要的配置。如果需要通过源代码编译，您可以在qiusheng-core模块中的resource文件夹下添加yml配置文件。其中内容可以参考以下代码：

在运行该服务器之前，请确保您为服务器创建了一个Mysql数据库，并在配置文件中进行相应的配置。

```yml
server:
  port: 80 #在此修改服务器端口

host:
  localhost: #填写此服务器的域名或公网ip

spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 1000MB
      max-request-size: 1000MB
    #利用multipart file进行文件传输，可以在这里配置相关属性
    # redis 配置
  redis:
    # 地址
    host: localhost
    # 端口，默认为6379
    port: 6379
    # 数据库索引
    database: 6
    # 密码
    password: #没有则不填
    # 连接超时时间
    timeout: 10s
    lettuce:
      pool:
        # 连接池中的最小空闲连接
        min-idle: 0
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池的最大数据库连接数
        max-active: 8
        # #连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: -1ms

  sql:
    init:
      schema-locations:
        - classpath:sql/qiusheng_core.sql #将在此进行数据库初始化
      mode: always

  datasource:
    hikari:
      max-lifetime: 120000
    url: jdbc:mysql://:3306/ #填写数据库地址
    username:  #数据库用户名
    password:  #数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver

qiusheng:
  greeting: Welcome to QiuSheng Server #自定义服务器欢迎语

mybatis:
  mapper-locations: classpath:/mapping/*.xml
  #mybatis的映射器文件目录
```

您同样可以下载release的压缩包，解压之后在目录中已经设置了配置文件，您同样需要对必要的信息进行补全：

> 1.更改端口为未被占用的端口（可选）  
2.填写本机ip地址或域名（必须）  
3.更改文件上传大小限制（可选，但不建议）  
4.填写数据源信息（Mysql&Redis）（必须）

配置完成之后就可以启动服务了。

### P.S.
------------------

作为一个兴趣广泛但是玩的很菜的音游狗，我却是在2021年11月的时候我才知道Malody已经发布了到Malody V的，于是在22年的寒假，便开始动手写了这个简陋的服务器。虽然现在steam版本的Malody V已经支持了创意工坊，官方也有了推荐的私服，这个时候写开源私服好像意义并不大，但是还是写了这样一个私服。虽然写代码的能力很菜，但是得益于Spring boot框架以及实现API确实比较简单，写出这个私服也比较简陋，所以这个项目并不成熟（写的也菜）。所以如果您乐意使用这个项目，也欢迎您对我们提出改进建议或者贡献代码。

同时祝Malody越来越好，谢谢这个献给世界音乐游戏爱好者的礼物。
