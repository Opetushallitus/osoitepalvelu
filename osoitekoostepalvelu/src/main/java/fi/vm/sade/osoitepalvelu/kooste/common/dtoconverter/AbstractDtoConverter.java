package fi.vm.sade.osoitepalvelu.kooste.common.dtoconverter;

import fi.ratamaa.dtoconverter.AbstractBaseDtoConverter;
import fi.ratamaa.dtoconverter.mapper.implementations.AnnotationResolvingDtoConversionMapper;
import fi.ratamaa.dtoconverter.mapper.implementations.CamelCaseResolvingDtoConversionMapper;
import fi.ratamaa.dtoconverter.mapper.implementations.MapDtoconversionMapper;
import fi.ratamaa.dtoconverter.mapper.implementations.ValidatorFeatureDtoConverterMapper;
import fi.ratamaa.dtoconverter.mapper.implementations.proxy.ProxyObjectDtoConversionMapper;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDtoConverter extends AbstractBaseDtoConverter {
    protected void registerMappers(fi.ratamaa.dtoconverter.mapper.resolver.MappersContainer mappers) {
        mappers
                .add( new ProxyObjectDtoConversionMapper(getProxyMappers()))
                .add( new AnnotationResolvingDtoConversionMapper() )
                .add( new CamelCaseResolvingDtoConversionMapper())
                .add( new MapDtoconversionMapper() )
                .add( new ValidatorFeatureDtoConverterMapper( configuration().getValidationFactory() ) );
    }
}
