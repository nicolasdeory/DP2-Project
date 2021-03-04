/*
 * ConversionConfiguration.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.configuration;

import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import acme.framework.utilities.LocalisedDateFormatter;
import acme.framework.utilities.LocalisedDoubleFormatter;
import acme.framework.utilities.LocalisedMoneyFormatter;

@Configuration
public class ConversionConfiguration implements WebMvcConfigurer {

	// Beans ------------------------------------------------------------------

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		LocalisedDateFormatter dateFormatter;
		LocalisedMoneyFormatter moneyFormatter;
		LocalisedDoubleFormatter doubleFormatter;

		registry.removeConvertible(String.class, Date.class);
		registry.removeConvertible(Date.class, String.class);
		dateFormatter = new LocalisedDateFormatter();
		registry.addFormatter(dateFormatter);

		registry.removeConvertible(String.class, Double.class);
		registry.removeConvertible(Double.class, String.class);
		doubleFormatter = new LocalisedDoubleFormatter();
		registry.addFormatter(doubleFormatter);

		moneyFormatter = new LocalisedMoneyFormatter();
		registry.addFormatter(moneyFormatter);
	}

}
