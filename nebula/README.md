# Nebula 基准测试指导文档

本文详细说明了如何在 `Nebula 2.0.0-GA` 上完成图数据库基准测试项。

PS：安装文档中所有的`localhost`都需要替换成服务器的`实际IP`

​		`Nebula集群`不存在主节点，以下说的`"主节点"`是指其中一台机器，目的是让所有`"主节点"`的操作在同一台服务器上执行

## 1. 安装 Nebula

1. 下载安装

   ~~~shell
   # PS：以下操作需要在所有节点上进行操作
   # 详细安装步骤请参考：https://docs.nebula-graph.com.cn/manual-CN/3.build-develop-and-administration/2.install/1.install-with-rpm-deb/
   
   # 简略步骤如下
   # 1.下载安装包
   # 本文为Ubuntu环境，采用 nebula-1.2.0.ubuntu1604.amd64.deb 包，centeos下载对应版本的rpm包
   下载地址：https://github.com/vesoft-inc/nebula-graph/releases
   
   # 下载ubuntu16版本对应的deb包
   wget https://github.com/vesoft-inc/nebula-graph/releases/download/v2.0.0/nebula-graph-2.0.0.ubuntu1604.amd64.deb
   
   # 下载Centeos6版本对应的deb包
   wget https://github.com/vesoft-inc/nebula-graph/releases/download/v2.0.0/nebula-graph-2.0.0.el6.x86_64.rpm
   
   # ubuntu 安装命令
   sudo dpkg -i nebula-graph-2.0.0.ubuntu1604.amd64.deb
   
   # Centos 安装命令
   sudo rpm -ivh nebula-graph-2.0.0.el6.x86_64.rpm
   
   # Nebula所有配置文件都在安装目录的etc目录下，进入该目录
   cd /usr/local/nebula/etc
   # 修改meta、graphd和storage配置文件
   sudo vim nebula-metad.conf
   sudo vim nebula-graphd.conf
   sudo vim nebula-storaged.conf
   # 将三个配置文件修改为如下结果
   --meta_server_addrs=ip1:9559,ip2:9559,ip3:9559
   --local_ip=localhost
   
   # 启动 Nebula 服务
   sudo /usr/local/nebula/scripts/nebula.service start all
   
   # 查看 Nebula 状态
   sudo /usr/local/nebula/scripts/nebula.service status all
   ~~~

2. 下载`nebula图连接工具`，`Nebula2.0`版本将连接脚本从`bin目录`中移除，需要单独下载`nebula-console`进行连接

   PS：只需在主节点上进行操作
   
   ~~~shell
   # 从Git下载nebula-console项目
   sudo git clone https://github.com/vesoft-inc/nebula-console
   # 进入Nebula-console目录
   cd $Nebula-console
   # 执行make生成nebula-console连接脚本
   make
   
   # 如果make报错，说明未安装go语言环境，需要先安装go环境
   # 安装Go语言环境请参考文档：http://c.biancheng.net/view/3993.html
   # 安装完成之后需要对go环境进行代理，代理请参考文档：https://goproxy.cn/，完成之后执行make命令
   ~~~
   
   `Nebula-Console`文档：https://github.com/vesoft-inc/nebula-console/blob/master/README.md
   
3. 关闭自动 compact

   ~~~shell
   # 关于compact的介绍请查看官方文档：https://docs.nebula-graph.io/2.0/8.service-tuning/compaction/
   # 进入Nebula-console目录
   cd $Nebula-console
   # 执行nebula-console脚本进行连接
   ./nebula-console --addr localhost -P 9669 -u root -p nebula
   
   # 进入nebula命令行可能会出现 error while loading shared libraries: libreadline.so.6: cannot open shared object file: No such file or directory 错误信息，如果出现以下信息，执行下面的命令之后重新进入nebula命令行
   
   # cd /lib/x86_64-linux-gnu/
   # sudo ln -s libreadline.so.7.0 libreadline.so.6
   
   # 关闭自动压缩
   nebula> UPDATE CONFIGS storage:rocksdb_column_family_options = {disable_auto_compactions = true};
   ~~~

