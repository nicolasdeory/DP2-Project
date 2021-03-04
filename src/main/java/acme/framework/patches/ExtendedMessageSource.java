/*
 * ExtendedMessageSource.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

// HINT: The original source was patched by RC so that it accepts ANT-like patterns. This
// HINT+ saves a lot of work and mistakes when specifying the i18n message bundles. The
// HINT+ default extension of the bundles was changed to ".messages", which seems more
// HINT+ intuitive than the generic ".properties" extension. The ".xml" extension is not
// HINT+ supports at all. The changes are marked with RC+ or RC- blocks in the source code.

package acme.framework.patches;

/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.context.MessageSource;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.util.StringUtils;

import acme.framework.helpers.StringHelper;

/**
 * Spring-specific {@link org.springframework.context.MessageSource} implementation
 * that accesses resource bundles using specified basenames, participating in the
 * Spring {@link org.springframework.context.ApplicationContext}'s resource loading.
 *
 * <p>
 * In contrast to the JDK-based {@link ResourceBundleMessageSource}, this class uses
 * {@link java.util.Properties} instances as its custom data structure for messages,
 * loading them via a {@link org.springframework.util.PropertiesPersister} strategy
 * from Spring {@link Resource} handles. This strategy is not only capable of
 * reloading files based on timestamp changes, but also of loading properties files
 * with a specific character encoding. It will detect XML property files as well.
 *
 * <p>
 * Note that the basenames set as {@link #setBasenames "basenames"} property
 * are treated in a slightly different fashion than the "basenames" property of
 * {@link ResourceBundleMessageSource}. It follows the basic ResourceBundle rule of not
 * specifying file extension or language codes, but can refer to any Spring resource
 * location (instead of being restricted to classpath resources). With a "classpath:"
 * prefix, resources can still be loaded from the classpath, but "cacheSeconds" values
 * other than "-1" (caching forever) might not work reliably in this case.
 *
 * <p>
 * For a typical web application, message files could be placed in {@code WEB-INF}:
 * e.g. a "WEB-INF/messages" basename would find a "WEB-INF/messages.properties",
 * "WEB-INF/messages_en.properties" etc arrangement as well as "WEB-INF/messages.xml",
 * "WEB-INF/messages_en.xml" etc. Note that message definitions in a <i>previous</i>
 * resource bundle will override ones in a later bundle, due to sequential lookup.
 *
 * <p>
 * This MessageSource can easily be used outside of an
 * {@link org.springframework.context.ApplicationContext}: it will use a
 * {@link org.springframework.core.io.DefaultResourceLoader} as default,
 * simply getting overridden with the ApplicationContext's resource loader
 * if running in a context. It does not have any other specific dependencies.
 *
 * <p>
 * Thanks to Thomas Achleitner for providing the initial implementation of
 * this message source!
 *
 * @author Juergen Hoeller
 * @see #setCacheSeconds
 * @see #setBasenames
 * @see #setDefaultEncoding
 * @see #setFileEncodings
 * @see #setPropertiesPersister
 * @see #setResourceLoader
 * @see org.springframework.util.DefaultPropertiesPersister
 * @see org.springframework.core.io.DefaultResourceLoader
 * @see ResourceBundleMessageSource
 * @see java.util.ResourceBundle
 */
public class ExtendedMessageSource extends AbstractResourceBasedMessageSource implements ResourceLoaderAware {

	protected static final String										PROPERTIES_SUFFIX		= ".messages";

	// RC-
	// protected static final String										XML_SUFFIX				= ".xml";
	// RC-

	@Nullable
	protected Properties												fileEncodings;

	protected boolean													concurrentRefresh		= true;

	protected PropertiesPersister										propertiesPersister		= new DefaultPropertiesPersister();

	protected ResourceLoader											resourceLoader			= new DefaultResourceLoader();

	// Cache to hold filename lists per Locale
	protected final ConcurrentMap<String, Map<Locale, List<String>>>	cachedFilenames			= new ConcurrentHashMap<>();

	// Cache to hold already loaded properties per filename
	protected final ConcurrentMap<String, PropertiesHolder>				cachedProperties		= new ConcurrentHashMap<>();

	// Cache to hold already loaded properties per filename
	protected final ConcurrentMap<Locale, PropertiesHolder>				cachedMergedProperties	= new ConcurrentHashMap<>();


