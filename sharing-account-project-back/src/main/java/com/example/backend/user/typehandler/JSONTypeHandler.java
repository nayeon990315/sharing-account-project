//package com.example.backend.user.typehandler;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.TypeReference;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//public class JSONTypeHandler extends BaseTypeHandler<List<String>> {
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
//        try {
//            ps.setString(i, objectMapper.writeValueAsString(parameter)); // List -> JSON 문자열
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        String json = rs.getString(columnName);
//        return objectMapper.readValue(json,
//                new TypeReference<List<String>>() {}); // JSON 문자열 -> List
//    }
//
//    @Override
//    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        String json = rs.getString(columnIndex);
//        return objectMapper.readValue(json, new TypeReference<List<String>>() {}); // JSON 문자열 -> List
//    }
//
//    @Override
//    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//        String json = cs.getString(columnIndex);
//        return objectMapper.readValue(json, new TypeReference<List<String>>() {}); // JSON 문자열 -> List
//    }
//}
