package com.sunny.BizCode.Service.Impl;

import com.sunny.BizCode.Service.GeneratorService;
import com.sunny.BizCode.dao.TableDao;
import com.sunny.BizCode.entity.Flow;
import com.sunny.BizCode.entity.Table;
import com.sunny.BizCode.util.FreemarkerUtil;
import com.sunny.BizCode.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {
    @Resource
    private TableDao tableDao;
    @Resource
    private FreemarkerUtil freemarkerUtil;
    @Resource
    private SqlUtil sqlUtil;

    @Override
    public Integer generator(Flow flow) throws Exception {
        return null;
    }

    @Override
    public Integer generator(Table table) throws Exception {

        //表中文名
        String ctablename = table.getCtablename();
        //Entity类名
        String entityName = table.getEntityname();
        //表名
        String tableName = table.getCtable();
        //对象名
        String entityNameLower = freemarkerUtil.smallfirst(entityName);

        log.error("entityNameLower=" + entityNameLower);
        // oracle查询表字段的语句,如果是其他数据库,修改此处查询语句
        table.setCtable(table.getCtable().toUpperCase());
        List<Table> tablelist = tableDao.getList(table);

        List<Map<String, String>> list = new LinkedList<>();
        //查询内容
        String resultColumns = "";
        //查询条件
        String dynamicCondition = "";
        //新建字段
        String insertname = "ID,";
        //新建值
        String insertvalue = tableName + "_SEQ.NEXTVAL\n,";
//修改内容
        String cupdatevalue = "";
        for (Table t : tablelist) {
            Map<String, String> columnMap = new HashMap<>(16);
            //表字段
            String cname = t.getCname();
            // 字段名称
            String columnName = freemarkerUtil.underlineToHump(t.getCname()).toLowerCase();
            resultColumns = resultColumns + entityName + "." + cname + " as " + columnName + "\n,";
            if ("ddate".equals(columnName)) {
                resultColumns = resultColumns + "to_char(" + entityName + "." + cname + ",'yyyy-MM-dd')" + " as " + "cdate" + "\n,";
                Map<String, String> dateMap = new HashMap<>(16);
                dateMap.put("columnType", "String");
                dateMap.put("entityColumnNo", "cdate");
                list.add(dateMap);
                Map<String, String> afterMap = new HashMap<>(16);
                afterMap.put("columnType", "String");
                afterMap.put("entityColumnNo", "ddateAfter");
                list.add(afterMap);
                Map<String, String> beforeMap = new HashMap<>(16);
                beforeMap.put("columnType", "String");
                beforeMap.put("entityColumnNo", "ddateBefore");
                list.add(beforeMap);

                dynamicCondition = dynamicCondition + " <if test=\"ddateAfter!= null and  ddateAfter!= \'\' \">\n" +
                        "                and " + entityName + ".D_DATE <![CDATA[<=]]>#{ddateAfter}\n" +
                        "            </if>\n" +
                        "            <if test=\"ddateBefore!= null  and  ddateBefore!= \'\' \">\n" +
                        "                and " + entityName + ".D_DATE <![CDATA[>=]]>#{ddateBefore}\n" +
                        "            </if>";

            }

            String cmemo = t.getCmemo();
            columnMap.put("columnName", cmemo);
            // 字段类型
            String columnType = t.getCtype();
            columnType = freemarkerUtil.convertToJava(columnType);
            if ("String".equals(columnType)) {
                dynamicCondition = dynamicCondition + "<if test=\"" + columnName + " != null and " + columnName + " != ''\"> \n and " + entityName + "." + cname + " like concat(concat('%',#{" + columnName + "}),'%')  \n</if>\n";
            } else {
                dynamicCondition = dynamicCondition + "<if test=\"" + columnName + "!= null\"> \n and " + entityName + "." + cname + " = #{" + columnName + "}  \n</if>\n";
            }

            if (!"ID".equals(cname) && !"VERSION".equals(cname) && !"TS".equals(cname)) {
                insertname = insertname + cname.toUpperCase() + "\n,";
                String cvalue = "";
                String cupdate = "";
                if ("Date".equals(columnType)) {
                    cvalue = " #{" + columnName + ",jdbcType=TIMESTAMP}\n,";
                    cupdate = " <if test=\"" + columnName + " != null \">\n" + cname + " = #{" + columnName + "},\n</if>\n";

                } else if ("BigDecimal".equals(columnType) || "DECIMAL".equals(columnType)) {
                    cvalue = "#{" + columnName + ",jdbcType=DECIMAL}\n,";
                    cupdate = " <if test=\"" + columnName + " != null \">\n" + cname + " = #{" + columnName + "},\n</if>\n";


                } else {
                    cvalue = "#{" + columnName + ",jdbcType=VARCHAR}\n,";
                    cupdate = " <if test=\"" + columnName + " != null and " + columnName + " != ''\">\n" + cname + " = #{" + columnName + "},\n</if>\n";
                }
                insertvalue = insertvalue + cvalue;
                cupdatevalue = cupdatevalue + cupdate;

            }


            columnMap.put("columnType", columnType);
            // 字段
            columnMap.put("entityColumnNo", columnName);
            list.add(columnMap);
        }
        resultColumns = resultColumns.substring(0, resultColumns.length() - 1);
        insertname = insertname.substring(0, insertname.length() - 1);
        insertvalue = insertvalue.substring(0, insertvalue.length() - 1);

        String saveUrl = table.getSaveUrl() == null ? "C:\\Users\\jthanwj\\Desktop\\files" : table.getSaveUrl();
        String basePackageUrl = table.getBasePackageUrl() == null ? "com.sunny.fims" : table.getBasePackageUrl();
        Map<String, Object> root = new HashMap<>();

        root.put("resultColumns", resultColumns);
        root.put("cupdatevalue", cupdatevalue);
        root.put("dynamicCondition", dynamicCondition);
        root.put("insertname", insertname);
        root.put("insertvalue", insertvalue);
        root.put("basePackageUrl", basePackageUrl);
        root.put("entityName", entityName);
        root.put("tableName", tableName);
        root.put("entityNameLower", entityNameLower);
        root.put("ctablename", ctablename);
        root.put("columns", list);
        // 生成bean
        freemarkerUtil.generate(root, "entity.ftl", saveUrl, entityName + ".java");
        log.info("生成bean");
        // 生成dto
        freemarkerUtil.generate(root, "dto.ftl", saveUrl, entityName + "Dto.java");
        log.info("生成dto");
        // 生成dao
        freemarkerUtil.generate(root, "dao.ftl", saveUrl, entityName + "Mapper.java");
        log.info("生成dao");
        // 生成mapper
        freemarkerUtil.generate(root, "mapper.ftl", saveUrl, entityName + "Mapper.xml");
        log.info("生成mapper");
        // 生成controller
        freemarkerUtil.generate(root, "controller.ftl", saveUrl, entityName + "Controller.java");
        log.info("生成controller");
        //生成service
        freemarkerUtil.generate(root, "service.ftl", saveUrl, entityName + "Service.java");
        log.info("生成service");
        //生成serviceImpl
        freemarkerUtil.generate(root, "impl.ftl", saveUrl, entityName + "ServiceImpl.java");
        log.info("生成serviceImpl");
        return 1;
    }

    /**
     * 获取表信息
     */
    public List<Map<String, String>> getDataInfo(Table table) {
        // mysql查询表结构的语句,如果是其他数据库,修改此处查询语句
        table.setCtable(table.getCtable().toUpperCase());
        List<Table> list = tableDao.getList(table);

        List<Map<String, String>> columns = new LinkedList<>();

        for (Table t : list) {
            Map<String, String> columnMap = new HashMap<>(16);
            // 字段名称
            String columnName = t.getCname();
            String cmemo = t.getCmemo();
            columnMap.put("columnName", cmemo);
            // 字段类型
            String columnType = t.getCtype();
            columnType = freemarkerUtil.convertToJava(columnType);
            columnMap.put("columnType", columnType);
            // 成员名称
            columnMap.put("entityColumnNo", columnName.toLowerCase());
            columns.add(columnMap);
        }
        return columns;
    }

    public Object export(byte[] zip, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment'", fileName);
        headers.set("Content-Type", "application/zip");

        return new ResponseEntity<byte[]>(zip, headers, HttpStatus.OK);
    }


    @Override
    public Integer getSql(Table table) throws Exception {
        //表中文名
        String ctablename = table.getCtablename();
        //Entity类名
        String entityName = table.getEntityname();
        //表名
        String tableName = table.getCtable();
        String saveUrl = table.getSaveUrl() == null ? "C:\\Users\\use\\Desktop\\资料" : table.getSaveUrl();
        table.setCtable(table.getCtable().toUpperCase());
        List<Table> tablelist = tableDao.getList(table);
        //查询内容
        String resultColumns = "select ";

        //新建字段
        String insertname = "";
        //新建值
        String insertvalue = "";
        String insertsql="insert into "+tableName+" (";
        String smallname = sqlUtil.Deleteunderline(tableName);
        for (Table t : tablelist) {
            String cname = t.getCname();
            String columnName = sqlUtil.Deleteunderline(t.getCname());
            if ("DATE".equals(t.getCtype())) {
                resultColumns = resultColumns + "to_char(" + smallname + "." + cname + ",'yyyy-MM-dd')" + " as c" + columnName + "\n,";
            } else {
                resultColumns = resultColumns + smallname + "." + cname + " as " + columnName + "\n,";
            }
            insertname = insertname + cname.toUpperCase() + "\n,";
            insertvalue = insertvalue+"\"" +columnName+"\""+"," ;
        }
        insertname = insertname.substring(0, insertname.length() - 1);
        insertvalue = insertvalue.substring(0, insertvalue.length() - 1);
        insertsql=  insertsql+insertname+") value ("+insertvalue+")";

        resultColumns = resultColumns.substring(0, resultColumns.length() - 1);

        String sql = resultColumns + " from " + tableName + "  " + smallname;

        sql+=("\n"+insertsql);

        Map<String, Object> root = new HashMap<>();

        root.put("resultColumns", sql);
        freemarkerUtil.generate(root, "sql.ftl", saveUrl, tableName + ".txt");
        log.info("生成sql");
        return 1;
    }


    List<Table> getTableByDs(Table table){
        List<Table> tablelist = tableDao.getList(table);
        return  tablelist;
    }
}
