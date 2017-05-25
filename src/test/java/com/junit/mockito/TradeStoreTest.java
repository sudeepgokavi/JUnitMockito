package com.junit.mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;

import com.junit.mockito.dao.TradeStoreDao;

import com.junit.mockito.model.Trade;

//import junit.framework.Assert;

public class TradeStoreTest {

	private static TradeStoreDao tradeStorDao;
	private static Trade trade1;

	private static List<Trade> tradeList;

	@BeforeClass
	public static void setUp() {
		tradeStorDao = mock(TradeStoreDao.class);

		prepareTradeData();

		when(tradeStorDao.getAllTrades()).thenReturn(tradeList);

		when(tradeStorDao.getTrade("T1", 1, "CP-1")).thenReturn(tradeList.get(0));
	}

	private static void prepareTradeData() {

		tradeList = new ArrayList<Trade>();
		Trade tradeObj1 = new Trade("T1", 1, "CP-1", "B1", parseDate("20/05/2020"), parseDate("20/05/2017"), 'N');
		Trade tradeObj2 = new Trade("T2", 2, "CP-2", "B1", parseDate("20/05/2021"), parseDate("20/05/2017"), 'N');
		Trade tradeObj3 = new Trade("T2", 1, "CP-1", "B1", parseDate("20/05/2021"), parseDate("14/03/2015"), 'N');
		Trade tradeObj4 = new Trade("T3", 3, "CP-3", "B2", parseDate("20/05/2014"), parseDate("20/05/2017"), 'Y');

		tradeList.add(tradeObj1);
		tradeList.add(tradeObj2);
		tradeList.add(tradeObj3);
		tradeList.add(tradeObj4);

		// System.out.println(tradeList.size());
	}

	private static java.sql.Date parseDate(String date) {
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date date1 = sdf1.parse(date);
			java.sql.Date sqlEndDate = new java.sql.Date(date1.getTime());

			return sqlEndDate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Test
	public void testTradeList() {
		List<Trade> tradeList = tradeStorDao.getAllTrades();
		Assert.assertEquals(4, tradeList.size());
	}

	/**
	 * Case - 1
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTrades() throws Exception {
		String tradeID = "T1";
		int version = 1;
		String counterPartyId = "CP-1";
		String bookId = "B3";

		int versionReceived = 1;

		Trade tradeObj = tradeStorDao.getTrade(tradeID, version, counterPartyId);
		if (versionReceived < tradeObj.getVersion()) {
			throw new Exception("DDDD");
		} else {
			tradeObj.setBookId(bookId);
		}

		Assert.assertEquals("B3", tradeList.get(0).getBookId());
	}

	/**
	 * Case - 2
	 */
	@Test
	public void testAddTrade() {
		Trade tradeObj5 = new Trade("T3", 1, "CP-1", "B2", parseDate("16/05/2017"), parseDate("20/05/2017"), 'Y');
		String currDate = "20/05/2017";

		if (tradeObj5.getMaturityDate().after(parseDate(currDate))) {
			tradeList.add(tradeObj5);
		} else {
			System.out.println("Maturity Date is less than today's date.");
		}
		Assert.assertEquals(4, tradeList.size());
	}

	/**
	 * Case - 3
	 */
	@Test
	public void testUpdateTrade() {
		Date currDate = parseDate("19/06/2020");
		int count = 0;
		for (Trade tradeObj : tradeList) {
			if (tradeObj.getMaturityDate().before(currDate)) {
				tradeObj.setExpired('Y');
				count++;
			}
		}
		System.out.println(count + " Traded Updated");
		Assert.assertEquals('Y', tradeList.get(0).getExpired());
	}

}
