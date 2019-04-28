package cn.zqtao.monster.config.listener;

import lombok.extern.slf4j.Slf4j;
import cn.zqtao.monster.dao.repository.ParamRepository;
import cn.zqtao.monster.exception.UploadPathMissException;
import cn.zqtao.monster.model.constant.NoteBlogV4;
import cn.zqtao.monster.model.constant.Upload;
import cn.zqtao.monster.model.entity.NBParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;


@Slf4j
@Component
@Order(3)
public class UploadFolderListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ParamRepository paramRepository;
    private final Environment env;

    @Autowired
    public UploadFolderListener(ParamRepository paramRepository, Environment env) {
        this.paramRepository = paramRepository;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initUploadFolder();
    }

    /**
     * 初始化上传文件夹
     *
     * @throws Exception
     */
    private void initUploadFolder() {
        NBParam param = paramRepository.findByName(NoteBlogV4.Param.UPLOAD_TYPE);
        String value = param.getValue();
        if (Upload.Method.LOCAL.name().equalsIgnoreCase(value)) {
            String uploadPathKey = "noteblog.upload.path";
            String path = env.getProperty(uploadPathKey);
            if (!StringUtils.isEmpty(path)) {
                log.info("「怪兽营」APP 文件上传路径设置为：[{}]", path);
                path = path.replace("file:", "");
                File filePath = new File(path + "file/");
                File imgPath = new File(path + "img/");
                boolean f = false, i = false;
                if (!filePath.exists() && !filePath.isDirectory()) {
                    f = filePath.mkdirs();
                }
                if (!imgPath.exists() && !imgPath.isDirectory()) {
                    i = imgPath.mkdirs();
                }
                if (f && i) {
                    log.info("「怪兽营」APP 成功创建上传文件夹目录：[{}] 和 [{}]", path + "file/", path + "img/");
                } else if (f) {
                    log.info("「怪兽营」APP 目录 [{}] 已存在上传文件夹或创建失败", path + "img/");
                } else if (i) {
                    log.info("「怪兽营」APP 目录 [{}] 已存在上传文件夹或创建失败", path + "file/");
                } else {
                    log.info("「怪兽营」APP 已存在上传文件夹！");
                }
            } else {
                log.error("上传路径未正确设置");
                throw new UploadPathMissException("上传路径未正确设置！");
            }
        }
    }
}
