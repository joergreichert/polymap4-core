/* 
 * polymap.org
 * Copyright 2012, Falko Br�utigam. All rights reserved.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.polymap.core.model2.engine;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.annotation.Nullable;

import org.polymap.core.model2.Association;
import org.polymap.core.model2.CollectionProperty;
import org.polymap.core.model2.Composite;
import org.polymap.core.model2.Computed;
import org.polymap.core.model2.Immutable;
import org.polymap.core.model2.MaxOccurs;
import org.polymap.core.model2.NameInStore;
import org.polymap.core.model2.Property;
import org.polymap.core.model2.PropertyBase;
import org.polymap.core.model2.Queryable;
import org.polymap.core.model2.runtime.PropertyInfo;

/**
 * 
 *
 * @author <a href="http://www.polymap.de">Falko Br�utigam</a>
 */
public class PropertyInfoImpl<T>
        implements PropertyInfo<T> {

    private Field                   field;

    
    public PropertyInfoImpl( Field field ) {
        assert PropertyBase.class.isAssignableFrom( field.getType() );
        this.field = field;
    }

    Field getField() {
        return field;
    }

    @Override
    public Class getType() {
        ParameterizedType declaredType = (ParameterizedType)field.getGenericType();
        return (Class)declaredType.getActualTypeArguments()[0];
    }
    
    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public String getNameInStore() {
        return field.getAnnotation( NameInStore.class ) != null
                ? field.getAnnotation( NameInStore.class ).value()
                : field.getName();
    }

    @Override
    public boolean isAssociation() {
        return Association.class.isAssignableFrom( field.getType() );
    }

    @Override
    public boolean isNullable() {
        return field.getAnnotation( Nullable.class ) != null;
    }

    @Override
    public boolean isImmutable() {
        return field.getAnnotation( Immutable.class ) != null;
    }

    @Override
    public boolean isComputed() {
        return field.getAnnotation( Computed.class ) != null;
    }

    @Override
    public boolean isQueryable() {
        return field.getAnnotation( Queryable.class ) != null;
    }

    @Override
    public int getMaxOccurs() {
        if (CollectionProperty.class.isAssignableFrom( field.getType() )) {
            return field.getAnnotation( MaxOccurs.class ) != null
                    ? field.getAnnotation( MaxOccurs.class ).value()
                    : Integer.MAX_VALUE;
        }
        else {
            return 1;
        }
    }

    @Override
    public T getDefaultValue() {
        return (T)DefaultValues.valueOf( field );
    }

    
    @Override
    public T getValue( Composite composite ) {
        try {
            if (!field.isAccessible()) { 
                field.setAccessible( true ); 
            }
            PropertyBase<T> prop = (PropertyBase<T>)field.get( composite );
            if (prop instanceof Property) {
                return ((Property<T>)prop).get();
            }
            else if (prop instanceof Association) {
                return (T)((Association)prop).get();
            }
            else if (prop instanceof CollectionProperty) {
                throw new IllegalStateException( "Getting the value of a CollectionProperty via PropertyInfo is not supported.");
            }
            else {
                throw new RuntimeException( "Unknown Property type: " + prop.getClass().toString() );
            }
        }
        catch (Exception e) {
            throw new RuntimeException( e );
        }
    }

    
    @Override
    public void setValue( Composite composite, T value ) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible( true );
            }
            ((Property<T>)field.get( composite )).set( value );
        }
        catch (Exception e) {
            throw new RuntimeException( e );
        }
    }

}
