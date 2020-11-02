# ChooseFile
Android系统的文件选择和查看;在app进行先择文档的libary,通过接入TBS服务实现在app内部查看文档(适配了android10+的Scope Storage文件存储的及访问的新特性)。  
## 实现效果  
![项目效果1](./test1_20201010.gif)![项目效果2](./test2_20201010.gif)![项目效果2](./test3_20201010.gif)  
## 在项目中添加依赖  
* Gradle  
 1.Add it in your root build.gradle at the end of repositories:
```java  
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
    }  
```  
2.Add the dependency  
```java  
dependencies {
    implementation 'com.github.hanjie511:ChooseFile:1.0.0'
  }  
```  
* Maven  
```java  
<repositories>
    <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependency>
    <groupId>com.github.hanjie511</groupId>
    <artifactId>ChooseFile</artifactId>
    <version>1.0.0</version>
</dependency>  
```  




