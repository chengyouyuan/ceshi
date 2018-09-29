package com.winhxd.b2c.pay.weixin.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.winhxd.b2c.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanAndXmlUtil {

    /**
     * 大写字母'A'ASCII值
     */
    private static final int A = 65;
    /**
     * 大写字母'Z'ASCII值
     */
    private static final int Z = 90;
    /**
     * 下划线ASCII值
     */
    private static final int UNDERLINE = 95;
    /**
     * 大小写字母ASCII偏移量
     */
    private static final int OFFSET = 32;
    /**
     * 微信接口日期格式
     */
    private static final String WX_DATE_FORMAT = "yyyyMMddHHmmss";

    public static String bean2Xml(Object obj) throws Exception{
        Document document = newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);

        Map<String,String> map = new HashMap<String, String>();
        Class<?> cls = obj.getClass();
        List<Field> fields = new ArrayList<>() ;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (cls != null && !"java.lang.object".equals(cls.getName().toLowerCase())) {
        	fields.addAll(Arrays.asList(cls .getDeclaredFields()));
        	//得到父类,然后赋给自己
        	cls = cls.getSuperclass(); 
        }
        for(int i=0; i<fields.size(); i++){
            Field f = fields.get(i);

            f.setAccessible(true);
            System.out.println("属性名:" + f.getName() + " 属性值:" + f.get(obj));
            if (f.get(obj) != null) {
                String value =String.valueOf(f.get(obj));
                value = value.trim();
                String key = underscoreName(f.getName());
                Element filed = document.createElement(key);
                filed.appendChild(document.createTextNode(value));
                root.appendChild(filed);

                map.put(f.getName(), String.valueOf(f.get(obj)).toString());
            }

        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();

        return output;
    }


    private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);

        return documentBuilderFactory.newDocumentBuilder();
    }

    private static Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }

    /**
     * 将大写转化为下划线小写，例如orderNo >>>> order_no
     * @param name
     * @return
     */
    private static String underscoreName(String name){
        StringBuilder result = new StringBuilder();
        if ((name != null) && (name.length() > 0)) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase())) {
                    result.append("_");
                    result.append(s.toLowerCase());
                }
                else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }

    /**
     * 将下划线_s%变成S%,例如 order_no >>>>  orderNo
     * @param name
     * @return
     */
    private static String underscoreName1(String name){
        boolean flag = false;
        StringBuilder result = new StringBuilder();
        if ((name != null) && (name.length() > 0)) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                if (("_").equals(s)) {
                    flag = true;
                }
                else {
                    if(flag){
                        result.append(s.toUpperCase());
                        flag = false;
                    }else {
                        result.append(s);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 实体对象转成sortedMap
     * @param obj 实体对象
     * @return
     */
    public static Map<String, String> beanToSortedMap(Object obj) {
        SortedMap<String,String> sortedMap = new TreeMap<String,String>();
        if (obj == null) {
            return sortedMap;
        }
        Class clazz = obj.getClass();
        List<Field> fields = new ArrayList<>() ;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (clazz != null && !"java.lang.object".equals(clazz.getName().toLowerCase())) {
        	fields.addAll(Arrays.asList(clazz .getDeclaredFields()));
        	//得到父类,然后赋给自己
        	clazz = clazz.getSuperclass(); 
        }
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if(null == field.get(obj)){
                    continue;
                }
                sortedMap.put(humpToUnderline(field.getName()), String.valueOf(field.get(obj)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sortedMap;
    }
    
    /**
     * 实体对象转成Map
     * @author mahongliang
     * @date  2018年8月18日 下午8:17:10
     * @Description 
     * @param obj
     * @return
     */
    public static Map<String, String> beanToMap(Object obj) {
        Map<String,String> map = new HashMap<String,String>();
        if (obj == null) {
            return map;
        }
        Class<? extends Object> clazz = obj.getClass();
        List<Field> fields = new ArrayList<>() ;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (clazz != null && !"java.lang.object".equals(clazz.getName().toLowerCase())) {
        	fields.addAll(Arrays.asList(clazz .getDeclaredFields()));
        	//得到父类,然后赋给自己
        	clazz = clazz.getSuperclass(); 
        }
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if(null == field.get(obj)){
                    continue;
                }
                if(Date.class.getName().equals(field.getType().getName())) {
                    map.put(humpToUnderline(field.getName()), DateUtil.format((Date)field.get(obj), WX_DATE_FORMAT));
                } else{
                    map.put(humpToUnderline(field.getName()), String.valueOf(field.get(obj)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 驼峰命名方式转换为下划线方式
     * @param field 属性名
     * @return 转换完成属性名
     */
    public static String humpToUnderline(String field){
        StringBuilder destString = new StringBuilder();
        if(StringUtils.isBlank(field)){
            return destString.toString();
        }
        char[] fieldChars = field.toCharArray();
        for (char fieldChar : fieldChars) {
            if (fieldChar >= A && fieldChar <= Z) {
                destString.append("_").append((char) (fieldChar + OFFSET));
            } else {
                destString.append(fieldChar);
            }
        }
        return destString.toString();
    }

    /**
     * 下划线方式转换为驼峰命名方式
     * @param field 属性名
     * @return 转换完成属性名
     */
    public static String underlineToHump(String field){
        StringBuilder destString = new StringBuilder();
        if(StringUtils.isBlank(field)){
            return destString.toString();
        }
        char[] fieldChars = field.toCharArray();
        boolean upCase = false;
        for (char fieldChar : fieldChars) {
            if (upCase) {
                destString.append((char) (fieldChar - OFFSET));
                upCase = false;
                continue;
            }
            if (fieldChar == UNDERLINE) {
                upCase = true;
            } else {
                destString.append(fieldChar);
            }
        }
        return destString.toString();
    }

    public static <T> T xml2Bean(String strXML,Class<T> c) throws Exception{
        T bean = null;
        InputStream stream = null;
        try {
            bean = c.newInstance();
            DocumentBuilder documentBuilder = newDocumentBuilder();
            stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String key = underscoreName1(element.getNodeName());
                    invokeSet(bean,key,element.getTextContent());
                    //data.put(element.getNodeName(), element.getTextContent());
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
        }

        return bean;
    }

    /**
     * java反射bean的get方法
     *
     * @param objectClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Method getGetMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * java反射bean的set方法
     *
     * @param objectClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行set方法
     *
     * @param o 执行对象
     * @param fieldName 属性
     * @param value 值
     */
    private static void invokeSet(Object o, String fieldName, Object value) {

        Class tempClass = o.getClass();
        List<Field> fieldList = new ArrayList<>() ;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            Field[] fields = tempClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                boolean isStatic = Modifier.isStatic(fields[i].getModifiers());
                if(!isStatic) {
                    fieldList.add(fields[i]);
                }
            }
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        Class methodClass = o.getClass();
        Method method = getSetMethod(methodClass, fieldName);
        while (methodClass != null && method == null){
            methodClass = methodClass.getSuperclass();
            method = getSetMethod(methodClass, fieldName);
        }
        try {
            //根据反射将字段类型转译出来，并格式化value值
            Field field = getFieldOrSuper(fieldList, fieldName);
            Class fieldTypeClass = field.getType();
            value = convertValType(value, fieldTypeClass);
            method.invoke(o, new Object[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Field> listAddAllExStatic(Class c){
        List<Field> fieldList = new ArrayList<>() ;
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            boolean isStatic = Modifier.isStatic(fields[i].getModifiers());
            if(!isStatic) {
                fieldList.add(fields[i]);
            }
        }
        return fieldList;
    }

    /**
     * 递归获得父类属性
     * @param fieldList
     * @param fieldName
     * @return
     */
    private static Field getFieldOrSuper(List<Field> fieldList, String fieldName) throws NoSuchFieldException{
        for (Field f : fieldList) {
            if (fieldName.equals(f.getName())){
                return f;
            }
        }
        throw new NoSuchFieldException("找不到该字段");
    }

    /**
     * 执行get方法
     *
     * @param o 执行对象
     * @param fieldName 属性
     */
    private static Object invokeGet(Object o, String fieldName) {
        Method method = getGetMethod(o.getClass(), fieldName);
        try {
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Object类型的值，转换成bean对象属性里对应的类型值
     *
     * @param value Object对象值
     * @param fieldTypeClass 属性的类型
     * @return 转换后的值
     */
    private static Object convertValType(Object value, Class<?> fieldTypeClass)
            throws NumberFormatException, ParseException {
        Object retVal = value;
        if(Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if(Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if(Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else if(Date.class.getName().equals(fieldTypeClass.getName())) {
            DateFormat bf = new SimpleDateFormat(WX_DATE_FORMAT);
            retVal = bf.parse(value.toString());
        } else if(BigDecimal.class.getName().equals(fieldTypeClass.getName())) {
            retVal = new BigDecimal(value.toString());
        }
        return retVal;
    }

    /**
     * 将map装换为javabean对象
     * @param map
     * @param
     * @return
     */
    public static <T> T mapToBean(Map<String, String> map,Class<T> c) throws Exception{
        T bean = null;
        try {
            bean = c.newInstance();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                invokeSet(bean,underlineToHump(entry.getKey()),entry.getValue());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return bean;
    }
}
