/*
 * SqlCommentStatementInspector.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.utilities;

import java.util.logging.Level;
import java.util.regex.Pattern;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import lombok.extern.java.Log;

@Log
public class SqlCommentStatementInspector implements StatementInspector {

	protected static final long		serialVersionUID	= 1L;
	protected static final Pattern	SQL_COMMENT_PATTERN	= Pattern.compile("\\/\\*.*?\\*\\/\\s*");


	@Override
	public String inspect(String sql) {
		String result;

		result = SQL_COMMENT_PATTERN.matcher(sql).replaceAll("");
		SqlCommentStatementInspector.log.log(Level.INFO, result);

		return result;
	}
}
