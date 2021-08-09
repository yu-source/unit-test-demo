package com.arjun.subjective.demo.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoCodeBuilder
 * 自动代码生成工具类
 *
 * @author lsl
 * @date 2021/08/09
 */
@Slf4j
public class AutoCodeBuilder {

    public static void main(String[] args) {
        String[] tableNames = {
                "tb_mlt_contract_setting",
        };
        //模块
        String childPackage = "dam";
        execute(tableNames, childPackage);
    }

    /**
     * 代码生成工具
     */
    public static void execute(String[] tableNames, String childPackage) {
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 输出文件夹路径
        // 自定义输出位置
        gc.setOutputDir("E:\\autoCodeBuilder");
        gc.setFileOverride(true);
        // ActiveRecord特性
        gc.setActiveRecord(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setEnableCache(false);
        // 自动打开输出目录
        gc.setOpen(true);
        // 作者
        gc.setAuthor("lsl");
        gc.setSwagger2(true);
        // 主键策略
        gc.setIdType(IdType.ASSIGN_ID);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        // 数据库类型
        // 设置数据库连接信息
        dsc.setDbType(DbType.MYSQL);
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setDriverName("com.mysql.jdbc.Driver"); // mysql-connector-java 5
        dsc.setUrl("jdbc:mysql://IP:3306/ngoss-dev?&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        // 用户名
        dsc.setUsername("XXXX");
        // 密码
        dsc.setPassword("xxxxxxx");
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String doubleValue = "number";
                String bigintValue = "bigint";
                String dateValue = "date";
                //将数据库中NUMBER转换成double
                if (fieldType.toLowerCase().contains(doubleValue)) {
                    return DbColumnType.DOUBLE;
                }
                if (fieldType.toLowerCase().contains(bigintValue)) {
                    return DbColumnType.LONG;
                }
                if (fieldType.toLowerCase().contains(dateValue)) {
                    return DbColumnType.DATE;
                }
                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        });
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(false);
        strategy.setRestControllerStyle(true);
        strategy.setEntityLombokModel(true);
        strategy.setEntityTableFieldAnnotationEnable(false);
        // 此处可以移除表前缀表前缀
        // 设置数据库表前缀
        strategy.setTablePrefix("tb");

        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 字段生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityColumns("create_time", "update_time");
        // mapper 父类
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        // 实体父类
        strategy.setSuperEntityClass(Model.class);
        // 接口父类
        strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
        // 接口实现类父类
        strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        // 需要生成的表
        strategy.setInclude(tableNames);

        ConfigBuilder config = new ConfigBuilder(new PackageConfig(), dsc, strategy, new TemplateConfig(), gc);
        List<TableInfo> list = config.getTableInfoList();
        log.info("生成表信息:" + list.toString());

        // 公共字段填充
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(new TableFill("createTime", FieldFill.INSERT));
        tableFills.add(new TableFill("updateTime", FieldFill.UPDATE));
        strategy.setTableFillList(tableFills);
        mpg.setStrategy(strategy);
        // 包配置
        PackageConfig pc = new PackageConfig();
        // 父包名
        pc.setParent("com.tellhow.ngoss");
        // 父包模块名
        pc.setModuleName("");
        // 实体类父包
        pc.setEntity("common.entity." + childPackage);
        // controller父包
        pc.setController(childPackage + ".controller");
        // mapper父包
        pc.setMapper(childPackage + ".mapper");
        // xml父包
        pc.setXml(childPackage + ".xml");
        pc.setServiceImpl(childPackage + ".service.impl");
        pc.setService(childPackage + ".service");
        mpg.setPackageInfo(pc);
        // 执行生成
        mpg.execute();
    }
}
