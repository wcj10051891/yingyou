package daogen.dao;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.Node;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import daogen.Config;
import daogen.SvnDownloader;
import daogen.TableInfo;
import daogen.TableMetaData;
import daogen.TableNameMap;
import daogen.Utils;
import daogen.VelocityRuner;
import daogen.entity.EntityGen;

/**
 * 读取生成Entity时候产生的TableNameMap，Entity类名->表名的映射文件，
 * 取的类名表名信息，来生成相应Dao，XXXXDao，包含一些常用CRUD方法，
 * 而且会从svn下载一份对应的最新版本，与生成版本比较合并，不会覆盖。
 * 
 * 也可以生成sharding分表的dao，指定的表名格式为player_item_1或player_item_2，会生成PlayerItemDao
 * @author wangchengjie	wcj10051891@gmail.com
 */
public class DaoGen {
	private static Config cfg = new Config("daoGen.properties");
    private static final String packageName = cfg.getString("dao.package.name", "com.cndw.xianhun.persist.dao");
    private static final String templatePath = cfg.getString("dao.gen.template.file", "src/gen/dao/daoTemplate.vm");
    private static final String shardTemplatePath = cfg.getString("dao.gen.shard.template.file", "src/gen/dao/shardDaoTemplate.vm");
    private static final String outputPath = cfg.getString("dao.gen.output.dir", "../xianhun/src/com/cndw/xianhun/persist/dao");
    
    // 排除列表，不生成
    private static final Set<String> excludes = new HashSet<String>(Arrays.asList(cfg.getString("dao.gen.excludes").split(",")));
    // 包含列表，如果是空，表示生成所有表，如果有值表示生成指定表
    private static final Set<String> includes = new HashSet<String>(Arrays.asList(cfg.getString("dao.gen.includes").split(",")));

    public static void main(String[] args) throws Exception {
    	TableNameMap tableNameMap = new TableNameMap(cfg.getString("entity.gen.tablemapfile", "src/gen/entity/map.properties"));
        Properties map = tableNameMap.load(true);
        Map<String, TableMetaData> tableInfos = null;
        for(Iterator<Object> it = map.keySet().iterator();it.hasNext();)
        {
            String className = String.valueOf(it.next());
            String tableName = map.getProperty(className);

            if (excludes.contains(tableName))
                continue;

            if (includes.isEmpty() || includes.contains(tableName))
            {
                if(tableInfos == null)
                {
                    tableInfos = new TableInfo(cfg).get();
                }
                TableMetaData tableMetaData = tableInfos.get(tableName);
                if(tableMetaData != null)
                {

                    if(EntityGen.shardingTablePattern.matcher(tableName).find())
                        genShardDao(className, tableName, tableMetaData.getColumnNames());
                    else
                        genDao(className, tableName, tableMetaData.getColumnNames());
                }
            }
        }
    }

    private static void genDao(String classFullName, String tableName, List<String> properties) throws Exception
    {
        Map<String, Object> ctx = new HashMap<String, Object>();

        int index = classFullName.lastIndexOf(".");
        String className = classFullName;
        if(index != -1)
            className = classFullName.substring(index + 1);
        ctx.put("packageName", packageName);
        ctx.put("className", className);
        ctx.put("entityName", Utils.firstLowerCase(className));
        ctx.put("tableName", tableName);
        ctx.put("properties", properties);
        ctx.put("classFullName", classFullName);
        String outPutPath = new File(outputPath, className +"Dao.java").getCanonicalPath();
        VelocityRuner.run(ctx, templatePath, outPutPath);
//        和svn对比
        File generated = new File(outPutPath);
        String svnOldDaoFullClassName = packageName + "." + className + "Dao";
        compareToSvnLastest(generated, svnOldDaoFullClassName);
        System.out.println("gen normal dao success:" + className + "->" + outPutPath);
    }
    
    private static void genShardDao(String classFullName, String tableName, List<String> properties) throws Exception
    {
        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put("packageName", packageName);
        int index = classFullName.lastIndexOf(".");
        String className = classFullName;
        if(index != -1)
            className = classFullName.substring(index + 1);
        ctx.put("className", className);
        ctx.put("entityName", Utils.firstLowerCase(className));
        ctx.put("tableName", tableName);
        ctx.put("properties", properties);
        ctx.put("classFullName", classFullName);

        String outPutPath = new File(outputPath, className +"Dao.java").getCanonicalPath();
        VelocityRuner.run(ctx, shardTemplatePath, outPutPath);
//		和svn对比
        File generated = new File(outPutPath);
        String svnOldDaoFullClassName = packageName + "." + className + "Dao";
        compareToSvnLastest(generated, svnOldDaoFullClassName);

        System.out.println("gen shard dao success: " + className + "->" + outPutPath);
    }

    private static void compareToSvnLastest(File o, String className) {
        byte[] data = SvnDownloader.getFile(className);
        if (data != null && data.length > 0) {
            try {
                String result = compare(new FileInputStream(o), new ByteArrayInputStream(data));
                if (result != null && result.length() > 0)
                    Utils.writeFile(o, result);
            } catch (Exception e) {
                System.out.println("compare to svn lastest failure.");
            }
        }
    }
    
