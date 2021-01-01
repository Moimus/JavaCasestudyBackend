package com.moimus.casestudy;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.Request;
import spark.Response;

public class API {

	public static Sql2o sql2o = new Sql2o("jdbc:mysql://localhost:3306/voucherdb", "root", "");
	private static Gson gson = new GsonBuilder().setDateFormat("dd.MM.yy hh:mm:ss").create();

	// Boilerplate for GET
	public static List<VoucherModel> boilGet(Request req, Response res) {
		if (req.queryParams("gtin") != null) {
			List<VoucherModel> result = getVoucherByGTIN(req.queryParams("gtin"));
			res.header("Content-Type", "application/json");
			return result;
		} else if (req.queryParams("getMode") != null && "getAllActive".equals(req.queryParams("getMode"))) {
			List<VoucherModel> result = getAllActiveVouchers();
			res.header("Content-Type", "application/json");
			return result;
		} else {
			return null;
		}

	}

	public static Boolean boilPost(Request req, Response res) {
		try {
			VoucherModel voucherModel = gson.fromJson(req.body(), VoucherModel.class);
			if (voucherModel.isValid()) {
				insertNewVoucher(voucherModel);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			App.logger.error(e.getMessage());
			return false;
		}

	}

	public static Boolean boilPut(Request req, Response res) {
		try {
			VoucherModel voucherModel = gson.fromJson(req.body(), VoucherModel.class);
			if (voucherModel.isValid()) {
				updateVoucher(voucherModel);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			App.logger.error(e.getMessage());
			return false;
		}
	}

	public static Boolean boilDelete(Request req, Response res) {
		if (req.queryParams("gtin") != null) {
			Boolean result = deleteVoucherByGTIN(req.queryParams("gtin"));
			return result;
		} else {
			return false;
		}
	}

	private static List<VoucherModel> getVoucherByGTIN(String voucherGtin) {
		try (Connection con = sql2o.open()) {
			final String query = "SELECT * FROM voucher WHERE gtin =:voucherGtin";
			Query q = con.createQuery(query);
			q.addParameter("voucherGtin", voucherGtin);
			return q.executeAndFetch(VoucherModel.class);
		} catch (Exception e) {
			App.logger.error(e.getMessage());
			return null;
		}

	}

	private static List<VoucherModel> getAllActiveVouchers() {
		try (Connection con = sql2o.open()) {
			final String query = "SELECT * FROM voucher WHERE active =:active";
			Query q = con.createQuery(query);
			q.addParameter("active", 1);
			return q.executeAndFetch(VoucherModel.class);
		} catch (Exception e) {
			App.logger.error(e.getMessage());
			return null;
		}

	}

	private static Boolean insertNewVoucher(VoucherModel newVoucher) {
		try (Connection con = sql2o.beginTransaction()) {
			final String query = "INSERT INTO voucher (gtin, campaignStart, campaignEnd, discount, active, campaignName) "
					+ "values(:gtin, :campaignStart, :campaignEnd, :discount, :active, :campaignName)";
			Query q = con.createQuery(query);
			q.addParameter("gtin", newVoucher.gtin);
			q.addParameter("campaignStart", newVoucher.campaignStart);
			q.addParameter("campaignEnd", newVoucher.campaignEnd);
			q.addParameter("discount", newVoucher.discount);
			q.addParameter("active", newVoucher.active);
			q.addParameter("campaignName", newVoucher.campaignName);
			q.executeUpdate();
			con.commit();
			return true;
		} catch (Exception e) {
			App.logger.error(e.getMessage());
			return false;
		}
	}

	private static Boolean updateVoucher(VoucherModel updatedVoucher) {
		try (Connection con = sql2o.beginTransaction()) {
			final String query = "UPDATE voucher"
					+ " SET gtin = :gtin, campaignStart = :campaignStart, campaignEnd = :campaignEnd, discount = :discount, active = :active, campaignName = :campaignName"
					+ " WHERE gtin = :gtin";
			Query q = con.createQuery(query);
			q.addParameter("gtin", updatedVoucher.gtin);
			q.addParameter("campaignStart", updatedVoucher.campaignStart);
			q.addParameter("campaignEnd", updatedVoucher.campaignEnd);
			q.addParameter("discount", updatedVoucher.discount);
			q.addParameter("active", updatedVoucher.active);
			q.addParameter("campaignName", updatedVoucher.campaignName);
			q.executeUpdate();
			con.commit();
			return true;
		} catch (Exception e) {
			App.logger.error(e.getMessage());
			return false;
		}
	}

	private static Boolean deleteVoucherByGTIN(String voucherGtin) {
		try (Connection con = sql2o.beginTransaction()) {
			final String query = "DELETE FROM voucher WHERE gtin = :gtin";
			Query q = con.createQuery(query);
			q.addParameter("gtin", voucherGtin);
			q.executeUpdate();
			con.commit();
			return true;
		} catch (Exception e) {
			App.logger.error(e.getMessage());
			return false;
		}
	}
}
