package com.mycompany.group234.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
	public static ObjectMapper jsonObjectMapper = new ObjectMapper(); // can reuse, share globally
	public static ObjectMapper jsonObjectMapperPrettyPrinter = new ObjectMapper(); // can reuse, share globally
	static {
		jsonObjectMapperPrettyPrinter.enable(SerializationFeature.INDENT_OUTPUT);
		jsonObjectMapper = jsonObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		jsonObjectMapper = jsonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2Map(String json)
			throws JsonParseException, JsonMappingException, IOException {
		if (null == json)
			return null;
		return jsonObjectMapper.readValue(json, Map.class);
	}

	public static <K, V> Map<K, V> json2MapOfType(String json, Class<K> keyClass, Class<V> valueClass)
			throws JsonParseException, JsonMappingException, IOException {
		if (null == json)
			return null;
		return jsonObjectMapper.readValue(json, new TypeReference<Map<K, V>>() {
		});
	}

	public static Object json2Object(String json) throws JsonParseException, JsonMappingException, IOException {
		return json2Object(json, Object.class);
	}

	public static <T> T json2Object(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		if (null == json)
			return null;
		return jsonObjectMapper.readValue(json, clazz);
	}

	public static <T> List<T> json2List(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		if (null == json)
			return null;
		return jsonObjectMapper.readValue(json, new TypeReference<List<T>>() {
		});
	}

	@SuppressWarnings("rawtypes")
	public static <T> List<T> json2ListOfClass(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		if (null == json)
			return null;
		List<Map> maps = jsonObjectMapper.readValue(json, new TypeReference<List<Map>>() {
		});
		List<T> objs = new ArrayList<T>();
		for (Map m : maps)
			objs.add(map2Object(m, clazz));
		return objs;
	}

	public static <T> T json2Type(String json, TypeReference<T> type)
			throws JsonParseException, JsonMappingException, IOException {
		if (null == json)
			return null;
		return jsonObjectMapper.readValue(json, type);
	}

	public static String object2JsonString(Object obj) throws JsonProcessingException {
		if (null == obj)
			return null;
		return jsonObjectMapper.writeValueAsString(obj);
	}

	@SuppressWarnings("unchecked")
	public static Map<Object, Object> object2Map(Object srcObj) {
		if (null == srcObj)
			return null;
		Map<Object, Object> map = jsonObjectMapper.convertValue(srcObj, Map.class);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static <T> T map2Object(Map srcMap, Class<T> clazz) {
		if (null == srcMap)
			return null;
		T obj = jsonObjectMapper.convertValue(srcMap, clazz);
		return obj;
	}

	public static String prettyPrint(Object object) throws JsonProcessingException {
		if (null == object)
			return null;
		return jsonObjectMapperPrettyPrinter.writeValueAsString(object);
	}
}
