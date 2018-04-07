package net.imwork.lhqing.qmall.sso.service;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;

/**
 * Token服务层接口
 * @author lhq_i
 *
 */
public interface TokenService {
	/**
	 * 根据Token取redis中的用户信息
	 * @param token
	 * @return
	 */
	QmallResult getUserByToken(String token);
}
