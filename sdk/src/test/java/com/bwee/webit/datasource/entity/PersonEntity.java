package com.bwee.webit.datasource.entity;

import com.bwee.webit.model.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.LIST;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;

@Data
@Accessors(chain = true)
@Table("Person")
@NoArgsConstructor
public class PersonEntity implements Entity<PersonEntity, String> {

    @PrimaryKey
    private String id;

    private String name;

    private LocalDateTime createTime;

    @CassandraType(type = CassandraType.Name.VARCHAR)
    private Person.Gender gender;

    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> tags;

    public PersonEntity(final Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.tags = person.getTags();
        this.createTime = person.getCreateTime();
        this.gender = person.getGender();
    }

    public Person toModel() {
        return new Person().setId(id)
                .setName(name)
                .setCreateTime(createTime)
                .setGender(gender)
                .setTags(tags == null ? Collections.emptyList() : tags);
    }
}
