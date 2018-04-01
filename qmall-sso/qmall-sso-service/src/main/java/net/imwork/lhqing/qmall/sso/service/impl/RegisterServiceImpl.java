package net.imwork.lhqing.qmall.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.mapper.UserMapper;
import net.imwork.lhqing.qmall.pojo.User;
import net.imwork.lhqing.qmall.pojo.UserExample;
import net.imwork.lhqing.qmall.pojo.UserExample.Criteria;
import net.imwork.lhqing.qmall.sso.service.RegisterService;

/**
 * 用户注册处理Service
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
		//判断结果中是否包含数据
		if (userList != null && userList.size() > 0) {
			//如果有数据返回false
			return QmallResult.ok(false);
		} else {
			//如果没有数据返回true
			return QmallResult.ok(true);
		}

	}

}
