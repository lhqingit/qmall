package net.imwork.lhqing.qmall.sso.service;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;

/**
 * 用户登录服务层接口
 * @author lhq_i
 *
 */
public interface LoginService {
	public QmallResult userLogin(String username,String md5Password);
}
