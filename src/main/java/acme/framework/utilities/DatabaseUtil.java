/*
 * DatabaseUtil.java
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToWriter;
import org.hibernate.tool.schema.spi.ScriptTargetOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import acme.framework.entities.DomainEntity;
import acme.framework.helpers.StringHelper;
import acme.framework.helpers.ThrowableHelper;

@Component
public class DatabaseUtil {

	// Internal state ---------------------------------------------------------

	@PersistenceContext
	protected EntityManager			entityManager;

	@Autowired
	protected PlatformTransactionManager		transactionManager;

	@Autowired
	protected Environment			environment;

	protected TransactionStatus		transactionStatus;
	protected TransactionDefinition	transactionDefinition;

	// Transaction management --------------------------------------------------


	public void setReadUncommittedIsolationLevel() {
		this.executeCommand("set transaction isolation level read uncommitted;");
	}

	public void setReadCommittedIsolationLevel() {
		this.executeCommand("set transaction isolation level read committed;");
	}

	public void startTransaction() {
		assert !this.isTransactionActive();

		this.transactionDefinition = new DefaultTransactionDefinition();		
		this.transactionStatus = this.transactionManager.getTransaction(this.transactionDefinition);
		this.setReadUncommittedIsolationLevel();
		this.clearPersistenceContext();
	}

	public void commitTransaction() {
		assert this.isTransactionActive();

		try {
			this.transactionManager.commit(this.transactionStatus);
		} catch (final Throwable oops) {
			ThrowableHelper.print(oops);
		} finally {
			this.transactionStatus = null;
			this.clearPersistenceContext();
		}
	}

	public void rollbackTransaction() {
		assert this.isTransactionActive();

		try {
			this.transactionManager.rollback(this.transactionStatus);
		} catch (final Throwable oops) {
			ThrowableHelper.print(oops);
		} finally {
			this.transactionStatus = null;
			this.clearPersistenceContext();
		}
	}

	public boolean isTransactionActive() {
		boolean result;

		result = this.transactionStatus != null;

		return result;
	}

	// Schema management ------------------------------------------------------

	public void recreateSchema() {
		Metadata metadata;
		String[] dropScript, createScript;
		
		metadata = this.generateMetadata();
		dropScript = this.generateScript(metadata, Action.DROP);
		this.executeScript(dropScript);
		createScript = this.generateScript(metadata, Action.CREATE);
		this.executeScript(createScript);
	}

	// Script execution -------------------------------------------------------

	public void executeScript(final String... commands) {
		assert commands != null;

		try {
			this.startTransaction();
			for (final String command : commands) {
				if (!StringHelper.isBlank(command))
					this.executeCommand(command);
			}
			this.commitTransaction();
		} catch (final Throwable oops) {
			if (this.isTransactionActive()) {
				this.rollbackTransaction();
			}
			throw new RuntimeException(oops);
		}
	}

	// Command execution ------------------------------------------------......

	public void executeCommand(final String command) {
		assert !StringHelper.isBlank(command);

		Session session;

		session = this.entityManager.unwrap(Session.class);
		session.doWork(connection -> {
			Statement statement;

			statement = connection.createStatement();
			statement.execute(command);
			connection.commit();
		});
	}

	public int executeUpdate(final String command) {
		assert !StringHelper.isBlank(command);

		int result;
		Query query;

		query = this.entityManager.createQuery(command);
		result = query.executeUpdate();

		return result;
	}

	public List<?> executeSelect(final String command) {
		assert !StringHelper.isBlank(command);

		List<?> result;
		Query query;

		query = this.entityManager.createQuery(command);
		result = query.getResultList();

		return result;
	}

	public void persist(final DomainEntity entity) {
		assert entity != null;
		assert this.isTransactionActive();

		this.entityManager.persist(entity);
	}

	// Miscellaneous ----------------------------------------------------------

	public void printPersistenceContext() {
		SessionImplementor session;
		org.hibernate.engine.spi.PersistenceContext context;
		Entry<Object, EntityEntry>[] entries;

		session = this.entityManager.unwrap(SessionImplementor.class);
		context = session.getPersistenceContext();
		entries = context.reentrantSafeEntityEntries();

		for (final Entry<Object, EntityEntry> entry : entries) {
			System.out.printf("%s%n", entry.getValue());
		}
	}

	public void clearPersistenceContext() {
		this.entityManager.clear();
	}

	// Internal methods -------------------------------------------------------

	protected Metadata generateMetadata() {
		Metadata result;
		Map<String, String> settings;
		StandardServiceRegistryBuilder registryBuilder;
		ServiceRegistry registry;
		MetadataSources metadataBuilder;
		Metamodel metamodel;
		Collection<EntityType<?>> entities;
		Collection<EmbeddableType<?>> embeddables;

		settings = new HashMap<String, String>();
		settings.put("connection.driver_class", this.environment.getRequiredProperty("spring.datasource.driver-class-name"));
		settings.put("dialect", this.environment.getRequiredProperty("spring.jpa.hibernate.dialect"));
		settings.put("hibernate.connection.url", this.environment.getRequiredProperty("spring.datasource.url"));
		settings.put("hibernate.connection.username", this.environment.getRequiredProperty("spring.datasource.username"));
		settings.put("hibernate.connection.password", this.environment.getRequiredProperty("spring.datasource.password"));
		settings.put("hibernate.physical_naming_strategy", this.environment.getRequiredProperty("spring.jpa.hibernate.naming.physical-strategy"));
		settings.put("hibernate.implicit_naming_strategy", this.environment.getRequiredProperty("spring.jpa.hibernate.naming.implicit-strategy"));
		settings.put("show_sql", this.environment.getRequiredProperty("spring.jpa.hibernate.show-sql"));
		settings.put("format_sql", this.environment.getRequiredProperty("spring.jpa.hibernate.format-sql"));
		settings.put("hibernate.globally_quoted_identifiers", "true");

		registryBuilder = new StandardServiceRegistryBuilder();
		registryBuilder.applySettings(settings);
		registry = registryBuilder.build();

		metadataBuilder = new MetadataSources(registry);
		metamodel = this.entityManager.getMetamodel();
		entities = metamodel.getEntities();
		for (final EntityType<?> entity : entities) {
			metadataBuilder.addAnnotatedClass(entity.getJavaType());
		}
		embeddables = metamodel.getEmbeddables();
		for (final EmbeddableType<?> embeddable : embeddables) {
			metadataBuilder.addAnnotatedClass(embeddable.getJavaType());
		}
		result = metadataBuilder.buildMetadata();

		return result;
	}

	protected String[] generateScript(final Metadata metadata, final Action action) {
		assert metadata != null;
		assert action != null && (action.equals(Action.CREATE) || action.equals(Action.DROP));

		String[] result;
		String text;
		SchemaExport exporter;
		ScriptTargetOutput target;

		text = null;
		try (OutputStream outputStream = new ByteArrayOutputStream(); Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
			target = new ScriptTargetOutputToWriter(writer);

			exporter = new SchemaExport();
			exporter.setHaltOnError(true);
			exporter.setFormat(false);
			exporter.setDelimiter(";");

			exporter.perform(action, metadata, target);
			text = outputStream.toString();
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}

		assert text != null;
		text = "set foreign_key_checks=0;\n" + text + "set foreign_key_checks=1;\n";

		result = text.split(";\\s*[\r\n]+");
		if (action.equals(Action.DROP)) {
			// HINT: the exporter generates ALTER statements without checking if the tables exists!
			// HINT+ Thus, we need to remove those statements explicitly.
			List<String> patch;

			patch = new ArrayList<String>();
			for (final String command : result) {
				if (!command.trim().startsWith("alter table"))
					patch.add(command);
			}
			result = patch.toArray(new String[0]);
		}

		return result;
	}

	// Scrap stuff ------------------------------------------------------------

	//	public void executeScript(final File file) {
	//		assert file != null && file.isFile() && file.canRead();
	//
	//		String[] commands;
	//
	//		commands = this.readScript(file);
	//		this.executeScript(commands);
	//	}

	//	protected String[] readScript(final File file) {
	//		assert file != null && file.isFile() && file.canRead();
	//
	//		String script;
	//		String[] result;
	//
	//		try {
	//			script = FileUtils.readFileToString(file, "utf-8");
	//			script = script.replaceAll(";\\s*[\r\n]+", ">\\|\\|<");
	//			script = script.replaceAll("[\r\n]+", " ");
	//			result = script.split(">\\|\\|<");
	//		} catch (Throwable oops) {
	//			throw new RuntimeException(oops);
	//		}
	//
	//		return result;
	//	}

	//	public void executeScript(final List<String> commands) {
	//		assert !CollectionHelper.someNull(commands);
	//
	//		try {
	//			this.startTransaction();
	//			for (final String command : commands) {
	//				this.executeCommand(command);
	//			}
	//			this.commitTransaction();
	//		} catch (Throwable oops) {
	//			if (this.isTransactionActive()) {
	//				this.rollbackTransaction();
	//			}
	//			throw new RuntimeException(oops);
	//		}
	//	}

}
