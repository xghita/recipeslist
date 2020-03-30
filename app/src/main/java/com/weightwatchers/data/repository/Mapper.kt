package com.weightwatchers.data.repository

interface Mapper<I, O> {
    fun mapToDto(input: I): O
    fun mapToDtoList(input: List<I>): List<O>
}


