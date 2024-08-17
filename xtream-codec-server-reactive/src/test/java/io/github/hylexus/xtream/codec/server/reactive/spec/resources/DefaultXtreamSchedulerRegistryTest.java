/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.codec.server.reactive.spec.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry.SCHEDULER_NAME_BLOCKING;
import static io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSchedulerRegistry.SCHEDULER_NAME_NON_BLOCKING;
import static org.junit.jupiter.api.Assertions.*;

class DefaultXtreamSchedulerRegistryTest {

    private DefaultXtreamSchedulerRegistry registry;
    private Scheduler defaultBlockingScheduler;
    private Scheduler defaultNonBlockingScheduler;

    @BeforeEach
    void setUp() {
        defaultBlockingScheduler = Schedulers.parallel();
        defaultNonBlockingScheduler = Schedulers.single();
        registry = new DefaultXtreamSchedulerRegistry(defaultNonBlockingScheduler, defaultBlockingScheduler);
    }

    @Test
    void testGetSchedulerShouldReturnDefaultNonBlockingScheduler() {
        assertSame(defaultNonBlockingScheduler, registry.defaultNonBlockingScheduler());
        assertEquals(2, registry.asMapView().size());
    }

    @Test
    void testGetSchedulerShouldReturnDefaultBlockingScheduler() {
        assertSame(defaultBlockingScheduler, registry.defaultBlockingScheduler());
        assertEquals(2, registry.asMapView().size());
    }

    @Test
    void testGetSchedulerNonRegisteredSchedulerShouldReturnEmpty() {
        assertEquals(Optional.empty(), registry.getScheduler("nonExistentScheduler"));
        assertEquals(Optional.empty(), registry.getScheduler("nonExistentScheduler2"));
        assertEquals(Optional.empty(), registry.getScheduler("nonExistentScheduler3"));
        assertEquals(2, registry.asMapView().size());
    }

    @Test
    void testRegisterSchedulerShouldRegisterSchedulerSuccessfully() {
        final String schedulerName = "testScheduler";
        final Scheduler scheduler = Schedulers.parallel();
        final Scheduler scheduler2 = Schedulers.single();
        assertTrue(registry.registerScheduler(schedulerName, scheduler));
        assertFalse(registry.registerScheduler(schedulerName, scheduler2));
        assertEquals(Optional.of(scheduler), registry.getScheduler(schedulerName));
        assertEquals(3, registry.asMapView().size());
    }

    @Test
    void testAsMapViewShouldReturnUnmodifiableMapView() {
        Map<String, Scheduler> view = registry.asMapView();
        assertThrows(UnsupportedOperationException.class, () -> view.put("test", Schedulers.parallel()));
        assertEquals(2, view.size());
    }


    @Test
    void testRegisteredSchedulersShouldBePresentInMapView() {
        assertTrue(registry.registerScheduler("testScheduler1", Schedulers.parallel()));
        assertFalse(registry.registerScheduler("testScheduler1", Schedulers.parallel()));
        assertTrue(registry.registerScheduler("testScheduler2", Schedulers.single()));
        assertFalse(registry.registerScheduler("testScheduler2", Schedulers.single()));
        Map<String, Scheduler> view = registry.asMapView();
        assertEquals(4, view.size());
        assertTrue(view.containsKey("testScheduler1"));
        assertTrue(view.containsKey("testScheduler2"));
    }


    @Test
    void testReactiveStyleExecutionOnNonBlockingScheduler() {
        AtomicBoolean executed = new AtomicBoolean(false);
        registry.defaultNonBlockingScheduler().schedule(() -> executed.set(true));
        assertEventuallyTrue(executed);
    }

    @Test
    void testReactiveStyleExecutionOnBlockingScheduler() {
        AtomicBoolean executed = new AtomicBoolean(false);
        registry.defaultBlockingScheduler().schedule(() -> executed.set(true));
        assertEventuallyTrue(executed);
    }

    @Test
    void testSchedulerShouldNotBeRemovedOnceRegistered() {
        final String schedulerName = "testScheduler";
        final Scheduler scheduler = Schedulers.parallel();

        registry.registerScheduler(schedulerName, scheduler);
        assertTrue(registry.getScheduler(schedulerName).isPresent());

        // Direct removal from map
        registry.schedulerMap.remove(schedulerName);
        assertTrue(registry.getScheduler(schedulerName).isEmpty());
    }

    @Test
    void testRemoveScheduler() {
        assertTrue(registry.registerScheduler("testScheduler", Schedulers.parallel()));
        assertEquals(3, registry.asMapView().size());

        assertTrue(registry.removeScheduler("testScheduler"));
        assertEquals(2, registry.asMapView().size());

        assertFalse(registry.removeScheduler("testScheduler"));
        assertEquals(2, registry.asMapView().size());

        assertFalse(registry.getScheduler("testScheduler").isPresent());

        assertThrows(UnsupportedOperationException.class, () -> registry.removeScheduler(SCHEDULER_NAME_BLOCKING));
        assertThrows(UnsupportedOperationException.class, () -> registry.removeScheduler(SCHEDULER_NAME_NON_BLOCKING));
        assertEquals(2, registry.asMapView().size());
    }

    private void assertEventuallyTrue(AtomicBoolean executed) {
        final long start = System.currentTimeMillis();
        while (!executed.get()) {
            if (System.currentTimeMillis() - start > 1000) {
                fail("Condition was not met within timeout");
            }
        }
    }
}
