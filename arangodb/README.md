# Arango 基准测试指导文档

本文详细说明了如何在 `Arango3.7.6` 上完成图数据库基准测试项。

## 1. 安装 Arango

1. 下载安装

   ~~~shell
   # PS：以下操作需要在所有节点上进行操作
   # 详细安装步骤请参考：https://docs.Arango-graph.com.cn/manual-CN/3.build-develop-and-administration/2.install/1.install-with-rpm-deb/
   
   # 简略步骤如下
   # 下载安装包
   sudo wget https://download.arangodb.com/arangodb37/Community/Linux/arangodb3-linux-3.7.6.tar.gz
   
   # 解压安装包
   tar -zxvf arangodb3-linux-3.7.6.tar.gz
   
   # 进入arango目录
   cd arangodb3-3.7.6
   
   # 创建gstore
   sudo mkdir gstore
   
   # 主节点启动
   sudo bin/arangodb --starter.data-dir=$ARANGO_HOME/gstore --starter.host=主节点IP
   
   # 副节点启动
   sudo bin/arangodb --starter.data-dir=$ARANGO_HOME/gstore --starter.join 主节点IP:8528
   ~~~


## 2. 导入数据

### 2.1 Graph500

 1. 进入`Arango` 8529 前端页面，地址为`主节点:8529`，点击左侧菜单栏的`GRAPHS`，点击`Add Graph`，在弹出的窗口中填写如下Graph信息

    ~~~shell
    # Name*:	Graph500
    # Shards:	3
    # Replication factor:	1
    # Write concern:	
    # Edge definitions*:	Friend
    	
    # fromCollections*:	Person
    # toCollections*:	Person
    # Vertex collections:	Person
    ~~~

 2. 下载数据集

    ```
    wget https://www.galaxybase.com/public/download/graph500.zip
    ```

 3. 解压数据集

    ```
    unzip graph500.zip
    ```

 4. 将解压后的文件夹移动到`ArangoDB`所在目录下

    ~~~shell
    # 移动graph500文件夹
    mv graph500 $ArangoPath/
    ~~~
    
 5. 执行导入命令，等待导入完成

    ```bash
    # 进入ArangoDB所在目录
    cd $ArangoPath
    # 导入点数据
    sudo bin/arangoimport --file $ArangoPath/graph500/vertex.csv --type csv --collection "Person"
    # 导入边数据
    sudo bin/arangoimport --file $ArangoPath/graph500/edge.csv --type csv --collection "Friend" --separator '\t'
    ```

### 2.2 Twitter-2010

1. 进入`Arango` 8529 前端页面，地址为`主节点:8529`，点击左侧菜单栏的`GRAPHS`，点击`Add Graph`，在弹出的窗口中填写如下Graph信息

   ~~~shell
   # Name*:	Twitter2010
   # Shards:	3
   # Replication factor:	1
   # Write concern:	
   # Edge definitions*:	Friend
   	
   # fromCollections*:	Person
   # toCollections*:	Person
   # Vertex collections:	Person
   ~~~

2. 下载数据集

   ```
   wget https://www.galaxybase.com/public/download/twitter2010.zip
   ```

3. 解压数据集

   ~~~shell
   unzip twitter2010.zip
   ~~~

4. 将解压后的文件夹移动到`ArangoDB`所在目录下

   ~~~shell
   # 移动twitter2010文件夹
   mv twitter2010 $ArangoPath/
   ~~~
   
5. 执行导入命令，等待导入完成

   ```bash
   # 进入ArangoDB所在目录
   cd $ArangoPath
   # 导入点数据
   sudo bin/arangoimport --file $ArangoPath/twitter2010/vertex.csv --type csv --collection "Person"
   # 导入边数据
   sudo bin/arangoimport --file $ArangoPath/twitter2010/edge.csv --type csv --collection "Friend" --separator '\t'
   ```

## 3.运行测试脚本

### 3.1 打包测试项目

1. 切换到`galaxybase-release/lib`目录，运行以下语句，将 `lib` 下的 `galaxybase-bolt-driver-3.0.1.jar` 添加到本地仓库中

   ```shell
   mvn install:install-file -DgroupId=com.galaxybase -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=lib\galaxybase-bolt-driver-3.0.1.jar
   ```

2. `cd`到项目根目录运行 `mvn package`，获得 jar 包：`benchmark-Arango-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

   ```shell
   mvn package
   ```

### 3.2 执行测试

#### 3.2.1 Graph500

1. 将测试jar包 `benchmark-Arango-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Graph500` 目录；
2. 将 `application.yaml` 中的 `localhost` 改为`集群主节点的IP`；
3. 使用 `java -jar benchmark-Arango-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 3.2.2 Twitter-2010

1. 将测试jar包 `benchmark-Arango-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Twitter-2010` 目录；
2. 将 `application.yaml` 中的 `localhost` 改为`集群主节点的IP`；
3. 使用 `java -jar benchmark-Arango-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。



*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见README.md*

