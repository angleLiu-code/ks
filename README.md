# 宿舍报修与维修管理系统

## 项目简介

这是一个基于 Java Web 的综合性宿舍报修与维修管理系统，用于实现学生在线提交报修申请、维修人员处理维修任务以及管理员统一管理维修流程。

## 技术栈

### 后端
- Java 8+
- Spring Boot 2.7.14
- MyBatis-Plus 3.5.2
- MySQL 8.0
- JWT（身份验证）
- Maven

### 前端
- Vue.js 3
- Element Plus
- Axios

## 项目结构

```
dormitory-repair-system/
├── src/
│   ├── main/
│   │   ├── java/com/university/repair/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── mapper/          # 数据访问层
│   │   │   ├── entity/          # 实体类
│   │   │   ├── dto/             # 数据传输对象
│   │   │   ├── vo/              # 视图对象
│   │   │   ├── interceptor/     # 拦截器
│   │   │   ├── filter/          # 过滤器
│   │   │   ├── util/            # 工具类
│   │   │   └── RepairSystemApplication.java  # 主程序
│   │   ├── resources/
│   │   │   ├── application.yml  # 应用配置
│   │   │   └── db/
│   │   │       └── init.sql     # 数据库初始化脚本
│   │   └── webapp/
│   └── test/
│       └── java/com/university/repair/
│           └── service/         # 服务层测试
├── pom.xml                       # Maven 配置
└── README.md
```

## 快速开始

### 1. 数据库初始化

```bash
# 在 MySQL 中执行 init.sql
mysql -u root -p < src/main/resources/db/init.sql
```

### 2. 配置应用程序

编辑 `src/main/resources/application.yml`，配置数据库信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/repair_system?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    username: root
    password: your_password
```

### 3. 运行应用

```bash
mvn clean install
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动

## API 文档

### 用户模块

#### 注册
```
POST /api/users/register
Content-Type: application/json

{
  "username": "student01",
  "password": "123456",
  "realName": "张三",
  "email": "student@university.edu",
  "phone": "13800138000",
  "role": "student"
}
```

#### 登录
```
POST /api/users/login
Content-Type: application/json

{
  "username": "student01",
  "password": "123456"
}
```

### 报修申请模块

#### 提交报修
```
POST /api/repair-requests
Content-Type: multipart/form-data
Authorization: Bearer {token}

categoryId=1
title=水管漏水
description=宿舍卫生间水管严重漏水
dormNumber=5-201
image={file}
```

#### 查看我的报修记录
```
GET /api/repair-requests/my-requests
Authorization: Bearer {token}
```

## 核心功能

### 学生端
- ✅ 用户注册与登录
- ✅ 提交报修申请
- ✅ 上传故障图片
- ✅ 查看维修进度
- ✅ 维修完成确认
- ✅ 维修评价

### 维修人员端
- ✅ 查看维修任务
- ✅ 接单处理
- ✅ 更新维修状态
- ✅ 填写维修结果

### 管理员端
- ✅ 用户信息管理
- ✅ 维修任务管理
- ✅ 报修分类管理
- ✅ 数据统计

## 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest
```

## 部署

```bash
mvn clean package
java -jar target/dormitory-repair-system-1.0.0.jar
```

## 许可证

MIT
