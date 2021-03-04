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
import acme.framework.helpers.ThrowableHelper;
import acme.framework.utilities.DatabaseInquirer;
import acme.framework.utilities.DatabasePopulator;
import lombok.extern.java.Log;

@SpringBootApplication(scanBasePackages = "acme")
@Log
public class Launcher extends SpringBootServletInitializer {

	// Command-line entry point -----------------------------------------------

	public static void main(final String[] argv) {
		Map<String, String> arguments;
		ConfigurableApplicationContext context;

		context = null;
		try {
			// Launcher.printClassPath();
			arguments = Launcher.analyseArguments(argv);
			Launcher.setProfile(arguments);
			context = SpringApplication.run(Launcher.class, argv);
			FactoryHelper.initialise(context);
			Launcher.doExtraWork(arguments, context);
		} catch (final Throwable oops) {
			ThrowableHelper.print(System.err, oops);
			Launcher.exit(context);
		}
	}

	// SpringBootServletInitializer interface ---------------------------------

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		Object root;
		ConfigurableApplicationContext context;

		// Launcher.printClassPath();

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
		String[] validProfiles, validActions;
		Options options;
		CommandLineParser parser;
		CommandLine commandLine;
		String profile, action;

		result = new HashMap<String, String>();
		result.put("profile", "development");
		result.put("action", "run");

		validProfiles = new String[] {
			"development", "production"
		};
		validActions = new String[] {
			"run", "populate-initial", "populate-sample", "inquire-database"
		};

		options = new Options();
		options.addOption("p", "profile", true, "sets the profile");
		options.addOption("a", "action", true, "performs an action");

		try {
			parser = new DefaultParser();
			commandLine = parser.parse(options, argv);
			profile = (String) commandLine.getParsedOptionValue("p");
			if (profile != null) {
				Assert.state(ArrayUtils.contains(validProfiles, profile), "Wrong profile");
				result.put("profile", profile);
			}
			action = (String) commandLine.getParsedOptionValue("a");
			if (action != null) {
				Assert.state(ArrayUtils.contains(validActions, action), "Wrong action");
				result.put("action", action);
			}
		} catch (final Throwable oops) {
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
		System.err.println("");
		System.err.println("Usage: launcher (--profile value)? (--action value)?");
		System.err.println("");
		System.err.println("Profile values:");
		System.err.println("development     sets the development profile (default)");
		System.err.println("production      sets the production profile");
		System.err.println("");
		System.err.println("Action values:");
		System.err.println("run              runs the system (default)");
		System.err.println("populate-initial populates the database with initial data");
		System.err.println("populate-sample  populates the database with sample data");
		System.err.println("inquire-database opens a shell to query the database using JPQL");
		System.err.println("");
		System.err.println("Note that populating the database requires creating the create/drop SQL scripts,");
		System.err.println("which is performed automatically.  Note, too, that populating the database with");
		System.err.println("sample data requires populating it with the initial data, which is also performed");
		System.err.println("automatically.");
		System.err.println("");
		System.exit(1);
	}

	protected static void setProfile(final Map<String, String> arguments) {
		assert arguments != null;

		String action;
		String baseProfile, actionProfile;

		baseProfile = arguments.get("profile");
		actionProfile = "default";

		action = arguments.get("action");
		switch (action) {
		case "run":
			actionProfile = "default";
			break;
		case "populate-initial":
		case "populate-sample":
			actionProfile = "populator";
			break;
		case "inquire-database":
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

		action = arguments.get("action");
		switch (action) {
		case "run":
			System.out.println("Running application (standalone)");
			break;
		case "populate-initial":
			System.out.println("Populating (initial data)");
			databasePopulator = FactoryHelper.getBean(DatabasePopulator.class);
			databasePopulator.populateInitial();
			Launcher.exit(context);
			break;
		case "populate-sample":
			System.out.println("Populating (sample data)");
			databasePopulator = FactoryHelper.getBean(DatabasePopulator.class);
			databasePopulator.populateSample();
			Launcher.exit(context);
			break;
		case "inquire-database":
			System.out.println("Inquiring database");
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
		
		try { Thread.sleep(1000); } catch (final Throwable oops) { }
		status = (context == null ? 1 : SpringApplication.exit(context));		
		System.exit(status);
	}

}
