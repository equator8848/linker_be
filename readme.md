# Linker 前后端联调平台

## 指定配置文件启动
```shell
java -jar target/linker-0.0.1-SNAPSHOT.jar --spring.config.location=C:/Data/code/linker/src/main/resources/external/
```

## 测试
```shell
http://localhost:8888/linker-server/api/v1/anonymous/ping
```

## 代码混淆
- 妈的，jd-gui，有缓存~重新打包后，拖jar包进去，显示还是原来的代码
- 实体类都需要加上注解：@TableField

## docker部署
> 需要文件
- [Dockerfile](Dockerfile) 
- [application.yml](src%2Fmain%2Fresources%2Fexternal%2Fapplication.yml)
- [linker.jar](target%2Flinker.jar)
```shell
docker build -t equator-linker:latest .

sudo docker run -d -p 8888:8888 --restart always --name linker-instance equator-linker:latest
```