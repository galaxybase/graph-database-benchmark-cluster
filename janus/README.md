# JanusGraph 基准测试指导文档

本文详细说明了如何在 `JanusGraph 0.5.3` 上完成图数据库基准测试项。

## 1. 安装JanusGraph

```shell
#详细安装步骤请参考JanusGraph官网：https://docs.janusgraph.org/getting-started/installation/
#集群所用到的机子都需安装cassandra,主节点安装elasticsearch
#简略步骤如下
#1.解压安装包
tar -zxvf apache-cassandra-3.11.10-bin.tar.gz
tar -zxvf elasticsearch-7.6.2-linux-x86_64.tar.gz
unzip janusgraph-0.5.3.zip
#2.cassandra配置
# listen_address、rpc_address修改为本机ip,seeds修改为集群ip(例："<ip1>,<ip2>,<ip3>")
vim cassandra.yaml
#3.所有节点进入cassandra安装目录并执行启动cassandra命令
./bin/cassandra &
#4.elasticsearch配置
vim elasticsearch.yml
node.name: node-1
network.host: ip # 填写本机ip
http.port: 9200
cluster.initial_master_nodes: node-1
#5.主节点进入elasticsearch安装目录并执行启动elasticsearch命令
nohup bin/elasticsearch &
```

## 2. 导入数据

`#只需在主节点执行以下导入数据的操作`

`使用IBM/jaunsgraph-util导入Graph500数据到janusgraph-hbase时，用了7小时。因此改用了cassandra作为存储后端，并实现了一个使用TinkerPop API 添加顶点和边的Java程序`

### 2.1 Graph500

 1. 下载数据集

    ```
    wget https://www.galaxybase.com/public/download/graph500.zip
    ```

 2. 解压数据集

    ```
    unzip graph500.zip
    ```

 3. 获取导入脚本

    ```bash
    # 解压janus下的janusgraph-csv-load.zip
    unzip janusgraph-csv-load.zip
    cd janusgraph-csv-load
    ```
    
 4. 将 `janusgraph-Graph500.properties` 中的`index.search.hostname` 改为连接的主节点的IP，`storage.hostname`改为集群ip地址集，并移动到janusgraph-csv-load目录下

 5. 执行导入脚本，等待导入完成

    ```bash
    bash run.sh false /datapath vertex.csv edge.csv janusgraph-Graph500.properties
    ```

### 2.2 Twitter-2010

1. 下载数据集

   ```
   wget https://www.galaxybase.com/public/download/twitter2010.zip
   ```

2. 解压数据集

   ```
   unzip xf twitter2010.zip
   ```

3. 获取导入脚本

   ```shell
   # 解压janus下的janusgraph-csv-load.zip
   unzip janusgraph-csv-load.zip
   cd janusgraph-csv-load
   ```
   
4. 将 `janusgraph-Twitter.properties` 中的`index.search.hostname` 改为连接的主节点的IP，`storage.hostname`改为集群ip地址集，并移动到janusgraph-csv-load目录下

5. 执行导入脚本，等待导入完成

   ```bash
   bash run.sh true /datapath vertex.csv edge.csv janusgraph-Twitter.properties
   ```

## 3.运行测试脚本

### 3.1 打包测试项目

`cd`到项目根目录运行 `mvn package`，获得 jar 包：`benchmark-janus-1-jar-with-dependencies.jar`

```shell
mvn package
```


### 3.2 执行测试

#### 3.2.1 Graph500

1. 将测试jar包 `benchmark-janus-1-jar-with-dependencies.jar` 移到 `Graph500` 目录
2. 将 `janusgraph-Graph500.properties` 中的`index.search.hostname` 改为连接的主节点的IP，`storage.hostname`改为集群ip地址集
3. 使用`java -jar benchmark-janus-1-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 3.2.2 Twitter-2010

1. 将测试jar包 `benchmark-janus-1-jar-with-dependencies.jar` 移到 `Twitter-2010` 目录
2. 将 `janusgraph-Twitter.properties`  中的`index.search.hostname` 改为连接的主节点的IP，`storage.hostname`改为集群ip地址集
3. 使用 `java -jar benchmark-janus-1-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。





*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见README.md*

