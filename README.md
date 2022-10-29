# eBookBackend 互联网应用开发基础 课程大作业后端工程

给我狠狠的优雅！！！

![image](https://user-images.githubusercontent.com/84625273/177540340-a1f5f67f-1f17-40c0-9e8f-cd4f7e8952a6.png)

要运行项目，需要准备下列操作：
1. Kafka
2. 数据库
3. Redis

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
