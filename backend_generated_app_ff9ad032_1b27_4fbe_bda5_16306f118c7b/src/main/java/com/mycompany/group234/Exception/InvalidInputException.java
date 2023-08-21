package com.mycompany.group234.Exception;

import java.util.Locale;

import org.apache.olingo.server.api.ODataApplicationException;

public class InvalidInputException extends ODataApplicationException {

	public InvalidInputException(String msg, int statusCode, Locale locale, String oDataErrorCode) {
		super(msg, statusCode, locale, oDataErrorCode);
	}

}
