package net.imwork.lhqing.qmall.mapper;

import java.util.List;
import net.imwork.lhqing.qmall.pojo.OrderItem;
import net.imwork.lhqing.qmall.pojo.OrderItemExample;
import org.apache.ibatis.annotations.Param;

public interface OrderItemMapper {
    int countByExample(OrderItemExample example);

    int deleteByExample(OrderItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> selectByExample(OrderItemExample example);

    OrderItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OrderItem record, @Param("example") OrderItemExample example);

    int updateByExample(@Param("record") OrderItem record, @Param("example") OrderItemExample example);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}