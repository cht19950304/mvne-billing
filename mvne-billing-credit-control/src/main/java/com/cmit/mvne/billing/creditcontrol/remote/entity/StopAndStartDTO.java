/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.remote.entity;

import lombok.Data;

/**
 * @Description 调用CRM停复机接口的参数类
 * @author jiangxm02
 */
@Data
public class StopAndStartDTO {
	
	/**
	 * @Description 需要调用停复机接口的号码
	 */
	private String billId;
	
	/**
	 * @Description 调用停复机接口的来源，这里是信控侧的调用，默认值为“7”
	 */
	private String fromType;
	
	/**
	 * @Description 表示需要调用停机还是复机，停机为“1004”，复机为“1006”
	 */
	private String busiType;
	
	/**
	 * @Description 表示号码对应的基础运营商
	 */
	private String basicOrgId;
	
	/**
	 * @Description 表示号码对应的虚拟运营商
	 */
	private String mvnoId;
}
