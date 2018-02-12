package net.imwork.lhqing.qmall.common.pojo;

import java.io.Serializable;

/**
 * Q:商品分类信息
 * @author lhq_i
 *
 */
public class EasyUITreeNode implements Serializable{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	//Q:节点ID，对加载远程数据很重要。
	private long id;
	//Q:显示节点文本。
	private String text;
	//Q:state：节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点。
	private String state;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
