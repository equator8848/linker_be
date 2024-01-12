# Linker 前后端联调平台代码

# 用户文档

- [用户文档知识库](https://m1lifwiv5r.feishu.cn/wiki/G96mwEUH0ig1pfkxEfrcGFBbnZf?from=from_copylink)

## 代码混淆

- jd-gui，有缓存~重新打包后，拖jar包进去，显示还是原来的代码。需要关闭窗口后再把jar包托进入验证。
- 实体类都需要加上注解：@TableField

## docker部署

> 需要文件

- [Dockerfile](config_sample/Dockerfile)
- [application.yml](config_sample/application.yml)
- [linker.jar](target%2Flinker.jar)

```shell
docker build -t equator-linker:latest .

docker rm -vf linker-instance

sudo docker run -d -p 8888:8888 --restart always --name linker-instance equator-linker:latest
```

## 测试服务是否正常启动

```shell
curl http://ip:8888/linker-server/api/v1/anonymous/ping
```

# 版本记录

- 1.0.0 一个比较完善的版本
- 1.0.1 修复部分SCM仓库无法获取commit列表问题
- 1.0.2 增加定制化模板（ID为：20240111），模板列表从数据库获取改成从代码动态获取、支持配置公开编辑权限配置

# 备忘录

1. 设置环境变量启动docker测试，如：docker run --rm -p 28181:80 -e gatewayServer1=192.168.0.10:19206 -e
   gatewayServer2=192.168.0.10:19206 --name nginx-test training-center:1.1.24
2. 20240111模板用到了envsubst命令进行环境变量的替换