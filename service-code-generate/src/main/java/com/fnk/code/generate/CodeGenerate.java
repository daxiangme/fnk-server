package com.fnk.code.generate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.fnk.code.generate.config.CodeGenerateConfig;
import com.fnk.common.db.entity.BaseEntity;
import com.fnk.starter.web.controller.BaseController;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 代码生成器<br>
 * 使用此生成器需要将 resources/generate-template.yml 复制一份为 generate-template-dev.yml <br>
 * 根据自己的需要修改配置内的字段属性即可 <br>
 * <a href="https://baomidou.com/pages/981406/">.配置参考->代码生成器配置新 .</a>
 *
 * @author Enigma
 */
public class CodeGenerate {

    public static void main(String[] args) {
        new CodeGenerate().run("generate-template-dev.yml");
        System.out.println("代码生成完成");
    }

    public void run(String templateName) {
        CodeGenerateConfig config;
        try {
            config = this.generateConfig(templateName);
        } catch (IORuntimeException e) {
            throw new RuntimeException("代码生成配置读取错误，请检查配置是否存在");
        }
        if (config.getDb() == null || StrUtil.isEmpty(config.getDb().getUrl()) || StrUtil.isEmpty(config.getDb().getUsername())
                || StrUtil.isEmpty(config.getDb().getPassword())) {
            throw new RuntimeException("请检查数据库配置是否完整");
        }
        this.generate(config);
    }

    private CodeGenerateConfig generateConfig(String templateName) throws IORuntimeException {
        String s = FileUtil.readString(new ClassPathResource(templateName).getPath(), StandardCharsets.UTF_8);
        Yaml yml = new Yaml();
        return yml.loadAs(s, CodeGenerateConfig.class);
    }

    private void generate(CodeGenerateConfig config) {
        FastAutoGenerator.create(config.getDb().getUrl(), config.getDb().getUsername(), config.getDb().getPassword())
                .globalConfig(builder -> {
                    builder.disableOpenDir();
                    builder.author(config.getAuthor())
                            .fileOverride() // 全局覆盖已有文件的配置已失效，已迁移到策略配置中
                            .outputDir(config.getOutput());
                    if (config.isSwagger()) {
                        builder.enableSwagger();
                    }
                })
                .packageConfig(builder -> builder.parent(config.getPkg().getParent())
                        .moduleName(config.getPkg().getModule())
                        .xml("resources.mapper")
                        .service("service")
                        .entity("dal.entity")
                        .mapper("dal.mapper")
                )
                .templateConfig(builder -> {
                    builder.entity("/templates/entity");
                    builder.controller("/templates/controller");
                    builder.service("/templates/service");
                    builder.serviceImpl(null);
                })
                .strategyConfig(builder -> builder.addInclude(config.getTables())
                        // Entity 策略配置
                        .entityBuilder()
//                        .enableFileOverride()
                        .enableLombok()
                        .disableSerialVersionUID()
                        .superClass(BaseEntity.class)
                        .formatFileName("%sDO")
                        .addIgnoreColumns("create_time", "update_time", "deleted")
                        // Service 策略配置
                        .serviceBuilder()
//                        .enableFileOverride()
                        .formatServiceFileName("%sService")
                        // Controller 策略配置
                        .controllerBuilder()
//                        .enableFileOverride()
                        .formatFileName("%sController")
                        .superClass(BaseController.class)
                        .enableRestStyle()
                        // Mapper 策略配置
                        .mapperBuilder()
//                        .enableFileOverride()
                        .superClass(BaseMapper.class)
                        .formatMapperFileName("%sMapper")
                        .enableMapperAnnotation()
                        .enableBaseResultMap()
                        .formatXmlFileName("%sMapper"))
                .injectionConfig(builder -> {
                    // vue代码生成配置
                    builder.customFile(new HashMap<String, String>() {{
                        put("index.vue", "/templates/vue-page.ftl");
                        put("entity-drawer.vue", "/templates/vue-drawer.ftl");
                        put("facade.java", "/templates/facade.ftl");
                        put("facade-impl.java", "/templates/facade-impl.ftl");
                        put("query.java", "/templates/query.ftl");
                        put("request.java", "/templates/request.ftl");
                        put("response.java", "/templates/response.ftl");
                        put("convert.java", "/templates/convert.ftl");
                    }});
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
