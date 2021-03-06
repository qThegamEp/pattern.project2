package com.qthegamep.pattern.project2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.qthegamep.pattern.project2.exception.runtime.JsonConverterServiceRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.StringConverterRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.XmlConverterServiceRuntimeException;
import com.qthegamep.pattern.project2.model.container.Error;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ConverterServiceImpl implements ConverterService {

    private ObjectMapper json;
    private XmlMapper xml;

    @Inject
    public ConverterServiceImpl(ObjectMapper json,
                                XmlMapper xml) {
        this.json = json;
        this.xml = xml;
    }

    @Override
    public <T> T fromJson(String entity, Class<T> modelClass) {
        try {
            return json.readValue(entity, modelClass);
        } catch (IOException e) {
            throw new JsonConverterServiceRuntimeException(e, Error.JSON_CONVERTER_ERROR);
        }
    }

    @Override
    public String toJson(Object model) {
        try {
            return json.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new JsonConverterServiceRuntimeException(e, Error.JSON_CONVERTER_ERROR);
        }
    }

    @Override
    public <T> T fromXml(String entity, Class<T> modelClass) {
        try {
            return xml.readValue(entity, modelClass);
        } catch (IOException e) {
            throw new XmlConverterServiceRuntimeException(e, Error.XML_CONVERTER_ERROR);
        }
    }

    @Override
    public String toXml(Object model) {
        try {
            return xml.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new XmlConverterServiceRuntimeException(e, Error.XML_CONVERTER_ERROR);
        }
    }

    @Override
    public String toString(InputStream inputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return bufferedReader.lines()
                    .parallel()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new StringConverterRuntimeException(e, Error.STRING_CONVERTER_ERROR);
        }
    }
}
