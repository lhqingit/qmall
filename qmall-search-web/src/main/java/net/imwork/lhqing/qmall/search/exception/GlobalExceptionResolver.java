package net.imwork.lhqing.qmall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器  
 * @author lhq_i
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception e) {
		//打印控制台
		e.printStackTrace();
		//写日志
		logger.debug("我是一条Debug日志");
		logger.info("我是一条info日志消息。。。。");
		logger.error("系统发生异常", e);
		//发邮件，使用jmail工具包
		//发短信，使用第三方的WebService
		//显示错误信息
		return new ModelAndView("/error/exception");
	}

}
