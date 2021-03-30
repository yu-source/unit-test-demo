package com.arjun.subjective.demo.dbUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHint;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

public class DBDataToWord {
    public static String[] FILEDS = new String[]{"id","字段名","字段类型","长度","描述"};
    public static int[] COLUMN_WIDTHS = new int[] {1504,1504,1504,1504,1504,1504};
    public static String[] FILED_NAMES = new String[]{"id","name","reftype","length","descrip"};
    public static String DATABASE="jysystem";

    public static void main(String[] args) throws Exception {
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("BASE_ACCOUNT", "登录账号");
        params.put("BASE_ACCOUNT_LOGS", "登录日志");
        params.put("BASE_ACTION", "系统资源-功能操作");
        params.put("BASE_API", "系统资源-API接口");
        params.put("BASE_APP", "系统应用-基础信息");
        params.put("BASE_AUTHORITY", "系统权限-菜单权限、操作权限、API权限");
        params.put("BASE_DEVELOPER", "系统用户-开发者信息");
        params.put("BASE_MENU", "系统资源-菜单信息");
        params.put("BASE_ROLE", "系统角色-基础信息");
        params.put("BASE_TENTANT", "租户信息表");
        params.put("BASE_TENTANT_MODULES", "租户模块");
        params.put("BASE_USER", "系统用户-管理员信息");
        params.put("GATEWAY_ACCESS_LOGS", "开放网关-访问日志");
        params.put("GATEWAY_IP_LIMIT", "开放网关-IP访问控制-策略");
        params.put("GATEWAY_RATE_LIMIT", "开放网关-流量控制-策略");
        params.put("GATEWAY_ROUTE", "开放网关-路由");
        params.put("OAUTH_ACCESS_TOKEN", "oauth2访问令牌");
        params.put("OAUTH_CLIENT_DETAILS", "oauth2客户端信息");
        params.put("OAUTH_CLIENT_TOKEN", "oauth2客户端令牌");
        params.put("OPT_CASE_INFO", "出清方案基本信息表");
        params.put("SYS_MARKET_BASE_INFO", "市场基本信息表");
        params.put("SYS_MARKET_RULE_SETTEL_CONFIG", "市场结算规则配置表");
        params.put("SYS_MARKET_RULE_SUBJECT_CONFIG", "市场参与主体规则配置表");
        params.put("SYS_PRODUCT_BASE_INFO", "产品基本信息表");
        params.put("SYS_PRODUCT_CLEARING_CONFIG", "产品出清算法配置");
        params.put("SYS_PRODUCT_CLEARING_CONFIG_CHANGE", "产品出清算法配置变更表");
        params.put("SYS_PRODUCT_MARKET", "产品与市场信息关联表");
        params.put("SYS_PRODUCT_MLT_DECOMPOSE_CONFIG", "产品中长期电量分解配置");
        params.put("SYS_PRODUCT_MLT_DECOMPOSE_CONFIG_CHANGE", "产品中长期电量分解配置变更表");
        params.put("SYS_PRODUCT_QUOTE_CONFIG", "产品交易报价配置");
        params.put("SYS_PRODUCT_QUOTE_CONFIG_CHANGE", "产品交易报价配置变更表");
        params.put("TBL_ASM_MLT_DECOMPOSE_B_DAY", "省间中长期日分解电量信息表");
        params.put("TBL_ASM_STATEMENT_RESULT_IN", "省内辅助服务市场结算结果");
        params.put("TBL_ASM_STATEMENT_RESULT_OUT", "省间辅助服务市场结算结果");
        params.put("TBL_ASM_TIELINE_PLAN", "省间联络线计划表");
        params.put("TBL_CON_MAINT_B_PLAN", "检修计划基本信息表");
        params.put("TBL_CON_MAINT_B_TEMP", "临时检修信息表");
        params.put("TBL_CON_MAINT_B_UNIT", "机组检修基本信息表");
        params.put("TBL_CON_PARTITION_B_PRICE_ACM", "实时市场分区电价信息表");
        params.put("TBL_CON_PARTITION_B_PRICE_DAM", "日前市场分区电价信息表");
        params.put("TBL_CON_PLANT_B_PLAN_OUTPUT", "电厂出力计划信息表");
        params.put("TBL_CON_PWRGRID_B_LF_96POINT", "电网负荷预测数据96点表");
        params.put("TBL_CON_PWRGRID_B_OPERATION", "电网运行信息表");
        params.put("TBL_CON_PWRGRID_B_POWER", "用户用电量表");
        params.put("TBL_CON_SECTION_B", "断面基本信息");
        params.put("TBL_CON_SECTION_H_DAY", "断面日电量历史数据表");
        params.put("TBL_CON_SECTION_LIMIT", "断面限额/约束信息");
        params.put("TBL_CON_SECTION_R_EQUIP", "断面包含设备信息");
        params.put("TBL_CON_SECTION_STATU_AC", "断面实时状态表");
        params.put("TBL_CON_SYSTEM_B_PRICE_ACM", "实时市场系统电价信息表");
        params.put("TBL_CON_SYSTEM_B_PRICE_DAM", "日前市场系统电价信息表");
        params.put("TBL_CON_TIELINE_B", "联络线基本信息表");
        params.put("TBL_CON_TIELINE_H_DAY", "联络线日电量历史数据表");
        params.put("TBL_CON_TIELINE_P_HISTORY", "联络线功率历史数据表");
        params.put("TBL_DAY_COLD_WARN", "寒潮预警天数表");
        params.put("TBL_DAY_GUA_POWER_SUPPLY", "保供电天数表");
        params.put("TBL_DAY_MAJOR_FESTVALS", "重大节日天数表");
        params.put("TBL_DAY_TEM_WARN", "高温预警天数表");
        params.put("TBL_DEV_BUSBAR_LOAD", "母线负荷基本表");
        params.put("TBL_DEV_BUSBAR_LOAD_FORECAST", "母线负荷预测表");
        params.put("TBL_DEV_ELEBUS_BASIC", "电气母线基本信息表");
        params.put("TBL_DEV_GEN_ASSIGN_STATE", "机组指定状态表");
        params.put("TBL_DEV_GEN_CONNEC_BUS", "机组指定并网母线表");
        params.put("TBL_DEV_GEN_INITIAL_STATE", "机组初始状态表");
        params.put("TBL_DEV_GEN_MAINT_B", "机组检修基本信息表（待定）");
        params.put("TBL_DEV_GEN_P", "机组物理参数表");
        params.put("TBL_DEV_GEN_PLAN_DIRECT", "机组调试计划表");
        params.put("TBL_DEV_GEN_PLAN_NET", "网调机组计划");
        params.put("TBL_DEV_GEN_RAMP_CAPABILITY", "机组调节能力表");
        params.put("TBL_DEV_GEN_SHUTDOWN_CURVE", "机组停机曲线表");
        params.put("TBL_DEV_GEN_STARTUP_CURVE", "机组启动曲线表");
        params.put("TBL_DEV_PLAN_ENERGY_DAY", "电厂每日发电量表");
        params.put("TBL_DEV_PLAN_OUTPUT_ACTUAL", "厂站实际出力表");
        params.put("TBL_DEV_PWRTRANSFM_H_DAY", "变压器日电量历史数据表");
        params.put("TBL_DEV_SWITCH_STATUS", "开关刀闸状态表");
        params.put("TBL_DEV_TERMINATE_PLAN", "输变电设备停运计划表");
        params.put("TBL_DEV_UNIT_OUTPUT_ACTUAL", "机组实际出力表");
        params.put("TBL_MLT_CONTRACT_B", "中长期合同分解电量信息");
        params.put("TBL_MLT_CONTRACT_DECOMPOSE_B", "中长期合同分解电量信息");
        params.put("TBL_MLT_CONTRACT_DECOMPOSE_B_DETAIL", "中长期合同分解电量明细表");
        params.put("TBL_MLT_DECOMPOSE_B_DAY", "中长期日分解电量信息表（待定）");
        params.put("TBL_MLT_GUARANTEE_POWER", "保障性收购购电量信息表");
        params.put("TBL_NEWENERGY_OUTPUT_PREDICTION", "新能源出力预测");
        params.put("TBL_NEWENERGY_WIND_LIGHT_DATA", "新能源弃风弃光量数据");
        params.put("TBL_NEWENERGY_WIND_LIGHT_DATA_NEW", "新能源弃风弃光数据");
        params.put("TBL_ORG_GENCOM_B", "发电企业注册信息表");
        params.put("TBL_ORG_GOV_ONGRID_PRICE", "政府核准上网电价表");
        params.put("TBL_ORG_TRADECOM_B", "售电公司注册信息表");
        params.put("TBL_ORG_WHOLESALECOM_B", "批发用户注册信息表");
        params.put("TBL_ORG_WHOLESALECOM_CATA_PRICE", "用户目录电价");
        params.put("TBL_PORTAL_INFORMATION_TRENDS", "门户-资讯动态");
        params.put("TBL_PORTAL_LEARNING_TRAINING", "门户-学习培训");
        params.put("TBL_PWDS_DECLARE_B_DAM", "用电侧日前申报信息表");
        params.put("TBL_PWDS_MARKET_STATEMENT_B_DAY", "用电侧日清分信息");
        params.put("TBL_PWDS_MARKET_STATEMENT_B_DAY_DETAIL", "用电日清分明细表");
        params.put("TBL_PWDS_MARKET_STATEMENT_B_DAY_UNBAL_FUNDS_DETAIL", "用电日清分不平衡资金明细");
        params.put("TBL_PWDS_MARKET_STATEMENT_B_MONTH", "用电侧月结算信息");
        params.put("TBL_PWDS_MARKET_STATEMENT_B_MONTH_UNBAL_FUNDS", "用电月结算不平衡资金明细");
        params.put("TBL_PWDS_POWER_96POINT", "用户用电量信息");
        params.put("TBL_PWDS_POWER_MARKET_STATEMENT_B_DAY", "用电侧日清分信息");
        params.put("TBL_PWDS_PRICE_B_ACM", "用电侧实时市场出清电价信息表");
        params.put("TBL_PWDS_PRICE_B_DAM", "用电侧日前市场出清电价信息表");
        params.put("TBL_PWDS_RESULT_B_DAM", "用电侧日前市场交易结果信息表");
        params.put("TBL_PWGS_DECLARE_B_DAM", "发电侧日前申报信息表");
        params.put("TBL_PWGS_DECLARE_B_DAM_DETAIL", "发电侧日前申报信息表");
        params.put("TBL_PWGS_MARKET_STATEMENT_B_DAY", "发电日清分信息");
        params.put("TBL_PWGS_MARKET_STATEMENT_B_DAY_DETAIL", "发电日清分明细表");
        params.put("TBL_PWGS_MARKET_STATEMENT_B_DAY_UNBAL_FUNDS_DETAIL", "发电日清分不平衡资金明细");
        params.put("TBL_PWGS_MARKET_STATEMENT_B_MONTH", "发电月结算信息");
        params.put("TBL_PWGS_MARKET_STATEMENT_B_MONTH_DETAIL", "发电月结算明细表");
        params.put("TBL_PWGS_MARKET_STATEMENT_B_MONTH_UNBAL_FUNDS", "发电月结算不平衡资金信息");
        params.put("TBL_PWGS_POWER_96POINT", "发电侧主体实际发电量");
        params.put("TBL_PWGS_POWER_MARKET_STATEMENT_B_DAY", "发电日清分电量信息");
        params.put("TBL_PWGS_RESULT_B_ACM", "发电侧实时市场交易结果信息表");
        params.put("TBL_PWGS_RESULT_B_DAM", "发电侧日前市场交易结果信息表");
        params.put("TBL_PWGS_WINNING_RESULT_ACM_DETAIL", "实时中标量详情表");
        params.put("TBL_PWGS_WINNING_RESULT_DAM", "日前中标量主体表");
        params.put("TBL_PWGS_WINNING_RESULT_DAM_DETAIL", "日前中标量详情表");
        params.put("TBL_TIDE_SECTION_ACM", "实时市场断面潮流出清结果");
        params.put("TBL_TIDE_SECTION_DAM", "日前市场断面潮流出清结果");
        params.put("TBL_TRANSPROVINCIAL_DECLARATION_ACM", "跨区现货交易实时市场申报表");
        params.put("TBL_TRANSPROVINCIAL_DECLARATION_ACM_DETAIL", "跨区现货交易实时市场申报信息明细表");
        params.put("TBL_TRANSPROVINCIAL_DECLARATION_DAM", "跨区现货交易日前市场申报信息表");
        params.put("TBL_TRANSPROVINCIAL_DECLARATION_DAM_DETAIL", "跨区现货交易日前市场申报信息明细表");
        params.put("TBL_TRANSPROVINCIAL_STATEMENT_ACM", "跨区现货交易的实时出清结果");
        params.put("TBL_TRANSPROVINCIAL_STATEMENT_ACM_DETAIL", "跨区现货交易的实时出清结果明细表");
        params.put("TBL_TRANSPROVINCIAL_STATEMENT_B", "跨区交易的结算结果");
        params.put("TBL_TRANSPROVINCIAL_STATEMENT_DAM", "跨区现货交易的日前出清结果信息");
        params.put("TBL_TRANSPROVINCIAL_STATEMENT_DAM_DETAIL", "跨区现货交易的日前出清结果信息明细表");
        params.put("TBL_TWORULES_ASSESSMENT_RESULTS", "两个细则考核结果");
        params.put("TBL_UNBALANCED_FUNDS_B_DAY", "不平衡资金日清分");
        params.put("TBL_UNBALANCED_FUNDS_B_MONTH", "不平衡资金月结算");


//--------------  以下测试内容
//        Connection connection = new DbPoolImpl(1).getConnection();
//        String str = getTableSql("TBL_CON_TIELINE_B");
//        PreparedStatement preparedStatement = connection.prepareStatement(str);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            if (ObjectUtils.isEmpty(resultSet.next())){
//                return;
//            }
//            System.out.println("————————————");
//            String id = resultSet.getString("id");
//            System.out.println(id);
//            String column_name = resultSet.getString("name");
//            System.out.println(column_name);
//            String data_type = resultSet.getString("reftype");
//            System.out.println(data_type);
//            String data_length = resultSet.getString("length");
//            System.out.println(data_length);
//            String comments = resultSet.getString("descrip");
//            System.out.println(comments);
//        }
//        resultSet.close();
//        preparedStatement.close();
//--------------  以上测试内容

        XWPFDocument xdoc = new XWPFDocument();
        DBDataToWord xsg_data = new DBDataToWord();
        for(Map.Entry<String,Object> entry : params.entrySet()){
            String tableName = entry.getKey();
            String tableName_CN = String.valueOf(entry.getValue());
            xsg_data.createTable(xdoc,tableName,tableName_CN);
        }
        xsg_data.saveDocument(xdoc, "E:/" + "xsg" + ".docx");
    }