    public static String compare(InputStream leftContent, InputStream righContent) {
		try {
			CompilationUnit left = JavaParser.parse(leftContent);
			CompilationUnit right = JavaParser.parse(righContent);
			
			CompareInfo leftInfo = getInfo(left);
			CompareInfo rightInfo = getInfo(right);
			
			ClassOrInterfaceDeclaration leftDef = leftInfo.getClassOrInterface();
			ClassOrInterfaceDeclaration rightDef = rightInfo.getClassOrInterface();
			
			List<ClassOrInterfaceType> leftExtends = ensureNotNull(leftDef.getExtends());
			List<ClassOrInterfaceType> leftImpls = ensureNotNull(leftDef.getImplements());
			
			List<ClassOrInterfaceType> rightExtends = ensureNotNull(rightDef.getExtends());
			List<ClassOrInterfaceType> rightImpls = ensureNotNull(rightDef.getImplements());
			
			if(rightExtends.size() > leftExtends.size()){
				leftInfo.getClassOrInterface().setExtends(rightInfo.getClassOrInterface().getExtends());
			}
			
			if(rightImpls.size() > leftImpls.size()){
				leftInfo.getClassOrInterface().setImplements(rightInfo.getClassOrInterface().getImplements());
			}			
			
			Iterator<Entry<String, FieldDeclaration>> it = rightInfo.getFields().entrySet().iterator();
			while(it.hasNext()){
				Entry<String, FieldDeclaration> en = it.next();
				if(!leftInfo.getFields().containsKey(en.getKey())){
					leftInfo.getClassOrInterface().getMembers().add(en.getValue());
				}
			}
			
			Iterator<Entry<String, MethodDeclaration>> it2 = rightInfo.getMethods().entrySet().iterator();
			Map<String, MethodDeclaration> leftMethods = leftInfo.getMethods();
			while(it2.hasNext()){
				Entry<String, MethodDeclaration> en = it2.next();
				if(!leftMethods.containsKey(en.getKey())){
					leftInfo.getClassOrInterface().getMembers().add(en.getValue());
				}
			}
			
			Iterator<Entry<String, ImportDeclaration>> it3 = rightInfo.getImports().entrySet().iterator();
			while(it3.hasNext()){
				Entry<String, ImportDeclaration> en = it3.next();
				if(!leftInfo.getImports().containsKey(en.getKey())){
					left.getImports().add(en.getValue());
				}
			}
			return left.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static CompareInfo getInfo(CompilationUnit left) {
		ClassOrInterfaceDeclaration coid = null;
		Map<String, MethodDeclaration> methods = new LinkedHashMap<String, MethodDeclaration>();
		Map<String, FieldDeclaration> fields = new LinkedHashMap<String, FieldDeclaration>();
		Map<String, ImportDeclaration> imports = new LinkedHashMap<String, ImportDeclaration>();
		for(TypeDeclaration td : left.getTypes()){
			if(td instanceof ClassOrInterfaceDeclaration){
				coid = (ClassOrInterfaceDeclaration)td;
				for(BodyDeclaration bd : coid.getMembers()){
					if(bd instanceof MethodDeclaration){
						MethodDeclaration _m = (MethodDeclaration)bd;
						StringBuilder method_unique_key = new StringBuilder();
						List<Parameter> params = _m.getParameters();
						if(params == null || params.size() == 0){
							method_unique_key.append(_m.getType().toString()).append("_").append(_m.getName());
						}else{
							method_unique_key.append(_m.getType().toString()).append("_").append(_m.getName());
							for(int i=0;i<params.size();i++){
								method_unique_key.append("_");
								method_unique_key.append(params.get(i).getType());
							}
						}
						methods.put(method_unique_key.toString(), _m);
					}else if(bd instanceof FieldDeclaration){
						FieldDeclaration _m = (FieldDeclaration)bd;
						fields.put(_m.getVariables().get(0).toString(), _m);
					}
				}
			}
		}
		List<ImportDeclaration> importss = left.getImports();
		if(importss != null){
			for(ImportDeclaration id : importss)
				imports.put(id.getName().getName(), id);
		}
		return new CompareInfo(coid, fields, methods, imports);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Node> List<T> ensureNotNull(List<T> objects) {
		return objects == null ? Collections.EMPTY_LIST : objects;
	}
	static class CompareInfo {
		private ClassOrInterfaceDeclaration classOrInterface;
		private Map<String, FieldDeclaration> fields;
		private Map<String, MethodDeclaration> methods;
		private Map<String, ImportDeclaration> imports;
		
		public CompareInfo(ClassOrInterfaceDeclaration classOrInterface,
				Map<String, FieldDeclaration> fields,
				Map<String, MethodDeclaration> methods, Map<String, ImportDeclaration> imports) {
			this.classOrInterface = classOrInterface;
			this.fields = fields;
			this.methods = methods;
			this.imports = imports;
		}
		public ClassOrInterfaceDeclaration getClassOrInterface() {
			return classOrInterface;
		}
		public Map<String, FieldDeclaration> getFields() {
			return fields;
		}
		public Map<String, MethodDeclaration> getMethods() {
			return methods;
		}
		public Map<String, ImportDeclaration> getImports() {
			return imports;
		}
	}
}
