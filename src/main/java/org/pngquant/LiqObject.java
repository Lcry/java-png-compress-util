package org.pngquant;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

abstract class LiqObject {
    static {
        //方法一：直接设置java.library.path依赖路径
        // libimagequant.jnilib or libimagequant.so must be in java.library.path
//        System.loadLibrary("libimagequant");
        //方法二: 直接拷贝加载
        try {
            loadLibrary();
            System.out.println("load libimagequant lib successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long handle;

    /**
     * 根据不同系统load不同依赖
     *
     * @throws IOException IO异常
     */
    public static void loadLibrary() throws IOException {
        File directory = new File(System.getProperty("java.io.tmpdir"), "libimagequant_temp");
        System.out.println("load lib to path:" + directory.getAbsolutePath());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("create libimagequant library dir failed!");
            }
        }
        FileUtils.cleanDirectory(directory);
        //mac:".jnilib", windows:".dll" ,linux:".so"
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            loadLib(directory, "libimagequant.jnilib");
        } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
            loadLib(directory, "libimagequant.dll");
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            loadLib(directory, "libimagequant.so");
        } else {
            throw new RuntimeException("unsupported os for libimagequant!");
        }
    }

    /**
     * load依赖
     *
     * @param directory 目录
     * @param libName   依赖名字
     * @throws IOException IO异常
     */
    private static void loadLib(File directory, String libName) throws IOException {
        FileUtils.copyInputStreamToFile(
                getResourceAsStream(File.separator + "libimagequant" + File.separator + libName),
                new File(directory, libName));
        System.load(directory.getPath() + File.separator + libName);
    }

    private static InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? LiqObject.class.getResourceAsStream(resource) : in;
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Free memory used by the library. The object must not be used after this call.
     */
    abstract public void close();

    @Override
    protected void finalize() {
        close();
    }
}
