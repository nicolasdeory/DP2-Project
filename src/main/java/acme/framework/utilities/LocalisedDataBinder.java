/*
 * LocalisedDataBinder.java
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

import org.springframework.lang.Nullable;
import org.springframework.validation.AbstractPropertyBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.bind.WebDataBinder;

import acme.framework.helpers.StringHelper;

public class LocalisedDataBinder extends WebDataBinder {

	// Constructors -----------------------------------------------------------

	public LocalisedDataBinder(final Object target) {
		super(target);

		assert target != null;
	}

	public LocalisedDataBinder(@Nullable final Object target, final String objectName) {
		super(target, objectName);

		assert target != null;
		assert !StringHelper.isBlank(objectName);
	}

	// Internal state ---------------------------------------------------------


	protected MessageCodesResolver messageCodesResolver;

	// Overrriden methods -----------------------------------------------------


	@Override
	public void setMessageCodesResolver(final MessageCodesResolver messageCodesResolver) {
		assert messageCodesResolver != null;
		assert this.messageCodesResolver == null;

		super.setMessageCodesResolver(messageCodesResolver);
		this.messageCodesResolver = messageCodesResolver;
	}

	@Override
	protected AbstractPropertyBindingResult createBeanPropertyBindingResult() {
		BeanPropertyBindingResult result;

		result = new LocalisedBeanPropertyBindingResult( //
			this.getTarget(), //
			this.getObjectName(), // 
			this.isAutoGrowNestedPaths(), //
			this.getAutoGrowCollectionLimit());

		if (super.getConversionService() != null) {
			result.initConversion(super.getConversionService());
		}
		if (this.messageCodesResolver != null) {
			result.setMessageCodesResolver(this.messageCodesResolver);
		}

		return result;
	}

}
