# Galaxybase 基准测试指导文档

本文详细说明了如何在 `Galaxybase`集群版上完成图数据库基准测试项。

## 1. 安装Galaxybase

```shell
#详细安装步骤请参考：https://www.galaxybase.com/document?file=dev&docid=27
#集群所用到的机子都需安装Galaxybase
#简略步骤如下
#1.解压安装包
tar -zxf docker-cmbc-debian_amd64_20210219071411.tar.gz

#2.进入目录
cd docker-galaxybase

#3.检查环境及镜像安装
sudo bin/install

#4.启动Galaxybase服务
galaxybased build --private 内网地址 --public 外网地址 --home 挂载路径 
（主节点需要加上--master）

#5.在浏览器中打开主节点服务器地址的51314端口（如当前地址是192.168.0.1，则在浏览器中输入 http://192.168.0.1:51314）。打开管理页面，初始用户名和密码均为admin。点击添加结点，添加使用的结点的ip（主节点的节点编号为0）然后点击启动集群，输入授权码。

#6.点击启动结点按钮，在弹框中需要进行Galaxy账号的登录

#7.打开“服务”栏，启动数据可视化服务
```



## 2. 导入数据

`#只需在主节点执行以下导入数据的操作`

### 2.1 Graph500

 1. 下载数据集

    ```
    wget https://www.galaxybase.com/public/download/graph500.zip
    ```

 2. 解压数据集

    ```
    unzip graph500.zip
    ```

 3. 移入指定目录

    ```bash
    # 移动数据源文件到挂载路径下的data目录下，例如启动galaxybase-D服务时设置了挂载路径 --home /galaxy-home，则需执行 mv graph500 /galaxy-home/data/
    
    mv graph500 挂载路径/data/
    ```

 4. 执行导入脚本，等待导入完成

    ```bash
    bash load-graph500.sh
    ```

### 2.2 Twitter-2010

1. 下载数据集

   ```
   wget https://www.galaxybase.com/public/download/twitter2010.zip
   ```

2. 解压数据集

   ```
   unzip  twitter2010.zip
   ```

3. 移入指定目录

   ```
   # 移动数据源文件到挂载路径下的data目录下，例如启动galaxybase-D服务时设置了挂载路径 --home /galaxy-home，则需执行 mv twitter2010 /galaxy-home/data/
   
   mv twitter2010 挂载路径/data/
   ```

4. 执行导入脚本，等待导入完成

   ```bash
   bash load-twitter.sh
   ```

*Galaxybase 的数据导入流程可参考详细文档：https://www.galaxybase.com/document?file=dev&docid=24*



## 3.运行测试脚本

### 3.1 打包测试项目

1. 切换到lib目录，运行以下语句，将 `lib` 下的 `galaxybase-bolt-driver-3.0.1.jar` 添加到本地仓库中

   ```shell
   mvn install:install-file -DgroupId=com.galaxybase -DartifactId=bolt-driver -Dversion=3.1.0 -Dpackaging=jar -Dfile=lib\galaxybase-bolt-driver-3.1.0.jar
   ```

2. `cd`到项目根目录运行 `mvn package`，获得 jar 包：`benchmark-galaxybase-realase-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

   ```shell
   mvn package
   ```


### 3.2 执行测试

#### 3.2.1 Graph500

1. 将测试jar包 `benchmark-galaxybase-realase-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Graph500` 目录
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP
3. 使用 `benchmark-galaxybase-realase-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 3.2.2 Twitter-2010

1. 将测试jar包 `benchmark-galaxybase-realase-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Twitter-2010` 目录
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP
3. 使用 `java -jar benchmark-galaxybase-realase-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。





*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见README.md*

