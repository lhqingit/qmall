package net.imwork.lhqing.qmall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.common.jedis.JedisClient;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.mapper.UserMapper;
import net.imwork.lhqing.qmall.pojo.User;
import net.imwork.lhqing.qmall.pojo.UserExample;
import net.imwork.lhqing.qmall.pojo.UserExample.Criteria;
import net.imwork.lhqing.qmall.sso.service.LoginService;

/**
 * 用户登录服务层实现类
 * @author lhq_i
 *
 */
@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private UserMapper userMapper;
	@Autowired 
	private JedisClient JedisClient;
	
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	
	@Override
	public QmallResult userLogin(String username, String md5Password) {
		//判断用户和密码是否正确
		UserExample userExample = new UserExample();
		Criteria criteria = userExample.createCriteria();
		criteria.andUsernameEqualTo(username).andPasswordEqualTo(md5Password);
		List<User> userList = userMapper.selectByExample(userExample);
		if(userList == null || userList.size() != 1){
			//如果不正确，返回登录失败
			return QmallResult.build(400, "用户名或密码错误");
		}
		//如果正确生成token
		String token = "SESSION:"+UUID.randomUUID();
		//把用户信息写入redis，Key:token value:用户信息
		User user = userList.get(0);
		user.setPassword(null);//不返回密码信息
		JedisClient.set(token, JsonUtils.objectToJson(user));
		//设置Session过期时间
		JedisClient.expire(token, SESSION_EXPIRE);
		//把token返回
		return QmallResult.ok(token);
	}
	
}
