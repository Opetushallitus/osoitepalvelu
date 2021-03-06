/*
 * Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter;

import java.io.Serializable;

import fi.ratamaa.dtoconverter.AbstractBaseDtoConverter;
import fi.ratamaa.dtoconverter.mapper.implementations.AnnotationResolvingDtoConversionMapper;
import fi.ratamaa.dtoconverter.mapper.implementations.CamelCaseResolvingDtoConversionMapper;
import fi.ratamaa.dtoconverter.mapper.implementations.MapDtoconversionMapper;
import fi.ratamaa.dtoconverter.mapper.implementations.ValidatorFeatureDtoConverterMapper;
import fi.ratamaa.dtoconverter.typeconverter.TypeConversionContainer;
import fi.ratamaa.dtoconverter.typeconverter.TypeConverterAdapter;

import org.joda.time.LocalDate;

/**
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:56 PM
 */
public abstract class AbstractDtoConverter extends AbstractBaseDtoConverter implements Serializable {
    private static final long serialVersionUID = 4587427351950818897L;

    protected void registerMappers(fi.ratamaa.dtoconverter.mapper.resolver.MappersContainer mappers) {
        mappers
            .add(new AnnotationResolvingDtoConversionMapper()).add(new CamelCaseResolvingDtoConversionMapper())
            .add(new MapDtoconversionMapper())
            .add(new ValidatorFeatureDtoConverterMapper(configuration().getValidationFactory()));
    }

    @Override
    protected void registerConverters(TypeConversionContainer conversions) {
        super.registerConverters(conversions);

        conversions.add(String.class, LocalDate.class, new TypeConverterAdapter<String, LocalDate>() {
            public LocalDate convert(String obj) {
                // uses ISODateTimeFormat.localDateParser() that covers SQL date format
                return LocalDate.parse(obj);
            }
        }).add(LocalDate.class, String.class, new TypeConverterAdapter<LocalDate, String>() {
            public String convert(LocalDate obj) {
                return obj.toString(); // ISO8601 format (yyyy-MM-dd)
            }
        });
    }
}
