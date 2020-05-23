package org.Query.Columnar.SM.cert;

import java.math.BigInteger;

public interface CertSNAllocator {
    BigInteger incrementAndGet() throws Exception;
}
