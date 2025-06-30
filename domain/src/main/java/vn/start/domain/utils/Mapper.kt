package vn.start.domain.utils

interface Mapper<E, M> {
    fun fromEntity(entity: E): M
}