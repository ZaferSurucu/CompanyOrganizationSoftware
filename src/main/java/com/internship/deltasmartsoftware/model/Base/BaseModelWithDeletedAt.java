package com.internship.deltasmartsoftware.model.Base;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseModelWithDeletedAt extends BaseModel {

    private LocalDateTime deletedAt;


}
