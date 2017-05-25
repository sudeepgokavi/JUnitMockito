package com.junit.mockito.dao;

import java.util.List;

import com.junit.mockito.model.Trade;

public interface TradeStoreDao {

	public List<Trade> getAllTrades();
	
	public Trade getTrade(String tradeId, int version, String counterPartyId);
	
	public void addTrade(Trade tradeObj);
	
	public void updateTrade(Trade tradeObj);
	
	public void validateTrade(Trade tradeObj);
}
