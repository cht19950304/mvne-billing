
/**  
* All rights Reserved, Designed By zengxf  
* @Project mvne-customer 
* @File Generator.java
* @Package com.cmsz.mvne.customer.utils
* @date 2017年10月13日 
*/
    
package com.cmit.mvne.billing.user.analysis.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: Generator
* @Description:
* @author zengxf
* @date 2017年10月13日
* @version V1.0  
*/

public class Generator {
	
	private static Logger logger = LoggerFactory.getLogger(Generator.class);
	
	public static void main(String[] args) throws Exception {
		//MBG 执行过程中的警告信息
		List<String> warnings = new ArrayList<>();
		//当生成的代码重复时，覆盖原代码
		boolean overwrite = true;
		//读取我们的 MBG 配置文件
		InputStream is = Generator.class.getResourceAsStream("/generator/generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(is);
		is.close();
		
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		//创建 MBG
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		//执行生成代码
		myBatisGenerator.generate(null);
		//输出警告信息
		for(String warning : warnings){
			logger.error(warning);
		}
	}

}
