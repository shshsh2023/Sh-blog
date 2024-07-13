package com.sh.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.nio.file.Paths;

public class CodeGenerator {

    private final static String url = "jdbc:mysql://localhost:3306/shblog?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private final static String driver = "com.mysql.cj.jdbc.Driver";
    private final static String username = "root";
    private final static String password = "root";


    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password).globalConfig(builder -> {
                    builder.author("song")
                            .outputDir(Paths.get(System.getProperty("user.dir")) + "/shBlogBacken/backend" + "/src/main/java")
                            .commentDate("yyyy/MM/dd HH:mm:ss");
                }).packageConfig(builder -> {
                    builder.parent("com.sh.pojo")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper.xml");
                }).strategyConfig(builder -> {
                    builder.entityBuilder()
                            .enableLombok();
                }).templateEngine(new VelocityTemplateEngine()).execute();
        System.out.println(Paths.get(System.getProperty("user.dir")));
    }


//    public static void main(String[] args) {
    // 代码生成器
//        AutoGenerator mpg = new AutoGenerator();

//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "\\src\\main\\java");
//        gc.setAuthor("sara");
//        gc.setIdType(IdType.AUTO);
//        gc.setOpen(false);
//        gc.setBaseResultMap(true);
//        gc.setBaseColumnList(true);
//        gc.setFileOverride(false);
//        gc.setServiceName("%sService");
//        mpg.setGlobalConfig(gc);
//
//        mpg.setDataSource(saraDataSourceConfig());
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("com.sh");
//        pc.setMapper("dao");
//        mpg.setPackageInfo(pc);
//
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();
////        templateConfig.setController(null);
////        templateConfig.setService(null);
////        templateConfig.setServiceImpl(null);
////        templateConfig.setEntity(null);
////        templateConfig.setMapper(null);
////        templateConfig.setXml(null);
//        mpg.setTemplate(templateConfig);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setEntityLombokModel(true);
//        strategy.setRestControllerStyle(true);
//        strategy.setEntityTableFieldAnnotationEnable(true);
//
//        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
//        strategy.setLogicDeleteFieldName("deleted");
//        mpg.setStrategy(strategy);
//
//        mpg.execute();
//    }


//    private static DataSourceConfig saraDataSourceConfig() {
//        return new DataSourceConfig()
//                .setDbType(DbType.MYSQL)
//                .setUrl("jdbc:mysql://ip:port/poetize?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
//                .setDriverName("com.mysql.cj.jdbc.Driver")
//                .setUsername("username")
//                .setPassword("password");
//    }
}