    /**
     * core code
     * @param xdoc
     * @param tableName
     * @param tableName_CN
     * @throws Exception
     */
    public void createTable(XWPFDocument xdoc,String tableName,String tableName_CN) throws Exception {
        //查数据有多少列

        Connection conn = new DbPoolImpl(3).getConnection();
        String sql = "select count(1) from ("+getTableSql(tableName)+")t";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int count = 1;
        if(rs.next()) {
            count = rs.getInt(1);
        }

        XWPFParagraph p = xdoc.createParagraph();
        setParagraphSpacingInfo(p, true, "0", "80", null, null, true, "500", STLineSpacingRule.EXACT);
        setParagraphAlignInfo(p, ParagraphAlignment.CENTER, TextAlignment.CENTER);
        XWPFRun pRun = getOrAddParagraphFirstRun(p, false, false);

        /**设置标题头 start*/
        setParagraphRunFontInfo(p, pRun, tableName_CN, "宋体", "Times New Roman", "36", true, false, false, false, null, null, 0, 0, 90);
        p = xdoc.createParagraph();
        setParagraphSpacingInfo(p, true, "0", "80", null, null, true, "500", STLineSpacingRule.EXACT);
        setParagraphAlignInfo(p, ParagraphAlignment.CENTER, TextAlignment.CENTER);
        pRun = getOrAddParagraphFirstRun(p, false, false);
        /**end*/
        setParagraphRunFontInfo(p, pRun, tableName, "宋体", "Times New Roman", "36", true, false, false, false, null, null, 0, 0, 90);
        p = xdoc.createParagraph();
        setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "240", STLineSpacingRule.AUTO);
        setParagraphAlignInfo(p, ParagraphAlignment.LEFT, TextAlignment.CENTER);
        pRun = getOrAddParagraphFirstRun(p, false, false);

