/**
 *
 */
package com.example.springboot.jwt.application.common;

import org.springframework.http.MediaType;

/**
 * @author 39450160
 *
 */
public final class Constants {

	public static final String ENV_KEY = "env";
	public static final String BRANCH_KEY = "branch";
	public static final String VERSION_KEY = "version";
	public static final String TIME_KEY = "time";
	public static final String TIME_STAMP = "timestamp";
	public static final String ERROR = "error";
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String PATH = "path";
	public static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;
	public static final String CHAR_ENCODE_UTF = "UTF-8";
	public static final String UNAUTH_RESPONSE = "UnAuthorized";
	public static final String JWT_EXCEPTION_RESPONSE = "Invalid User token or token expired";
	public static final String DATE_FORMAT_YYY_MM_DD_MMSS = "yyyy-MM-dd HH:mm:ss.SSS";
}
