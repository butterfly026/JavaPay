
package com.city.city_collector.channel.iface;

import com.city.city_collector.channel.bean.CashInfo;

/**
 * @author nb
 * @Description:
 */
public interface PayOrderDataHandler {

    CashInfo dealPayOrderData(String datas);
}