## 2. 导入数据

PS：以下操作只在其中一个节点上进行即可

本次测试使用 Nebula 提供的 `Nebula Importer` 工具进行导入，使用docker安装 `Nebula Importer` 工具：

```shell
# nebula2.0版本对应的nebula-importer版本未latest
sudo docker pull vesoft/nebula-importer:latest
```

`Nebula Importer` 使用文档：https://github.com/vesoft-inc/nebula-importer/blob/master/README.md

### 2.1 Graph500

 1. 进入 Nebula 命令行，创建 `Graph500` 图

    ```shell
    # 进入Nebula-console目录
    cd $Nebula-console
    # 执行nebula-console脚本进行连接
    ./nebula-console --addr localhost -P 9669 -u root -p nebula
    
    # 进入命令行后输出如下命令创建图
    DROP SPACE Graph500;
    CREATE SPACE Graph500(vid_type = INT);
    USE Graph500;
    CREATE TAG ve(id int);
    CREATE EDGE ed();
    ```

 2. 下载数据集

    ```shell
    wget https://www.galaxybase.com/public/download/graph500.zip
    ```

 3. 解压数据集

    ```shell
    unzip graph500.zip
    
    # 若解压之后是vertex.csv和edge.csv文件，创建graph500目录将两个文件移入
    sudo mkdir graph500
    sudo mv vertex.csv graph500/
    sudo mv edge.csv graph500/
    ```

 4. 将测试数据集移动到本文提供的 `graph500.yaml` 文件所在的路径（项目下的`Graph500`目录）

    ```bash
    # 移动graph500录入至Graph500
    mv graph500 $nebula/Graph500
    ```
    
 5. 数据集预处理，删除文件首行的 Head

    ```shell
    # 进入Graph500目录
    cd $nebula/Graph500
    # 删除文件的第一行
    sed -i '1d' graph500/vertex.csv graph500/edge.csv
    ```
    
 6. 将`Graph500`目录下的配置文件移入`graph500`目录

    ~~~shell
    # 将导入配置文件中的localhost替换成实际的服务器IP如192.168.1.1
    sudo sed -i 's/localhost/192.168.1.1/' graph500.yaml
    
    sudo mv graph500.yaml graph500/
    ~~~
    
 7. 执行导入命令，等待导入完成

    ```bash
    sudo docker run --rm -ti -v $nebula/Graph500/graph500/:$nebula/Graph500/graph500/ vesoft/nebula-importer:latest --config $nebula/Graph500/graph500/graph500.yaml
    ```

8. 执行`compact`任务

   ~~~shell
   # 进入 Nebula 命令行
   cd $Nebula-console
   ./nebula-console --addr localhost -P 9669 -u root -p nebula
   
   # 选择图Graph500
   Use Graph500
   
   # 执行Compact任务
   Submit Job compact
   
   # Compact任务执行需要一段时间，大约需要几分钟，过程中不要进行操作，否则可能会影响性能
   # 查看Compact操作是否完成，可以使用Show Job JobId命令查看，JobId是执行Submit Job compact命令时打印在控制台的数字
   ~~~

9. 查看落盘大小

   ~~~shell
   # 查看节点落盘大小
   sudo du -sh /usr/local/nebula/data/storage/nebula/1/data
   ~~~

10. 数据统计

    ~~~shell
    # 统计当前节点的数据量，集群所有节点数据量累加就是整个图的数据量
    /usr/local/nebula/bin/db_dump --space_name=Graph500 --db_path=/usr/local/nebula/data/storage/nebula --meta_server=localhost:9559 --limit=-1 --mode=stat
    ~~~

### 2.2 Twitter-2010

1. 进入 Nebula 命令行，创建 `Twitter-2010` 图

   ~~~shell
   # 进入Nebula-console目录
   cd $Nebula-console
   # 执行nebula-console脚本进行连接
   ./nebula-console --addr localhost -P 9669 -u root -p nebula
   
   # 进入命令行后输出如下命令创建图
   DROP SPACE Twitter2010;
   CREATE SPACE Twitter2010(vid_type = INT);
   USE Twitter2010;
   CREATE TAG ve(id int);
   CREATE EDGE ed();
   ~~~