        // 创建表格21行3列
        XWPFTable table = xdoc.createTable((count==1?1:count+1), FILEDS.length);
        setTableBorders(table, STBorder.SINGLE, "4", "auto", "0");
        setTableWidthAndHAlign(table, "9024", STJc.CENTER);
        setTableCellMargin(table, 0, 108, 0, 108);
        setTableGridCol(table, COLUMN_WIDTHS);

        XWPFTableRow row = table.getRow(0);
        setRowHeight(row, "460", STHeightRule.AT_LEAST);
        XWPFTableCell cell = row.getCell(0);
        setCellShdStyle(cell, true, "FFFFFF", null);
        p = getCellFirstParagraph(cell);
        pRun = getOrAddParagraphFirstRun(p, false, false);

        int index = 0;
        //创建行
        row = table.getRow(index);
        setRowHeight(row, "567", STHeightRule.AT_LEAST);

        //创建列
        for(int i=0;i<FILEDS.length;i++){
            cell = row.getCell(i);
            setCellWidthAndVAlign(cell, String.valueOf(COLUMN_WIDTHS[i]), STTblWidth.DXA, STVerticalJc.TOP);
            p = getCellFirstParagraph(cell);
            pRun = getOrAddParagraphFirstRun(p, false, false);
            setParagraphRunFontInfo(p, pRun, FILEDS[i], "宋体", "Times New Roman", "21", false, false, false, false, null, null, 0, 6, 0);
        }

