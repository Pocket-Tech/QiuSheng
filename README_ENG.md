# QiuSheng
An open-source store server for Malody V.

English | [简体中文](README.md)

### Introduction
------------------
QiuSheng is a simple Malody V chart store server based on Spring boot + Mybatis and following the Malody Store API. By simply modifying the configuration file, players can build a chart server on their own server computer. At present, QiuSheng has realized some main functions of chart query, upload and download. In the future, related functions such as chart server events, store functions of Malody skins, and the management terminal for server owners will be updated. 

At present, Qiusheng is following the Malody Store API version: 202112.

> ~~（P.S：QiuSheng named after one of our friend's network nickname, you can think of it as the project's spirit totem ，although he isn't involved in development of this at all^_^）~~

### Set .yml configuration file 
------------------
Because of the project is developed based on spring boot, the server owner should make some necessary configurations in the configuration file according to their own needs. If you need to compile from source code, you can add a yml configuration file to the resource folder in the qiusheng-core module. The content can refer to the following code:

```yml
server:
  port: 80 #Change server port

host:
  localhost: #Fill in the domain name or public IP of your server

spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 1000MB
      max-request-size: 1000MB
    #Because of using multipart file for file transfer, you can configure related properties here 

  sql:
    init:
      schema-locations:
        - classpath:sql/qiusheng_core.sql #Using this to initialize the database
      mode: always

  datasource:
    url: jdbc:mysql://:3306/ #Fill in the url of your database server
    username:  #username of your database server
    password:  #password of your database server
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  mapper-locations: classpath:/mapping/*.xml
```

You can also download the release zip file. The yml configuration file has been set in the dictionary after unzipping, you also need to complete the necessary information:

> 1.Change the port to an unused port(optional)  
2.Fill in the local ip address or domain name (required)  
3.Change the limit size of upload file (optional but not suggested)  
4.Fill in the information of datasource(required)

You can start the server after completeting the information.

### P.S.
------------------

As an rhythem game player with a wide range of interests but not good at playing, I only knew that Malody had been released to Malody V in November 2021, so during the winter vacation of 2022 years, I began to write this simple server. Although the Malody V of steam version has now supported the workshop, and the official also has some recommended private servers, it seems that it does not make much sense to write an open-source private server at this time, but I still writen this. Although my ability to write code is not very well, but thanks to the Spring boot framework and the implementation of the API is indeed relatively easy, I'm not too hard to write this. So this project is not mature (the writing is also bad). So if you are happy to use this project, you are also welcome to suggest improvements or contribute code to us.

At the same time, I wish Malody better and better, thank you for this gift to the world music game lovers. 
