/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0 
 *******************************************************************************/
package org.osgi.test.cases.jaxrs.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.service.jaxrs.client.SseEventSourceFactory;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.test.cases.jaxrs.resources.SseResource;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.service.ServiceExtension;
import org.osgi.util.promise.Promise;

@ExtendWith(ServiceExtension.class)
public class SSETestCase extends AbstractJAXRSTestCase {
	
	/**
	 * A basic test that ensures the SseEventSourceFactory is registered as a
	 * service and that it can be used to get a stream of data (151.9.3)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJaxRsSseEventSourceFactory(@InjectService(timeout = 2000)
	ClientBuilder clientBuilder, @InjectService(timeout = 2000)
	SseEventSourceFactory sseFactory) throws Exception {

		Client c = clientBuilder.build();

		String baseURI = getBaseURI();

		WebTarget target = c.target(baseURI).path("whiteboard/stream");

		// Register a whiteboard service
		Dictionary<String,Object> properties = new Hashtable<>();
		properties.put(JaxrsWhiteboardConstants.JAX_RS_RESOURCE, Boolean.TRUE);

		Promise<Void> awaitSelection = helper.awaitModification(runtime, 5000);

		context.registerService(SseResource.class,
				new SseResource(MediaType.TEXT_PLAIN_TYPE), properties);

		awaitSelection.getValue();

		// Get the SSE stream
		AtomicReference<Throwable> ref = new AtomicReference<Throwable>();
		List<Integer> list = new CopyOnWriteArrayList<>();
		Semaphore s = new Semaphore(0);

		SseEventSource source = sseFactory.newSource(target);

		source.register(e -> list.add(e.readData(Integer.class)),
				t -> ref.set(t), s::release);

		source.open();

		assertTrue(s.tryAcquire(10, TimeUnit.SECONDS));

		assertNull(ref.get());

		assertEquals(Arrays.asList(42, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list);
		
	}
}