        index = 1;
        //查询数据库表
        sql = getTableSql(tableName);
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        ResultSetMetaData metaData= rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while(rs.next()){
            row = table.getRow(index);
            for(int i=0;i<columnCount;i++){
                String filedName = FILED_NAMES[i];
                String columnValue = rs.getString(filedName)==null?"":rs.getString(FILED_NAMES[i]);
                if("COLUMN_TYPE".equals(filedName.toUpperCase())){
                    try{
                        String tmp = columnValue.substring(columnValue.indexOf("(")+1,columnValue.length()-1);
                        Integer.parseInt(tmp);
                        columnValue=tmp;
                    }catch (Exception e){
                        columnValue=columnValue;
                    }
                }cell = row.getCell(i);
                setCellWidthAndVAlign(cell, String.valueOf(COLUMN_WIDTHS[i]), STTblWidth.DXA, STVerticalJc.TOP);
                p = getCellFirstParagraph(cell);
                pRun = getOrAddParagraphFirstRun(p, false, false);
                setParagraphRunFontInfo(p, pRun, columnValue, "宋体", "Times New Roman", "21", false, false, false, false, null, null, 0, 6, 0);
            }
            index++;
        }
        System.out.println("创建【"+tableName_CN+"】成功！");
    }

    private static String getTableSql(String tableName){
        return "select distinct A.COLUMN_ID as id, A.column_name name,A.data_type reftype,A.data_length as length,B.comments as descrip " +
                "from user_tab_columns AS A,user_col_comments AS B ,all_cons_columns AS C,USER_TAB_COMMENTS AS D  where A.COLUMN_NAME=B.column_name and A.Table_Name = B.Table_Name " +
                "and A.Table_Name = D.Table_Name and (A.TABLE_NAME=C.table_name) and  A.Table_Name='"+tableName+"' ORDER BY id ";
    }

    /**
     * @Description: 设置Table的边框
     */
    public void setTableBorders(XWPFTable table, STBorder.Enum borderType,
                                String size, String color, String space) {
        CTTblPr tblPr = getTableCTTblPr(table);
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders()
                : tblPr.addNewTblBorders();
        CTBorder hBorder = borders.isSetInsideH() ? borders.getInsideH()
                : borders.addNewInsideH();
        hBorder.setVal(borderType);
        hBorder.setSz(new BigInteger(size));
        hBorder.setColor(color);
        hBorder.setSpace(new BigInteger(space));

        CTBorder vBorder = borders.isSetInsideV() ? borders.getInsideV()
                : borders.addNewInsideV();
        vBorder.setVal(borderType);
        vBorder.setSz(new BigInteger(size));
        vBorder.setColor(color);
        vBorder.setSpace(new BigInteger(space));

        CTBorder lBorder = borders.isSetLeft() ? borders.getLeft() : borders
                .addNewLeft();
        lBorder.setVal(borderType);
        lBorder.setSz(new BigInteger(size));
        lBorder.setColor(color);
        lBorder.setSpace(new BigInteger(space));

        CTBorder rBorder = borders.isSetRight() ? borders.getRight() : borders
                .addNewRight();
        rBorder.setVal(borderType);
        rBorder.setSz(new BigInteger(size));
        rBorder.setColor(color);
        rBorder.setSpace(new BigInteger(space));

        CTBorder tBorder = borders.isSetTop() ? borders.getTop() : borders
                .addNewTop();
        tBorder.setVal(borderType);
        tBorder.setSz(new BigInteger(size));
        tBorder.setColor(color);
        tBorder.setSpace(new BigInteger(space));

        CTBorder bBorder = borders.isSetBottom() ? borders.getBottom()
                : borders.addNewBottom();
        bBorder.setVal(borderType);
        bBorder.setSz(new BigInteger(size));
        bBorder.setColor(color);
        bBorder.setSpace(new BigInteger(space));
    }

    /**
     * @Description: 得到Table的CTTblPr,不存在则新建
     */
    public CTTblPr getTableCTTblPr(XWPFTable table) {
        CTTbl ttbl = table.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl
                .getTblPr();
        return tblPr;
    }

    /**
     * @Description: 设置表格总宽度与水平对齐方式
     */
    public void setTableWidthAndHAlign(XWPFTable table, String width, STJc.Enum enumValue) {
        CTTblPr tblPr = getTableCTTblPr(table);
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr
                .addNewTblW();
        if (enumValue != null) {
            CTJc cTJc = tblPr.addNewJc();
            cTJc.setVal(enumValue);
        }
        tblWidth.setW(new BigInteger(width));
        tblWidth.setType(STTblWidth.DXA);
    }

    /**
     * @Description: 设置单元格Margin
     */
    public void setTableCellMargin(XWPFTable table, int top, int left,
                                   int bottom, int right) {
        table.setCellMargins(top, left, bottom, right);
    }

    /**
     * @Description: 设置表格列宽
     */
    public void setTableGridCol(XWPFTable table, int[] colWidths) {
        CTTbl ttbl = table.getCTTbl();
        CTTblGrid tblGrid = ttbl.getTblGrid() != null ? ttbl.getTblGrid()
                : ttbl.addNewTblGrid();
        for (int j = 0, len = colWidths.length; j < len; j++) {
            CTTblGridCol gridCol = tblGrid.addNewGridCol();
            gridCol.setW(new BigInteger(String.valueOf(colWidths[j])));
        }
    }

    /**
     * @Description: 设置行高
     */
    public void setRowHeight(XWPFTableRow row, String hight,
                             STHeightRule.Enum heigthEnum) {
        CTTrPr trPr = getRowCTTrPr(row);
        CTHeight trHeight;
        if (trPr.getTrHeightList() != null && trPr.getTrHeightList().size() > 0) {
            trHeight = trPr.getTrHeightList().get(0);
        } else {
            trHeight = trPr.addNewTrHeight();
        }
        trHeight.setVal(new BigInteger(hight));
        if (heigthEnum != null) {
            trHeight.setHRule(heigthEnum);
        }
    }

    /**
     * @Description: 设置底纹
     */
    public void setCellShdStyle(XWPFTableCell cell, boolean isShd,
                                String shdColor, STShd.Enum shdStyle) {
        CTTcPr tcPr = getCellCTTcPr(cell);
        if (isShd) {
            // 设置底纹
            CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
            if (shdStyle != null) {
                shd.setVal(shdStyle);
            }
            if (shdColor != null) {
                shd.setColor(shdColor);
                shd.setFill(shdColor);
            }
        }
    }

    /**
     * @Description: 得到CTTrPr,不存在则新建
     */
    public CTTrPr getRowCTTrPr(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();
        return trPr;
    }

    /**
     *
     * @Description: 得到Cell的CTTcPr,不存在则新建
     */
    public CTTcPr getCellCTTcPr(XWPFTableCell cell) {
        CTTc cttc = cell.getCTTc();
        CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
        return tcPr;
    }

    /**
     * @Description: 设置段落间距信息,一行=100 一磅=20
     */
    public void setParagraphSpacingInfo(XWPFParagraph p, boolean isSpace,
                                        String before, String after, String beforeLines, String afterLines,
                                        boolean isLine, String line, STLineSpacingRule.Enum lineValue) {
        CTPPr pPPr = getParagraphCTPPr(p);
        CTSpacing pSpacing = pPPr.getSpacing() != null ? pPPr.getSpacing()
                : pPPr.addNewSpacing();
        if (isSpace) {
            // 段前磅数
            if (before != null) {
                pSpacing.setBefore(new BigInteger(before));
            }
            // 段后磅数
            if (after != null) {
                pSpacing.setAfter(new BigInteger(after));
            }
            // 段前行数
            if (beforeLines != null) {
                pSpacing.setBeforeLines(new BigInteger(beforeLines));
            }
            // 段后行数
            if (afterLines != null) {
                pSpacing.setAfterLines(new BigInteger(afterLines));
            }
        }
        // 间距
        if (isLine) {
            if (line != null) {
                pSpacing.setLine(new BigInteger(line));
            }
            if (lineValue != null) {
                pSpacing.setLineRule(lineValue);
            }
        }
    }

    /**
     * @Description: 得到段落CTPPr
     */
    public CTPPr getParagraphCTPPr(XWPFParagraph p) {
        CTPPr pPPr = null;
        if (p.getCTP() != null) {
            if (p.getCTP().getPPr() != null) {
                pPPr = p.getCTP().getPPr();
            } else {
                pPPr = p.getCTP().addNewPPr();
            }
        }
        return pPPr;
    }

    /**
     * @Description: 设置段落对齐
     */
    public void setParagraphAlignInfo(XWPFParagraph p,
                                      ParagraphAlignment pAlign, TextAlignment valign) {
        if (pAlign != null) {
            p.setAlignment(pAlign);
        }
        if (valign != null) {
            p.setVerticalAlignment(valign);
        }
    }

    public XWPFRun getOrAddParagraphFirstRun(XWPFParagraph p, boolean isInsert,
                                             boolean isNewLine) {
        XWPFRun pRun = null;
        if (isInsert) {
            pRun = p.createRun();
        } else {
            if (p.getRuns() != null && p.getRuns().size() > 0) {
                pRun = p.getRuns().get(0);
            } else {
                pRun = p.createRun();
            }
        }
        if (isNewLine) {
            pRun.addBreak();
        }
        return pRun;
    }

    /**
     * @Description: 得到单元格第一个Paragraph
     */
    public XWPFParagraph getCellFirstParagraph(XWPFTableCell cell) {
        XWPFParagraph p;
        if (cell.getParagraphs() != null && cell.getParagraphs().size() > 0) {
            p = cell.getParagraphs().get(0);
        } else {
            p = cell.addParagraph();
        }
        return p;
    }

    /**
     * @Description: 设置列宽和垂直对齐方式
     */
    public void setCellWidthAndVAlign(XWPFTableCell cell, String width,
                                      STTblWidth.Enum typeEnum, STVerticalJc.Enum vAlign) {
        CTTcPr tcPr = getCellCTTcPr(cell);
        CTTblWidth tcw = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
        if (width != null) {
            tcw.setW(new BigInteger(width));
        }
        if (typeEnum != null) {
            tcw.setType(typeEnum);
        }
        if (vAlign != null) {
            CTVerticalJc vJc = tcPr.isSetVAlign() ? tcPr.getVAlign() : tcPr
                    .addNewVAlign();
            vJc.setVal(vAlign);
        }
    }

    /**
     * @Description: 设置段落文本样式(高亮与底纹显示效果不同)设置字符间距信息(CTSignedTwipsMeasure)
     *            : SUPERSCRIPT上标 SUBSCRIPT下标
     * @param position
     *            :字符间距位置：>0提升 <0降低=磅值*2 如3磅=6
     * @param spacingValue
     *            :字符间距间距 >0加宽 <0紧缩 =磅值*20 如2磅=40
     * @param indent
     *            :字符间距缩进 <100 缩
     */

    public void setParagraphRunFontInfo(XWPFParagraph p, XWPFRun pRun,
                                        String content, String cnFontFamily, String enFontFamily,
                                        String fontSize, boolean isBlod, boolean isItalic,
                                        boolean isStrike, boolean isShd, String shdColor,
                                        STShd.Enum shdStyle, int position, int spacingValue, int indent) {
        CTRPr pRpr = getRunCTRPr(p, pRun);
        if (StringUtils.isNotBlank(content)) {
            // pRun.setText(content);
            if (content.contains("\n")) {// System.properties("line.separator")
                String[] lines = content.split("\n");
                pRun.setText(lines[0], 0); // set first line into XWPFRun
                for (int i = 1; i < lines.length; i++) {
                    // add break and insert new text
                    pRun.addBreak();
                    pRun.setText(lines[i]);
                }
            } else {
                pRun.setText(content, 0);
            }
        }
        // 设置字体
        CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
                .addNewRFonts();
        if (StringUtils.isNotBlank(enFontFamily)) {
            fonts.setAscii(enFontFamily);
            fonts.setHAnsi(enFontFamily);
        }
        if (StringUtils.isNotBlank(cnFontFamily)) {
            fonts.setEastAsia(cnFontFamily);
            fonts.setHint(STHint.EAST_ASIA);
        }
        // 设置字体大小
        CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
        sz.setVal(new BigInteger(fontSize));

        CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr
                .addNewSzCs();
        szCs.setVal(new BigInteger(fontSize));

        // 设置字体样式
        // 加粗
        if (isBlod) {
            pRun.setBold(isBlod);
        }
        // 倾斜
        if (isItalic) {
            pRun.setItalic(isItalic);
        }
        // 删除线
        if (isStrike) {
            pRun.setStrike(isStrike);
        }
        if (isShd) {
            // 设置底纹
            CTShd shd = pRpr.isSetShd() ? pRpr.getShd() : pRpr.addNewShd();
            if (shdStyle != null) {
                shd.setVal(shdStyle);
            }
            if (shdColor != null) {
                shd.setColor(shdColor);
                shd.setFill(shdColor);
            }
        }

        // 设置文本位置
        if (position != 0) {
            pRun.setTextPosition(position);
        }
        if (spacingValue > 0) {
            // 设置字符间距信息
            CTSignedTwipsMeasure ctSTwipsMeasure = pRpr.isSetSpacing() ? pRpr
                    .getSpacing() : pRpr.addNewSpacing();
            ctSTwipsMeasure
                    .setVal(new BigInteger(String.valueOf(spacingValue)));
        }
        if (indent > 0) {
            CTTextScale paramCTTextScale = pRpr.isSetW() ? pRpr.getW() : pRpr
                    .addNewW();
            paramCTTextScale.setVal(indent);
        }
    }

    /**
     * @Description: 跨列合并
     */
    public void mergeCellsHorizontal(XWPFTable table, int row, int fromCell,
                                     int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == fromCell) {
                // The first merged cell is set with RESTART merge value
                getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one,are set with CONTINUE
                getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * @Description: 得到XWPFRun的CTRPr
     */
    public CTRPr getRunCTRPr(XWPFParagraph p, XWPFRun pRun) {
        CTRPr pRpr = null;
        if (pRun.getCTR() != null) {
            pRpr = pRun.getCTR().getRPr();
            if (pRpr == null) {
                pRpr = pRun.getCTR().addNewRPr();
            }
        } else {
            pRpr = p.getCTP().addNewR().addNewRPr();
        }
        return pRpr;
    }

    /**
     * @Description: 保存文档
     */
    public void saveDocument(XWPFDocument document, String savePath)
            throws Exception {
        FileOutputStream fos = new FileOutputStream(savePath);
        System.out.println(fos.toString());
        document.write(fos);
        fos.close();
    }
}
