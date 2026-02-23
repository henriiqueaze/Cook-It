package com.p5Project.cookIt.mappers;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static ModelMapper mapper = new ModelMapper();

    static {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public static <O, D> D parseItem(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> void mapNonNullFields(O origin, D destination) {
        mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseItemsList(List<O> origin, Class<D> destination) {
        ArrayList<D> destinationList = new ArrayList<>();

        for (O o : origin) {
            destinationList.add(mapper.map(o, destination));
        }

        return destinationList;
    }
}
