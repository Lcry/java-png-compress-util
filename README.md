## 🔨Java实现的 Png 图片压缩工具

本项目底层使用 [libimagequant](https://pngquant.org/lib/) 依赖库，使用java JNI调用实现对PNG图片进行压缩，压缩效果和目前在线的 TinyPNG 可以媲美，压缩率比例可以自己参数控制，默认参数可以使PNG格式图片压缩50%~70%左右，本项目已经打包好了windows、mac以及linux所需的依赖包。

### 项目结构

```shell
—>java-png-compress-util
├─src
│  ├─main
│  │  ├─java
│  │  │  ├─com
│  │  │  │  └─biejieshi
│  │  │  │      └─compress 		#工具类
│  │  │  └─org
│  │  │      └─pngquant			#JNI方法
│  │  └─resources
│  │      └─libimagequant		#libimagequant打包依赖
│  └─test
│      ├─java
│      │  └─com
│      │      └─biejieshi
│      │          └─compress	#测试用例
│      └─resources				#测试用例图片位置
└─-
```

### 压缩效果

| 测试用例文件名 | 原文件大小 | 压缩后文件大小 | 压缩率     |
| -------------- | ---------- | -------------- | ---------- |
| test-1.png     | 534.7 Kb   | 227.02 Kb      | **57.5 %** |
| test-2.png     | 103.61 Kb  | 38.22 Kb       | **63.1 %** |
| test-3.png     | 78.69 Kb   | 35.45 Kb       | **54.9 %** |
| test-4.png     | 2.77 Mb    | 1.08 Mb        | **62.1 %** |

### 使用步骤

- 克隆本项目打包并引入依赖

```xml
    <groupId>com.biejieshi.compress</groupId>
    <artifactId>java-png-compress-util</artifactId>
    <version>1.0.0</version>
```

- 使用压缩方法

```java
    /**
     * 测试Png压缩
     * 方法：PngCompressUtils.compressPng(BufferedImage bufferedImage)
     */
    @Test
    public void testPngCompress() {
        String testFileName = "test-1.png";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(testFileName);
        try {
            ImageIO.write(PngCompressUtils.compressPng(ImageIO.read(inputStream)), "png",
                    new FileOutputStream(testFileName.replace(".png", "") + "-compress-result.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("compress complete!");
    }
```

### libimagequant 编译步骤

- 下载源码包

下载地址：https://github.com/ImageOptim/libimagequant

源码是C编写的，java需要通过JNI（java native interface）来调用，不了解JNI的同学可自行百度，简单来讲，JNI就是java像调用本地方法一样调用用C写的代码。

- 编译

Linux下安装java环境， 执行`make java` 

Windows环境下安装MinGW-w64，执行`make java-dll`

**注意：**mac使用".jnilib"文件，windows使用".dll" 文件，linux使用".so"文件（本项目resources/libimagequant下提供了已经编译好的最新版本，可直接使用）

