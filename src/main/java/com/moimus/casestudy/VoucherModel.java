package com.moimus.casestudy;

import java.awt.image.BufferedImage;
import java.util.Date;

import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherModel {
	protected String gtin;
	protected String campaignName;
	protected Date campaignStart;
	protected Date campaignEnd;
	protected int discount;
	protected Boolean active;

	public Boolean isValid() {
		String gtinPrefix = gtin.replace("-", "").substring(0, 3);
		if ((gtinPrefix.equals("981") || gtinPrefix.equals("982")) && EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(gtin)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}

	public BufferedImage toQR() {
		BufferedImage out = null;
		try {
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix matrix = writer.encode(this.toString(), BarcodeFormat.QR_CODE, 512, 512);
			out = MatrixToImageWriter.toBufferedImage(matrix);
		} catch (Exception e) {
			App.logger.error("Voucher To QR Failed");
		}
		return out;
	}
}
