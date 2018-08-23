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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {
	private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);
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
    
    /**
     * Bean格式字符串转换为XML，驼峰转下划线
     * @author mahongliang
     * @date  2018年8月20日 下午7:56:37
     * @Description 
     * @param c
     * @return
     * @throws Exception
     */
    public static <T> String bean2Xml(Object o) throws Exception{
        Map<String, String> map = bean2MapUnderline2Hump(o);
		return mapToXml(map);
    	
    }
    
    /**
     * XML格式字符串转换为Bean，下划线转驼峰
     * @author mahongliang
     * @date  2018年8月20日 下午6:32:24
     * @Description 
     * @param strXML
     * @param c
     * @return
     * @throws Exception
     */
    public static <T> T xml2Bean(String strXML,Class<T> c) throws Exception{
    	if(strXML == null) {
    		return null;
    	}
    	Map<String, String> map = xmlToMap(strXML);
    	
		return map2Bean(map, c);
    }
    
    /**
     * Map格式字符串转换为Bean，下划线转驼峰
     * @author mahongliang
     * @date  2018年8月23日 下午2:05:59
     * @Description 
     * @param map
     * @param c
     * @return
     * @throws Exception
     */
    public static <T> T map2Bean(Map<String, String> map,Class<T> c) throws Exception{
    	T bean = c.newInstance();
    	for (Map.Entry<String, String> entry : map.entrySet()) {
    		String fieldName = entry.getKey();
    		//下划线转驼峰
    		fieldName = underlineToHump(fieldName);
    		String fieldValue = entry.getValue();
    		Field field = getField(c, fieldName);
    		if(field == null) {
    			continue;
    		}
    		Class<?> fieldTypeClass = field.getType();
    		//类型转换
    		Object value = convertValType(fieldValue, fieldTypeClass);
    		Method method = getSetMethod(c, fieldName);
    		method.invoke(bean, new Object[] { value });
    	}
		return bean;
    }
    
    /**
     * bean转换为Map格式的字符串，驼峰转下划线
     * @author mahongliang
     * @date  2018年8月20日 下午8:26:15
     * @Description 
     * @param o
     * @return
     * @throws Exception
     */
    public static Map<String, String> bean2MapUnderline2Hump(Object o) throws Exception {
    	Class<?> c = o.getClass();
    	Class<?> clazzTemp = c;
        Map<String, String> map = new HashMap<>();
      //当父类为Object的时候说明到达了最上层的父类.
        while (clazzTemp != null && !clazzTemp.getName().equalsIgnoreCase(Object.class.getName())) {
            Field[] fields = clazzTemp.getDeclaredFields();
            for (Field field : fields) {
            	field.setAccessible(true);
            	boolean isStatic = Modifier.isStatic(field.getModifiers());
            	boolean isFinal = Modifier.isFinal(field.getModifiers());
                if(!isStatic && !isFinal) {
                	if(field.get(o) != null) {
                		String value = convertString(field.get(o));
                    	//驼峰转下划线
                    	map.put(humpToUnderline(field.getName()), value);
                	}
                }
            }
            //得到父类,然后赋给自己
            clazzTemp = clazzTemp.getSuperclass();
        }
        
        return map;
    }
    
    /**
     * bean转换为Map格式的字符串
     * @author mahongliang
     * @date  2018年8月21日 上午1:48:58
     * @Description 
     * @param o
     * @return
     * @throws Exception
     */
    public static Map<String, String> bean2Map(Object o) throws Exception {
    	Class<?> c = o.getClass();
    	Class<?> clazzTemp = c;
        Map<String, String> map = new HashMap<>();
      //当父类为Object的时候说明到达了最上层的父类.
        while (clazzTemp != null && !clazzTemp.getName().equalsIgnoreCase(Object.class.getName())) {
            Field[] fields = clazzTemp.getDeclaredFields();
            for (Field field : fields) {
            	field.setAccessible(true);
            	boolean isStatic = Modifier.isStatic(field.getModifiers());
            	boolean isFinal = Modifier.isFinal(field.getModifiers());
                if(!isStatic && !isFinal) {
                	if(field.get(o) != null) {
                		String value = convertString(field.get(o));
                    	//驼峰转下划线
                    	map.put(field.getName(), value);
                	}
                }
            }
            //得到父类,然后赋给自己
            clazzTemp = clazzTemp.getSuperclass();
        }
        
        return map;
    }
    
    /**
     * XML格式字符串转换为Map，下划线转驼峰
     * @author mahongliang
     * @date  2018年8月20日 下午3:48:31
     * @Description 
     * @param strXML
     * @return
     * @throws Exception
     */
    public static Map<String, String> xml2MapUnderline2Hump(String strXML) throws Exception {
    	Map<String, String> map = xmlToMap(strXML);
    	Map<String, String> newMap = new HashMap<>();
    	for (Map.Entry<String, String> entry : map.entrySet()) {
    		String key = entry.getKey();
    		//下划线转驼峰
    		key = underlineToHump(key);
    		newMap.put(key, entry.getValue());
    	}
    	return newMap;
    }
    
    /**
     * 将Map转换为XML格式的字符串，驼峰转下划线
     * @author mahongliang
     * @date  2018年8月20日 下午3:54:22
     * @Description 
     * @param map
     * @return
     * @throws Exception
     */
    public static String map2XmlHump2Underline(Map<String, String> map) throws Exception {
    	Map<String, String> newMap = new HashMap<>();
    	for (Map.Entry<String, String> entry : map.entrySet()) {
    		String key = entry.getKey();
    		//下划线转驼峰
    		key = humpToUnderline(key);
    		newMap.put(key, entry.getValue());
    	}
    	return mapToXml(newMap);
    }

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
        	logger.error("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }

    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        Document document = newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }
    
    /**
     * 对象转字符串
     * @author mahongliang
     * @date  2018年8月20日 下午8:10:06
     * @Description 
     * @param value
     * @return
     */
    private static String convertString(Object value) {
    	if(value == null) {
    		return null;
    	}
        String retVal = null;
        if(value instanceof Number){
            retVal = String.valueOf(value);
        } else if(value instanceof BigDecimal) {
            retVal = ((BigDecimal) value).toString();
        } else if(value instanceof Date) {
            retVal = DateUtil.format((Date) value, WX_DATE_FORMAT);
        } else{
        	retVal = String.valueOf(value);
        }
        return retVal;
    }

    /**
     * 
     * @author mahongliang
     * @date  2018年8月20日 下午8:02:58
     * @Description 
     * @param value
     * @param fieldTypeClass
     * @return
     * @throws NumberFormatException
     * @throws ParseException
     */
    private static Object convertValType(String value, Class<?> fieldTypeClass)
            throws NumberFormatException, ParseException {
        Object retVal = value;
        if(Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value);
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value);
        } else if(Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value);
        } else if(Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value);
        } else if(Date.class.getName().equals(fieldTypeClass.getName())) {
            retVal = DateUtil.toDate(value, WX_DATE_FORMAT);
        } else if(BigDecimal.class.getName().equals(fieldTypeClass.getName())) {
            retVal = new BigDecimal(value);
        }
        return retVal;
    }
    
    /**
     * 向父类递归回去属性
     * @author mahongliang
     * @date  2018年8月20日 下午8:20:50
     * @Description 
     * @param c
     * @param fieldName
     * @return
     */
    private static <T> Field getField(Class<T> c, String fieldName) {
    	Field field = null;
		try {
			field = c.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {}
    	//获取父类属性
    	if(field == null && c.getSuperclass() != null && !c.getSuperclass().getName().equalsIgnoreCase(Object.class.getName())) {
    		field = getField(c.getSuperclass(), fieldName);
    	}
    	return field;
    }
    
    /**
     * java反射bean的set方法（递归）
     * @param <T>
     *
     * @param objectClass
     * @param fieldName
     * @return
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws NoSuchMethodException 
     */
    private static <T> Method getSetMethod(final Class<T> c, final String fieldName) {
    	Method method = null;
    	try {
    		Field field = c.getDeclaredField(fieldName);
            Class<?> parameterType = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            method = c.getMethod(sb.toString(), parameterType);
		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException e) {}
        //找父类set方法
        if(method == null && c.getSuperclass() != null && !c.getSuperclass().getName().equalsIgnoreCase(Object.class.getName())) {
            method = getSetMethod(c.getSuperclass(), fieldName);
        }
        return method;
    }
    
    /**
     * 驼峰命名方式转换为下划线方式
     * @param field 属性名
     * @return 转换完成属性名
     */
    public static String humpToUnderline(String fieldName){
        StringBuilder destString = new StringBuilder();
        if(StringUtils.isBlank(fieldName)){
            return fieldName;
        }
        char[] fieldChars = fieldName.toCharArray();
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
            return field;
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
    
    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
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

    public static Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }

}
