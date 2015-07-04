package com.yuandu.erp.common.persistence.interceptor;

import java.io.Serializable;
import java.util.Properties;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;

import com.yuandu.erp.common.config.Global;
import com.yuandu.erp.common.persistence.FlexPage;
import com.yuandu.erp.common.persistence.dialect.Dialect;
import com.yuandu.erp.common.persistence.dialect.db.MySQLDialect;
import com.yuandu.erp.common.utils.Reflections;

/**
 * Mybatis分页拦截器基类
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {
	
	private static final long serialVersionUID = 1L;

    protected static final String FLEXPAGE = "flexpage";
    
    protected static final String DELEGATE = "delegate";

    protected static final String MAPPED_STATEMENT = "mappedStatement";

    protected Log log = LogFactory.getLog(this.getClass());

    protected Dialect DIALECT;

    /**
     * 对参数进行转换和检查
     * @param parameterObject 参数对象
     * @param page            分页对象
     * @return 分页对象
     * @throws NoSuchFieldException 无法找到参数
     */
    @SuppressWarnings("unchecked")
	protected static FlexPage<Object> convertParameter(Object parameterObject, FlexPage<Object> page) {
    	try{
            if (parameterObject instanceof FlexPage) {
                return (FlexPage<Object>) parameterObject;
            } else {
                return (FlexPage<Object>)Reflections.getFieldValue(parameterObject, FLEXPAGE);
            }
    	}catch (Exception e) {
			return null;
		}
    }

    /**
     * 设置属性，支持自定义方言类和制定数据库的方式
     * <code>dialectClass</code>,自定义方言类。可以不配置这项
     * <ode>dbms</ode> 数据库类型，插件支持的数据库
     * <code>sqlPattern</code> 需要拦截的SQL ID
     * @param p 属性
     */
    protected void initProperties(Properties p) {
    	Dialect dialect = null;
        String dbType = Global.getConfig("jdbc.type");
        if("mysql".equals(dbType)){
        	dialect = new MySQLDialect();
        }
        if (dialect == null) {
            throw new RuntimeException("mybatis dialect error.");
        }
        DIALECT = dialect;
    }
}
