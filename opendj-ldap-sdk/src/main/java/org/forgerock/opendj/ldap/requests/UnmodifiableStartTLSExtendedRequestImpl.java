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
 *      Copyright 2010 Sun Microsystems, Inc.
 */

package org.forgerock.opendj.ldap.requests;

import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.forgerock.opendj.ldap.responses.ExtendedResult;

/**
 * Unmodifiable start TLS extended request implementation.
 */
final class UnmodifiableStartTLSExtendedRequestImpl extends
        AbstractUnmodifiableExtendedRequest<StartTLSExtendedRequest, ExtendedResult> implements
        StartTLSExtendedRequest {
    UnmodifiableStartTLSExtendedRequestImpl(StartTLSExtendedRequest impl) {
        super(impl);
    }

    public SSLContext getSSLContext() {
        return impl.getSSLContext();
    }

    public StartTLSExtendedRequest addEnabledProtocol(String... protocols) {
        throw new UnsupportedOperationException();
    }

    public StartTLSExtendedRequest addEnabledCipherSuite(String... suites) {
        throw new UnsupportedOperationException();
    }

    public List<String> getEnabledProtocols() {
        return Collections.unmodifiableList(impl.getEnabledProtocols());
    }

    public List<String> getEnabledCipherSuites() {
        return Collections.unmodifiableList(impl.getEnabledCipherSuites());
    }

    public StartTLSExtendedRequest setSSLContext(SSLContext sslContext) {
        throw new UnsupportedOperationException();
    }
}
