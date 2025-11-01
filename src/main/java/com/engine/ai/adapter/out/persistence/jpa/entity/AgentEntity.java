package com.engine.ai.adapter.out.persistence.jpa.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "agents")
@Getter
@Setter
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_active = true")
@SQLDelete(sql = "UPDATE agents SET is_active = false WHERE id = ?")
public class AgentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Length(max = 255)
    @NotBlank
    private String name;

    @NotBlank
    private String prompt;

    @NotNull
    private Long version;

    private boolean isActive;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> capabilities;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Column(name = "parent_id")
    private UUID parentId;

}
