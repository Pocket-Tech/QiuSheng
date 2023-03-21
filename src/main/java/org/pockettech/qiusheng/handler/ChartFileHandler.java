package org.pockettech.qiusheng.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.ogg.util.OggInfoReader;
import org.pockettech.qiusheng.constant.SystemConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pockettech.qiusheng.entity.Chart;
import org.pockettech.qiusheng.entity.Song;
import org.pockettech.qiusheng.entity.User;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartFileHandler {

    // 压缩文件路径
    private String zipChartFilePath;

    // 解压文件路径
    private String unzipChartFilePath;

    // 谱面文件名
    private String chartFileName;

    // 访问文件url前缀
    private String rootResourcePath = SystemConfig.ROOT_RESOURCE_PATH;

    // 文件大小
    private long file_size;

    // 谱面文件数据
    private JsonObject meta;

    // 谱面文件中歌曲数据
    private JsonObject jsonSong;

    public ChartFileHandler(String rootResourcePath) {
        this.rootResourcePath = rootResourcePath;
    }

    /**
     * 获取本地默认存储文件路径
     * @return 文件路径
     */
    public static String getLocalFilePath() throws FileNotFoundException {
        String os = System.getProperty("os.name");
        String filePath = "";

        if (os.toLowerCase().startsWith("win")) {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            filePath = path.getParentFile().getParentFile().getParent() + File.separator + "MalodyV" + File.separator;
            // 项目作为jar包运行时路径会带上“file:\\”，在此找到并删除
            int sub = filePath.indexOf("file:" + File.separator);
            if (sub != -1){
                filePath = filePath.substring(sub + ("file:" + File.separator).length());
            }
        } else {
            filePath = "MalodyV" + File.separator;
        }
        return filePath;
    }

    /**
     * 删除本地文件
     * @param chart 谱面信息
     * @return 删除结果
     */
    public static boolean deleteFileFromChart(Chart chart) {
        try {
            String filePath = getLocalFilePath() + "_song_" + chart.getSid() + File.separator + chart.getC_file_path().substring(chart.getC_file_path().lastIndexOf("/") + 1);
            File file = new File(filePath);
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件后缀
     * @param filePath 文件路径
     * @return 文件后缀
     */
    public static String getFileExtension(String filePath) {
        String extension = "";
        extension = filePath.substring(filePath.lastIndexOf("."));

        return extension;
    }

    /**
     * 初始化解压参数
     * @param zipChartFilePath 待解压的文件路径
     */
    public void setZipChartFile(String zipChartFilePath){
        this.zipChartFilePath = zipChartFilePath;
        this.unzipChartFilePath = zipChartFilePath;

        File oldName = new File(this.zipChartFilePath);
        this.chartFileName = oldName.getName();
        File newName = new File(this.zipChartFilePath + "x");

        if(oldName.renameTo(newName))
            this.zipChartFilePath = this.zipChartFilePath + "x";
    }

    /**
     * 解压文件
     */
    public void unzipFile() {
        FileOutputStream fos = null;
        ZipInputStream zIStream = null;
        InputStream iStream = null;

        try {
            ZipFile zipFile = new ZipFile(zipChartFilePath);
            zIStream = new ZipInputStream(Files.newInputStream(Paths.get(zipChartFilePath)));
            fos = new FileOutputStream(unzipChartFilePath);
            ZipEntry entry = zIStream.getNextEntry();

            assert entry != null;
            iStream = zipFile.getInputStream(entry);

            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = iStream.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
            zipFile.close();
            File file = new File(unzipChartFilePath);
            file_size = file.length();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert iStream != null;
                iStream.close();
                zIStream.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从谱面文件获取谱面某些元数据
     */
    public void getMetaFromFile() {
        String content = "";
        try{
            Path path = Paths.get(unzipChartFilePath);
            Stream<String> lines = Files.lines(path);

            content = lines.collect(Collectors.joining(System.lineSeparator()));
            lines.close();
        }catch(IOException e){
            e.printStackTrace();
        }


        meta = JsonParser.parseString(content).getAsJsonObject().get("meta").getAsJsonObject();
        jsonSong = (JsonObject) meta.get("song");
    }

    /**
     * 获取文件的MD5值
     * @param filePath 文件路径
     * @return 文件的MD5值
     */
    public static String getFileMD5(String filePath){
        BigInteger bigInteger = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            File f = new File(filePath);
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bigInteger = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return bigInteger.toString(16);
    }

    /**
     * 返回从元数据构造的歌曲对象
     * @return 歌曲信息对象
     */
    public Song returnSong() {
        // 获取当前时间，并转换格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        int nowDay = Integer.parseInt(simpleDateFormat.format(System.currentTimeMillis()));

        return new Song(
                jsonSong.get("id").getAsInt(),
                240,
                jsonSong.get("bpm").getAsInt(),
                jsonSong.get("title").getAsString(),
                jsonSong.get("artist").getAsString(),
                meta.get("mode").getAsInt(),
                nowDay,
                null,
                rootResourcePath  + "/_song_" + jsonSong.get("id").getAsInt() + "/" + jsonSong.get("file").getAsString(),
                null,
                rootResourcePath  + "/_song_" + jsonSong.get("id").getAsInt() + "/" + meta.get("background").getAsString()
        );
    }

    /**
     * 返回从元数据构造的谱面对象
     * @return 谱面信息对象
     */
    public Chart returnChart() {
        User user = new User(0, meta.get("creator").getAsString());
        String version = meta.get("version").getAsString();
        int num = Math.max(version.indexOf("LV."), Math.max(version.indexOf("Lv."), version.indexOf("lv.")));
        String level = version.substring(num + 3);

        return new Chart(
                meta.get("id").getAsInt(), //cid
                jsonSong.get("id").getAsInt(),//sid
                0,//uid
                meta.get("version").getAsString(),//version
                level.matches("[0-9]+") ? Integer.parseInt(level) : 0,//c_level
                0,//type
                file_size,//size
                meta.get("mode").getAsInt(),//mode
                null,
                rootResourcePath + "/_song_" + jsonSong.get("id").getAsInt() + "/" + chartFileName, //file_path
                null,
                null,
                null,
                user
        );
    }

    /**
     * 解析音频文件时长
     * @param oggPath 音频文件路径
     * @return 时长
     */
    public static float getOggLength(String oggPath) throws CannotReadException, IOException {

        File file = new File(oggPath);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rwd");
        OggInfoReader oggInfoReader = new OggInfoReader();
        GenericAudioHeader read = oggInfoReader.read(randomAccessFile);
        float length = read.getPreciseLength();

        randomAccessFile.close();
        return length;
    }


}
