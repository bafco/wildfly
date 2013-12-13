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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

@Stateless
@RunAs("alarm")
@PermitAll
public class Alarm {

    private static final CountDownLatch latch = new CountDownLatch(1);
    private static final int TIMER_TIMEOUT_TIME_MS = 100;
    private static final int TIMER_CALL_WAITING_S = 30;

    private static boolean runAsIdentityOK = false;

    @EJB
    private Bell bell;

    @Resource
    private SessionContext ctx;

    public void createTimer() {
        ctx.getTimerService().createTimer(TIMER_TIMEOUT_TIME_MS, null);
    }

    public static boolean awaitTimerCall() {
        try {
            latch.await(TIMER_CALL_WAITING_S, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return runAsIdentityOK;
    }

    @Timeout
    public void ejbTimeout(Timer timer) {
        bell.ring();

        runAsIdentityOK = true;
        latch.countDown();
    }
}
