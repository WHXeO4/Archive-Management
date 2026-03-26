# Archive-Management

> 声明：这是武汉理工大学Java综合实验任务用个人风格完成的示例代码  
  学习资源，仅供参考，请不要投机取巧直接下载后作为自己的作业提交  
  下载后请于24h内删除，有问题本人概不负责

## 0. 写在前面
1. 请自行安装 `Mysql JDBC`库
2. 在`server`文件夹下的 `/config/JDBCConfiguration.java` 中配置好自己的数据库的用户名，密码，数据库名称，数据表名称，在上传前已统一改成了 `your` 开头的字段
3. `server`文件夹与`client`文件夹，本来均为`src`，请分别放到两个项目中，在使用时，最好先启动`server`，再启动`client`，尽管做了每过10s自动检测网路连接的功能但似乎并未奏效，若奏效了本人会删除这一行`readme`

## 1. 项目概述
### 1.1 任务目标

基于 JavaSE 和 数据库，编写一个档案管理系统

系统是一个基于C/S的GUI应用程序，使用Java SE开发，综合运用面向对象编程的相关知识

该系统应该具有以下功能：
 
1. 用户管理：新增用户，修改用户，删除用户，用户查询，用户列表，用户登录，个人信息修改
2. 档案管理：档案查询，档案上传，档案下载，档案列表
3. 身份控制，对于不同身份的用户，应当提供不同的服务
    >各类用户可执行的操作如下：
    >
    >系统管理员 **Administrator**：1.新增用户；2.删除用户；3.修改用户；4.用户列表；5.下载档案；6.档案列表；7.修改个人密码；8.退出登录。
    >
    >档案录入人员 **Operator**：1.上传档案；2.下载档案；3.档案列表；4.修改个人密码；5.退出登录。
    >
    >档案浏览人员 **Browser**：1.下载档案；2.档案列表；3.修改个人密码；4.退出登录。
4. 数据库（本示例中采用`Mysql-JDBC`）
5. 网络通信，即划分 C/S 双端(双端的`src`分别命名为`Server`和`Client`给出)

## 2. 项目设计
### 2.1 数据库设计
#### 2.1.1 数据表
应包含两张数据表，user 与 document

`user`表存储用户信息，包括 **id**（primary-key），**name**（用户名），**password**（密码），**role**（身份）

`document`表存储文档信息，包括 **id**(primary-key)，**title**（标题），**url**（在服务端的存储地址），**description**（档案的描述），**createTime**(创建时间)，**updateTime**（更新时间）

#### 2.1.2 参考代码
`user` 表的参考代码

    CREATE TABLE user(
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(20) NOT NULL,
        password VARCHAR(35) NOT NULL,
        role VARCHAR(15) NOT NULL
    );

`document` 表的参考代码

    CREATE TABLE document(
        id INT AUTO_INCREMENT PRIMATY KEY,
        title VARCHAR(50) NOT NULL,
        url VARCHAR(256) NOT NULL,
        description VARCHAR(10000),
        createTime DATETIME,
        updateTime DATETIME
    );

使用 `desc` 命令查看数据表可得以下结果  

    desc user;  
    +----------+-------------+------+-----+---------+----------------+  
    | Field    | Type        | Null | Key | Default | Extra          |  
    +----------+-------------+------+-----+---------+----------------+  
    | id       | int         | NO   | PRI | NULL    | auto_increment |  
    | name     | varchar(20) | NO   |     | NULL    |                |  
    | password | varchar(35) | YES  |     | NULL    |                |  
    | role     | varchar(15) | NO   |     | NULL    |                |  
    +----------+-------------+------+-----+---------+----------------+  

    desc document;  
    +-------------+----------------+------+-----+---------+----------------+
    | Field       | Type           | Null | Key | Default | Extra          |
    +-------------+----------------+------+-----+---------+----------------+
    | id          | int            | NO   | PRI | NULL    | auto_increment |
    | title       | varchar(50)    | NO   |     | NULL    |                |
    | url         | varchar(256)   | NO   |     | NULL    |                |
    | description | varchar(10000) | YES  |     | NULL    |                |
    | createTime  | datetime       | YES  |     | NULL    |                |
    | updateTime  | datetime       | YES  |     | NULL    |                |
    +-------------+----------------+------+-----+---------+----------------+

### 2.2 Server端
#### 2.2.1 设计思路
`Server端`，一般可称为后端，负责从前端`Client端`接收数据并加以处理，然后从与数据库进行读写交互并将最终结果返回给`Client端`

网络通信部分非常简单，所以在这里不做讨论，我们主要关注`User`相关接口和`Document`相关接口的设计，接口具体功能参照开发文档

以`User`相关接口为例，大体思路如下，前端传回的`Request`对象中的数据通过`Handler`接收后，向`Controller`传递，`Controller`接收到数据后做参数校验，并讲数据传给`dao`包下的`Service`层，`Service`层将数据做进一步处理，处理为`JDBCUtil`接受的状态，然后将结果返回到`Controller`层，借由`Controller`层利用`Result`对象封装结果并通过网络通信以`Response`的形式传回`Client`

`Document`相关接口设计同理，这里不多赘述

#### 2.2.2 与学校课程设计给出的思路的不同
参看代码，不难发现，学校给出的课程示例代码中，`entity类`，尤其是`User类`，除了存储用户个人信息外，包含了大量的方法

出于某种执着，我不喜欢实体类里面塞太多东西，我认为实体类就老老实实的作为**容器**就行，其它东西交给别人去干

在此理念驱动下，我删除了大部分代码，将其修改为了，`Server端`不用关注发起请求的用户身份，只需要处理数据即可，这，至少在我的认知里面，大大降低了耦合度

所以你会发现，我并没有用所谓的继承去派生用户的身份

### 2.3 Client端
#### 2.3.1 设计思路
仿照`Server端`的`Handler`，我在`Client端`一样创建了大量的`Controller`，这些`Controller`各司其职，共同起到调用网络通信接口`ServerHandler`向`Server端`发送`Request`，获取`Response`的功能，

为了防止线程阻塞，每个`Controller`都会首先创建一个线程并等待

关于`GUI`部分，这里使用了一个很多人可能不会去考虑的技巧，那就是在`table`组件每一行末尾直接添加功能性按钮，比如**下载**，**删除**，**修改**等按钮，具体是借由`ButtonColumn`实现的，实现原理请自行 AI

## 3. 结束语

本人才疏学浅，很多地方恐有疏漏，万望海涵，同时这段代码很多是我的个人风格的产物，不一定具有普适性，仅作为参考思路供大家学习使用

对代码有疑问的，先请教 AI ，如果有 AI 解决不了的疑问，发现 `bug` 或有任何改进意见，请发邮件到 `whxeo46@whut.edu.cn`，最好**带图和详细描述**，本人将不定期查看