	/**
	 * Set per-file charsets to use for parsing properties files.
	 * <p>
	 * Only applies to classic properties files, not to XML files.
	 *
	 * @param fileEncodings
	 *            a Properties with filenames as keys and charset
	 *            names as values. Filenames have to match the basename syntax,
	 *            with optional locale-specific components: e.g. "WEB-INF/messages"
	 *            or "WEB-INF/messages_en".
	 * @see #setBasenames
	 * @see org.springframework.util.PropertiesPersister#load
	 */
	public void setFileEncodings(final Properties fileEncodings) {
		this.fileEncodings = fileEncodings;
	}

	/**
	 * Specify whether to allow for concurrent refresh behavior, i.e. one thread
	 * locked in a refresh attempt for a specific cached properties file whereas
	 * other threads keep returning the old properties for the time being, until
	 * the refresh attempt has completed.
	 * <p>
	 * Default is "true": this behavior is new as of Spring Framework 4.1,
	 * minimizing contention between threads. If you prefer the old behavior,
	 * i.e. to fully block on refresh, switch this flag to "false".
	 *
	 * @since 4.1
	 * @see #setCacheSeconds
	 */
	public void setConcurrentRefresh(final boolean concurrentRefresh) {
		this.concurrentRefresh = concurrentRefresh;
	}

	/**
	 * Set the PropertiesPersister to use for parsing properties files.
	 * <p>
	 * The default is a DefaultPropertiesPersister.
	 *
	 * @see org.springframework.util.DefaultPropertiesPersister
	 */
	public void setPropertiesPersister(@Nullable final PropertiesPersister propertiesPersister) {
		this.propertiesPersister = propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister();
	}

	/**
	 * Set the ResourceLoader to use for loading bundle properties files.
	 * <p>
	 * The default is a DefaultResourceLoader. Will get overridden by the
	 * ApplicationContext if running in a context, as it implements the
	 * ResourceLoaderAware interface. Can be manually overridden when
	 * running outside of an ApplicationContext.
	 *
	 * @see org.springframework.core.io.DefaultResourceLoader
	 * @see org.springframework.context.ResourceLoaderAware
	 */
	@Override
	public void setResourceLoader(@Nullable final ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader != null ? resourceLoader : new DefaultResourceLoader();
	}

