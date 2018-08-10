package com.winhxd.b2c.common.exception.support;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.exception.BusinessException;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 处理业务异常Decoder
 *
 * @author lixiaodong
 */
public class BusinessDecoder implements Decoder {
    private Decoder delegate;

    public BusinessDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        delegate = new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)));
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        Object obj = delegate.decode(response, type);
        if (obj instanceof ResponseResult) {
            ResponseResult result = (ResponseResult) obj;
            if (result.getCode() > 0) {
                throw new BusinessException(result.getCode());
            }
        }
        return obj;
    }
}
