/*
 * PerformanceFileHelper.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.extension.ExtensionContext;

public class PerformanceFileHelper {

	// Constructors -----------------------------------------------------------

	private PerformanceFileHelper() {
		;
	}


	public static final String	FOLDER	= "./reports";

	public static String		TEST_FILEPATH;
	public static String		REQUEST_FILEPATH;

	static {
		long currentTime;
		Timestamp timestamp;
		String suffix;
		File folder;

		folder = new File(PerformanceFileHelper.FOLDER);
		assert !folder.exists() || folder.canWrite() : "Cannot write to './reports' folder";
		if (!folder.exists())
			folder.mkdirs();

		currentTime = System.currentTimeMillis();
		timestamp = new Timestamp(currentTime);
		suffix = timestamp.toString().replace(" ", "T").replace(":", "-");
		PerformanceFileHelper.TEST_FILEPATH = String.format("%s/performance-tests;%s.csv", PerformanceFileHelper.FOLDER, suffix);

		PerformanceFileHelper.REQUEST_FILEPATH = String.format("%s/performance-requests;%s.csv", PerformanceFileHelper.FOLDER, suffix);
	}

	// Internal state ---------------------------------------------------------

	private static boolean		initialised;
	private static Set<String>	irrelevantSimplePaths;

	static {
		PerformanceFileHelper.initialised = false;
		PerformanceFileHelper.irrelevantSimplePaths = new HashSet<String>();
		PerformanceFileHelper.irrelevantSimplePaths.add("/master/referrer");
		PerformanceFileHelper.irrelevantSimplePaths.add("/master/oops");
		PerformanceFileHelper.irrelevantSimplePaths.add("/master/populate-initial");
		PerformanceFileHelper.irrelevantSimplePaths.add("/master/populate-sample");
	}

	// Business methods -------------------------------------------------------	


	public static void initialise() {
		synchronized (PerformanceFileHelper.class) {
			if (!PerformanceFileHelper.initialised) {
				PerformanceFileHelper.clearPerformanceFile(PerformanceFileHelper.TEST_FILEPATH, "timestamp,test-class,test-method,time,description,result");
				PerformanceFileHelper.clearPerformanceFile(PerformanceFileHelper.REQUEST_FILEPATH, "timestamp,simple-path,time");
				PerformanceFileHelper.initialised = true;
			}
		}
	}

	public static void writeTestRecord(final ExtensionContext context, final long duration) {
		assert context != null;
		assert duration >= 0;

		String record;

		record = PerformanceFileHelper.buildPerformanceRecord(context, duration);
		PerformanceFileHelper.appendToFile(PerformanceFileHelper.TEST_FILEPATH, record);
	}

	public static void writeRequestRecord(final String simplePath, final long duration) {
		assert !StringHelper.isBlank(simplePath);
		assert duration >= 0;

		String record;

		if (!PerformanceFileHelper.irrelevantSimplePaths.contains(simplePath)) {
			record = PerformanceFileHelper.buildPerformanceRecord(simplePath, duration);
			PerformanceFileHelper.appendToFile(PerformanceFileHelper.REQUEST_FILEPATH, record);
		}
	}

	// Ancillary methods ------------------------------------------------------

	protected static String buildPerformanceRecord(final ExtensionContext context, final long duration) {
		assert context != null;
		assert duration >= 0;

		StringBuilder result;
		long currentTime;
		Timestamp timestamp;
		String clazzName, methodName, description, message;
		Optional<Throwable> exception;

		currentTime = System.currentTimeMillis();
		timestamp = new Timestamp(currentTime);
		clazzName = context.getRequiredTestClass().getName();
		methodName = context.getRequiredTestMethod().getName();
		description = String.format("\"%s\"", context.getDisplayName().replace("\"", "'"));
		exception = context.getExecutionException();
		message = exception.isPresent() ? exception.get().getLocalizedMessage() : "OK";

		result = new StringBuilder();
		result.append(timestamp);
		result.append(",");
		result.append(clazzName);
		result.append(",");
		result.append(methodName);
		result.append(",");
		result.append(duration);
		result.append(",");
		result.append(description);
		result.append(",");
		result.append(message);

		return result.toString();
	}

	protected static String buildPerformanceRecord(final String simplePath, final long duration) {
		assert !StringHelper.isBlank(simplePath);
		assert duration >= 0;

		StringBuilder result;
		long currentTime;
		Timestamp timestamp;

		currentTime = System.currentTimeMillis();
		timestamp = new Timestamp(currentTime);

		result = new StringBuilder();
		result.append(timestamp);
		result.append(",");
		result.append(simplePath);
		result.append(",");
		result.append(duration);

		return result.toString();
	}

	protected static void clearPerformanceFile(final String filePath, final String header) {
		assert !StringHelper.isBlank(filePath);
		assert !StringHelper.isBlank(header);

		File file;
		boolean deleted, created;

		file = new File(filePath);
		assert !file.exists() || file.canWrite() : String.format("Cannot clear performance file '%s'", filePath);
		try {
			if (file.exists()) {
				deleted = file.delete();
				assert deleted : String.format("Cannot delete current performance file '%s'", filePath);
			}
			created = file.createNewFile();
			assert created : String.format("Cannot create new performance file '%s'", filePath);
			PerformanceFileHelper.appendToFile(filePath, header);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
	}

	protected static void appendToFile(final String filePath, final String record) {
		assert !StringHelper.isBlank(filePath);
		assert !StringHelper.isBlank(record);

		File file;
		byte[] buffer, crlf;

		synchronized (PerformanceFileHelper.class) {
			file = new File(filePath);
			try (FileOutputStream output = new FileOutputStream(file, true)) {
				assert file.exists() && file.canWrite() : String.format("Cannot write to performance file '%s'", filePath);
				buffer = record.getBytes(StandardCharsets.UTF_8);
				output.write(buffer);
				crlf = System.lineSeparator().getBytes(StandardCharsets.UTF_8);
				output.write(crlf);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		}
	}

	protected static String getFullTestName(final ExtensionContext context) {
		assert context != null;

		String result;
		Class<?> clazz;
		Method method;

		clazz = context.getRequiredTestClass();
		method = context.getRequiredTestMethod();

		result = String.format("%s::%s", clazz.getName(), method.getName());

		return result;
	}

}
