package main.util;

import main.config.JDBCConfiguration;
import main.entity.Result;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class JDBCUtil {
    /**
     * 数据库的插入方法
     * @param items 插入的数据项
     * @param table 目标表格名称
     */
    public static Result insert(Map<String, Object> items, String table) {
        if (items == null || items.isEmpty()) {
            return Result.error("插入的数据不能为空");
        }

        StringJoiner columns = new StringJoiner(", ", "(", ")");
        StringJoiner placeholders = new StringJoiner(", ", "(", ")");
        List<Object> params = new ArrayList<>();

        for (Map.Entry<String, Object> entry : items.entrySet()) {
            columns.add(entry.getKey());
            placeholders.add("?");
            params.add(entry.getValue());
        }

        String sql = "INSERT INTO " + table + " " + columns + " VALUES " + placeholders;
        System.out.println("Executing SQL: " + sql);

        try (
                Connection conn = DriverManager.getConnection(JDBCConfiguration.DB_URL, JDBCConfiguration.USER, JDBCConfiguration.PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            int affectedRows = pstmt.executeUpdate();

            return Result.success("添加成功");

        } catch (SQLException e) {
            e.printStackTrace();
            return Result.error(e.getMessage()==null ? "操作失败":e.getMessage());
        }
    }

    /**
     * 数据库的搜索方法
     * @param clazz        传入的对象的类
     * @param table        需要查询的数据表
     * @param whereClauses 搜索条件
     * @param likeClauses 支持正则表达式
     * @return 查询到的数据(组)
     * 利用？占位符进行占位，用于防止恶意的SQL注入
     */
    public static <T> Result<List<T>> select(Class<T> clazz, String table, Map<String, Object> whereClauses, Map<String, String> likeClauses) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ").append(table);
        List<Object> params = new ArrayList<>();

        if ((whereClauses != null && !whereClauses.isEmpty())
                || (likeClauses != null && !likeClauses.isEmpty())) {

            sqlBuilder.append(" WHERE ");
            StringJoiner whereJoiner = new StringJoiner(" AND ");
            if (whereClauses != null && !whereClauses.isEmpty()) {
                for (Map.Entry<String, Object> entry : whereClauses.entrySet()) {
                    whereJoiner.add(entry.getKey() + " = ?");
                    params.add(entry.getValue());
                }
                sqlBuilder.append(whereJoiner);
            }
            if (likeClauses != null && !likeClauses.isEmpty()) {
                for (Map.Entry<String, String> entry : likeClauses.entrySet()) {
                    whereJoiner.add(entry.getKey() + " LIKE " + entry.getValue());
                }
                sqlBuilder.append(whereJoiner);
            }
        }

        String sql = sqlBuilder.toString();
        System.out.println("Executing SQL: " + sql);

        List<T> resultList = new ArrayList<>();

        try (
                Connection conn = DriverManager.getConnection(JDBCConfiguration.DB_URL, JDBCConfiguration.USER, JDBCConfiguration.PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                Set<String> columnNames = new HashSet<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    columnNames.add(metaData.getColumnLabel(i).toLowerCase());
                }

                while (rs.next()) {
                    T instance = clazz.getDeclaredConstructor().newInstance();
                    for (Field field : clazz.getDeclaredFields()) {
                        String fieldName = field.getName();
                        if (columnNames.contains(fieldName.toLowerCase())) {
                            Object value = rs.getObject(fieldName);
                            field.setAccessible(true);
                            field.set(instance, value);
                        }
                    }
                    resultList.add(instance);
                }
            }
            return Result.success("查询成功", resultList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage()==null ? "操作失败":e.getMessage());
        }
    }

    /**
     * 数据库的删除方法
     * @param table 需要删除数据的数据表名
     * @param whereClauses 删除条件
     * */
    public static Result delete(String table, Map<String, Object> whereClauses) {
        if (whereClauses == null || whereClauses.isEmpty()) {
            return Result.error("删除条件不能为空");
        }

        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ").append(table).append(" WHERE ");
        List<Object> params = new ArrayList<>();
        StringJoiner whereJoiner = new StringJoiner(" AND ");

        for (Map.Entry<String, Object> entry : whereClauses.entrySet()) {
            whereJoiner.add(entry.getKey() + " = ?");
            params.add(entry.getValue());
        }
        sqlBuilder.append(whereJoiner);

        String sql = sqlBuilder.toString();
        System.out.println("Executing SQL: " + sql);

        try (
                Connection conn = DriverManager.getConnection(JDBCConfiguration.DB_URL, JDBCConfiguration.USER, JDBCConfiguration.PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            int affectedRows = pstmt.executeUpdate();

            return Result.success("删除成功");

        } catch (SQLException e) {
            e.printStackTrace();
            return Result.error(e.getMessage()==null ? "操作失败":e.getMessage());
        }
    }

    /**
     * 数据库的更新方法
     * @param setItems 需更改的数据项
     * @param whereClauses 更改项的条件
     * @param table 需更改的数据表名称
     * */
    public static Result update(Map<String, Object> setItems, Map<String, Object> whereClauses, String table) {
        if (setItems == null || setItems.isEmpty()) {
            return Result.error("需要更新的数据不能为空");
        }
        if (whereClauses == null || whereClauses.isEmpty()) {
            return Result.error("更新条件不能为空");
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(table).append(" SET ");
        StringJoiner setJoiner = new StringJoiner(", ");
        List<Object> params = new ArrayList<>();

        for (Map.Entry<String, Object> entry : setItems.entrySet()) {
            setJoiner.add(entry.getKey() + " = ?");
            params.add(entry.getValue());
        }
        sqlBuilder.append(setJoiner);

        sqlBuilder.append(" WHERE ");
        StringJoiner whereJoiner = new StringJoiner(" AND ");
        for (Map.Entry<String, Object> entry : whereClauses.entrySet()) {
            whereJoiner.add(entry.getKey() + " = ?");
            params.add(entry.getValue());
        }
        sqlBuilder.append(whereJoiner);

        String sql = sqlBuilder.toString();
        System.out.println("Executing SQL: " + sql);

        try (
                Connection conn = DriverManager.getConnection(JDBCConfiguration.DB_URL, JDBCConfiguration.USER, JDBCConfiguration.PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            int affectedRows = pstmt.executeUpdate();

            return Result.success("更新成功");

        } catch (SQLException e) {
            e.printStackTrace();
            return Result.error(e.getMessage() == null ? "操作失败" : e.getMessage());
        }
    }
}