	/**
	 * Resolves the given message code as key in the retrieved bundle files,
	 * returning the value found in the bundle as-is (without MessageFormat parsing).
	 */
	@Override
	protected String resolveCodeWithoutArguments(final String code, final Locale locale) {
		if (this.getCacheMillis() < 0) {
			final PropertiesHolder propHolder = this.getMergedProperties(locale);
			final String result = propHolder.getProperty(code);
			if (result != null) {
				return result;
			}
		} else {
			for (final String basename : this.getBasenameSet()) {
				final List<String> filenames = this.calculateAllFilenames(basename, locale);
				for (final String filename : filenames) {
					final PropertiesHolder propHolder = this.getProperties(filename);
					final String result = propHolder.getProperty(code);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Resolves the given message code as key in the retrieved bundle files,
	 * using a cached MessageFormat instance per message code.
	 */
	@Override
	@Nullable
	protected MessageFormat resolveCode(final String code, final Locale locale) {
		if (this.getCacheMillis() < 0) {
			final PropertiesHolder propHolder = this.getMergedProperties(locale);
			final MessageFormat result = propHolder.getMessageFormat(code, locale);
			if (result != null) {
				return result;
			}
		} else {
			for (final String basename : this.getBasenameSet()) {
				final List<String> filenames = this.calculateAllFilenames(basename, locale);
				for (final String filename : filenames) {
					final PropertiesHolder propHolder = this.getProperties(filename);
					final MessageFormat result = propHolder.getMessageFormat(code, locale);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get a PropertiesHolder that contains the actually visible properties
	 * for a Locale, after merging all specified resource bundles.
	 * Either fetches the holder from the cache or freshly loads it.
	 * <p>
	 * Only used when caching resource bundle contents forever, i.e.
	 * with cacheSeconds < 0. Therefore, merged properties are always
	 * cached forever.
	 */
	protected PropertiesHolder getMergedProperties(final Locale locale) {
		PropertiesHolder mergedHolder = this.cachedMergedProperties.get(locale);
		if (mergedHolder != null) {
			return mergedHolder;
		}
		final Properties mergedProps = this.newProperties();
		long latestTimestamp = -1;
		final String[] basenames = StringUtils.toStringArray(this.getBasenameSet());
		for (int i = basenames.length - 1; i >= 0; i--) {
			final List<String> filenames = this.calculateAllFilenames(basenames[i], locale);
			for (int j = filenames.size() - 1; j >= 0; j--) {
				final String filename = filenames.get(j);
				final PropertiesHolder propHolder = this.getProperties(filename);
				if (propHolder.getProperties() != null) {
					mergedProps.putAll(propHolder.getProperties());
					if (propHolder.getFileTimestamp() > latestTimestamp) {
						latestTimestamp = propHolder.getFileTimestamp();
					}
				}
			}
		}
		mergedHolder = new PropertiesHolder(mergedProps, latestTimestamp);
		final PropertiesHolder existing = this.cachedMergedProperties.putIfAbsent(locale, mergedHolder);
		if (existing != null) {
			mergedHolder = existing;
		}
		return mergedHolder;
	}

	/**
	 * Calculate all filenames for the given bundle basename and Locale.
	 * Will calculate filenames for the given Locale, the system Locale
	 * (if applicable), and the default file.
	 *
	 * @param basename
	 *            the basename of the bundle
	 * @param locale
	 *            the locale
	 * @return the List of filenames to check
	 * @see #setFallbackToSystemLocale
	 * @see #calculateFilenamesForLocale
	 */
	// RC-
	//	protected List<String> calculateAllFilenames(final String basename, final Locale locale) {
	//		Map<Locale, List<String>> localeMap = this.cachedFilenames.get(basename);
	//		if (localeMap != null) {
	//			List<String> filenames = localeMap.get(locale);
	//			if (filenames != null) {
	//				return filenames;
	//			}
	//		}
	//		List<String> filenames = new ArrayList<>(7);
	//		filenames.addAll(this.calculateFilenamesForLocale(basename, locale));
	//		if (this.isFallbackToSystemLocale() && !locale.equals(Locale.getDefault())) {
	//			List<String> fallbackFilenames = this.calculateFilenamesForLocale(basename, Locale.getDefault());
	//			for (String fallbackFilename : fallbackFilenames) {
	//				if (!filenames.contains(fallbackFilename)) {
	//					// Entry for fallback locale that isn't already in filenames list.
	//					filenames.add(fallbackFilename);
	//				}
	//			}
	//		}
	//		filenames.add(basename);
	//		if (localeMap == null) {
	//			localeMap = new ConcurrentHashMap<>();
	//			Map<Locale, List<String>> existing = this.cachedFilenames.putIfAbsent(basename, localeMap);
	//			if (existing != null) {
	//				localeMap = existing;
	//			}
	//		}
	//		localeMap.put(locale, filenames);
	//		return filenames;
	//	}
	// RC-

	// RC+
	@SuppressWarnings("deprecation")
	protected List<String> calculateAllFilenames(final String basename, final Locale locale) {
		List<String> result;
		Map<Locale, List<String>> localeMap, existing;
		Locale defaultLocale;
		String path;
		List<String> defaults;

		result = null;
		localeMap = this.cachedFilenames.get(basename);
		if (localeMap != null) {
			result = localeMap.get(locale);
		}

		if (result == null) {
			defaultLocale = Locale.getDefault();

			path = basename;
			if (path.endsWith(ExtendedMessageSource.PROPERTIES_SUFFIX)) {
				path = path.replace(ExtendedMessageSource.PROPERTIES_SUFFIX, "");
			}

			result = this.computeFilenames(path, locale);
			if (this.isFallbackToSystemLocale() && !locale.equals(defaultLocale)) {
				defaults = this.computeFilenames(path, defaultLocale);
				result.addAll(defaults);
			}

			if (localeMap == null) {
				localeMap = new ConcurrentHashMap<>();
				existing = this.cachedFilenames.putIfAbsent(basename, localeMap);
				if (existing != null) {
					localeMap = existing;
				}
			}

			localeMap.put(locale, result);
		}

		return result;
	}

	protected List<String> computeFilenames(final String path, final Locale locale) {
		List<String> result;
		List<String> suffixes;
		String localisedPath;

		result = new ArrayList<String>();
		suffixes = this.computeSuffixes(locale);
		for (final String suffix : suffixes) {
			localisedPath = String.format("%s%s", path, suffix);
			result.add(localisedPath);
		}

		return result;
	}

	protected List<String> computeSuffixes(final Locale locale) {
		List<String> result;
		String language, country, variant;
		StringBuilder builder;

		result = new ArrayList<String>();

		language = locale.getLanguage();
		country = locale.getCountry();
		variant = locale.getVariant();

		builder = new StringBuilder();

		if (!language.isEmpty()) {
			builder.append(String.format("_%s", language));
			result.add(0, builder.toString());
		}

		if (!country.isEmpty()) {
			builder.append(String.format("_%s", country));
			result.add(0, builder.toString());
		}

		if (!variant.isEmpty()) {
			builder.append(String.format("_%s", variant));
			result.add(0, builder.toString());
		}

		return result;
	}
	// RC+

	// RC-
	//	/**
	//	 * Calculate the filenames for the given bundle basename and Locale,
	//	 * appending language code, country code, and variant code.
	//	 * E.g.: basename "messages", Locale "de_AT_oo" -> "messages_de_AT_OO",
	//	 * "messages_de_AT", "messages_de".
	//	 * <p>
	//	 * Follows the rules defined by {@link java.util.Locale#toString()}.
	//	 *
	//	 * @param basename
	//	 *            the basename of the bundle
	//	 * @param locale
	//	 *            the locale
	//	 * @return the List of filenames to check
	//	 */

	//	protected List<String> calculateFilenamesForLocale(final String basename, final Locale locale) {
	//		List<String> result = new ArrayList<>(3);
	//		String language = locale.getLanguage();
	//		String country = locale.getCountry();
	//		String variant = locale.getVariant();
	//		StringBuilder temp = new StringBuilder(basename);
	//
	//		temp.append('_');
	//		if (language.length() > 0) {
	//			temp.append(language);
	//			result.add(0, temp.toString());
	//		}
	//
	//		temp.append('_');
	//		if (country.length() > 0) {
	//			temp.append(country);
	//			result.add(0, temp.toString());
	//		}
	//
	//		if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
	//			temp.append('_').append(variant);
	//			result.add(0, temp.toString());
	//		}
	//
	//		return result;
	//	}
	// RC-

	/**
	 * Get a PropertiesHolder for the given filename, either from the
	 * cache or freshly loaded.
	 *
	 * @param filename
	 *            the bundle filename (basename + Locale)
	 * @return the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder getProperties(final String filename) {
		PropertiesHolder propHolder = this.cachedProperties.get(filename);
		long originalTimestamp = -2;

		if (propHolder != null) {
			originalTimestamp = propHolder.getRefreshTimestamp();
			if (originalTimestamp == -1 || originalTimestamp > System.currentTimeMillis() - this.getCacheMillis()) {
				// Up to date
				return propHolder;
			}
		} else {
			propHolder = new PropertiesHolder();
			final PropertiesHolder existingHolder = this.cachedProperties.putIfAbsent(filename, propHolder);
			if (existingHolder != null) {
				propHolder = existingHolder;
			}
		}

		// At this point, we need to refresh...
		if (this.concurrentRefresh && propHolder.getRefreshTimestamp() >= 0) {
			// A populated but stale holder -> could keep using it.
			if (!propHolder.refreshLock.tryLock()) {
				// Getting refreshed by another thread already ->
				// let's return the existing properties for the time being.
				return propHolder;
			}
		} else {
			propHolder.refreshLock.lock();
		}
		try {
			final PropertiesHolder existingHolder = this.cachedProperties.get(filename);
			if (existingHolder != null && existingHolder.getRefreshTimestamp() > originalTimestamp) {
				return existingHolder;
			}
			return this.refreshProperties(filename, propHolder);
		} finally {
			propHolder.refreshLock.unlock();
		}
	}


	// RC+
	protected PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();


	protected PropertiesHolder refreshProperties(final String filename, final PropertiesHolder oldHolder) {
		assert !StringHelper.isBlank(filename);
		assert oldHolder != null;

		PropertiesHolder result;
		Properties properties;
		long lastModified;
		Resource[] resources;
		String basename, path;
		PropertiesHolder newHolder;

		result = null;
		properties = new Properties();
		lastModified = -1;
		try {
			//basename = String.format("classpath:%s", filename +  ExtendedMessageSource.PROPERTIES_SUFFIX);
			basename = filename + ExtendedMessageSource.PROPERTIES_SUFFIX;
			//this.logger.info("user.dir = " + System.getProperty("user.dir"));
			//this.logger.info("basename = " + basename);
			resources = this.resolver.getResources(basename);
			for (final Resource resource : resources) {
				path = resource.getURI().toString().replace(ExtendedMessageSource.PROPERTIES_SUFFIX, "");
				//this.logger.info("resource = " + path);
				newHolder = this.originalRefreshProperties(path, oldHolder);
				properties.putAll(newHolder.getProperties());
				if (lastModified < resource.lastModified()) {
					lastModified = resource.lastModified();
				}
			}
			result = new PropertiesHolder(properties, lastModified);
		} catch (final IOException oops) {
			throw new RuntimeException(oops);
		}

		assert result != null;

		return result;
	}
	// RC+

	/**
	 * Refresh the PropertiesHolder for the given bundle filename.
	 * The holder can be {@code null} if not cached before, or a timed-out cache entry
	 * (potentially getting re-validated against the current last-modified timestamp).
	 *
	 * @param filename
	 *            the bundle filename (basename + Locale)
	 * @param propHolder
	 *            the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder originalRefreshProperties(final String filename, @Nullable PropertiesHolder propHolder) {
		final long refreshTimestamp = this.getCacheMillis() < 0 ? -1 : System.currentTimeMillis();
		final Resource resource = this.resourceLoader.getResource(filename + ExtendedMessageSource.PROPERTIES_SUFFIX);

		// RC-
		//		if (!resource.exists()) {
		//			resource = this.resourceLoader.getResource(filename + ExtendedMessageSource.XML_SUFFIX);
		//		}
		// RC-

		if (resource.exists()) {
			long fileTimestamp = -1;
			if (this.getCacheMillis() >= 0) {
				// Last-modified timestamp of file will just be read if caching with timeout.
				try {
					fileTimestamp = resource.lastModified();
					if (propHolder != null && propHolder.getFileTimestamp() == fileTimestamp) {
						if (this.logger.isDebugEnabled()) {
							this.logger.debug("Re-caching properties for filename [" + filename + "] - file hasn't been modified");
						}
						propHolder.setRefreshTimestamp(refreshTimestamp);
						return propHolder;
					}
				} catch (final IOException ex) {
					// Probably a class path resource: cache it forever.
					if (this.logger.isDebugEnabled()) {
						this.logger.debug(resource + " could not be resolved in the file system - assuming that it hasn't changed", ex);
					}
					fileTimestamp = -1;
				}
			}
			try {
				final Properties props = this.loadProperties(resource, filename);
				propHolder = new PropertiesHolder(props, fileTimestamp);
			} catch (final IOException ex) {
				if (this.logger.isWarnEnabled()) {
					this.logger.warn("Could not parse properties file [" + resource.getFilename() + "]", ex);
				}
				// Empty holder representing "not valid".
				propHolder = new PropertiesHolder();
			}
		}
		else {
			// Resource does not exist.
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
			}
			// Empty holder representing "not found".
			propHolder = new PropertiesHolder();
		}

		propHolder.setRefreshTimestamp(refreshTimestamp);
		this.cachedProperties.put(filename, propHolder);
		return propHolder;
	}

	/**
	 * Load the properties from the given resource.
	 *
	 * @param resource
	 *            the resource to load from
	 * @param filename
	 *            the original bundle filename (basename + Locale)
	 * @return the populated Properties instance
	 * @throws IOException
	 *             if properties loading failed
	 */
	protected Properties loadProperties(final Resource resource, final String filename) throws IOException {
		final Properties props = this.newProperties();
		try (InputStream is = resource.getInputStream()) {
			// RC-
			// String resourceFilename = resource.getFilename();
			// if (resourceFilename != null && resourceFilename.endsWith(ExtendedMessageSource.XML_SUFFIX)) {
			//     if (this.logger.isDebugEnabled()) {
			//         this.logger.debug("Loading properties [" + resource.getFilename() + "]");
			//     }
			//     this.propertiesPersister.loadFromXml(props, is);
			// } else
			// RC-
			// RC+
			String encoding = null;

			if (this.fileEncodings != null) {
				encoding = this.fileEncodings.getProperty(filename);
			}
			if (encoding == null) {
				encoding = this.getDefaultEncoding();
			}
			if (encoding != null) {
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Loading properties [" + resource.getFilename() + "] with encoding '" + encoding + "'");
				}
				this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
			} else {
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Loading properties [" + resource.getFilename() + "]");
				}
				this.propertiesPersister.load(props, is);
			}
			// RC+

			return props;
		}
	}

	/**
	 * Template method for creating a plain new {@link Properties} instance.
	 * The default implementation simply calls {@link Properties#Properties()}.
	 * <p>
	 * Allows for returning a custom {@link Properties} extension in subclasses.
	 * Overriding methods should just instantiate a custom {@link Properties} subclass,
	 * with no further initialization or population to be performed at that point.
	 *
	 * @return a plain Properties instance
	 * @since 4.2
	 */
	protected Properties newProperties() {
		return new Properties();
	}

	/**
	 * Clear the resource bundle cache.
	 * Subsequent resolve calls will lead to reloading of the properties files.
	 */
	public void clearCache() {
		this.logger.debug("Clearing entire resource bundle cache");
		this.cachedProperties.clear();
		this.cachedMergedProperties.clear();
	}

	/**
	 * Clear the resource bundle caches of this MessageSource and all its ancestors.
	 *
	 * @see #clearCache
	 */
	public void clearCacheIncludingAncestors() {
		this.clearCache();
		// RC-
		// if (this.getParentMessageSource() instanceof ReloadableResourceBundleMessageSource) {
		// 	((ReloadableResourceBundleMessageSource) this.getParentMessageSource()).clearCacheIncludingAncestors();
		// }
		// RC-
		// RC+
		MessageSource parent;
		final ReloadableResourceBundleMessageSource reloadableParent;

		parent = this.getParentMessageSource();
		if (parent instanceof ReloadableResourceBundleMessageSource) {
			reloadableParent = (ReloadableResourceBundleMessageSource) (this.getParentMessageSource());
			// INFO: SonarLint makes a mistake here.  reloadableParent is not nullable here because
			// INFO+ parent instanceof ReloadableResourceBundleMessageSource returns false if parent is null.
			assert reloadableParent != null; 
			reloadableParent.clearCacheIncludingAncestors();
		}

	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": basenames=" + this.getBasenameSet();
	}


	/**
	 * PropertiesHolder for caching.
	 * Stores the last-modified timestamp of the source file for efficient
	 * change detection, and the timestamp of the last refresh attempt
	 * (updated every time the cache entry gets re-validated).
	 */
	protected class PropertiesHolder {

		@Nullable
		protected final Properties											properties;

		protected final long												fileTimestamp;

		protected volatile long												refreshTimestamp		= -2;

		protected final ReentrantLock										refreshLock				= new ReentrantLock();

		/** Cache to hold already generated MessageFormats per message code. */
		protected final ConcurrentMap<String, Map<Locale, MessageFormat>>	cachedMessageFormats	= new ConcurrentHashMap<>();


		public PropertiesHolder() {
			this.properties = null;
			this.fileTimestamp = -1;
		}

		public PropertiesHolder(final Properties properties, final long fileTimestamp) {
			this.properties = properties;
			this.fileTimestamp = fileTimestamp;
		}

		@Nullable
		public Properties getProperties() {
			return this.properties;
		}

		public long getFileTimestamp() {
			return this.fileTimestamp;
		}

		public void setRefreshTimestamp(final long refreshTimestamp) {
			this.refreshTimestamp = refreshTimestamp;
		}

		public long getRefreshTimestamp() {
			return this.refreshTimestamp;
		}

		@Nullable
		public String getProperty(final String code) {
			if (this.properties == null) {
				return null;
			}
			return this.properties.getProperty(code);
		}

		@Nullable
		public MessageFormat getMessageFormat(final String code, final Locale locale) {
			if (this.properties == null) {
				return null;
			}
			Map<Locale, MessageFormat> localeMap = this.cachedMessageFormats.get(code);
			if (localeMap != null) {
				final MessageFormat result = localeMap.get(locale);
				if (result != null) {
					return result;
				}
			}
			final String msg = this.properties.getProperty(code);
			if (msg != null) {
				if (localeMap == null) {
					localeMap = new ConcurrentHashMap<>();
					final Map<Locale, MessageFormat> existing = this.cachedMessageFormats.putIfAbsent(code, localeMap);
					if (existing != null) {
						localeMap = existing;
					}
				}
				final MessageFormat result = ExtendedMessageSource.this.createMessageFormat(msg, locale);
				localeMap.put(locale, result);
				return result;
			}
			return null;
		}
	}

}
