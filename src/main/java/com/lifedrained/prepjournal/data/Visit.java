package com.lifedrained.prepjournal.data;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Visit extends BaseEntity implements Serializable {
    @NotNull
    private String scheduleUID;
    @Nullable
    private String masterName;
    @Nullable
    private Integer hoursWasted;
    @NonNull
    private Boolean isVisited;
}
