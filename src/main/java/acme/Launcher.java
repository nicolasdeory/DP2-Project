/*
 * Launcher.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import acme.framework.helpers.FactoryHelper;
import acme.framework.helpers.ProfileHelper;
import acme.framework.utilities.DatabaseInquirer;
import acme.framework.utilities.DatabasePopulator;
import lombok.extern.java.Log;

@SpringBootApplication(scanBasePackages = "acme")
@Log
public class Launcher extends SpringBootServletInitializer {

	private static final String PROFILE = "profile";
	private static final String ACTION = "action";
	private static final String RUN = "run";
	private static final String POPULATE_INITIAL = "populate-initial";
	private static final String POPULATE_SAMPLE = "populate-sample";
	private static final String INQUIRE_DATABASE = "inquire-database";
	
	// Command-line entry point -----------------------------------------------

	public static void main(final String[] argv) {
		Map<String, String> arguments;
		ConfigurableApplicationContext context;

		context = null;
		try {
			arguments = Launcher.analyseArguments(argv);
			Launcher.setProfile(arguments);
			context = SpringApplication.run(Launcher.class, argv);
			FactoryHelper.initialise(context);
			Launcher.doExtraWork(arguments, context);
		} catch (final Exception oops) {
			Launcher.log.severe(oops.toString());
			Launcher.exit(context);
		}
	}

	// SpringBootServletInitializer interface ---------------------------------

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		Object root;
		ConfigurableApplicationContext context;

		ProfileHelper.setProfiles("production");
		super.onStartup(servletContext);
		root = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		assert root instanceof ConfigurableApplicationContext;
		context = (ConfigurableApplicationContext) root;
		FactoryHelper.initialise(context);

		Launcher.log.info("Running application (servlet server)");
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
		SpringApplicationBuilder result;

		result = builder.sources(Launcher.class);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected static Map<String, String> analyseArguments(final String[] argv) {
		Map<String, String> result;
		String[] validProfiles;
		String[] validActions;
		Options options;
		CommandLineParser parser;
		CommandLine commandLine;
		String profile;
		String action;

		result = new HashMap<>();
		result.put(PROFILE, "development");
		result.put(ACTION, RUN);

		validProfiles = new String[] {
			"development", "production"
		};
		validActions = new String[] {
				RUN, POPULATE_INITIAL, POPULATE_SAMPLE, INQUIRE_DATABASE
		};

		options = new Options();
		options.addOption("p", PROFILE, true, "sets the profile");
		options.addOption("a", ACTION, true, "performs an action");

		try {
			parser = new DefaultParser();
			commandLine = parser.parse(options, argv);
			profile = (String) commandLine.getParsedOptionValue("p");
			if (profile != null) {
				Assert.state(ArrayUtils.contains(validProfiles, profile), "Wrong profile");
				result.put(PROFILE, profile);
			}
			action = (String) commandLine.getParsedOptionValue("a");
			if (action != null) {
				Assert.state(ArrayUtils.contains(validActions, action), "Wrong action");
				result.put(ACTION, action);
			}
		} catch (final Exception oops) {
			Launcher.showUsage();
		}

		return result;
	}

	protected static void printClassPath() {
		ClassLoader loader;
		URL[] sources;

		loader = ClassLoader.getSystemClassLoader();
		sources = ((URLClassLoader) loader).getURLs();

		for (final URL url : sources) {
			Launcher.log.info("Class path = " + url.getFile());
		}
	}

	protected static void showUsage() {
		Launcher.log.severe("");
		Launcher.log.severe("Usage: launcher (--profile value)? (--action value)?");
		Launcher.log.severe("");
		Launcher.log.severe("Profile values:");
		Launcher.log.severe("development     sets the development profile (default)");
		Launcher.log.severe("production      sets the production profile");
		Launcher.log.severe("");
		Launcher.log.severe("Action values:");
		Launcher.log.severe("run              runs the system (default)");
		Launcher.log.severe("populate-initial populates the database with initial data");
		Launcher.log.severe("populate-sample  populates the database with sample data");
		Launcher.log.severe("inquire-database opens a shell to query the database using JPQL");
		Launcher.log.severe("");
		Launcher.log.severe("Note that populating the database requires creating the create/drop SQL scripts,");
		Launcher.log.severe("which is performed automatically.  Note, too, that populating the database with");
		Launcher.log.severe("sample data requires populating it with the initial data, which is also performed");
		Launcher.log.severe("automatically.");
		Launcher.log.severe("");
		System.exit(1);
	}

	protected static void setProfile(final Map<String, String> arguments) {
		assert arguments != null;

		String action;
		String baseProfile;
		String actionProfile;

		baseProfile = arguments.get(PROFILE);
		actionProfile = "default";

		action = arguments.get(ACTION);
		switch (action) {
		case "run":
			actionProfile = "default";
			break;
		case POPULATE_INITIAL:
		case POPULATE_SAMPLE:
			actionProfile = "populator";
			break;
		case INQUIRE_DATABASE:
			actionProfile = "inquirer";
			break;
		default:
			assert false;
		}
		ProfileHelper.setProfiles(baseProfile, actionProfile);
	}

	protected static void doExtraWork(final Map<String, String> arguments, final ConfigurableApplicationContext context) {
		assert arguments != null;
		assert context != null;

		String action;
		DatabasePopulator databasePopulator;
		DatabaseInquirer databaseInquirer;

		action = arguments.get(ACTION);
		switch (action) {
		case RUN:
			Launcher.log.info("Running application (standalone)");
			break;
		case POPULATE_INITIAL:
			Launcher.log.info("Populating (initial data)");
			databasePopulator = FactoryHelper.getBean(DatabasePopulator.class);
			databasePopulator.populateInitial();
			Launcher.exit(context);
			break;
		case POPULATE_SAMPLE:
			Launcher.log.info("Populating (sample data)");
			databasePopulator = FactoryHelper.getBean(DatabasePopulator.class);
			databasePopulator.populateSample();
			Launcher.exit(context);
			break;
		case INQUIRE_DATABASE:
			Launcher.log.info("Inquiring database");
			databaseInquirer = FactoryHelper.getBean(DatabaseInquirer.class);
			databaseInquirer.run();
			Launcher.exit(context);
			break;
		default:
			Launcher.showUsage();
		}
	}

	protected static void exit(final ApplicationContext context) {
		// context is nullable
		
		int status;

		try { Thread.sleep(1000); } catch (final Exception oops) { /* do nothing */ }
		status = (context == null ? 1 : SpringApplication.exit(context));		
		System.exit(status);
	}

}
