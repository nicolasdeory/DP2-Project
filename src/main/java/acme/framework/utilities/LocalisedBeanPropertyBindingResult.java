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

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.BeanPropertyBindingResult;

public class LocalisedBeanPropertyBindingResult extends BeanPropertyBindingResult {

	// Serialisation ----------------------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Constructors -----------------------------------------------------------


	public LocalisedBeanPropertyBindingResult(final Object target, final String objectName) {
		super(target, objectName);

		assert target != null;
		assert objectName != null;
	}

	public LocalisedBeanPropertyBindingResult(final Object target, final String objectName, final boolean autoGrowNestedPaths, final int autoGrowCollectionLimit) {
		super(target, objectName, autoGrowNestedPaths, autoGrowCollectionLimit);

		assert target != null;
		assert objectName != null;
	}

	// Overrriden methods -----------------------------------------------------

	@Override
	protected BeanWrapper createBeanWrapper() {
		assert super.getTarget() != null;

		BeanWrapper result;

		result = new LocalisedBeanWrapperImpl(super.getTarget());

		return result;
	}

}
