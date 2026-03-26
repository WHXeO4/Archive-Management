package main.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义了用户的基本属性
 */
public class User implements Serializable {
    /**
     * 序号
     */
    private Integer id;

	/**
	 * 用户名
	 */
	private String name;
	
	/**
	 * 用户密码
	 */
	private String password;
	
	/**
	 * 用户角色
	 */
	private String role;

    public User() {
    }

    public User(Integer id, String password) {
        this.id = id;
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
	
	/**
	 * 构造方法
	 * @param name 用户名
	 * @param password 用户密码
	 * @param role 用户角色
	 */
	public User(String name,String password,String role){
		this.name=name;
		this.password=password;
		this.role=role;                
	}

    public User(Integer id, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     * @return 用户名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户名
     * @param name 用户名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取用户密码
     * @return 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户角色
     * @return 用户角色
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置用户角色
     * @param role 用户角色
     */
    public void setRole(String role) {
        this.role = role;
    }
}