/*
 * Request.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;

import acme.framework.entities.DomainEntity;
import acme.framework.entities.Principal;
import acme.framework.helpers.CollectionHelper;
import acme.framework.helpers.ErrorsHelper;
import acme.framework.helpers.FactoryHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.helpers.StringHelper;
import acme.framework.utilities.CustomBindingErrorProcessor;
import acme.framework.utilities.LocalisedDataBinder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request<E> {

	// Internal state ---------------------------------------------------------

	protected HttpMethod			method;
	protected Command				command;
	protected BasicCommand			baseCommand;

	protected Principal				principal;
	protected Locale				locale;
	protected Model					model;

	protected HttpServletRequest	servletRequest;
	protected HttpServletResponse	servletResponse;

	// Constructors -----------------------------------------------------------


	public Request( //
		final HttpMethod method, //
		final Command command, //
		final BasicCommand baseCommand, //
		final Map<String, Object> model, //
		final Locale locale, //
		final HttpServletRequest servletRequest, //
		final HttpServletResponse servletResponse) {

		assert method != null;
		assert command != null;
		assert baseCommand != null;
		assert model != null;
		assert locale != null;
		assert servletRequest != null;
		assert servletResponse != null;

		this.method = method;
		this.command = command;
		this.baseCommand = baseCommand;

		this.principal = PrincipalHelper.get();
		this.model = new Model();
		this.model.append(model);
		this.locale = locale;

		this.servletRequest = servletRequest;
		this.servletResponse = servletResponse;
	}

	// Business methods -------------------------------------------------------

	public boolean isMethod(final HttpMethod method) {
		assert method != null;

		boolean result;

		result = this.method == method;

		return result;
	}

	public boolean isCommand(final Command... commands) {
		assert !CollectionHelper.someNull(commands);

		boolean result;

		result = false;
		for (int index = 0; !result && index < commands.length; index++) {
			result = this.command == commands[index] || this.baseCommand == commands[index];
		}

		return result;
	}

	public void bind(final Object object, final Errors errors, final String... excludedProperties) {
		assert object != null;
		assert errors != null;
		assert !StringHelper.someBlank(excludedProperties);

		WebDataBinder binder;
		ServletRequest request;
		ServletRequestParameterPropertyValues values;
		ArrayList<String> exclusions;
		BindingResult binding;

		request = this.getServletRequest();
		values = new ServletRequestParameterPropertyValues(request);

		exclusions = new ArrayList<String>(Arrays.asList(excludedProperties));
		if (object instanceof DomainEntity) {
			exclusions.remove("id");
			exclusions.remove("version");
		}

		binder = this.createBinder(this, object, exclusions);
		binder.bind(values);
		binding = binder.getBindingResult();

		ErrorsHelper.transferErrors(this, binding, errors);
	}

	public void unbind(final Object object, final Model model, final String... includedProperties) {
		assert object != null;
		assert model != null;
		assert !StringHelper.someBlank(includedProperties);

		BeanWrapperImpl wrapper;
		List<String> inclusions;
		Object value;

		wrapper = new BeanWrapperImpl(object);
		wrapper.setAutoGrowNestedPaths(true);

		inclusions = new ArrayList<String>(Arrays.asList(includedProperties));
		if (object instanceof DomainEntity) {
			inclusions.add("id");
			inclusions.add("version");
		}

		for (final String property : inclusions) {
			assert wrapper.isReadableProperty(property);
			value = wrapper.getPropertyValue(property);
			model.setAttribute(property, value);
		}
	}

	public void transfer(final Model model, final String... includedProperties) {
		assert model != null;
		assert !StringHelper.someBlank(includedProperties);
		assert this.getModel().size() == 1;

		List<String> inclusions;
		String key;
		Object value;

		inclusions = new ArrayList<String>(Arrays.asList(includedProperties));
		inclusions.add("id");
		inclusions.add("version");

		for (final Entry<String, Object> entry : this.model.get(0).entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			if (inclusions.contains(key)) {
				model.setAttribute(key, value);
			}
		}
	}

	// Internal methods -------------------------------------------------------

	protected WebDataBinder createBinder(final Request<?> request, final Object target, final List<String> exclusions) {
		WebDataBinder result;
		ConversionService conversionService;
		CustomBindingErrorProcessor errorProcessor;

		conversionService = FactoryHelper.getConversionService();
		errorProcessor = new CustomBindingErrorProcessor();

		result = new LocalisedDataBinder(target);
		result.setConversionService(conversionService);
		result.setBindingErrorProcessor(errorProcessor);
		result.setDisallowedFields(exclusions.toArray(new String[] {}));
		result.setAutoGrowNestedPaths(true);

		return result;
	}

}
