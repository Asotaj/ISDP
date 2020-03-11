 # 基于SpringBoot智能战略决策平台（缩写ISDP）  
 ##1.部署方式
   1.数据库文件：ISDP\sql\telcom.sql<br>
   2.数据库配置信息：ISDP\src\main\resources\application-*.yml
   <br> 
   3.端口信息：ISDP\src\main\resources\application.yml server:port 
   <br> 
   4.将项目导入IntelliJ IDEA，修改配置信息
   <br> 
   5.运行ISDP\src\main\java\com\telcom\isdp\ISDPApplication.java
   <br>
   6.访问 localhost:8080，账号密码：admin/111111
 
 ##2.数据同步和NLP服务调用
 敏感信息，私下发，然后替换掉<br>
 ISDP\src\main\java\com\telcom\isdp\core\util\DataNtpUtil.java

 ##3.安装软件
 IntelliJ IDEA<br>
 JDK1.8 <br>
 Mysql5.6+<br>
 Maven3