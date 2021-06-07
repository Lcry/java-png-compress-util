package com.biejieshi.compress;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Unit test for simple App.
 */
public class PngCompressUtilsTest {
    /**
     * 测试png压缩
     */
    @Test
    public void testPngCompress() {
        String testFileName = "test-1.png";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(testFileName);
        try {
            assert inputStream != null;
            ImageIO.write(PngCompressUtils.compressPng(ImageIO.read(inputStream)), "png",
                    new FileOutputStream(testFileName.replace(".png", "") + "-compress-result.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("compress complete!");
    }

}