2. 下载数据集

   ```shell
   wget https://www.galaxybase.com/public/download/twitter2010.zip
   ```

3. 解压数据集

   ```shell
   unzip twitter2010.zip
   
   # 若解压之后是vertex.csv和edge.csv文件，创建twitter2010目录将两个文件移入
   sudo mkdir twitter2010
   sudo mv vertex.csv twitter2010/
   sudo mv edge.csv twitter2010/
   ```

4. 将测试数据集移动到本文提供的 `twitter-2010.yaml` 文件所在的路径（项目下的`Twitter-2010`目录）

   ```shell
   # 移动twitter2010录入至Twitter-2010
   mv twitter2010 $nebula/Twitter-2010
   ```

5. 修改`Twitter-2010`目录下的配置文件IP并移入`twitter2010`目录

   ~~~shell
   # 将导入配置文件中的localhost替换成实际的服务器IP如192.168.1.1
   sudo sed -i 's/localhost/192.168.1.1/' twitter-2010.yaml
   
   sudo mv twitter-2010.yaml twitter2010/
   ~~~

6. 执行导入脚本，等待导入完成

   ```bash
   sudo docker run --rm -ti -v $nebula/Twitter-2010/twitter2010/:$nebula/Twitter-2010/twitter2010/ vesoft/nebula-importer:latest --config $nebula/Twitter-2010/twitter2010/twitter-2010.yaml
   ```

7. 执行`compact`任务

   ~~~shell
   # 进入 Nebula 命令行
   cd $Nebula-console
   ./nebula-console --addr localhost -P 9669 -u root -p nebula
   
   # 选择图Twitter2010
   Use Twitter2010
   
   # 执行Compact任务
   Submit Job compact
   
   # Compact任务执行需要一段时间，大约需要1个小时，过程中不要进行操作，否则可能会影响性能
   # 查看Compact操作是否完成，可以使用Show Job JobId命令查看，JobId是执行Submit Job compact命令时打印在控制台的数字
   ~~~

8. 查看落盘大小

   ~~~shell
   # 查看节点落盘大小
   sudo du -sh /usr/local/nebula/data/storage/nebula/2/data
   ~~~

9. 数据统计

   ~~~shell
   # 统计当前节点的数据量，集群所有节点数据量累加就是整个图的数据量
   /usr/local/nebula/bin/db_dump --space_name=Twitter2010 --db_path=/usr/local/nebula/data/storage/nebula --meta_server=localhost:9559 --limit=-1 --mode=stat
   ~~~

*Nebula 的数据导入流程可参考详细文档：https://github.com/vesoft-inc/nebula-importer/blob/master/README.md*

## 3.运行测试脚本

### 3.1 打包测试项目

1. 切换到`common`模块根目录，运行以下语句，将`common`模块打包

   ```shell
   # 将common模块打成jar包
   mvn install
   ```

2. `cd`到项目根目录运行 `mvn package`，获得 jar 包：`benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

   ```shell
   # 将nebula模块打成可执行jar包
   mvn package
   ```

### 3.2 执行测试

#### 3.2.1 Graph500

1. 将测试jar包 `benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Graph500` 目录；
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP；
3. 使用 `java -jar benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 3.2.2 Twitter-2010

1. 将测试jar包 `benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Twitter-2010` 目录；
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP；
3. 使用 `java -jar benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

## 注意事项

Nebula在处理KN深度大于3的时候可能会导致服务异常，如果发现后续测试结果不符合预期，请先检查所有节点的服务是否正常

- 若节点服务全部正常，重启连接节点的所有服务
- 若存在节点服务挂掉，使用`sudo kill`杀死其他存活的服务，等待一分钟后重启服务，修改配置文件将已测试的测试项注释，然后重新进行测试

*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见README.md*

