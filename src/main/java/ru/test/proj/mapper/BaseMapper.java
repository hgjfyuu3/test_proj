package ru.test.proj.mapper;

public interface BaseMapper<D, E> {

    E toEntity(D usersDto);

    D toDto(E usersEntity);

}