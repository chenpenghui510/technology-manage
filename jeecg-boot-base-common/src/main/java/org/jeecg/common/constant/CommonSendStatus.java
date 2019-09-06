package org.jeecg.common.constant;

/**
 * 	系统通告 - 发布状态
 * @Author LeeShaoQing
 *
 */
public interface CommonSendStatus {
	
	public static final String UNPUBLISHED_STATUS_0 = "0";	//未发布
	
	public static final String PUBLISHED_STATUS_1 = "1";		//已发布
	
	public static final String REVOKE_STATUS_2 = "2";			//撤销

	public static final String EXAMINE_STATUS_3 ="3";    //待审核

	public static final String REJECT_STATUS_4 ="4";     //驳回

   public static final String PUBLISHED_STATUS_5 ="5";//发送中

    public static final String PUBLISHED_STATUS_6 ="6";//部分失败

}
