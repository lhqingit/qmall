package net.imwork.lhqing.qmall.common.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * Q:返回结果集
 * @author lhq_i
 *
 */
public class EasyUIDataGridResult implements Serializable{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	//总页数
	private long total;
	//返回数据,返回的数据类型不确定，因此List不加泛型
	private List<?> rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
}
