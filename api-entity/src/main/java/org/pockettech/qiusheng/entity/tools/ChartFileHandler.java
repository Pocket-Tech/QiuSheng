package org.pockettech.qiusheng.entity.tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NoArgsConstructor;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.ogg.util.OggInfoReader;
import org.pockettech.qiusheng.entity.Data.Chart;
import org.pockettech.qiusheng.entity.Data.Song;
import org.pockettech.qiusheng.entity.Data.User;

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

@NoArgsConstructor
public class ChartFileHandler {

    private String zipChartFilePath;
    private String unzipChartFilePath;
    private String rootResourcePath = "http://localhost/resource";

    private long file_size;

    private JsonObject meta;
    private JsonObject jsonSong;


    public ChartFileHandler(String rootResourcePath) {
        this.rootResourcePath = rootResourcePath;
    }

    //获取文件后缀
    public static String getFileExtension(String filePath) {
        String extension = "";
        extension = filePath.substring(filePath.lastIndexOf("."));

        return extension;
    }

    //初始化解压参数
    public void setZipChartFile(String zipChartFilePath) throws UnsupportedEncodingException {
//        zipChartFilePath = new String(zipChartFilePath.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        this.zipChartFilePath = zipChartFilePath;
        this.unzipChartFilePath = zipChartFilePath;

        File oldName = new File(this.zipChartFilePath);
        File newName = new File(this.zipChartFilePath + "x");

        if(oldName.renameTo(newName))
            this.zipChartFilePath = this.zipChartFilePath + "x";
    }

    //解压文件，有一说一其实可以跟上面一并做了
    public void unzipFile() {
        FileOutputStream fos = null;
        ZipInputStream zIStream = null;
        InputStream iStream = null;

        try {
            ZipFile zipFile = new ZipFile(zipChartFilePath);
            zIStream = new ZipInputStream(new FileInputStream(zipChartFilePath));
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

    //从谱面文件获取谱面某些元数据
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

        meta = (JsonObject) new JsonParser().parse(content).getAsJsonObject().get("meta").getAsJsonObject();
        jsonSong = (JsonObject) meta.get("song").getAsJsonObject();
    }

    //获取文件的MD5值
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


    //返回从元数据构造的歌曲对象
    public Song returnSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        int nowDay = Integer.parseInt(simpleDateFormat.format(System.currentTimeMillis()).toString());
        return new Song(
                jsonSong.get("id").getAsInt(),
                240,
                jsonSong.get("bpm").getAsInt(),
                jsonSong.get("title").getAsString(),
                jsonSong.get("artist").getAsString(),
//                meta.get("mode").getAsInt(),
                meta.get("mode").getAsInt(),
                nowDay,
                null,
                rootResourcePath  + "/_song_" + jsonSong.get("id").getAsInt() + "/" + jsonSong.get("file").getAsString(),
                null,
                rootResourcePath  + "/_song_" + jsonSong.get("id").getAsInt() + "/" + meta.get("background").getAsString()
        );
    }

    //返回从元数据构造的谱面对象
    public Chart returnChart() {
        String filename = "";
        filename = unzipChartFilePath.substring(unzipChartFilePath.lastIndexOf("\\") + 1);
        User user = new User(0, meta.get("creator").getAsString());
        Chart chart = new Chart(
                meta.get("id").getAsInt(), //cid
                jsonSong.get("id").getAsInt(),//sid
                0,//uid
//                meta.get("creator").getAsString(),//creator
                meta.get("version").getAsString(),//version
                6,//c_level
                0,//type
                file_size,//size
                meta.get("mode").getAsInt(),//mode
                null,
                rootResourcePath + "/_song_" + jsonSong.get("id").getAsInt() + "/" + filename, //file_path
                null,
                user
        );
//        chart.setMsgFromSong(returnSong());

        return chart;
    }

    //解析音频文件时长
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
