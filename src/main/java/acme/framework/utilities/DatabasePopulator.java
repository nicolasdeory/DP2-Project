/*
 * DatabasePopulator.java
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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import acme.framework.entities.DomainEntity;
import acme.framework.helpers.CollectionHelper;
import acme.framework.helpers.FactoryHelper;
import acme.framework.helpers.StringHelper;
import acme.framework.helpers.ThrowableHelper;
import lombok.extern.java.Log;

@Component
@Log
public class DatabasePopulator {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected DatabaseUtil	databaseUtil;

	@Autowired
	protected Validator		validator;

	// Business methods -------------------------------------------------------


	public void populateInitial() {
		Environment environment;
		String initialFilename;

		DatabasePopulator.log.info("Populating initial data");
		
		environment = FactoryHelper.getBean(Environment.class);
		initialFilename = environment.getRequiredProperty("acme.initial-data", String.class);

		this.recreateSchema();
		this.populate(initialFilename);
	}

	public void populateSample() {
		Environment environment;
		String initialFilename, sampleFilename;

		DatabasePopulator.log.info("Populating sample data");
		
		environment = FactoryHelper.getBean(Environment.class);
		initialFilename = environment.getRequiredProperty("acme.initial-data", String.class);
		sampleFilename = environment.getRequiredProperty("acme.sample-data", String.class);

		this.recreateSchema();
		this.populate(initialFilename);
		this.populate(sampleFilename);
	}

	public void recreateSchema() {
		DatabasePopulator.log.info("Recreating schema");
		
		this.databaseUtil.recreateSchema();
	}

	public void populate(final String filename) {
		assert !StringHelper.isBlank(filename);

		Map<String, DomainEntity> entityMap;
		List<Entry<String, DomainEntity>> entityList;

		DatabasePopulator.log.info(String.format("Populating entities from %s", filename));
		
		try (AbstractXmlApplicationContext context = new FileSystemXmlApplicationContext(filename)) {
			entityMap = context.getBeansOfType(DomainEntity.class);
			entityList = new LinkedList<Entry<String, DomainEntity>>(entityMap.entrySet());
			this.validate(filename, entityList);
			this.persist(filename, entityList);
		}
	}

	public void validate(final String filename, final List<Entry<String, DomainEntity>> entityList) {
		assert !StringHelper.isBlank(filename);
		assert !CollectionHelper.someNull(entityList);

		Set<ConstraintViolation<DomainEntity>> violations;
		String name;
		DomainEntity entity;
		String message;

		DatabasePopulator.log.info("Validating your entities.");
		
		for (final Entry<String, DomainEntity> entry : entityList) {
			name = entry.getKey();
			entity = entry.getValue();

			violations = this.validator.validate(entity);
			if (violations.isEmpty())
				DatabasePopulator.log.info(String.format("Validating %s ... PASS", name));
			else {
				message = ThrowableHelper.toString(name, violations);
				DatabasePopulator.log.info(String.format("Validating %s ... FAILED", name));
				DatabasePopulator.log.info(message);
				throw new ValidationException(message);
			}
		}
	}

	public void persist(final String filename, final List<Entry<String, DomainEntity>> entityList) {
		assert !StringHelper.isBlank(filename);
		assert !CollectionHelper.someNull(entityList);

		boolean done;
		int attemptCounter, persistedCounter;
		String name;
		DomainEntity entity;
		String message;

		DatabasePopulator.log.info("Sorting your entities topologically.");
		
		done = entityList.isEmpty();
		attemptCounter = 0;
		persistedCounter = 0;
		while (!done && attemptCounter < entityList.size()) {
			persistedCounter = this.tryPersist(entityList, persistedCounter - 1);
			done = (persistedCounter == entityList.size());
			attemptCounter++;
		}
		if (!done) {
			message = String.format("Could not sort the entities in %s topologically!", filename);
			DatabasePopulator.log.info(message);			
			throw new RuntimeException(message);
		}

		DatabasePopulator.log.info("Persiting your entities.");
		for (final Entry<String, DomainEntity> entry : entityList) {
			name = entry.getKey();
			entity = entry.getValue();
			DatabasePopulator.log.info(String.format("> Persisting %s = %s", name, entity));
		}
	}

	public int tryPersist(final List<Entry<String, DomainEntity>> entityList, final int threshold) {
		assert !CollectionHelper.someNull(entityList);

		int result;
		boolean sucess;
		Entry<String, DomainEntity> entry;
		String name;
		DomainEntity entity;
		int index;

		this.databaseUtil.startTransaction();
		this.resetEntities(entityList);
		sucess = true;
		result = 0;
		index = 0;
		while (sucess && result < entityList.size()) {
			entry = entityList.get(result);
			name = entry.getKey();
			entity = entry.getValue();
			try {
				this.databaseUtil.persist(entity);
				if (result > threshold) {
					DatabasePopulator.log.info(String.format("> Setting index %d for %s.", threshold + index + 1, name));
					index++;
				}
				result++;
				sucess = true;
			} catch (final Throwable oops) {
				DatabasePopulator.log.info(String.format("> Deferring %s.%n", name));
				entityList.remove(result);
				entityList.add(entry);
				sucess = false;
			}
		}

		if (sucess)
			this.databaseUtil.commitTransaction();
		else
			this.databaseUtil.rollbackTransaction();		

		return result;
	}

	protected void resetEntities(final List<Entry<String, DomainEntity>> result) {
		assert !CollectionHelper.someNull(result);

		DomainEntity entity;

		for (final Entry<String, DomainEntity> entry : result) {
			entity = entry.getValue();
			entity.setId(0);
			entity.setVersion(0);
		}
	}

	// Scrap stuff ------------------------------------------------------------

	//	protected void deleteEntityMap(final String mapFilename) {
	//		assert !StringHelper.isBlank(mapFilename);
	//
	//		File file;
	//
	//		file = new File(mapFilename);
	//		file.delete();
	//	}
	//
	//	protected void saveEntityMap(final String mapFilename, final String entityFilename, final List<Entry<String, DomainEntity>> entities) {
	//		assert !StringHelper.isBlank(mapFilename);
	//		assert !StringHelper.isBlank(entityFilename);
	//		assert !CollectionHelper.someNull(entities);
	//
	//		String name;
	//		DomainEntity entity;
	//
	//		try (PrintStream writer = new PrintStream(new FileOutputStream(mapFilename, true))) {
	//			writer.println();
	//			writer(String.format("# %s%n", entityFilename);
	//			writer.println();
	//			for (final Entry<String, DomainEntity> entry : entities) {
	//				name = entry.getKey();
	//				entity = entry.getValue();
	//
	//				writer(String.format("%s=%s%n", name, entity.getId());
	//				writer.println();
	//				PrinterHelper.print(writer, "# ", entity);
	//				writer.println();
	//			}
	//		} catch (final Throwable oops) {
	//			throw new RuntimeException(oops);
	//		}
	//	}

}
