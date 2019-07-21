package com.fasto.datamanager.model.converters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface Convertible<DtoClass extends Serializable> {
    DtoClass convert();

    static <DtoClass extends Serializable, ConvertibleClass extends Convertible<DtoClass>> List<DtoClass> convert(final List<ConvertibleClass> entities) {
        if (!Optional.ofNullable(entities).isPresent()) return new ArrayList<>();
        return entities.stream().map((c) -> c.convert()).collect(toList());
    }
}