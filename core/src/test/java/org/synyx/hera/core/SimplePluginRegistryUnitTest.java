/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.synyx.hera.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link SimplePluginRegistry}.
 * 
 * @author Oliver Gierke
 */
public class SimplePluginRegistryUnitTest extends AbstractMutablePluginRegistryUnitTest {

	SamplePlugin plugin;

	SimplePluginRegistry<SamplePlugin, String> registry;

	/**
	 * Initializes a {@code PluginRegistry} and equips it with an {@code EmailNotificationProvider}.
	 */
	@Before
	public void setUp() {

		plugin = new SamplePluginImplementation();
		registry = SimplePluginRegistry.create();
	}

	/*
	 * (non-Javadoc)
	 * @see org.synyx.hera.core.AbstractMutablePluginRegistryUnitTest#getRegistry()
	 */
	@Override
	protected MutablePluginRegistry<SamplePlugin, String> getRegistry() {

		return SimplePluginRegistry.create();
	}

	/**
	 * Asserts that the registry contains the plugin it was initialized with.
	 * 
	 * @throws Exception
	 */
	@Test
	public void assertRegistryInitialized() throws Exception {

		registry.addPlugin(plugin);

		assertThat(registry.countPlugins(), is(1));
		assertTrue(registry.contains(plugin));
	}

	/**
	 * Asserts asking for a plugin with the {@code PluginMetadata} provided by the {@link EmailNotificationProvider}.
	 */
	@Test
	public void assertFindsEmailNotificationProvider() {

		registry.addPlugin(plugin);

		String delimiter = "FOO";

		List<SamplePlugin> plugins = registry.getPluginsFor(delimiter);
		assertThat(plugins, is(notNullValue()));
		assertThat(plugins.size(), is(1));

		SamplePlugin provider = plugins.get(0);
		assertThat(provider, is(instanceOf(SamplePluginImplementation.class)));
	}

	/**
	 * Expects the given exception to be thrown if no {@link Plugin} found.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void throwsExceptionIfNoPluginFound() {

		registry.getPluginFor("BAR", new IllegalArgumentException());
	}

	/**
	 * Expects the given exception to be thrown if no {@link Plugin}s found.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void throwsExceptionIfNoPluginsFound() {

		registry.getPluginsFor("BAR", new IllegalArgumentException());
	}

	/**
	 * Expect the defualt plugin to be returned if none found.
	 */
	@Test
	public void returnsDefaultIfNoneFound() {

		SamplePlugin defaultPlugin = new SamplePluginImplementation();

		assertThat(registry.getPluginFor("BAR", defaultPlugin), is(defaultPlugin));
	}

	/**
	 * Expect the given default plugins to be returned if none found.
	 */
	@Test
	public void returnsDefaultsIfNoneFound() {

		List<? extends SamplePlugin> defaultPlugins = Arrays.asList(new SamplePluginImplementation());

		List<SamplePlugin> result = registry.getPluginsFor("BAR", defaultPlugins);
		assertTrue(result.containsAll(defaultPlugins));
	}

	@Test
	public void handlesAddingNullPluginsCorrecty() throws Exception {

		registry.addPlugin(null);

		assertThat(registry.countPlugins(), is(0));
	}
}
