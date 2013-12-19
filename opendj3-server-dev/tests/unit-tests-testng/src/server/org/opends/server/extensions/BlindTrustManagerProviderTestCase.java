/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2006-2008 Sun Microsystems, Inc.
 */
package org.opends.server.extensions;



import java.security.cert.X509Certificate;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.opends.server.TestCaseUtils;

import static org.testng.Assert.*;



/**
 * A set of test cases for the blind trust manager provider.
 */
public class BlindTrustManagerProviderTestCase
       extends ExtensionsTestCase
{
  /**
   * Ensures that the Directory Server is running.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @BeforeClass()
  public void startServer()
         throws Exception
  {
    TestCaseUtils.startServer();
  }



  /**
   * Tests the blind trust manager provider by creating a new instance,
   * initializing it, and getting the trust managers and issuers.  In this case,
   * since we know that all certificates will always be trusted then we can also
   * invoke the checkClientTrusted and checkServerTrusted methods with empty
   * certificate chains.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testBlindTrustManagerProvider()
         throws Exception
  {
    BlindTrustManagerProvider provider = new BlindTrustManagerProvider();
    provider.initializeTrustManagerProvider(null);
    assertNotNull(provider.getTrustManagers());
    assertNotNull(provider.getAcceptedIssuers());

    provider.checkClientTrusted(new X509Certificate[0], "");
    provider.checkServerTrusted(new X509Certificate[0], "");

    provider.finalizeTrustManagerProvider();
  }
}

