package com.paypal.sdk.openidconnect;

import java.util.HashMap;
import java.util.Map;

public class CreateFromRefreshTokenParameters {

	/**
	 * Scope
	 */
	private static final String SCOPE = "scope";
	
	/**
	 * Grant Type
	 */
	private static final String GRANTTYPE = "grant_type";

	// Map backing QueryParameters intended to processed
	// by SDK library 'RESTUtil'
	private Map<String, String> containerMap;

	public CreateFromRefreshTokenParameters() {
		containerMap = new HashMap<String, String>();
		containerMap.put(GRANTTYPE, "refresh_token");
	}

	/**
	 * @return the containerMap
	 */
	Map<String, String> getContainerMap() {
		return containerMap;
	}

	/**
	 * Set the Scope
	 * 
	 * @param redirectURI
	 */
	public void setScope(String scope) {
		containerMap.put(SCOPE, scope);
	}
	
	/**
	 * Set the Grant Type
	 * 
	 * @param grantType
	 */
	public void setGrantType(String grantType) {
		containerMap.put(GRANTTYPE, grantType);
	}

}
