# 集成百度分布式唯一ID生成框架UidGenerator

## 简介

UidGenerator是Java实现的, 基于Snowflake算法的唯一ID生成器（[具体请点击查看官方介绍][1]）。

## 快速上手

### install UidGenerator 到本地

下载百度官方 [UidGenerator][2] 到本地，然后用 maven install 到本地仓库

当然，你嫌麻烦，已经帮你放项目里面去了，可以自取。

### 执行SQL
```
DROP DATABASE IF EXISTS `uid-generator`;
CREATE DATABASE `uid-generator` ;
use `uid-generator`;
DROP TABLE IF EXISTS WORKER_NODE;
CREATE TABLE WORKER_NODE
(
`ID` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
`HOST_NAME` VARCHAR(64) NOT NULL COMMENT 'host name',
`PORT` VARCHAR(64) NOT NULL COMMENT 'port',
`TYPE` INT NOT NULL COMMENT 'node type: ACTUAL or CONTAINER',
`LAUNCH_DATE` DATE NOT NULL COMMENT 'launch date',
`MODIFIED` datetime(0) NOT NULL COMMENT 'modified time',
`CREATED` datetime(0) NOT NULL COMMENT 'created time',
PRIMARY KEY(ID)
)
 COMMENT='DB WorkerID Assigner for UID Generator',ENGINE = INNODB;
```

### 修改数据库连接信息和端口号

因为 UidGenerator 在服务启动的时候会在数据库表中插一条记录，将记录的id作为 workerId，重启时继续插入，意味着 workerId 的分配策略为用完即弃。所以修改数据库信息如下

```
server.port=8080
mysql.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/uid-generator
jdbc.username=root
jdbc.password=12345678
```

默认端口为8080，这个根据实际项目修改

## 启动

访问 [http:localhost:8080/uidGenerator][3]

## 另外
可以使用zk或者redis来实现wordId的提供管理，可参考 [ecp-uid][4]

  [1]: https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md
  [2]: https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md
  [3]: http://localhost:8080/uidGenerator
  [4]: https://github.com/linhuaichuan/ecp-uid