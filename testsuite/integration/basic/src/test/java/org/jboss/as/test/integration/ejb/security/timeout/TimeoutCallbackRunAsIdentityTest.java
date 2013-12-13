/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.test.integration.ejb.security.timeout;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test that the run-as identity applies to the timeout callback method of an enterprise bean.
 * 
 * @author Matus Abaffy
 */
@RunWith(Arquillian.class)
public class TimeoutCallbackRunAsIdentityTest {

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class, "testTimeoutCallbackRunAsIdentity.war")
                .addPackage(TimeoutCallbackRunAsIdentityTest.class.getPackage());
    }

    @Test
    public void testTimeoutCallbackRunAsIdentity() throws NamingException {
        InitialContext ctx = new InitialContext();
        Alarm alarm = (Alarm) ctx.lookup("java:module/" + Alarm.class.getSimpleName());
        alarm.createTimer();
        Assert.assertTrue(Alarm.awaitTimerCall());
    }
}
