/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.bjf.remoting.protobuf;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.bjf.remoting.protobuf.annotation.Msg;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.utils.FieldInfo;
import com.baidu.bjf.remoting.protobuf.utils.ProtobufProxyUtils;
import com.baidu.bjf.remoting.protobuf.utils.Utils;
import com.thoughtworks.qdox.model.JavaClass;

/**
 * 
 * Utility class for generate protobuf IDL content from @{@link Protobuf}
 * 
 * @author xiemalin
 * @since 1.0.1
 */
public class ProtobufIDLGenerator {

    /**
     * get IDL content from class.
     * 
     * @param cls
     *            target protobuf class to parse
     * @return protobuf IDL content in string
     */
    public static String getIDL(Class<?> cls, Map<String, JavaClass> context, Set<String> nestedMsg) {
    	StringBuilder code = new StringBuilder();

        Set<Class<?>> cachedTypes = new HashSet<Class<?>>();
        Set<Class<?>> cachedEnumTypes = new HashSet<Class<?>>();

        // define package
        code.append("package ").append(cls.getPackage().getName()).append(";\n");

        // define outer name class
        code.append("option java_outer_classname = \"").append(cls.getSimpleName()).append("$$Gen").append("\";\n");

        cachedTypes.add(cls);

        JavaClass jclass = context.get(cls.getName());
        if(jclass != null) {
        	 code.append(Utils.getComment(jclass));
        }
        code.append(generateIDL(cls, context, cachedTypes, cachedEnumTypes, nestedMsg));

        return code.toString();
    }

    /**
     * @param code
     * @param cls
     * @return sub message class list
     */
    private static String generateIDL(Class<?> cls, Map<String, JavaClass> context, Set<Class<?>> cachedTypes,
        Set<Class<?>> cachedEnumTypes, Set<String> nestedMsg) {
//        List<Field> fields = FieldUtils.findMatchedFields(cls, Protobuf.class);
//        if (fields.isEmpty()) {
//            throw new IllegalArgumentException("Invalid class [" + cls.getName() + "] no field use annotation @"
//                    + Protobuf.class.getName());
//        }
    	Msg msg = cls.getAnnotation(Msg.class);
        if(msg == null)
        	throw new IllegalArgumentException("protobuf msg class [" + cls.getName() + "] must use annotation @" + Msg.class.getName());
        
        StringBuilder code = new StringBuilder();
        Set<Class<?>> subTypes = new HashSet<Class<?>>();
        Set<Class<Enum>> enumTypes = new HashSet<Class<Enum>>();
        code.append("message ").append(cls.getSimpleName()).append(" {  \n");

        List<FieldInfo> fieldInfos = ProtobufProxyUtils.processDefaultValue(cls.getDeclaredFields());
        for (FieldInfo field : fieldInfos) {
            if (field.hasDescription()) {
                code.append("// ").append(field.getDescription()).append("\n");
            } else {
                JavaClass jclass = context.get(cls.getName());
            	if(jclass != null) {
            		code.append(Utils.getComment(jclass.getFieldByName(field.getField().getName())));
            	}
            }
            if (field.getFieldType() == FieldType.OBJECT) {
                if (CodeGenerator.isListType(field.getField())) {
                    Type type = field.getField().getGenericType();
                    if (type instanceof ParameterizedType) {
                        ParameterizedType ptype = (ParameterizedType) type;

                        Type[] actualTypeArguments = ptype.getActualTypeArguments();

                        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                            Type targetType = actualTypeArguments[0];
                            if (targetType instanceof Class) {
                                Class c = (Class) targetType;
                                if (!cachedTypes.contains(c)) {
                                    cachedTypes.add(c);
                                    if(needImport(c))
                                    	subTypes.add(c);
                                }

                                code.append("repeated ").append(c.getSimpleName()).append(" ")
                                        .append(field.getField().getName()).append("=").append(field.getOrder())
                                        .append(";\n");
                            }
                        }
                    }
                } else {
                    Class c = field.getField().getType();
                    code.append(getFieldRequired(field.isRequired())).append(" ").append(c.getSimpleName()).append(" ")
                            .append(field.getField().getName()).append("=").append(field.getOrder()).append(";\n");
                    if (!cachedTypes.contains(c)) {
                        cachedTypes.add(c);
                        if(needImport(c))
                        	subTypes.add(c);
                    }
                }
            } else {
                String type = field.getFieldType().getType().toLowerCase();
                
                if (field.getFieldType() == FieldType.ENUM) {
                    // if enum type
                    Class c = field.getField().getType();
                    if (Enum.class.isAssignableFrom(c)) {
                        type = c.getSimpleName();
                        if (!cachedEnumTypes.contains(c)) {
                            cachedEnumTypes.add(c);
                            enumTypes.add(c);
                        }
                    }
                }

                code.append(getFieldRequired(field.isRequired())).append(" ")
                        .append(type).append(" ")
                        .append(field.getField().getName()).append("=").append(field.getOrder()).append(";\n");
            }

        }

        code.append("}\n");

        for (Class<Enum> subType : enumTypes) {
            generateEnumIDL(code, subType);
        }

//        if (subTypes.isEmpty()) {
//            return;
//        }
        if(!subTypes.isEmpty()) {
        	for (Class<?> subType : subTypes) {
        		nestedMsg.add(subType.getName());
        		code.append(generateIDL(subType, context, cachedTypes, cachedEnumTypes, nestedMsg));
        	}
        }
//        if(!subTypes.isEmpty()) {
//        	StringBuilder imports = new StringBuilder();
//        	for (Class<?> subType : subTypes) {
//        		imports.append("import \"").append(subType.getName().replaceAll("\\.", "/")).append(".proto\";\n");
//        	}
//        	return new StringBuilder().append(imports).append(code).toString();
//        }
        return code.toString();
    }
    
    private static boolean needImport(Class<?> cls) {
    	String clsName = cls.getName();
    	if(clsName.startsWith("java.") || clsName.startsWith("com.google"))
    		return false;
    	return true;
    }

    private static void generateEnumIDL(StringBuilder code, Class<Enum> cls) {
        code.append("enum ").append(cls.getSimpleName()).append(" {  \n");

        Field[] fields = cls.getFields();
        for (Field field : fields) {
            String name = field.getName();
            code.append(name).append("=");
            Enum value = Enum.valueOf(cls, name);
            if (value instanceof EnumReadable) {
                code.append(((EnumReadable) value).value());
            } else {
                code.append(value.ordinal());
            }
            code.append(";\n");
        }

        code.append("}\n ");
    }

    /**
     * @param protobuf
     * @return
     */
    private static String getFieldRequired(boolean required) {
        if (required) {
            return "required";
        }

        return "optional";
    }
}
