package net.imwork.lhqing.qmall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.mapper.UserMapper;
import net.imwork.lhqing.qmall.pojo.User;
import net.imwork.lhqing.qmall.pojo.UserExample;
import net.imwork.lhqing.qmall.pojo.UserExample.Criteria;
import net.imwork.lhqing.qmall.sso.service.RegisterService;

/**
 * 用户注册处理Service
 * 
 * @author lhq_i
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public QmallResult checkData(String data, int type) {

		// 根据不同的type生成不同的查询条件
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		// 1:用户名 2:手机号 3:邮箱
		if (type == 1) {
			criteria.andUsernameEqualTo(data);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(data);
		} else if (type == 3) {
			criteria.andEmailEqualTo(data);
		} else {
			return QmallResult.ok(false);
		}

		// 执行查询
		List<User> userList = userMapper.selectByExample(example);
		// 判断结果中是否包含数据
		if (userList != null && userList.size() > 0) {
			// 如果有数据返回false
			return QmallResult.ok(false);
		} else {
			// 如果没有数据返回true
			return QmallResult.ok(true);
		}

	}

	@Override
	public QmallResult register(User user) {
		// 数据有效性校验
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
				|| StringUtils.isBlank(user.getPhone())) {
			return QmallResult.build(500, "用户数据不完整");
		}
		// 1:用户名 2:手机号 3:邮箱
		if (!(boolean) checkData(user.getUsername(), 1).getData()) {
			return QmallResult.build(500, "用户名已存在");
		}
		if (!(boolean) checkData(user.getPhone(), 2).getData()) {
			return QmallResult.build(500, "手机号已存在");
		}
		// 对密码进行MD5加密
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		// 补全pojo属性
		user.setPassword(md5Password);
		user.setCreated(new Date());
		// 把用户数据插入到数据库中
		int i = userMapper.insert(user);
		if (i == 1) {
			// 返回添加成功
			return QmallResult.ok();
		} else {
			return QmallResult.build(500, "注册失败");
		}
	}

}
