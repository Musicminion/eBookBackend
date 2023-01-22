# eBookBackend  课程大作业后端工程

给我狠狠地优雅！！！

![image](https://user-images.githubusercontent.com/84625273/177540340-a1f5f67f-1f17-40c0-9e8f-cd4f7e8952a6.png)

要运行项目，需要准备下列操作：
1. Kafka
2. 数据库 [看我的Github 互联网开发的仓库包括所有静态资源、SQL文件]
3. Redis
4. ElasticSearch 7.17.6搭配分词器lk 7.17.6（配件plugin文件）

### Kafka
需要下载Kafka，参考这里：https://kafka.apache.org/quickstart
然后CD到Kafka的目录，然后输入下面的命令启动
```
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

### Redis
MacOS电脑
```
brew install redis
```
Windows电脑

Redis 官方不建议在 windows 下使用 Redis，所以官网没有 windows 版本可以下载。
微软团队维护了开源的 windows 版本，虽然只有3.2 版本。所以推荐Linux吧！

Linux
```
sudo apt update 
sudo apt full-upgrade
sudo apt install build-essential tcl
```


### Elastic
- 官方网站：[Elastic Search官网历史版本下载](https://www.elastic.co/cn/downloads/past-releases#elasticsearch)
- LK开源分词器：[Github链接](https://github.com/medcl/elasticsearch-analysis-ik)
- 安装方法： 到指定目录打开终端或者cd到相应下载文件夹执行下面的命令

Windows：
```
bin\elasticsearch.bat
```
Linux/MacOS：
```
bin/elasticsearch
```

### 重要版本依赖说明
- 2022.11.9：进行了Spring底层框架的更新：
    - SpringBoot更新使用2.7版本
    - ElasticSearch使用7.17.6版本（该版本支持Windows/Linux/Arm Mac/Intel Mac）
    - 依赖Spring Data Elasticsearch使用4.4.5版本
    - **以上内容如有任何的版本不一致都可能导致项目启动失败！**
    - 测试机环境：MacOS Venture13.0 Arm64 CPU


### 本机核弹启动快捷脚本
需要目录一致才可以！
```bash
# kafka
/Applications/kafka_2.13-3.3.1/bin/zookeeper-server-start.sh /Applications/kafka_2.13-3.3.1/config/zookeeper.properties &
/Applications/kafka_2.13-3.3.1/bin/kafka-server-start.sh /Applications/kafka_2.13-3.3.1/config/server.properties &

# search elasticsearch
/Applications/elasticsearch-7.17.6/bin/elasticsearch &

# redis
brew services start redis
```
