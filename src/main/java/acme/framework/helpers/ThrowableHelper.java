/*
 * ThrowableHelper.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.helpers;

import java.io.PrintStream;
import java.util.Set;

import javax.validation.ConstraintViolation;

import acme.framework.entities.DomainEntity;

public class ThrowableHelper {
	
	// Constructors -----------------------------------------------------------

	protected ThrowableHelper() {
	}

	// Business methods -------------------------------------------------------

	public static void print(final Throwable oops) {
		assert oops != null;

		ThrowableHelper.print(System.err, oops);
	}

	public static void print(final PrintStream writer, final Throwable oops) {
		assert writer != null;
		assert oops != null;

		String text;

		text = ThrowableHelper.toString(oops);
		writer.append(text);
	}

	public static String toString(final Throwable oops) {
		assert oops != null;

		StringBuilder result;
		String title, description, paragraph;
		Throwable iterator;

		result = new StringBuilder();
		iterator = oops;
		while (iterator != null) {
			title = iterator.getStackTrace()[0].toString();
			description = iterator.getLocalizedMessage();
			if (description != null) {
				paragraph = ThrowableHelper.formatParagraph(title, description);
			} else {
				paragraph = iterator.getClass().getName();
			}
			result.append(paragraph);
			iterator = iterator.getCause();
		}

		return result.toString();
	}
	
	public static String toString(final String entityName, final Set<ConstraintViolation<DomainEntity>> violations) {
		assert !StringHelper.isBlank(entityName);
		assert !CollectionHelper.someNull(violations);
		
		StringBuilder result;
		String comma, property, message;
		
		result = new StringBuilder();
		result.append(String.format("Violated constraints on entity %s: ", entityName));
		comma = "";
		for (final ConstraintViolation<DomainEntity> violation : violations) {
			property = violation.getPropertyPath().toString();
			message = violation.getMessage();
			result.append(String.format("%s%s : %s%n", comma, property, message));
			comma = ", ";
		}
		
		return result.toString();
	}

	public static String formatText(final String text) {
		assert !StringHelper.isBlank(text);

		String result;

		result = text.replace("Stacktrace:", "");
		result = result.replaceAll("[\\s]+$", "");
		result = result.replaceAll("^[\\s]+", "");
		result = result.replaceAll("([\\n\\r][\\ \\t]*){2,100}", System.lineSeparator());

		return result;
	}

	public static String formatParagraph(final String title, final String description) {
		assert !StringHelper.isBlank(title);
		assert !StringHelper.isBlank(description);

		StringBuilder result;
		String titleText, descriptionText;

		titleText = ThrowableHelper.formatText(title);
		descriptionText = ThrowableHelper.formatText(description);

		result = new StringBuilder();
		result.append(titleText);
		result.append(": ");
		result.append(descriptionText);
		result.append(System.lineSeparator());

		return result.toString();
	}

}
