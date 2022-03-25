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
└─
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
    <version>2022.1.0</version>
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

### libimagequant 自编译步骤

- 下载源码包

下载地址：https://github.com/ImageOptim/libimagequant

源码是C编写的，java需要通过JNI（java native interface）来调用，不了解JNI的同学可自行百度，简单来讲，JNI就是java像调用本地方法一样调用用C写的代码。

- **Linux 环境下编译**

1. 切换分支到当前稳定版本2.x，比如当前本项目编译的 2.17.0

2. Linux下安装java环境以及gcc环境，然后执行下面命令：

   ```shell
   make clean
   make distclean
   ./configure --extra-cflags="-fPIC"
   make java
   ```

   > 编译完成后就有了 libimagequant.jnilib 文件，这个文件是macos上面用的。

3. 复制步骤2中`make java` 的时候输出的最后一个命令，修改 `libimagequant.jnilib` 为 `libimagequant.so`，然后执行命令，就会生成linux下可用的libimagequant.so，命令内容大概长下面这个样：

   ```shell
   gcc -g -fno-math-errno -funroll-loops -fomit-frame-pointer -Wall -std=c99 -I. -O3 -DNDEBUG -DUSE_SSE=1 -msse -mfpmath=sse -Wno-unknown-pragmas -fexcess-precision=fast  -fPIC -lm -I'/usr/lib/jvm/java-1.8.0-openjdk/include' -I'/usr/lib/jvm/java-1.8.0-openjdk/include/linux' -I'/usr/lib/jvm/java-1.8.0-openjdk/include/win32' -I'/usr/lib/jvm/java-1.8.0-openjdk/include/darwin' -shared -o libimagequant.so org/pngquant/PngQuant.c libimagequant.a
   ```

- **Windows下编译**

  1. 切换分支到当前稳定版本2.x，比如当前本项目编译的 2.17.0

  2. 根据自己的电脑配置下载mingw-w64，对应地址：https://sourceforge.net/projects/mingw-w64/files/，将解压缩路径中bin配置到电脑的path环境变量中，并将bin目录下面的`mingw32-make.exe` 复制一份改为 `make.exe`。

     >- x86_64-posix-sjlj
     >- x86_64-posix-seh
     >- x86_64-win32-sjlj
     >- x86_64-win32-seh
     >- i686-posix-sjlj
     >- i686-posix-dwarf
     >- i686-win32-sjlj
     >- i686-win32-dwarf
     >
     >电脑系统是 64位的，选择 x86_64；如果是 32位 系统，则选择 i686 即可；
     >
     >如果你想要开发 Windows 程序，需要选择 win32 ，而开发 Linux、Unix、Mac OS 等其他操作系统下的程序，则需要选择 posix；
     >
     >异常处理在开发中非常重要，在开发的过程中，大部分的时间会耗在处理各种异常情况上。seh 是新发明的，而 sjlj 则是古老的。seh 性能比较好，但不支持 32位。 sjlj 稳定性好，支持 32位。建议64位操作系统选择seh。

  3. 执行`make java-dll` 即可生成 libimagequant.dll

**注意：** mac使用".jnilib"文件，windows使用".dll" 文件，linux使用".so"文件（本项目resources/libimagequant下提供了已经编译好的最新稳定版本2.17.0，可直接使用）

### 常见问题

1. %HOME%\Temp\libimagequant_temp\libimagequant.dll: %1 不是有效的 Win32 应用程序？

   解决办法：请确认是否有拷贝依赖包到C盘用户临时目录下的权限，程序执行前会先进行拷贝当前操作系统所需的依赖文件到用户临时目录下，详情代码参考：org.pngquant.LiqObject#loadLibrary()方法

   ```shell
   正常操作日志会打印如下：
   load lib to path:C:\Users\lcry\AppData\Local\Temp\libimagequant_temp
   load libimagequant lib successful!
   compress complete!
   ```

2. 压缩后没有效果？

   解决办法：本项目只能针对png图片的格式进行压缩，其他类型图片不支持，另针对有些很小的png图片压缩可能压缩无法达到测试用例中的比率，更多还请使用前自测。

