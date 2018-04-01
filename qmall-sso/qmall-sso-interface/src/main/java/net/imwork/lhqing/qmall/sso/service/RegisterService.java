package net.imwork.lhqing.qmall.sso.service;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.pojo.User;

public interface RegisterService {
	
	/**
	 * Q:核对数据唯一性
	 * @param param 
	 * @param type 1:用户名 2:手机号 3:邮箱
	 * @return
	 */
	QmallResult checkData(String param, int type);
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	QmallResult register(User user);
}
