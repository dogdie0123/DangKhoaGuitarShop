package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.DiscountCode;

public interface DiscountCodeService {

    DiscountCode getDiscountCodeByCode(String code);
}
