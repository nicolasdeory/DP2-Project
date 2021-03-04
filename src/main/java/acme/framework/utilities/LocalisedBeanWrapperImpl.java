/*
 * LocalisedLocalisedBeanWrapperImpl.java
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

import java.beans.PropertyChangeEvent;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import acme.framework.helpers.StringHelper;

public class LocalisedBeanWrapperImpl extends BeanWrapperImpl {

	// Constructors -----------------------------------------------------------

	public LocalisedBeanWrapperImpl() {
		super(true);
	}

	public LocalisedBeanWrapperImpl(final boolean registerDefaultEditors) {
		super(registerDefaultEditors);
	}

	public LocalisedBeanWrapperImpl(final Object target) {
		super(target);

		assert target != null;
	}

	public LocalisedBeanWrapperImpl(final Class<?> clazz) {
		super(clazz);

		assert clazz != null;
	}

	public LocalisedBeanWrapperImpl(final Object target, final String path, final Object root) {
		super(target, path, root);

		assert target != null;
		assert !StringHelper.isBlank(path);
		assert root != null;
	}

	// Overridden methods -----------------------------------------------------

	@Override
	protected Object convertForProperty(final String propertyName, final Object oldValue, final Object newValue, final TypeDescriptor type) throws TypeMismatchException {
		assert !StringHelper.isBlank(propertyName);
		// assert oldValue is nullable
		// assert newValue is nullable
		assert type != null;

		Object result;
		ConversionService conversionService;

		result = true;
		try {
			conversionService = super.getConversionService();
			assert conversionService != null;
			result = conversionService.convert(newValue, type.getType());
		} catch (final Throwable oops) {
			PropertyChangeEvent event;

			event = new PropertyChangeEvent(this.getRootInstance(), this.getNestedPath() + propertyName, oldValue, newValue);
			throw new ConversionNotSupportedException(event, type.getType(), oops);
		}

		return result;
	}

}
