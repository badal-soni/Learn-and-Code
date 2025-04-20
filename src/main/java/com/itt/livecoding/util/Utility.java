package com.itt.livecoding.util;

import com.itt.livecoding.constant.MemberShipLevel;
import com.itt.livecoding.dto.response.OrderResponse;

import java.time.LocalDateTime;

public final class Utility {

    public static double getDiscountRate(MemberShipLevel memberShipLevel) {
        if (memberShipLevel.equals(MemberShipLevel.GOLD)) {
            return 0.1;
        } else if (memberShipLevel.equals(MemberShipLevel.PLATINUM)) {
            return 0.15;
        }
        return 0.2;
    }

    public static boolean isDateInRange(OrderResponse order, LocalDateTime startDate, LocalDateTime endDate) {
        return order.createdAt.isAfter(startDate) && order.createdAt.isBefore(endDate);
    }

}
