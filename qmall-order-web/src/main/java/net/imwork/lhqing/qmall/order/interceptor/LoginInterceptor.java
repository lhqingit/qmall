package net.imwork.lhqing.qmall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.imwork.lhqing.qmall.cart.service.CartService;
import net.imwork.lhqing.qmall.common.pojo.QmallResult;
import net.imwork.lhqing.qmall.common.utils.CookieUtils;
import net.imwork.lhqing.qmall.common.utils.JsonUtils;
import net.imwork.lhqing.qmall.pojo.Item;
import net.imwork.lhqing.qmall.pojo.User;
import net.imwork.lhqing.qmall.sso.service.TokenService;

/**
 * 用户登录拦截器
 * 
 * @author lhq_i
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${COOKIES_KEY}")
	private String COOKIES_KEY;
	@Value("${CART_COOKIES_KEY}")
	private String CART_COOKIES_KEY;
	@Value("${SSO_URL}")
	private String SSO_URL;

	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从cookie中取token
		String token = CookieUtils.getCookieValue(request, COOKIES_KEY);
		// 判断token是否存在
		if (StringUtils.isBlank(token)) {
			// 如果token不存在，未登录状态，跳转到sso系统的登录界面，用户登录成功后，跳转到当前请求的URL
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			//拦截
			return false;
		}
		// 如果token存在，需要调用SSO系统的服务，根据token取用户信息
		QmallResult qmallResult = tokenService.getUserByToken(token);
		if (qmallResult.getStatus() != 200) {
			// 如果取不到，说明用户登录已经过期，需要登录
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURI());
			//拦截
			return false;
		}
		User user = (User) qmallResult.getData();
		// 如果取到用户信息，说明是登录状态，需要把用户信息写入request
		request.setAttribute("user", user);
		// 判断cookie中是否有购物车数据，如果有就合并服务器，
		String jsonCartList = CookieUtils.getCookieValue(request, CART_COOKIES_KEY, true);
		if (StringUtils.isNotBlank(jsonCartList)) {
			//合并购物车
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, Item.class));
		}
		// 放行
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
