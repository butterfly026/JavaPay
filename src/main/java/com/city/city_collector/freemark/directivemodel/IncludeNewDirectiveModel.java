package com.city.city_collector.freemark.directivemodel;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class IncludeNewDirectiveModel
        implements TemplateDirectiveModel {
    private static final String PATH = "path";
    private static final String ENCODING = "encoding";

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        if (params.containsKey("path")) {
            String path = params.get("path").toString();
            String encoding = params.get("encoding") == null ? "UTF-8" : params.get("encoding").toString();

            File file = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!file.exists()) file = new File("");

            file = new File(file.getAbsolutePath(), "freemark" + path);

            if (!file.exists()) {
                throw new TemplateModelException("引入的文件不存在：" + file.getPath());
            }
            Writer out = env.getOut();
            FileInputStream fis = new FileInputStream(file);
            out.write(IOUtils.toString(fis, encoding));
            IOUtils.closeQuietly(fis);

            if (body != null)
                body.render(env.getOut());
            else
                throw new TemplateModelException("includeNew标签之间至少需要一个空格，示例：<@includeNew path='xxx'> </@includeNew>");
        }
    }
}
