package ru.test.proj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.test.proj.dto.UsersDto;
import ru.test.proj.model.UsersEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UsersMapper extends BaseMapper<UsersDto, UsersEntity> {
}