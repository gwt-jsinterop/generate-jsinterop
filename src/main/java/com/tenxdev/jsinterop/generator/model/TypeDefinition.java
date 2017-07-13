package com.tenxdev.jsinterop.generator.model;

import java.util.Arrays;

public class TypeDefinition implements Definition {
    private String name;
    private String[] types;

    public TypeDefinition(String name, String[] types) {
        this.name = name;
        this.types = types;
    }

    public String[] getTypes() {
        return types;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isPartial() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeDefinition that = (TypeDefinition) o;

        if (!name.equals(that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(types);
        return result;
    }

    @Override
    public String toString() {
        return "\nTypeDefinition{" +
                "name='" + name + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
