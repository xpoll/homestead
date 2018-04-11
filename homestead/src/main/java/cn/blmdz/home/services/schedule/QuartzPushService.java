package cn.blmdz.home.services.schedule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import cn.blmdz.aide.file.FileServer;
import cn.blmdz.home.baiduyun.BaiduyunFileInfo;
import cn.blmdz.home.baiduyun.BaiduyunService;
import cn.blmdz.home.dao.BaseDataDao;
import cn.blmdz.home.dao.WeeklyDao;
import cn.blmdz.home.enums.EnumsBaseDataType;
import cn.blmdz.home.enums.EnumsWeekly;
import cn.blmdz.home.model.BaseData;
import cn.blmdz.home.model.Weekly;
import cn.blmdz.home.properties.OtherProperties;
import cn.blmdz.home.util.JsonMapper;
import cn.blmdz.home.util.PrintTextLocations;

@Component
public class QuartzPushService {

    @Autowired
    private BaiduyunService baiduyunService;
    @Autowired
    private BaseDataDao baseDataDao;
    @Autowired
    private FileServer fileServer;
    @Autowired
    private OtherProperties properties;
    @Autowired
    private WeeklyDao weeklyDao;

    private static JsonMapper mapper;

    static {
        mapper = JsonMapper.JSON_NON_EMPTY_MAPPER;
    }

    //@Scheduled(cron = "0/1 * * * * ?")
    @Scheduled(cron = "0 0 1 * * ?")
    public void doing() throws Exception {
        BaseData base = baseDataDao.findByType(EnumsBaseDataType.F_H_Z_K.value());
        Set<Long> set = mapper.fromJson(base.getDataJson(), mapper.createCollectionType(Set.class, Long.class));
        Integer num = set.size();
        // "dEWWnYD", "cfhk"
        BaseData kv = baseDataDao.findByType(EnumsBaseDataType.F_H_Z_K_KV.value());
        List<BaiduyunFileInfo> fileInfos = baiduyunService.getFileInfo(null, kv.getDataJson().split(":")[0], kv.getDataJson().split(":")[1], true, set, true);
        System.out.println(num);
        System.out.println(mapper.toJson(fileInfos));
        for (BaiduyunFileInfo fileInfo : fileInfos) {
        	Weekly weekly = weeklyDao.findByFsid(fileInfo.getFsId());
        	if (weekly != null) continue;
            try {
                PDDocument document = null;
                weekly = new Weekly();

                List<String> lists = Lists.newArrayList();
                try {
                    System.out.println("下载请求：" + fileInfo.getName());
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpGet hg = new HttpGet(fileInfo.getLink());
                    hg.setHeader("fileName", fileInfo.getName());
                    CloseableHttpResponse response = httpclient.execute(hg);
                    InputStream content = response.getEntity().getContent();
                    System.out.println("下载完成：" + fileInfo.getName());
                    System.out.println("解析文件请求：" + fileInfo.getName());
                    document = PDDocument.load(content);
                    PrintTextLocations printer = new PrintTextLocations();
                    printer.getText(document);
                    //for (int i = 0; i < ((document.getNumberOfPages() > 5) ? 5 : document.getNumberOfPages()); i++) {
                    for (int i = 0; i < document.getNumberOfPages(); i++) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(new PDFRenderer(document).renderImageWithDPI(i, 200, ImageType.RGB), "jpg", baos);
                        String fileName = "fhzk/" + fileInfo.getFsId() + "-" + i + ".jpg";
                        System.out.println("上传图片开始：" + fileName);
                        fileServer.write(fileName, new ByteArrayInputStream(baos.toByteArray()));
                        System.out.println("上传图片完成：" + fileName);
                        lists.add(properties.getOos().getImgDomain() + "/" + fileName);
                    }
                    weekly.setCategory(EnumsWeekly.W_DEFAULT.value());
                    weekly.setFsid(fileInfo.getFsId());
                    weekly.setStart(0);
                    weekly.setEnd(document.getNumberOfPages());
                    weekly.setName(fileInfo.getName().split("\\.")[0]);
                    weeklyDao.create(weekly);
                    System.out.println("解析文件完成：" + fileInfo.getName());
                } finally {
                    if (document != null) {
                        document.close();
                    }
                }
                StringBuffer sb = new StringBuffer();
                sb.append("#### ").append(fileInfo.getName().split("\\.")[0]).append("\n");
                for (String url : lists) {
                    sb.append("> ![screenshot](").append(url).append(")\n");
                }
                sb.append("> ###### [下载链接有效期8小时](").append(fileInfo.getLink()).append(") \n");
//                System.out.println("推送请求：" + fileInfo.getName());
//                Rebot.markdown(new RebotMarkdown(
//                        EnumsBaseDataType.F_H_Z_K.desc() + ": " + fileInfo.getName().split("\\.")[0], sb.toString()));
//                System.out.println("推送完成：" + fileInfo.getName());
                set.add(fileInfo.getFsId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!num.equals(set.size())) {
            base.setDataJson(mapper.toJson(set));
            baseDataDao.update(base);
            System.out.println("更新完成");
        }
    }
}
