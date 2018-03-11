package net.imwork.lhqing.qmall.item.pojo;

import net.imwork.lhqing.qmall.pojo.Item;

public class ItemData extends Item {

	public ItemData(Item item){
		this.setId(item.getId());
		this.setTitle(item.getTitle());
		this.setSellPoint(item.getSellPoint());
		this.setPrice(item.getPrice());
		this.setNum(item.getNum());
		this.setBarcode(item.getBarcode());
		this.setImage(item.getImage());
		this.setCid(item.getCid());
		this.setStatus(item.getStatus());
		this.setCreated(item.getCreated());
		this.setUpdated(item.getUpdated());
	}
	
	
	//Q:根据父类的image字段的值解析为图片URL数组
	public String[] getImages() {
		String imageStr = this.getImage();
		if (imageStr != null && !"".equals(imageStr)) {
			String[] strings = imageStr.split(",");
			return strings;
		}
		return null;
	}
}
