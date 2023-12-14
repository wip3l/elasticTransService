# elasticTransService

## 1. Introduction

CSV file parsing and query service based on Elasticsearch;

Media upload and browsing service based on HDFS.

## 2.Features

You can customize the CSV file title or use the originals;

You can customize the Separator  (escape characters include );

We use Elastic Search for data store so you can parse file without maintaining table structure;

You can also use global search and conditional search;

We support fuzzy matching;

HDFS based file storage can store massive amounts of data files;

Common media files such as videos, text, and images support online preview.

## 3.Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You can download  the code with following url:

`https://github.com/wip3l/elasticTransService.git`

### Installation

Build with compilation tools such as [IntelliJ IDEA](https://www.jetbrains.com/idea/).

### Deployment

You will get a file named <u>elasticTransService-0.0.1-SNAPSHOT.jar</u> after packaging.

We need a configuration file with the parameters required by program, here is an example:

```
server:
    port: 8080
spring:
    datasource:
        name: druidDataSource
        type: com.alibaba.druid.pool.DruidDataSource
        druid:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://192.168.0.114:3306/hj?characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
            username: root
            password: 123456
            initial-size: 50
            min-idle: 50
            max-active: 100
            max-wait: 60000
            time-between-eviction-runs-millis: 60000
            min-evictable-idle-time-millis: 300000
            validation-query: select 1 from dual
            test-while-idle: true
            test-on-borrow: false
            test-on-return: false
            pool-prepared-statements: false
            max-open-prepared-statements: 50
            max-pool-prepared-statement-per-connection-size: 20
            filters: stat,wall,slf4j,config
mybatis:
    configuration:
        # Starting automatic mapping of result set NONE means canceling automatic mapping; Partial will only automatically map result sets without defined nested result set mappings. Full will automatically map any complex result set (whether nested or not). The default is partial, which is a global setting
        auto-mapping-behavior: partial
        
        map-underscore-to-camel-case: true
        
        #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
pagehelper:
    helper-dialect: mysql
elasticsearch:
    auth: false
    bulkSize: 10000
    host: 192.168.0.114
    password: 123456
    port: 9200
    username: root
hdfs:
    address: hdfs://192.168.0.114:9000
    web: http://192.168.0.114:50070/webhdfs/v1
logging:
    config: classpath:logback.xml
```




## 4. Usage

You can see all the features on the Swagger page: 

 http://localhost:8080/swagger-ui.html
