package com.qthegamep.pattern.project2.service;

import com.qthegamep.pattern.project2.exception.CryptoServiceException;
import com.qthegamep.pattern.project2.model.ErrorType;
import com.qthegamep.pattern.project2.model.HashAlgorithm;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class CryptoServiceImpl implements CryptoService {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoServiceImpl.class);

    @Override
    public String encodeTo(String data, HashAlgorithm hashAlgorithm) throws CryptoServiceException {
        return encodeTo(data, hashAlgorithm, null);
    }

    @Override
    public String encodeTo(String data, HashAlgorithm hashAlgorithm, String requestId) throws CryptoServiceException {
        LOG.debug("Encode: {} Algorithm: {} RequestId: {}", data, hashAlgorithm.getAlgorithm(), requestId);
        if (HashAlgorithm.MD5.equals(hashAlgorithm)) {
            return encode(data, hashAlgorithm.getAlgorithm());
        } else {
            throw new CryptoServiceException(ErrorType.ENCODE_HASH_ALGORITHM_NOT_EXISTS_ERROR);
        }
    }

    private String encode(String data, String hashAlgorithm) throws CryptoServiceException {
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance(hashAlgorithm);
            md5MessageDigest.update(data.getBytes(), 0, data.length());
            return Hex.encodeHexString(md5MessageDigest.digest());
        } catch (Exception e) {
            throw new CryptoServiceException(e, ErrorType.ENCODE_HASH_ERROR);
        }
    }
}