package com.moimus.casestudy;

import java.util.Date;

public class Mock 
{
	public static VoucherModel getMockVoucher() {
		return new VoucherModel("123-2131", "Coke > pepis", new Date(1607178507000l), new Date(1608042507000l), 20, true);
	}
}
