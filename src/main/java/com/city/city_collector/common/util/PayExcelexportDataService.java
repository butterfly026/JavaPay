
package com.city.city_collector.common.util;

import java.util.List;
import java.util.Map;

public interface PayExcelexportDataService {

    public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders);
}
