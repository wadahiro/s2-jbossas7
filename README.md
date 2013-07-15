s2-jbossas7
===========

s2-jbossas7は、S2ContainerをJBoss AS7上で動作させるための拡張モジュールです。

使い方
-----------
1. pom.xmlの変更

    Mavenリポジトリに http://wadahiro.github.com/maven/release を追加します。
```xml
    <repositories>
        <repository>
            <id>maven.seasar.org</id>
            <name>The Seasar Foundation Maven2 Repository</name>
            <url>http://maven.seasar.org/maven2</url>
        </repository>
        <!-- 以下を追加 -->
        <repository>
            <id>maven.wadahiro.github.com</id>
            <name>The wadahiro's Maven2 Repository</name>
            <url>http://wadahiro.github.com/maven/release</url>
        </repository>
    </repositories>
```
    s2-jbossas7への依存関係を追加する。
```xml
        <!-- SAStruts -->
        <dependency>
            <groupId>org.seasar.sastruts</groupId>
            <artifactId>sa-struts</artifactId>
            <version>1.0.4-sp9</version>
        </dependency>
        <!-- 以下を追加 -->
        <dependency>
            <groupId>org.seasar.jboss</groupId>
            <artifactId>s2-jbossas7</artifactId>
            <version>0.1</version>
        </dependency>
```

2. VFSResourcesFactoryの登録

    JBoss AS7に対応したVFSResourcesFactoryを登録します。登録方法として3つ紹介しますが、どの方法でも構いません。

    1. Servlet 3.0の機能を利用して登録する

        web.xmlのweb-appタグをServlet 3.0の物に変更します。
```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0"> 
```
        S2ContainerServletの定義を削除(またはコメントアウト)してください。
```xml
        <!-- 
        <servlet>
              <servlet-name>s2container</servlet-name>
            <servlet-class>org.seasar.framework.container.servlet.S2ContainerServlet</servlet-class>
            <load-on-startup>2</load-on-startup>
        </servlet>
        -->

        <!--
        <servlet-mapping>
            <servlet-name>s2container</servlet-name>
            <url-pattern>/s2container</url-pattern>
        </servlet-mapping>
        -->
```
    2. S2ContainerServletOnJBossAS7を明示的に定義して登録する

        web.xmlに、S2ContainerServletの代わりにorg.seasar.framework.container.servlet.S2ContainerServletOnJBossAS7を定義します。web-appタグは旧来の物でも構いません。
```xml
        <servlet>
            <servlet-name>s2container</servlet-name>
            <servlet-class>org.seasar.framework.container.servlet.S2ContainerServletOnJBossAS7</servlet-class>
            <load-on-startup>2</load-on-startup>
        </servlet>

        <servlet-mapping>
            <servlet-name>s2container</servlet-name>
            <url-pattern>/s2container</url-pattern>
        </servlet-mapping>
```
    3. s2container.diconの定義で登録する

        s2container.diconに以下の記述を追加します。Servletを使わずにVFSResourcesFactoryを登録しますので、web.xmlの修正は不要です。
```xml
        <component>
            @org.seasar.framework.util.ResourcesUtil@addResourcesFactory(
                "vfs", 
                new org.seasar.framework.util.VFSResourcesFactory()
            )
        </component>
```

ライセンス
-----------
Copyright &copy; 2012 Hiroyuki Wada  
Licensed under the [Apache License, Version 2.0][Apache]
 
[Apache]: http://www.apache.org/licenses/LICENSE-2.0


