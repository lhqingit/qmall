package net.imwork.lhqing.qmall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import net.imwork.lhqing.qmall.common.jedis.JedisClient;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.pojo.User;
import net.imwork.lhqing.qmall.sso.service.TokenService;

/**
 * Token服务层实现类
 * @author lhq_i
 *
 */
@Service
public class TokenServiceImpl implements TokenService{
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;

	@Override
	public QmallResult getUserByToken(String token) {
		//根据token到redis中取用户信息
		String userJson = jedisClient.get(token);
		if(StringUtils.isBlank(userJson)){
			//取不到用户信息，登录已经过期，返回登录过期
			return QmallResult.build(210, "token不存在或已过期");
		}
		//取到用户信息
		User user = JsonUtils.jsonToPojo(userJson, User.class);
		//更新token的过期时间
		jedisClient.expire(token, SESSION_EXPIRE);
		//返回结果，QmallResult中包含User对象
		return QmallResult.ok(user);
	}

}
