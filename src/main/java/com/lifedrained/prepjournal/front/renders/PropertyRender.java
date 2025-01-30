package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class PropertyRender<ENTITY ,T extends ComponentRenderer<Component,ENTITY>> {
    private String propetryName, columnName;
    @Nullable
    T render = null;
    public PropertyRender(String propetryName, String columnName) {
        this.propetryName = propetryName;
        this.columnName = columnName;
    }

    public PropertyRender(String propetryName, String columnName, @Nullable T render) {
        this.propetryName = propetryName;
        this.columnName = columnName;
        this.render = render;
    }
}
