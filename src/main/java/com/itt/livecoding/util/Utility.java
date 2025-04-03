package com.itt.livecoding.util;

import com.itt.livecoding.constant.MemberShipLevel;

public final class Utility {

    public static double getDiscountPercentageFromCustomerMembershipLevel(MemberShipLevel memberShipLevel) {
        if (memberShipLevel.equals(MemberShipLevel.GOLD)) {
            return 0.1;
        } else if (memberShipLevel.equals(MemberShipLevel.PLATINUM)) {
            return 0.15;
        }
        return 0.2;
    }

}
